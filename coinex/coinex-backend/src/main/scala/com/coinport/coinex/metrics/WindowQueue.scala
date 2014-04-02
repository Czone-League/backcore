/**
 * Copyright 2014 Coinport Inc. All Rights Reserved.
 * Author: c@coinport.com (Chao Ma)
 */

package com.coinport.coinex.metrics

class WindowQueue[T](range: Long, interval: Long, elems: Array[T], var head: Int /* tail = head + 1 */ ,
    var lastTick: Long)(implicit m: Manifest[T]) extends Serializable {

  val NULL: T = new Array[T](1)(0)

  val length = (range / interval).toInt
  require(length == elems.size, "the length of elems must equals to range / interval")

  def this(range: Long, interval: Long)(implicit m: Manifest[T]) {
    this(range, interval, new Array[T]((range / interval).toInt), 0, 0L)
  }

  def addAfterTick(elem: T, tick: Long): Array[T] = addAtTick(elem, tick + lastTick)

  def addAtTick(elem: T, tick: Long): Array[T] = {
    val range = (tick - lastTick) / interval
    lastTick = tick
    val newHead = (tick / interval % length).toInt
    val emitElems: Array[T] = if ((range / length) > 0) {
      // entire list is out of date
      val t = (elems.slice(head + 1, length) ++ elems.slice(0, head + 1)).filter(_ != NULL)
      clean(0, length)
      elems(newHead) = elem
      t
    } else if (newHead == head) {
      null
    } else {
      val t = if (newHead > head) {
        val t = elems.slice(head + 1, newHead + 1).filter(_ != NULL)
        clean(head + 1, newHead + 1)
        t
      } else {
        val t = (elems.slice(head + 1, length) ++ elems.slice(0, newHead + 1)).filter(_ != NULL)
        clean(head + 1, length)
        clean(0, newHead + 1)
        t
      }
      elems(newHead) = elem
      t
    }
    head = newHead

    emitElems
  }

  // used in snapshot
  def copy = new WindowQueue[T](range, interval, elems.clone, head, lastTick)

  def toList = elems.toList

  override def toString() = "head: %d\nlastTick: %d\nelems: %s" format (head, lastTick, elems.toList)

  private def clean(start: Int, end: Int) {
    for (i <- start until end) elems(i) = NULL
  }
}
