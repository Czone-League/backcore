/**
 * Copyright 2014 Coinport Inc. All Rights Reserved.
 * Author: c@coinport.com (Chao Ma)
 */

package com.coinport.coinex.fee.rules

import com.coinport.coinex.data.MarketSide

class TransactionFeeRules extends FeeRules {
  var rules = Map.empty[MarketSide, TransactionFeeItem]
}
