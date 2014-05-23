package com.coinport.coinex.opendata

import akka.actor.ActorContext
import akka.persistence.PersistentRepr
import akka.persistence.hbase.journal.PluginPersistenceSettings
import akka.persistence.hbase.common.Const._
import akka.persistence.hbase.common.{ DeferredConversions, RowKey, HdfsSnapshotDescriptor, EncryptingSerializationExtension }
import akka.persistence.hbase.common.Columns._
import akka.persistence.serialization.Snapshot
import com.coinport.coinex.common.Manager
import com.coinport.coinex.data.ExportOpenDataMap
import com.coinport.coinex.serializers._
import java.util.{ ArrayList => JArrayList }
import java.io.{ Closeable, OutputStreamWriter, BufferedWriter, BufferedInputStream }
import org.apache.commons.io.IOUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{ Path, FileSystem }
import org.apache.hadoop.hbase.util.Bytes
import org.hbase.async.KeyValue
import scala.concurrent.Future
import scala.collection.mutable.Map
import scala.collection.JavaConverters._

import DeferredConversions._

class ExportOpenDataManager(val asyncHBaseClient: AsyncHBaseClient, val context: ActorContext, val openDataConfig: OpenDataConfig)
    extends Manager[ExportOpenDataMap] {
  private val config = context.system.settings.config
  private lazy val fs: FileSystem = openHdfsSystem(openDataConfig.hdfsHost)
  private val exportSnapshotHdfsDir = openDataConfig.exportSnapshotHdfsDir
  private val exportMessagesHdfsDir = openDataConfig.exportMessagesHdfsDir
  private val debugSnapshotHdfsDir = openDataConfig.debugSnapshotHdfsDir
  private val snapshotSerializerMap = openDataConfig.snapshotSerializerMap
  private val snapshotHdfsDir: String = config.getString("hadoop-snapshot-store.snapshot-dir")
  private val messagesTable = config.getString("hbase-journal.table")
  private val messagesFamily = config.getString("hbase-journal.family")
  private val cryptKey = config.getString("akka.persistence.encryption-settings")
  private val BUFFER_SIZE = 2048
  private val SCAN_MAX_NUM_ROWS = 200

  implicit var pluginPersistenceSettings = PluginPersistenceSettings(config, JOURNAL_CONFIG)
  implicit var executionContext = context.system.dispatcher
  implicit var serialization = EncryptingSerializationExtension(context.system, cryptKey)

  // [pid, dumpFileName]
  private val pFileMap = openDataConfig.pFileMap
  // [pId, (seqNum, timestamp)]
  private val pSeqMap = Map.empty[String, Long]
  pFileMap.keySet foreach { key => pSeqMap.put(key, 1L) }

  def getSnapshot(): ExportOpenDataMap = {
    ExportOpenDataMap(pSeqMap)
  }

  override def loadSnapshot(map: ExportOpenDataMap) {
    updatePSeqMap(Map.empty[String, Long] ++ map.processorSeqMap)
  }

  def updatePSeqMap(addedMap: Map[String, Long]) {
    addedMap.keySet foreach {
      key =>
        if (pFileMap.contains(key)) {
          pSeqMap.put(key, addedMap(key))
        }
    }
  }

  def exportData(): (Map[String, Long]) = {
    val dumpedMap = Map.empty[String, Long]
    pSeqMap.keySet foreach {
      processorId =>
        if (processorId != null && !processorId.isEmpty) {
          // "processedSeqNum" included in current process
          val processedSeqNum = pSeqMap(processorId)
          // "lastSeqNum" included in current process, excluded in next process
          val lastSeqNum = dumpSnapshot(processorId, processedSeqNum)
          if (lastSeqNum >= processedSeqNum) { // when lastSeqNum == processedSeqNum, there is one message
            dumpMessages(processorId, processedSeqNum, lastSeqNum + 1)
            dumpedMap += processorId -> (lastSeqNum + 1)
          }
        }
    }
    if (!dumpedMap.isEmpty) {
      pSeqMap ++= dumpedMap
    }
    dumpedMap
  }

  def dumpSnapshot(processorId: String, processedSeqNum: Long): Long = {
    val snapshotMetas = listSnapshots(snapshotHdfsDir, processorId)

    if (snapshotMetas.isEmpty) //no file to process, let processedSeqNum to former process's lastNum, which is processedSeqNum - 1
      return processedSeqNum - 1
    snapshotMetas.head match {
      // when lastSeqNum == processedSeqNum, there is one message
      case desc @ HdfsSnapshotDescriptor(processorId: String, seqNum: Long, _) if (seqNum >= processedSeqNum) =>
        val path = new Path(snapshotHdfsDir, desc.toFilename)
        val snapshot =
          serialization.deserialize(
            withStream(new BufferedInputStream(fs.open(path, BUFFER_SIZE), BUFFER_SIZE)) {
              IOUtils.toByteArray
            }, classOf[Snapshot])
        writeSnapshot(exportSnapshotHdfsDir, processorId, seqNum, snapshot, PrettyJsonSerializer)
        val debugSerializer =
          if (snapshotSerializerMap.contains(snapshot.data.getClass))
            snapshotSerializerMap(snapshot.data.getClass)
          else
            PrettyJsonSerializer
        writeSnapshot(debugSnapshotHdfsDir, processorId, seqNum, snapshot, debugSerializer)
        seqNum
      case _ => processedSeqNum - 1
    }
  }

  // "fromSeqNum" is inclusive, "toSeqNum" is exclusive
  def dumpMessages(processorId: String, fromSeqNum: Long, toSeqNum: Long) {
    if (toSeqNum <= fromSeqNum) return
    val client = asyncHBaseClient.getClient()
    val scanner = client.newScanner(Bytes.toBytes(messagesTable))
    scanner.setFamily(Bytes.toBytes(messagesFamily))
    scanner.setStartKey(RowKey(processorId, fromSeqNum).toBytes)
    scanner.setStopKey(RowKey.toKeyForProcessor(processorId, toSeqNum))
    scanner.setKeyRegexp(RowKey.patternForProcessor(processorId))
    scanner.setMaxNumRows(SCAN_MAX_NUM_ROWS)
    type AsyncBaseRows = JArrayList[JArrayList[KeyValue]]

    def getMessages(rows: AsyncBaseRows): String = {
      val builder = new StringBuilder()
      for (row <- rows.asScala) {
        builder ++= "{"
        for (column <- row.asScala) {
          if (java.util.Arrays.equals(column.qualifier, Message) || java.util.Arrays.equals(column.qualifier, SequenceNr)) {

            if (java.util.Arrays.equals(column.qualifier, Message)) {
              // will throw an exception if failed
              val msg = serialization.deserialize(column.value(), classOf[PersistentRepr])
              builder ++= "\"" ++= msg.payload.getClass.getEnclosingClass.getSimpleName ++= "\":"
              builder ++= PrettyJsonSerializer.toJson(msg.payload)
            } else {
              builder ++= "\"" ++= Bytes.toString(column.qualifier) ++= "\":"
              builder ++= Bytes.toLong(column.value()).toString
            }
            builder ++= ","
          }
        }
        builder.delete(builder.length - 1, builder.length)
        builder ++= "},"
      }
      builder.toString()
    }

    def handleRows(): Future[StringBuilder] = {
      scanner.nextRows() flatMap {
        case null =>
          scanner.close()
          Future(new StringBuilder())
        case rows: AsyncBaseRows =>
          val builder = new StringBuilder()
          builder ++= getMessages(rows)
          handleRows() map {
            res =>
              builder ++= res
          }
      }
    }

    handleRows() map {
      case data if !data.isEmpty =>
        val writer = new BufferedWriter(new OutputStreamWriter(fs.create(
          new Path(exportMessagesHdfsDir, s"coinport_events_${pFileMap(processorId)}_${String.valueOf(toSeqNum - 1).reverse.padTo(16, "0").reverse.mkString}_v1.json".toLowerCase))))
        writer.write(s"""{"timestamp": ${System.currentTimeMillis()},\n"events": [""")
        writer.write(data.substring(0, data.length - 1).toString())
        writer.write("]}")
        writer.flush()
        writer.close()
    }
  }

  def writeSnapshot(outputDir: String, processorId: String, seqNum: Long, snapshot: Snapshot, serializer: BaseJsonSerializer) {
    val exportSnapshotPath = new Path(outputDir,
      s"coinport_snapshot_${pFileMap(processorId)}_${String.valueOf(seqNum).reverse.padTo(16, "0").reverse.mkString}_v1.json".toLowerCase)
    val className = snapshot.data.getClass.getEnclosingClass.getSimpleName
    val jsonSnapshot = s"""{"timestamp": ${System.currentTimeMillis()},\n"${className}": ${serializer.toJson(snapshot.data)}}"""
    withStream(new BufferedWriter(new OutputStreamWriter(fs.create(exportSnapshotPath, true)), BUFFER_SIZE))(IOUtils.write(jsonSnapshot, _))
  }

  private def openHdfsSystem(defaultName: String): FileSystem = {
    val conf = new Configuration()
    conf.set("fs.default.name", defaultName)
    FileSystem.get(conf)
  }

  private def withStream[S <: Closeable, A](stream: S)(fun: S => A): A =
    try fun(stream) finally stream.close()

  private def listSnapshots(snapshotDir: String, processorId: String): Seq[HdfsSnapshotDescriptor] = {
    val descs = fs.listStatus(new Path(snapshotDir)) flatMap {
      HdfsSnapshotDescriptor.from(_, processorId)
    }
    if (descs.isEmpty) Nil else descs.sortWith(_.seqNumber > _.seqNumber).toSeq
  }

}