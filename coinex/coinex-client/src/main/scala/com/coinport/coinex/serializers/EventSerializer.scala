package com.coinport.coinex.serializers

import akka.serialization.Serializer
import com.twitter.bijection.scrooge.BinaryScalaCodec
import com.coinport.coinex.data._

// TODO(d): Auto-generate this file.

class EventSerializer extends Serializer {
  val includeManifest: Boolean = true
  val identifier = 870725

  def toBinary(obj: AnyRef): Array[Byte] = obj match {
    case m: DoRegisterUser => BinaryScalaCodec(DoRegisterUser)(m)
    case m: DoRequestPasswordReset => BinaryScalaCodec(DoRequestPasswordReset)(m)
    case m: DoResetPassword => BinaryScalaCodec(DoResetPassword)(m)
    case m: RegisterUserFailed => BinaryScalaCodec(RegisterUserFailed)(m)
    case m: RegisterUserSucceeded => BinaryScalaCodec(RegisterUserSucceeded)(m)
    case m: Login => BinaryScalaCodec(Login)(m)
    case m: LoginFailed => BinaryScalaCodec(LoginFailed)(m)
    case m: LoginSucceeded => BinaryScalaCodec(LoginSucceeded)(m)
    case m: RequestPasswordResetFailed => BinaryScalaCodec(RequestPasswordResetFailed)(m)
    case m: RequestPasswordResetSucceeded => BinaryScalaCodec(RequestPasswordResetSucceeded)(m)
    case m: ResetPasswordFailed => BinaryScalaCodec(ResetPasswordFailed)(m)
    case m: ResetPasswordSucceeded => BinaryScalaCodec(ResetPasswordSucceeded)(m)

    case m: AccountOperationResult => BinaryScalaCodec(AccountOperationResult)(m)
    case m: DoCancelOrder => BinaryScalaCodec(DoCancelOrder)(m)
    case m: DoConfirmCashWithdrawalFailed => BinaryScalaCodec(DoConfirmCashWithdrawalFailed)(m)
    case m: DoConfirmCashWithdrawalSuccess => BinaryScalaCodec(DoConfirmCashWithdrawalSuccess)(m)
    case m: DoDepositCash => BinaryScalaCodec(DoDepositCash)(m)
    case m: DoRequestCashWithdrawal => BinaryScalaCodec(DoRequestCashWithdrawal)(m)
    case m: DoSubmitOrder => BinaryScalaCodec(DoSubmitOrder)(m)
    case m: OrderCashLocked => BinaryScalaCodec(OrderCashLocked)(m)
    case m: QueryAccount => BinaryScalaCodec(QueryAccount)(m)
    case m: QueryAccountResult => BinaryScalaCodec(QueryAccountResult)(m)
    case m: QueryMarket => BinaryScalaCodec(QueryMarket)(m)
    case m: QueryMarketResult => BinaryScalaCodec(QueryMarketResult)(m)
    case m: QueryMarketUnsupportedMarketFailure => BinaryScalaCodec(QueryMarketUnsupportedMarketFailure)(m)
    case m: OrderCancelled => BinaryScalaCodec(OrderCancelled)(m)
    case m: OrderSubmissionFailed => BinaryScalaCodec(OrderSubmissionFailed)(m)
    case m: OrderSubmitted => BinaryScalaCodec(OrderSubmitted)(m)
    case m: QueryUserOrders => BinaryScalaCodec(QueryUserOrders)(m)
    case m: QueryUserOrdersResult => BinaryScalaCodec(QueryUserOrdersResult)(m)
    case m: QueryChartData => BinaryScalaCodec(QueryChartData)(m)
    case m: QueryChartDataResult => BinaryScalaCodec(QueryChartDataResult)(m)
    case m => throw new IllegalArgumentException("Cannot serialize object: " + m)
  }

