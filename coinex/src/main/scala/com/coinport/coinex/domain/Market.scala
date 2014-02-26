package com.coinport.coinex.domain

import scala.collection.immutable.SortedSet

/**
 * ATTENTION PLEASE:
 *
 * All classes here are case-classes or case-objects. This is required since we are
 * maintaining an in-memory state that's immutable, so that we snapshot is taken and
 * persistent, the program can still update the live state.
 *
 */
case class MarketSide(outCurrency: Currency, inCurrency: Currency) {
  def reverse = MarketSide(inCurrency, outCurrency)
  override def toString = "(%s/%s)".format(outCurrency, inCurrency)
}

object Market {
  implicit val ordering = new Ordering[OrderData] {
    def compare(a: OrderData, b: OrderData) = {
      if (a.price < b.price) -1
      else if (a.price > b.price) 1
      else if (a.id < b.id) -1
      else if (a.id > b.id) 1
      else 0
    }
  }

  type OrderPool = SortedSet[OrderData]
  type OrderPools = Map[MarketSide, Market.OrderPool]

  val EmptyOrderPool = SortedSet.empty[OrderData]
  val EmptyOrderPools = Map.empty[MarketSide, Market.OrderPool]
}

import Market._
case class Market(
  outCurrency: Currency,
  inCurrency: Currency,
  marketPriceOrderPools: Market.OrderPools = Market.EmptyOrderPools,
  limitPriceOrderPools: Market.OrderPools = Market.EmptyOrderPools,
  orderMap: Map[Long, Order] = Map.empty,
  stopOrders: Map[Long, StopOrder] = Map.empty) {

  val takerSide = MarketSide(outCurrency, inCurrency)
  val makerSide = takerSide.reverse
  val bothSides = Seq(takerSide, makerSide)

  def bestTakerSidePrice = getLimitPriceOrderPool(takerSide).headOption.map(_.price)
  def bestMakerSidePrice = getLimitPriceOrderPool(makerSide).headOption.map(_.price)

  def getMarketPriceOrderPool(side: MarketSide): OrderPool = {
    marketPriceOrderPools.getOrElse(side, Market.EmptyOrderPool)
  }

  def getLimitPriceOrderPool(side: MarketSide): OrderPool = {
    limitPriceOrderPools.getOrElse(side, Market.EmptyOrderPool)
  }

  def addStopOrder(stopOrder: StopOrder) = {
    
  }
  def addOrder(order: Order): Market = {
    val validated = validateOrder(order)
    val data = validated.data
    val side = validated.side
    val market = removeOrder(data.id)

    var mpos = market.marketPriceOrderPools
    var lpos = market.limitPriceOrderPools

    if (data.price <= 0) {
      mpos += (side -> (market.getMarketPriceOrderPool(side) + data))
    } else {
      lpos += (side -> (market.getLimitPriceOrderPool(side) + data))
    }
    val orders = market.orderMap + (data.id -> validated)

    market.copy(marketPriceOrderPools = mpos, limitPriceOrderPools = lpos, orderMap = orders)
  }

  def removeOrder(id: Long): Market = {
    orderMap.get(id) match {
      case Some(old) =>
        var mpos = marketPriceOrderPools
        var lpos = limitPriceOrderPools

        bothSides.foreach { side =>
          var pool = getMarketPriceOrderPool(side) - old.data
          if (pool.isEmpty) mpos -= side
          else mpos += (side -> pool)

          pool = getLimitPriceOrderPool(side) - old.data
          if (pool.isEmpty) lpos -= side
          else lpos += (side -> pool)
        }
        val orders = orderMap - id
        copy(marketPriceOrderPools = mpos, limitPriceOrderPools = lpos, orderMap = orders)

      case None =>
        this
    }
  }

  def validateOrder(order: Order): Order = {
    val data = if (order.data.price >= 0) order.data else order.data.copy(price = 0)
    order.copy(data = data)
  }
}