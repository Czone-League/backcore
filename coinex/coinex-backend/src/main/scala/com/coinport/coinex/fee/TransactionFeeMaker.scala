/**
 * Copyright 2014 Coinport Inc. All Rights Reserved.
 * Author: c@coinport.com (Chao Ma)
 */

package com.coinport.coinex.fee

import com.coinport.coinex.data._
import com.coinport.coinex.common.Constants.COINPORT_UID
import com.coinport.coinex.fee.rules.FeeRules

// TODO(c): consider the type of the user
class TransactionFeeMaker(rules: FeeRules) extends FeeMaker[Transaction] {
  override def count(serviceTakeItem: Transaction): (Transaction, Fee) = {
    (null, null)
  }
}
