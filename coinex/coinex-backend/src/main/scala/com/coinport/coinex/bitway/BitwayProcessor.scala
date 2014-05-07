/**
 * Copyright 2014 Coinport Inc. All Rights Reserved.
 * Author: c@coinport.com (Chao Ma)
 */

package com.coinport.coinex.bitway

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.event.LoggingReceive
import akka.persistence.Deliver
import akka.persistence.Persistent
import akka.persistence.EventsourcedProcessor

import com.redis._
import com.redis.serialization.Parse.Implicits.parseByteArray
import scala.collection.mutable.Set
import scala.concurrent.duration._
import scala.util.Random

import com.coinport.coinex.common.ExtendedProcessor
import com.coinport.coinex.common.PersistentId._
import com.coinport.coinex.data._
import com.coinport.coinex.serializers._
import Implicits._

object BitwayProcessor {
  final val REQUEST_CHANNEL = "creq"
  final val RESPONSE_CHANNEL = "cres"
  final val INIT_FETCH_ADDRESS_NUM = 100

  // TODO(c): add embeded redis for unit test instead of disable the redis client
  //          inject the RedisClient instead of hardcode here
  val pullClient: Option[RedisClient] = try {
    Some(new RedisClient("localhost", 6379))
  } catch {
    case ex: Throwable => None
  }
  val pushClient: Option[RedisClient] = try {
    Some(new RedisClient("localhost", 6379))
  } catch {
    case ex: Throwable => None
  }
  val serializer = new ThriftBinarySerializer()
}

class BitwayProcessor(transferProcessor: ActorRef) extends ExtendedProcessor with EventsourcedProcessor with ActorLogging {

  import BitwayProcessor._

  val delayinSeconds = 4
  override val processorId = BITWAY_PROCESSOR <<
  val channelToTransferProcessor = createChannelTo(ACCOUNT_TRANSFER_PROCESSOR <<) // DO NOT CHANGE

  val manager = new BitwayManager()

  override def preStart() = {
    super.preStart
    scheduleTryPour()
  }

  def receiveRecover = PartialFunction.empty[Any, Unit]

  def receiveCommand = LoggingReceive {
    case TryFetchAddresses =>
      if (recoveryFinished) {
        manager.getSupportedCurrency.filter(manager.isDryUp).foreach { x =>
          self ! FetchAddresses(x)
        }
      } else {
        scheduleTryPour()
      }
    case FetchAddresses(currency) if pushClient.isDefined =>
      pushClient.get.rpush(REQUEST_CHANNEL, serializer.toBinary(BitwayRequest(BitwayType.GenerateAddress,
        Random.nextLong, currency, generateAddressRequest = Some(GenerateAddressRequest(INIT_FETCH_ADDRESS_NUM)))))

    case m @ GetNewAddress(currency, _) =>
      val (address, needFetch) = manager.allocateAddress(currency)
      if (needFetch) self ! FetchAddresses(currency)
      if (address.isDefined) {
        persist(m) { event =>
          updateState(event.copy(assignedAddress = address))
        }
        sender ! GetNewAddressResult(ErrorCode.Ok, address)
      } else {
        sender ! GetNewAddressResult(ErrorCode.NotEnoughAddressInPool, None)
      }

    case m @ BitwayMessage(t, id, currency, Some(res), None, None, None, None) =>
      if (res.error == ErrorCode.Ok) {
        persist(res) { event =>
          updateState(m)
        }
      } else {
        log.error("error occur when fetch addresses: " + res)
      }
    case m @ BitwayMessage(t, id, currency, None, Some(res), None, None, None) =>
      println("~" * 40 + res)
    case m @ BitwayMessage(t, id, currency, None, None, Some(res), None, None) =>
      println("~" * 40 + res)
    case m @ BitwayMessage(t, id, currency, None, None, None, Some(tx), None) =>
    // channelToTransferProcessor forward Deliver(Persistent(CCTxsMsg(currency, List(tx))), transferProcessor.path)
    case m @ BitwayMessage(t, id, currency, None, None, None, None, Some(blocks)) =>
  }

  def updateState: Receive = {
    case GetNewAddress(currency, Some(address)) => manager.addressAllocated(currency, address)
    case BitwayMessage(_, _, currency, Some(res), None, None, None, None) => manager.faucetAddress(currency,
      Set.empty[String] ++ res.addresses)
  }

  private def scheduleTryPour() = {
    context.system.scheduler.scheduleOnce(delayinSeconds seconds, self, TryFetchAddresses)(context.system.dispatcher)
  }
}

class BitwayReceiver(bitwayProcessor: ActorRef) extends Actor with ActorLogging {
  import BitwayProcessor._
  implicit val executeContext = context.system.dispatcher

  override def preStart = {
    super.preStart
    listenAtRedis()
  }

  def receive = LoggingReceive {
    case ListenAtRedis if pullClient.isDefined =>
      pullClient.get.blpop[String, Array[Byte]](1, RESPONSE_CHANNEL) match {
        case Some(s) =>
          val response = serializer.fromBinary(s._2, classOf[BitwayMessage.Immutable])
          bitwayProcessor ! response
        case None => None
      }
      listenAtRedis()
  }

  private def listenAtRedis() {
    context.system.scheduler.scheduleOnce(0 seconds, self, ListenAtRedis)(context.system.dispatcher)
  }
}
