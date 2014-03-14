/**
 * Copyright (C) 2014 Coinport Inc. <http://www.coinport.com>
 *
 */

package com.coinport.coinex.postmarket

import akka.persistence.Persistent
import com.coinport.coinex.data._
import com.coinport.coinex.common.ExtendedView
import com.coinport.coinex.common.StateManager
import Implicits._

class UserLogsView extends ExtendedView {
  override def processorId = "coinex_pmp"
  private val manager = new UserLogsStateManager

  def receive = {
    case DebugDump =>
      log.info("state: {}", manager())

    case x =>
      log.info("~~~ saw: " + x)
      if (receiveMessage.isDefinedAt(x)) receiveMessage(x)
  }

  def receiveMessage: Receive = {
    case Persistent(OrderCancelled(side, order), _) =>
      manager.markOrderAs(order, OrderStatus.Cancelled)

    case Persistent(mu: MarketUpdate, _) =>
      manager.addOrderInfo(mu.originOrderInfo)
      mu.fullyExecutedOrders foreach { manager.markOrderAs(_, OrderStatus.FullyExecuted) }
      mu.partiallyExecutedOrders foreach { manager.markOrderAs(_, OrderStatus.PartiallyExecuted) }

    case q: QueryUserLog =>
      val userLog = manager.getOrderInfos(q)
      sender ! QueryUserLogResult(q.userId, userLog)
  }
}

private class UserLogsStateManager extends StateManager[UserLogs] {
  initWithDefaultState(UserLogs())

  def markOrderAs(order: Order, status: OrderStatus) = {
    var userLog = state.userLogs.getOrElse(order.userId, UserLog(Nil, Nil))
    val orderInfos = userLog.orderInfos map { oi =>
      if (oi.order.id == order.id) oi.copy(status = status)
      else oi
    }
    userLog = userLog.copy(orderInfos = orderInfos)
    val userLogs = state.userLogs + (order.userId -> userLog)
    state = state.copy(userLogs = userLogs)
  }

  def addOrderInfo(oi: OrderInfo) = {
    val id = oi.order.id
    var userLog = state.userLogs.getOrElse(oi.order.userId, UserLog(Nil, Nil))
    val (olds, others) = userLog.orderInfos.partition(_.order.id == id)
    val orderInfo = olds.headOption match {
      case Some(old) =>
        old.copy(status = oi.status, remainingQuantity = old.remainingQuantity, inAmount = old.inAmount + oi.inAmount)
      case None => oi
    }

    val orderInfos = Seq(orderInfo) ++ others
    userLog = userLog.copy(orderInfos = orderInfos)
    val userLogs = state.userLogs + (orderInfo.order.userId -> userLog)
    state = state.copy(userLogs = userLogs)
  }

  def getOrderInfos(param: QueryUserLog): UserLog = {
    def eval(orderInfo: OrderInfo) = (param.status.isEmpty || param.status.get == orderInfo.status)
    val userLog = state.userLogs.getOrElse(param.userId, UserLog(Nil, Nil))
    val orderInfos = userLog.orderInfos.filter(eval).drop(param.skipOrders.getOrElse(0)).take(param.numOrders.getOrElse(100))
    val txs = userLog.txs.drop(param.skipTxs.getOrElse(0)).take(param.numTxs.getOrElse(100))
    UserLog(orderInfos, txs)
  }
}