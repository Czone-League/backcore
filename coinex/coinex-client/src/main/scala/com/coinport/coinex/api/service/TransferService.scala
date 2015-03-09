package com.coinport.coinex.api.service

import com.coinport.coinex.data._
import com.coinport.coinex.api.model._
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TransferService extends AkkaService {
  def countTransfers(): Future[ApiResult] = {
    null
  }

  def getTransfers(userId: Option[Long], currency: Option[Currency], status: Option[TransferStatus], spanCur: Option[SpanCursor], transferTypes: Seq[TransferType], cur: Cursor, fromAdmin: Boolean = false, fromId: Option[Long] = None): Future[ApiResult] = {
    backend ? QueryTransfer(userId, currency, status, spanCur, transferTypes, cur, fromId) map {
      case result: QueryTransferResult =>
        val items = result.transfers.map(fromTransferItem(_, fromAdmin))
        ApiResult(data = Some(ApiPagingWrapper(cur.skip, cur.limit, items, result.count.toInt)))
      case x => ApiResult(false)
    }
  }

  def AdminConfirmTransfer(id: Long, success: Boolean) = {
    val transfer = AccountTransfer(id = id, userId = 0, `type` = TransferType.Withdrawal, currency = Currency.Btc, amount = 1, status = TransferStatus.Failed)
    if (success) backend ! AdminConfirmTransferSuccess(transfer)
    else backend ! AdminConfirmTransferFailure(transfer, ErrorCode.TransferReject)
  }

  def cancelWithdrawal(userId: Long, transferId: Long) = {
    val transfer = AccountTransfer(id = transferId, userId = userId, `type` = TransferType.Withdrawal, currency = Currency.Btc, amount = 1, status = TransferStatus.Failed)
    backend ! DoCancelTransfer(transfer)
  }

  def apiV2CancelWithdrawal(userId: Long, transferId: Long) = {
    val transfer = AccountTransfer(id = transferId, userId = userId, `type` = TransferType.Withdrawal, currency = Currency.Btc, amount = 1, status = TransferStatus.Failed)
    backend ? DoCancelTransfer(transfer) map {
      case result: AdminCommandResult =>
        ApiResult(data = Some(result.error))
      case x => ApiResult(false)
    }
  }
}