  def fromBinary(bytes: Array[Byte],
    clazz: Option[Class[_]]): AnyRef = clazz match {
    case Some(c) if c == classOf[DoRegisterUser.Immutable] => BinaryScalaCodec(DoRegisterUser).invert(bytes).get
    case Some(c) if c == classOf[DoRequestPasswordReset.Immutable] => BinaryScalaCodec(DoRequestPasswordReset).invert(bytes).get
    case Some(c) if c == classOf[DoResetPassword.Immutable] => BinaryScalaCodec(DoResetPassword).invert(bytes).get
    case Some(c) if c == classOf[RegisterUserFailed.Immutable] => BinaryScalaCodec(RegisterUserFailed).invert(bytes).get
    case Some(c) if c == classOf[RegisterUserSucceeded.Immutable] => BinaryScalaCodec(RegisterUserSucceeded).invert(bytes).get
    case Some(c) if c == classOf[Login.Immutable] => BinaryScalaCodec(Login).invert(bytes).get
    case Some(c) if c == classOf[LoginFailed.Immutable] => BinaryScalaCodec(LoginFailed).invert(bytes).get
    case Some(c) if c == classOf[LoginSucceeded.Immutable] => BinaryScalaCodec(LoginSucceeded).invert(bytes).get
    case Some(c) if c == classOf[RequestPasswordResetFailed.Immutable] => BinaryScalaCodec(RequestPasswordResetFailed).invert(bytes).get
    case Some(c) if c == classOf[RequestPasswordResetSucceeded.Immutable] => BinaryScalaCodec(RequestPasswordResetSucceeded).invert(bytes).get
    case Some(c) if c == classOf[ResetPasswordFailed.Immutable] => BinaryScalaCodec(ResetPasswordFailed).invert(bytes).get
    case Some(c) if c == classOf[ResetPasswordSucceeded.Immutable] => BinaryScalaCodec(ResetPasswordSucceeded).invert(bytes).get

    case Some(c) if c == classOf[AccountOperationResult.Immutable] => BinaryScalaCodec(AccountOperationResult).invert(bytes).get
    case Some(c) if c == classOf[DoCancelOrder.Immutable] => BinaryScalaCodec(DoCancelOrder).invert(bytes).get
    case Some(c) if c == classOf[DoConfirmCashWithdrawalFailed.Immutable] => BinaryScalaCodec(DoConfirmCashWithdrawalFailed).invert(bytes).get
    case Some(c) if c == classOf[DoConfirmCashWithdrawalSuccess.Immutable] => BinaryScalaCodec(DoConfirmCashWithdrawalSuccess).invert(bytes).get
    case Some(c) if c == classOf[DoDepositCash.Immutable] => BinaryScalaCodec(DoDepositCash).invert(bytes).get
    case Some(c) if c == classOf[DoRequestCashWithdrawal.Immutable] => BinaryScalaCodec(DoRequestCashWithdrawal).invert(bytes).get
    case Some(c) if c == classOf[DoSubmitOrder.Immutable] => BinaryScalaCodec(DoSubmitOrder).invert(bytes).get
    case Some(c) if c == classOf[OrderCashLocked.Immutable] => BinaryScalaCodec(OrderCashLocked).invert(bytes).get
    case Some(c) if c == classOf[QueryAccount.Immutable] => BinaryScalaCodec(QueryAccount).invert(bytes).get
    case Some(c) if c == classOf[QueryAccountResult.Immutable] => BinaryScalaCodec(QueryAccountResult).invert(bytes).get
    case Some(c) if c == classOf[QueryMarket.Immutable] => BinaryScalaCodec(QueryMarket).invert(bytes).get
    case Some(c) if c == classOf[QueryMarketResult.Immutable] => BinaryScalaCodec(QueryMarketResult).invert(bytes).get
    case Some(c) if c == classOf[QueryMarketUnsupportedMarketFailure.Immutable] => BinaryScalaCodec(QueryMarketUnsupportedMarketFailure).invert(bytes).get
    case Some(c) if c == classOf[OrderCancelled.Immutable] => BinaryScalaCodec(OrderCancelled).invert(bytes).get
    case Some(c) if c == classOf[OrderSubmissionFailed.Immutable] => BinaryScalaCodec(OrderSubmissionFailed).invert(bytes).get
    case Some(c) if c == classOf[OrderSubmitted.Immutable] => BinaryScalaCodec(OrderSubmitted).invert(bytes).get
    case Some(c) if c == classOf[QueryUserOrders.Immutable] => BinaryScalaCodec(QueryUserOrders).invert(bytes).get
    case Some(c) if c == classOf[QueryUserOrdersResult.Immutable] => BinaryScalaCodec(QueryUserOrdersResult).invert(bytes).get
    case Some(c) if c == classOf[QueryChartData.Immutable] => BinaryScalaCodec(QueryChartData).invert(bytes).get
    case Some(c) if c == classOf[QueryChartDataResult.Immutable] => BinaryScalaCodec(QueryChartDataResult).invert(bytes).get

    case Some(c) => throw new IllegalArgumentException("Cannot deserialize class: " + c.getCanonicalName)
    case None => throw new IllegalArgumentException("No class found in EventSerializer when deserializing array: " + bytes.mkString(""))
  }
}
