/**
 * Copyright 2014 Coinport Inc. All Rights Reserved.
 * Author: c@coinport.com (Chao Ma)
 */

package com.coinport.coinex.bitway

import org.specs2.mutable._
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Set

import com.coinport.coinex.data._
import Implicits._
import Currency._

class BitwayManagerSpec extends Specification {
  import CryptoCurrencyAddressType._
  import CryptoCurrencyTransactionType._
  import CryptoCurrencyTransactionStatus._

  "BitwayManager" should {
    "address accocate test" in {
      val bwm = new BitwayManager(Btc, 10)

      bwm.getSupportedCurrency mustEqual Btc

      bwm.isDryUp mustEqual true
      bwm.faucetAddress(Unused, Set("d1", "d2", "d3", "d4", "d5", "d6"))
      bwm.isDryUp mustEqual false

      val d1 = bwm.allocateAddress
      val d1_ = bwm.allocateAddress
      d1 mustEqual d1_
      d1._2 mustEqual false

      bwm.addressAllocated(d1._1.get)
      val d2 = bwm.allocateAddress
      d1 mustNotEqual d2
      d2._2 mustEqual false
      bwm.addressAllocated(d2._1.get)
      val d3 = bwm.allocateAddress
      d3._2 mustEqual false
      bwm.addressAllocated(d3._1.get)
      val d4 = bwm.allocateAddress
      d4._2 mustEqual true
      bwm.addressAllocated(d4._1.get)
      val d5 = bwm.allocateAddress
      d5._1 mustNotEqual None
      d5._2 mustEqual true
      bwm.addressAllocated(d5._1.get)
      val d6 = bwm.allocateAddress
      d6._1 mustNotEqual None
      d6._2 mustEqual true
      bwm.addressAllocated(d6._1.get)
      val none = bwm.allocateAddress
      none mustEqual (None, true)
      val noneAgain = bwm.allocateAddress
      noneAgain mustEqual (None, true)
    }

    "get tx type test" in {
      val bwm = new BitwayManager(Btc, 10)
      bwm.faucetAddress(UserUsed, Set("u1", "u2", "u3", "u4", "u5", "u6"))
      bwm.faucetAddress(Hot, Set("h1", "h2"))
      bwm.faucetAddress(Cold, Set("c1"))

      bwm.getCryptoCurrencyTransactionType(Set("d1"), Set("d2")) mustEqual None
      bwm.getCryptoCurrencyTransactionType(Set("u1", "d1"), Set("h1", "u1")) mustEqual Some(UserToHot)
      bwm.getCryptoCurrencyTransactionType(Set("u1", "d1"), Set("h1")) mustEqual Some(UserToHot)
      bwm.getCryptoCurrencyTransactionType(Set("h1"), Set("c1", "h1")) mustEqual Some(HotToCold)
      bwm.getCryptoCurrencyTransactionType(Set("c1"), Set("h1", "c1")) mustEqual Some(ColdToHot)
      bwm.getCryptoCurrencyTransactionType(Set("d1"), Set("u1", "d1")) mustEqual Some(Deposit)
      bwm.getCryptoCurrencyTransactionType(Set("h1"), Set("d1", "h1")) mustEqual Some(Withdrawal)
      bwm.getCryptoCurrencyTransactionType(Set("u1"), Set("d1")) mustEqual Some(Unknown)
    }

    "block chain test" in {
      val bwm = new BitwayManager(Btc, 10)
      bwm.getBlockIndexes mustEqual Some(ArrayBuffer.empty[BlockIndex])
      bwm.getCurrentBlockIndex mustEqual None
      bwm.appendBlockChain(List(
        BlockIndex(Some("b1"), Some(1)),
        BlockIndex(Some("b2"), Some(2)),
        BlockIndex(Some("b3"), Some(3)),
        BlockIndex(Some("b4"), Some(4)),
        BlockIndex(Some("b5"), Some(5)),
        BlockIndex(Some("b6"), Some(6))
      ), None)
      bwm.getBlockContinuity(CryptoCurrencyBlocksMessage(None, List(
        CryptoCurrencyBlock(BlockIndex(Some("b1"), Some(1)), BlockIndex(None, None), Nil),
        CryptoCurrencyBlock(BlockIndex(Some("b2"), Some(2)), BlockIndex(Some("b1"), Some(1)), Nil),
        CryptoCurrencyBlock(BlockIndex(Some("b3"), Some(3)), BlockIndex(Some("b2"), Some(2)), Nil)
      ))) mustEqual BlockContinuityEnum.DUP

      bwm.getBlockContinuity(CryptoCurrencyBlocksMessage(None, List(
        CryptoCurrencyBlock(BlockIndex(Some("b7"), Some(7)), BlockIndex(Some("b6"), Some(6)), Nil),
        CryptoCurrencyBlock(BlockIndex(Some("b8"), Some(8)), BlockIndex(Some("b7"), Some(7)), Nil),
        CryptoCurrencyBlock(BlockIndex(Some("b9"), Some(9)), BlockIndex(Some("b8"), Some(8)), Nil)
      ))) mustEqual BlockContinuityEnum.SUCCESSOR

      bwm.getBlockContinuity(CryptoCurrencyBlocksMessage(None, List(
        CryptoCurrencyBlock(BlockIndex(Some("b8"), Some(8)), BlockIndex(Some("b7"), Some(7)), Nil),
        CryptoCurrencyBlock(BlockIndex(Some("b9"), Some(9)), BlockIndex(Some("b8"), Some(8)), Nil)
      ))) mustEqual BlockContinuityEnum.GAP

      bwm.getBlockContinuity(CryptoCurrencyBlocksMessage(Some(BlockIndex(Some("b6"), Some(6))), List(
        CryptoCurrencyBlock(BlockIndex(Some("b7"), Some(7)), BlockIndex(Some("b6"), Some(6)), Nil),
        CryptoCurrencyBlock(BlockIndex(Some("b8"), Some(8)), BlockIndex(Some("b7"), Some(7)), Nil),
        CryptoCurrencyBlock(BlockIndex(Some("b9"), Some(9)), BlockIndex(Some("b8"), Some(8)), Nil)
      ))) mustEqual BlockContinuityEnum.SUCCESSOR

      bwm.getBlockContinuity(CryptoCurrencyBlocksMessage(Some(BlockIndex(Some("b2"), Some(2))), List(
        CryptoCurrencyBlock(BlockIndex(Some("b8"), Some(8)), BlockIndex(Some("b7p"), Some(7)), Nil),
        CryptoCurrencyBlock(BlockIndex(Some("b9"), Some(9)), BlockIndex(Some("b8"), Some(8)), Nil)
      ))) mustEqual BlockContinuityEnum.REORG

      bwm.getBlockContinuity(CryptoCurrencyBlocksMessage(Some(BlockIndex(None, None)), List(
        CryptoCurrencyBlock(BlockIndex(Some("b8"), Some(8)), BlockIndex(Some("b7"), Some(7)), Nil),
        CryptoCurrencyBlock(BlockIndex(Some("b9"), Some(9)), BlockIndex(Some("b8"), Some(8)), Nil)
      ))) mustEqual BlockContinuityEnum.OTHER_BRANCH
    }

    "tx generation test" in {
      val bwm = new BitwayManager(Btc, 10)
      bwm.faucetAddress(UserUsed, Set("u1", "u2", "u3", "u4", "u5", "u6"))
      bwm.faucetAddress(Hot, Set("h1", "h2", "h3"))
      bwm.faucetAddress(Cold, Set("c1"))

      val bi1 = BlockIndex(Some("b1"), Some(1))
      val rawTx = CryptoCurrencyTransaction(
        txid = Some("t1"),
        inputs = Some(Set(CryptoCurrencyTransactionPort("h1", Some(1.1)))),
        outputs = Some(Set(CryptoCurrencyTransactionPort("d1", Some(0.9)))),
        includedBlock = Some(bi1), status = Pending)
      bwm.completeCryptoCurrencyTransaction(rawTx, None, None) mustEqual Some(CryptoCurrencyTransaction(None, Some("t1"), None, Some(Set(CryptoCurrencyTransactionPort("h1", Some(1.1), Some(1100)))), Some(Set(CryptoCurrencyTransactionPort("d1", Some(0.9), Some(900)))), None, None, Some(Withdrawal), Pending))

      val infos = Seq(
        CryptoCurrencyTransferInfo(1, "i1", Some(1000)),
        CryptoCurrencyTransferInfo(2, "i2", Some(80)))
      bwm.completeTransferInfos(infos) mustEqual List(CryptoCurrencyTransferInfo(1, "i1", Some(1000), Some(1.0), None), CryptoCurrencyTransferInfo(2, "i2", Some(80), Some(0.08), None))

      val tx1 = CryptoCurrencyTransaction(
        txid = Some("t1"),
        inputs = Some(Set(CryptoCurrencyTransactionPort("h1", Some(1.1)))),
        outputs = Some(Set(CryptoCurrencyTransactionPort("d1", Some(0.9)))),
        includedBlock = Some(bi1), status = Pending)
      val tx2 = CryptoCurrencyTransaction(
        txid = Some("t2"),
        inputs = Some(Set(CryptoCurrencyTransactionPort("h2", Some(2.1)))),
        outputs = Some(Set(CryptoCurrencyTransactionPort("d2", Some(2.9)))),
        includedBlock = Some(bi1), status = Pending)
      val tx3 = CryptoCurrencyTransaction(
        txid = Some("t3"),
        inputs = Some(Set(CryptoCurrencyTransactionPort("h3", Some(3.1)))),
        outputs = Some(Set(CryptoCurrencyTransactionPort("d3", Some(3.9)))),
        includedBlock = Some(bi1), status = Pending)
      val tx4 = CryptoCurrencyTransaction(
        txid = Some("t4"),
        inputs = Some(Set(CryptoCurrencyTransactionPort("h4", Some(4.1)))),
        outputs = Some(Set(CryptoCurrencyTransactionPort("d4", Some(4.9)))),
        includedBlock = Some(bi1), status = Pending)
      val blocks = List(
        CryptoCurrencyBlock(BlockIndex(Some("b10"), Some(10)), BlockIndex(Some("b9"), Some(9)), List(
          tx1, tx2)),
        CryptoCurrencyBlock(BlockIndex(Some("b11"), Some(11)), BlockIndex(Some("b10"), Some(10)), List(
          tx3, tx4))
      )

      bwm.extractTxsFromBlocks(blocks) mustEqual List(
        CryptoCurrencyTransaction(None, Some("t1"), None,
          Some(Set(CryptoCurrencyTransactionPort("h1", Some(1.1), Some(1100)))),
          Some(Set(CryptoCurrencyTransactionPort("d1", Some(0.9), Some(900)))),
          Some(BlockIndex(Some("b9"), Some(9))),
          Some(BlockIndex(Some("b10"), Some(10))), Some(Withdrawal), Pending),
        CryptoCurrencyTransaction(None, Some("t2"), None,
          Some(Set(CryptoCurrencyTransactionPort("h2", Some(2.1), Some(2100)))),
          Some(Set(CryptoCurrencyTransactionPort("d2", Some(2.9), Some(2900)))),
          Some(BlockIndex(Some("b9"), Some(9))),
          Some(BlockIndex(Some("b10"), Some(10))), Some(Withdrawal), Pending),
        CryptoCurrencyTransaction(None, Some("t3"), None,
          Some(Set(CryptoCurrencyTransactionPort("h3", Some(3.1), Some(3100)))),
          Some(Set(CryptoCurrencyTransactionPort("d3", Some(3.9), Some(3900)))),
          Some(BlockIndex(Some("b10"), Some(10))), Some(BlockIndex(Some("b11"), Some(11))),
          Some(Withdrawal), Pending))
    }

    "lastTx/lastAlive test" in {
      val bwm = new BitwayManager(Btc, 10)
      bwm.faucetAddress(UserUsed, Set("u1", "u2", "u3", "u4", "u5", "u6"))
      bwm.faucetAddress(Hot, Set("h1", "h2"))
      bwm.faucetAddress(Cold, Set("c1"))

      val bi1 = BlockIndex(Some("b1"), Some(1))
      val bi2 = BlockIndex(Some("b2"), Some(2))

      bwm.updateLastTx(Seq(
        CryptoCurrencyTransaction(
          txid = Some("t1"),
          inputs = Some(Set(CryptoCurrencyTransactionPort("h1"))),
          outputs = Some(Set(CryptoCurrencyTransactionPort("u1"))),
          includedBlock = Some(bi1),
          status = Pending
        ),
        CryptoCurrencyTransaction(
          txid = Some("t2"),
          inputs = Some(Set(CryptoCurrencyTransactionPort("h1"), CryptoCurrencyTransactionPort("h2"))),
          outputs = Some(Set(CryptoCurrencyTransactionPort("u2"), CryptoCurrencyTransactionPort("h1"), CryptoCurrencyTransactionPort("h2"))),
          includedBlock = Some(bi2),
          status = Pending
        )
      ))
      bwm.updateLastAlive(1234L)
      bwm.getLastAlive mustEqual 1234L
      bwm.getLastTxs(Hot) mustEqual Map("h1" -> BlockIndex(Some("t2"), Some(2)), "h2" -> BlockIndex(Some("t2"), Some(2)))
      bwm.getLastTxs(Cold) mustEqual Map.empty[String, BlockIndex]
      bwm.getLastTxs(UserUsed) mustEqual Map("u1" -> BlockIndex(Some("t1"), Some(1)), "u2" -> BlockIndex(Some("t2"), Some(2)))
    }
  }
}