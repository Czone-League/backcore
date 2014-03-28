#!/bin/sh
exec scala "$0" "$@"
!#
import scala.util.matching.Regex
import java.io.File
import java.util.Calendar
object Generator {

  val structNameExtractor = """\s+struct\s+(\w+)\s*\{""".r
  val structFieldCounter = """\d+\w*:\w*""".r

  // Do the generation and replace existing files
  val time = Calendar.getInstance().getTime().toString
  val structs = extractStructsFromFile("coinex-client/src/main/thrift/messages.thrift")

  def apply() = {
    generateSerializerFile("ThriftBinarySerializer", 607870725, "BinaryScalaCodec", structs,
      "coinex-client/src/main/scala/com/coinport/coinex/serializers/ThriftJsonSerializer.scala", time)

    generateSerializerFile("ThriftJsonSerializer", 607100416, "JsonScalaCodec", structs,
      "coinex-client/src/main/scala/com/coinport/coinex/serializers/ThriftBinarySerializer.scala", time)

    generateConfigFile("ThriftBinarySerializer", structs,
      "coinex-client/src/main/resources/serialization.conf", time)
  }

  // Auto-generate EventSerializer code
  def extractStructsFromFile(file: String): Seq[String] = {
    val lines = scala.io.Source.fromFile(file).mkString
    val structs = structNameExtractor.findAllIn(lines).matchData.toSeq.map(_.group(1))
    structs.sortWith((a, b) => a < b)
  }

  def generateSerializerFile(className: String, id: Int, codec: String, structs: Seq[String], outputFile: String, time: String) = {
    val code = SERIALIZER_CODE_TEMPLATE.format(time, className, id,
      structs.zipWithIndex.map { case (struct, idx) => codecClause(idx, codec, struct) }.mkString("\n"),
      structs.zipWithIndex.map { case (struct, idx) => toBinaryClauses(idx, struct) }.mkString("\n"),
      structs.zipWithIndex.map { case (struct, idx) => fromBinaryClause(idx, struct) }.mkString("\n"))
    writeToFile(code, outputFile)
  }

  def codecClause(idx: Int, codec: String, struct: String) = s"  lazy val s_${idx} = ${codec}(${struct})"
  def toBinaryClauses(idx: Int, struct: String) = s"    case m: $struct => s_${idx}(m)"
  def fromBinaryClause(idx: Int, struct: String) = s"    case Some(c) if c == classOf[${struct}.Immutable] => s_${idx}.invert(bytes).get"

  // Auto-generate Akka serialization configuration file
  def generateConfigFileEntry(struct: String) = "      \"com.coinport.coinex.data.%s\" = event".format(struct)
  def generateConfigFile(className: String, structs: Seq[String], outputFile: String, time: String) = {
    val configs = SERIALIZATION_CONFIG_TEMPLATE.format(time, className, structs.map(generateConfigFileEntry).mkString("\n"))
    writeToFile(configs, outputFile)
  }

  def writeToFile(content: String, file: String) = {
    val pw = new java.io.PrintWriter(new File(file))
    try pw.write(content) finally pw.close()
  }

  val SERIALIZER_CODE_TEMPLATE = """
/**
 * Copyright (C) 2014 Coinport Inc. <http://www.coinport.com>
 *
 * This file was auto generated by auto_gen_serializer.sh on %s
 */

package com.coinport.coinex.serializers

import akka.serialization.Serializer
import com.twitter.bijection.scrooge.BinaryScalaCodec
import com.coinport.coinex.data._

class %s extends Serializer {
  val includeManifest: Boolean = true
  val identifier = %d
%s

  def toBinary(obj: AnyRef): Array[Byte] = obj match {
%s

    case m => throw new IllegalArgumentException("Cannot serialize object: " + m)
  }

  def fromBinary(bytes: Array[Byte],
    clazz: Option[Class[_]]): AnyRef = clazz match {
%s

    case Some(c) => throw new IllegalArgumentException("Cannot deserialize class: " + c.getCanonicalName)
    case None => throw new IllegalArgumentException("No class found in EventSerializer when deserializing array: " + bytes.mkString("").take(100))
  }
}
"""

  val SERIALIZATION_CONFIG_TEMPLATE = """
#
# Copyright (C) 2014 Coinport Inc. <http://www.coinport.com>
#
# This file was auto generated by auto_gen_serializer.sh on %s

akka {
  actor {
    serializers {
      bytes = "akka.serialization.ByteArraySerializer"
      proto = "akka.remote.serialization.ProtobufSerializer"
      akka-containers = "akka.remote.serialization.MessageContainerSerializer"
      daemon-create = "akka.remote.serialization.DaemonMsgCreateSerializer"
      akka-cluster = "akka.cluster.protobuf.ClusterMessageSerializer"
      akka-pubsub = "akka.contrib.pattern.protobuf.DistributedPubSubMessageSerializer"
      akka-persistence-snapshot = "akka.persistence.serialization.SnapshotSerializer"
      akka-persistence-message = "akka.persistence.serialization.MessageSerializer"
      event = "com.coinport.coinex.serializers.%s"
    }
    serialization-bindings {
      "[B" = bytes
      "akka.event.Logging$LogEvent" = bytes
      "com.google.protobuf.GeneratedMessage" = proto
      "com.google.protobuf.Message" = proto
      "akka.actor.ActorSelectionMessage" = akka-containers
      "akka.remote.DaemonMsgCreate" = daemon-create
      "akka.cluster.ClusterMessage" = akka-cluster
      "akka.contrib.pattern.DistributedPubSubMessage" = akka-pubsub
      "akka.persistence.serialization.Snapshot" = akka-persistence-snapshot
      "akka.persistence.serialization.Message" = akka-persistence-message
      
%s
    }  
  }
}
"""

  val JSON_PROTOCOL_TEMPLATE = """

/**
 * Copyright (C) 2014 Coinport Inc. <http://www.coinport.com>
 *
 * This file was auto generated by auto_gen_serializer.sh on %s
 */

import spray.json._
import DefaultJsonProtocol._

import com.coinport.coinex.data._

object MessageJsonProtocol extends DefaultJsonProtocol {
%s
}
"""
}

Generator()