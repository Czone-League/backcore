
/**
 * Copyright (C) 2014 Coinport Inc. <http://www.coinport.com>
 *
 * This file was auto generated by auto_gen_serializer.sh
 */

package com.coinport.coinex.serializers

import akka.serialization.Serializer
import com.twitter.bijection.scrooge.BinaryScalaCodec
import com.coinport.coinex.data._

class ThriftBinarySerializer extends Serializer {
  val includeManifest: Boolean = true
  val identifier = 607870725
  lazy val _cApiSecret = BinaryScalaCodec(ApiSecret)
  lazy val _cCandleData = BinaryScalaCodec(CandleData)
  lazy val _cCandleDataItem = BinaryScalaCodec(CandleDataItem)
  lazy val _cCashAccount = BinaryScalaCodec(CashAccount)
  lazy val _cCursor = BinaryScalaCodec(Cursor)
  lazy val _cDeposit = BinaryScalaCodec(Deposit)
  lazy val _cFee = BinaryScalaCodec(Fee)
  lazy val _cMarketDepth = BinaryScalaCodec(MarketDepth)
  lazy val _cMarketDepthItem = BinaryScalaCodec(MarketDepthItem)
  lazy val _cMarketSide = BinaryScalaCodec(MarketSide)
  lazy val _cMetrics = BinaryScalaCodec(Metrics)
  lazy val _cMetricsByMarket = BinaryScalaCodec(MetricsByMarket)
  lazy val _cOrder = BinaryScalaCodec(Order)
  lazy val _cOrderInfo = BinaryScalaCodec(OrderInfo)
  lazy val _cOrderUpdate = BinaryScalaCodec(OrderUpdate)
  lazy val _cQueryMarketSide = BinaryScalaCodec(QueryMarketSide)
  lazy val _cRedeliverFilterData = BinaryScalaCodec(RedeliverFilterData)
  lazy val _cSpanCursor = BinaryScalaCodec(SpanCursor)
  lazy val _cTransaction = BinaryScalaCodec(Transaction)
  lazy val _cTransactionItem = BinaryScalaCodec(TransactionItem)
  lazy val _cUserAccount = BinaryScalaCodec(UserAccount)
  lazy val _cUserLogsState = BinaryScalaCodec(UserLogsState)
  lazy val _cUserProfile = BinaryScalaCodec(UserProfile)
  lazy val _cWithdrawal = BinaryScalaCodec(Withdrawal)
  lazy val _cAdminCommandResult = BinaryScalaCodec(AdminCommandResult)
  lazy val _cAdminConfirmCashDepositFailure = BinaryScalaCodec(AdminConfirmCashDepositFailure)
  lazy val _cAdminConfirmCashDepositSuccess = BinaryScalaCodec(AdminConfirmCashDepositSuccess)
  lazy val _cAdminConfirmCashWithdrawalFailure = BinaryScalaCodec(AdminConfirmCashWithdrawalFailure)
  lazy val _cAdminConfirmCashWithdrawalSuccess = BinaryScalaCodec(AdminConfirmCashWithdrawalSuccess)
  lazy val _cApiSecretOperationResult = BinaryScalaCodec(ApiSecretOperationResult)
  lazy val _cCancelOrderFailed = BinaryScalaCodec(CancelOrderFailed)
  lazy val _cDoAddNewApiSecret = BinaryScalaCodec(DoAddNewApiSecret)
  lazy val _cDoCancelOrder = BinaryScalaCodec(DoCancelOrder)
  lazy val _cDoDeleteApiSecret = BinaryScalaCodec(DoDeleteApiSecret)
  lazy val _cDoRegisterUser = BinaryScalaCodec(DoRegisterUser)
  lazy val _cDoRequestCashDeposit = BinaryScalaCodec(DoRequestCashDeposit)
  lazy val _cDoRequestCashWithdrawal = BinaryScalaCodec(DoRequestCashWithdrawal)
  lazy val _cDoRequestPasswordReset = BinaryScalaCodec(DoRequestPasswordReset)
  lazy val _cDoResetPassword = BinaryScalaCodec(DoResetPassword)
  lazy val _cDoSendEmail = BinaryScalaCodec(DoSendEmail)
  lazy val _cDoSubmitOrder = BinaryScalaCodec(DoSubmitOrder)
  lazy val _cDoUpdateMetrics = BinaryScalaCodec(DoUpdateMetrics)
  lazy val _cDoUpdateUserProfile = BinaryScalaCodec(DoUpdateUserProfile)
  lazy val _cDumpStateToFile = BinaryScalaCodec(DumpStateToFile)
  lazy val _cGoogleAuthCodeVerificationResult = BinaryScalaCodec(GoogleAuthCodeVerificationResult)
  lazy val _cLogin = BinaryScalaCodec(Login)
  lazy val _cLoginFailed = BinaryScalaCodec(LoginFailed)
  lazy val _cLoginSucceeded = BinaryScalaCodec(LoginSucceeded)
  lazy val _cMessageNotSupported = BinaryScalaCodec(MessageNotSupported)
  lazy val _cOrderCancelled = BinaryScalaCodec(OrderCancelled)
  lazy val _cOrderFundFrozen = BinaryScalaCodec(OrderFundFrozen)
  lazy val _cOrderSubmitted = BinaryScalaCodec(OrderSubmitted)
  lazy val _cPasswordResetTokenValidationResult = BinaryScalaCodec(PasswordResetTokenValidationResult)
  lazy val _cQueryAccount = BinaryScalaCodec(QueryAccount)
  lazy val _cQueryAccountResult = BinaryScalaCodec(QueryAccountResult)
  lazy val _cQueryApiSecrets = BinaryScalaCodec(QueryApiSecrets)
  lazy val _cQueryApiSecretsResult = BinaryScalaCodec(QueryApiSecretsResult)
  lazy val _cQueryCandleData = BinaryScalaCodec(QueryCandleData)
  lazy val _cQueryCandleDataResult = BinaryScalaCodec(QueryCandleDataResult)
  lazy val _cQueryDeposit = BinaryScalaCodec(QueryDeposit)
  lazy val _cQueryDepositResult = BinaryScalaCodec(QueryDepositResult)
  lazy val _cQueryMarketDepth = BinaryScalaCodec(QueryMarketDepth)
  lazy val _cQueryMarketDepthResult = BinaryScalaCodec(QueryMarketDepthResult)
  lazy val _cQueryOrder = BinaryScalaCodec(QueryOrder)
  lazy val _cQueryOrderResult = BinaryScalaCodec(QueryOrderResult)
  lazy val _cQueryTransaction = BinaryScalaCodec(QueryTransaction)
  lazy val _cQueryTransactionResult = BinaryScalaCodec(QueryTransactionResult)
  lazy val _cQueryWithdrawal = BinaryScalaCodec(QueryWithdrawal)
  lazy val _cQueryWithdrawalResult = BinaryScalaCodec(QueryWithdrawalResult)
  lazy val _cRegisterUserFailed = BinaryScalaCodec(RegisterUserFailed)
  lazy val _cRegisterUserSucceeded = BinaryScalaCodec(RegisterUserSucceeded)
  lazy val _cRequestCashDepositFailed = BinaryScalaCodec(RequestCashDepositFailed)
  lazy val _cRequestCashDepositSucceeded = BinaryScalaCodec(RequestCashDepositSucceeded)
  lazy val _cRequestCashWithdrawalFailed = BinaryScalaCodec(RequestCashWithdrawalFailed)
  lazy val _cRequestCashWithdrawalSucceeded = BinaryScalaCodec(RequestCashWithdrawalSucceeded)
  lazy val _cRequestPasswordResetFailed = BinaryScalaCodec(RequestPasswordResetFailed)
  lazy val _cRequestPasswordResetSucceeded = BinaryScalaCodec(RequestPasswordResetSucceeded)
  lazy val _cResetPasswordFailed = BinaryScalaCodec(ResetPasswordFailed)
  lazy val _cResetPasswordSucceeded = BinaryScalaCodec(ResetPasswordSucceeded)
  lazy val _cSubmitOrderFailed = BinaryScalaCodec(SubmitOrderFailed)
  lazy val _cTakeSnapshotNow = BinaryScalaCodec(TakeSnapshotNow)
  lazy val _cUpdateUserProfileFailed = BinaryScalaCodec(UpdateUserProfileFailed)
  lazy val _cUpdateUserProfileSucceeded = BinaryScalaCodec(UpdateUserProfileSucceeded)
  lazy val _cValidatePasswordResetToken = BinaryScalaCodec(ValidatePasswordResetToken)
  lazy val _cVerifyGoogleAuthCode = BinaryScalaCodec(VerifyGoogleAuthCode)
  lazy val _cTAccountState = BinaryScalaCodec(TAccountState)
  lazy val _cTApiSecretState = BinaryScalaCodec(TApiSecretState)
  lazy val _cTExportToMongoState = BinaryScalaCodec(TExportToMongoState)
  lazy val _cTMarketDepthState = BinaryScalaCodec(TMarketDepthState)
  lazy val _cTMarketState = BinaryScalaCodec(TMarketState)
  lazy val _cTUserState = BinaryScalaCodec(TUserState)

  def toBinary(obj: AnyRef): Array[Byte] = obj match {
    case m: ApiSecret => _cApiSecret(m)
    case m: CandleData => _cCandleData(m)
    case m: CandleDataItem => _cCandleDataItem(m)
    case m: CashAccount => _cCashAccount(m)
    case m: Cursor => _cCursor(m)
    case m: Deposit => _cDeposit(m)
    case m: Fee => _cFee(m)
    case m: MarketDepth => _cMarketDepth(m)
    case m: MarketDepthItem => _cMarketDepthItem(m)
    case m: MarketSide => _cMarketSide(m)
    case m: Metrics => _cMetrics(m)
    case m: MetricsByMarket => _cMetricsByMarket(m)
    case m: Order => _cOrder(m)
    case m: OrderInfo => _cOrderInfo(m)
    case m: OrderUpdate => _cOrderUpdate(m)
    case m: QueryMarketSide => _cQueryMarketSide(m)
    case m: RedeliverFilterData => _cRedeliverFilterData(m)
    case m: SpanCursor => _cSpanCursor(m)
    case m: Transaction => _cTransaction(m)
    case m: TransactionItem => _cTransactionItem(m)
    case m: UserAccount => _cUserAccount(m)
    case m: UserLogsState => _cUserLogsState(m)
    case m: UserProfile => _cUserProfile(m)
    case m: Withdrawal => _cWithdrawal(m)
    case m: AdminCommandResult => _cAdminCommandResult(m)
    case m: AdminConfirmCashDepositFailure => _cAdminConfirmCashDepositFailure(m)
    case m: AdminConfirmCashDepositSuccess => _cAdminConfirmCashDepositSuccess(m)
    case m: AdminConfirmCashWithdrawalFailure => _cAdminConfirmCashWithdrawalFailure(m)
    case m: AdminConfirmCashWithdrawalSuccess => _cAdminConfirmCashWithdrawalSuccess(m)
    case m: ApiSecretOperationResult => _cApiSecretOperationResult(m)
    case m: CancelOrderFailed => _cCancelOrderFailed(m)
    case m: DoAddNewApiSecret => _cDoAddNewApiSecret(m)
    case m: DoCancelOrder => _cDoCancelOrder(m)
    case m: DoDeleteApiSecret => _cDoDeleteApiSecret(m)
    case m: DoRegisterUser => _cDoRegisterUser(m)
    case m: DoRequestCashDeposit => _cDoRequestCashDeposit(m)
    case m: DoRequestCashWithdrawal => _cDoRequestCashWithdrawal(m)
    case m: DoRequestPasswordReset => _cDoRequestPasswordReset(m)
    case m: DoResetPassword => _cDoResetPassword(m)
    case m: DoSendEmail => _cDoSendEmail(m)
    case m: DoSubmitOrder => _cDoSubmitOrder(m)
    case m: DoUpdateMetrics => _cDoUpdateMetrics(m)
    case m: DoUpdateUserProfile => _cDoUpdateUserProfile(m)
    case m: DumpStateToFile => _cDumpStateToFile(m)
    case m: GoogleAuthCodeVerificationResult => _cGoogleAuthCodeVerificationResult(m)
    case m: Login => _cLogin(m)
    case m: LoginFailed => _cLoginFailed(m)
    case m: LoginSucceeded => _cLoginSucceeded(m)
    case m: MessageNotSupported => _cMessageNotSupported(m)
    case m: OrderCancelled => _cOrderCancelled(m)
    case m: OrderFundFrozen => _cOrderFundFrozen(m)
    case m: OrderSubmitted => _cOrderSubmitted(m)
    case m: PasswordResetTokenValidationResult => _cPasswordResetTokenValidationResult(m)
    case m: QueryAccount => _cQueryAccount(m)
    case m: QueryAccountResult => _cQueryAccountResult(m)
    case m: QueryApiSecrets => _cQueryApiSecrets(m)
    case m: QueryApiSecretsResult => _cQueryApiSecretsResult(m)
    case m: QueryCandleData => _cQueryCandleData(m)
    case m: QueryCandleDataResult => _cQueryCandleDataResult(m)
    case m: QueryDeposit => _cQueryDeposit(m)
    case m: QueryDepositResult => _cQueryDepositResult(m)
    case m: QueryMarketDepth => _cQueryMarketDepth(m)
    case m: QueryMarketDepthResult => _cQueryMarketDepthResult(m)
    case m: QueryOrder => _cQueryOrder(m)
    case m: QueryOrderResult => _cQueryOrderResult(m)
    case m: QueryTransaction => _cQueryTransaction(m)
    case m: QueryTransactionResult => _cQueryTransactionResult(m)
    case m: QueryWithdrawal => _cQueryWithdrawal(m)
    case m: QueryWithdrawalResult => _cQueryWithdrawalResult(m)
    case m: RegisterUserFailed => _cRegisterUserFailed(m)
    case m: RegisterUserSucceeded => _cRegisterUserSucceeded(m)
    case m: RequestCashDepositFailed => _cRequestCashDepositFailed(m)
    case m: RequestCashDepositSucceeded => _cRequestCashDepositSucceeded(m)
    case m: RequestCashWithdrawalFailed => _cRequestCashWithdrawalFailed(m)
    case m: RequestCashWithdrawalSucceeded => _cRequestCashWithdrawalSucceeded(m)
    case m: RequestPasswordResetFailed => _cRequestPasswordResetFailed(m)
    case m: RequestPasswordResetSucceeded => _cRequestPasswordResetSucceeded(m)
    case m: ResetPasswordFailed => _cResetPasswordFailed(m)
    case m: ResetPasswordSucceeded => _cResetPasswordSucceeded(m)
    case m: SubmitOrderFailed => _cSubmitOrderFailed(m)
    case m: TakeSnapshotNow => _cTakeSnapshotNow(m)
    case m: UpdateUserProfileFailed => _cUpdateUserProfileFailed(m)
    case m: UpdateUserProfileSucceeded => _cUpdateUserProfileSucceeded(m)
    case m: ValidatePasswordResetToken => _cValidatePasswordResetToken(m)
    case m: VerifyGoogleAuthCode => _cVerifyGoogleAuthCode(m)
    case m: TAccountState => _cTAccountState(m)
    case m: TApiSecretState => _cTApiSecretState(m)
    case m: TExportToMongoState => _cTExportToMongoState(m)
    case m: TMarketDepthState => _cTMarketDepthState(m)
    case m: TMarketState => _cTMarketState(m)
    case m: TUserState => _cTUserState(m)

    case m => throw new IllegalArgumentException("Cannot serialize object: " + m)
  }

  def fromBinary(bytes: Array[Byte],
    clazz: Option[Class[_]]): AnyRef = clazz match {
    case Some(c) if c == classOf[ApiSecret.Immutable] => _cApiSecret.invert(bytes).get
    case Some(c) if c == classOf[CandleData.Immutable] => _cCandleData.invert(bytes).get
    case Some(c) if c == classOf[CandleDataItem.Immutable] => _cCandleDataItem.invert(bytes).get
    case Some(c) if c == classOf[CashAccount.Immutable] => _cCashAccount.invert(bytes).get
    case Some(c) if c == classOf[Cursor.Immutable] => _cCursor.invert(bytes).get
    case Some(c) if c == classOf[Deposit.Immutable] => _cDeposit.invert(bytes).get
    case Some(c) if c == classOf[Fee.Immutable] => _cFee.invert(bytes).get
    case Some(c) if c == classOf[MarketDepth.Immutable] => _cMarketDepth.invert(bytes).get
    case Some(c) if c == classOf[MarketDepthItem.Immutable] => _cMarketDepthItem.invert(bytes).get
    case Some(c) if c == classOf[MarketSide.Immutable] => _cMarketSide.invert(bytes).get
    case Some(c) if c == classOf[Metrics.Immutable] => _cMetrics.invert(bytes).get
    case Some(c) if c == classOf[MetricsByMarket.Immutable] => _cMetricsByMarket.invert(bytes).get
    case Some(c) if c == classOf[Order.Immutable] => _cOrder.invert(bytes).get
    case Some(c) if c == classOf[OrderInfo.Immutable] => _cOrderInfo.invert(bytes).get
    case Some(c) if c == classOf[OrderUpdate.Immutable] => _cOrderUpdate.invert(bytes).get
    case Some(c) if c == classOf[QueryMarketSide.Immutable] => _cQueryMarketSide.invert(bytes).get
    case Some(c) if c == classOf[RedeliverFilterData.Immutable] => _cRedeliverFilterData.invert(bytes).get
    case Some(c) if c == classOf[SpanCursor.Immutable] => _cSpanCursor.invert(bytes).get
    case Some(c) if c == classOf[Transaction.Immutable] => _cTransaction.invert(bytes).get
    case Some(c) if c == classOf[TransactionItem.Immutable] => _cTransactionItem.invert(bytes).get
    case Some(c) if c == classOf[UserAccount.Immutable] => _cUserAccount.invert(bytes).get
    case Some(c) if c == classOf[UserLogsState.Immutable] => _cUserLogsState.invert(bytes).get
    case Some(c) if c == classOf[UserProfile.Immutable] => _cUserProfile.invert(bytes).get
    case Some(c) if c == classOf[Withdrawal.Immutable] => _cWithdrawal.invert(bytes).get
    case Some(c) if c == classOf[AdminCommandResult.Immutable] => _cAdminCommandResult.invert(bytes).get
    case Some(c) if c == classOf[AdminConfirmCashDepositFailure.Immutable] => _cAdminConfirmCashDepositFailure.invert(bytes).get
    case Some(c) if c == classOf[AdminConfirmCashDepositSuccess.Immutable] => _cAdminConfirmCashDepositSuccess.invert(bytes).get
    case Some(c) if c == classOf[AdminConfirmCashWithdrawalFailure.Immutable] => _cAdminConfirmCashWithdrawalFailure.invert(bytes).get
    case Some(c) if c == classOf[AdminConfirmCashWithdrawalSuccess.Immutable] => _cAdminConfirmCashWithdrawalSuccess.invert(bytes).get
    case Some(c) if c == classOf[ApiSecretOperationResult.Immutable] => _cApiSecretOperationResult.invert(bytes).get
    case Some(c) if c == classOf[CancelOrderFailed.Immutable] => _cCancelOrderFailed.invert(bytes).get
    case Some(c) if c == classOf[DoAddNewApiSecret.Immutable] => _cDoAddNewApiSecret.invert(bytes).get
    case Some(c) if c == classOf[DoCancelOrder.Immutable] => _cDoCancelOrder.invert(bytes).get
    case Some(c) if c == classOf[DoDeleteApiSecret.Immutable] => _cDoDeleteApiSecret.invert(bytes).get
    case Some(c) if c == classOf[DoRegisterUser.Immutable] => _cDoRegisterUser.invert(bytes).get
    case Some(c) if c == classOf[DoRequestCashDeposit.Immutable] => _cDoRequestCashDeposit.invert(bytes).get
    case Some(c) if c == classOf[DoRequestCashWithdrawal.Immutable] => _cDoRequestCashWithdrawal.invert(bytes).get
    case Some(c) if c == classOf[DoRequestPasswordReset.Immutable] => _cDoRequestPasswordReset.invert(bytes).get
    case Some(c) if c == classOf[DoResetPassword.Immutable] => _cDoResetPassword.invert(bytes).get
    case Some(c) if c == classOf[DoSendEmail.Immutable] => _cDoSendEmail.invert(bytes).get
    case Some(c) if c == classOf[DoSubmitOrder.Immutable] => _cDoSubmitOrder.invert(bytes).get
    case Some(c) if c == classOf[DoUpdateMetrics.Immutable] => _cDoUpdateMetrics.invert(bytes).get
    case Some(c) if c == classOf[DoUpdateUserProfile.Immutable] => _cDoUpdateUserProfile.invert(bytes).get
    case Some(c) if c == classOf[DumpStateToFile.Immutable] => _cDumpStateToFile.invert(bytes).get
    case Some(c) if c == classOf[GoogleAuthCodeVerificationResult.Immutable] => _cGoogleAuthCodeVerificationResult.invert(bytes).get
    case Some(c) if c == classOf[Login.Immutable] => _cLogin.invert(bytes).get
    case Some(c) if c == classOf[LoginFailed.Immutable] => _cLoginFailed.invert(bytes).get
    case Some(c) if c == classOf[LoginSucceeded.Immutable] => _cLoginSucceeded.invert(bytes).get
    case Some(c) if c == classOf[MessageNotSupported.Immutable] => _cMessageNotSupported.invert(bytes).get
    case Some(c) if c == classOf[OrderCancelled.Immutable] => _cOrderCancelled.invert(bytes).get
    case Some(c) if c == classOf[OrderFundFrozen.Immutable] => _cOrderFundFrozen.invert(bytes).get
    case Some(c) if c == classOf[OrderSubmitted.Immutable] => _cOrderSubmitted.invert(bytes).get
    case Some(c) if c == classOf[PasswordResetTokenValidationResult.Immutable] => _cPasswordResetTokenValidationResult.invert(bytes).get
    case Some(c) if c == classOf[QueryAccount.Immutable] => _cQueryAccount.invert(bytes).get
    case Some(c) if c == classOf[QueryAccountResult.Immutable] => _cQueryAccountResult.invert(bytes).get
    case Some(c) if c == classOf[QueryApiSecrets.Immutable] => _cQueryApiSecrets.invert(bytes).get
    case Some(c) if c == classOf[QueryApiSecretsResult.Immutable] => _cQueryApiSecretsResult.invert(bytes).get
    case Some(c) if c == classOf[QueryCandleData.Immutable] => _cQueryCandleData.invert(bytes).get
    case Some(c) if c == classOf[QueryCandleDataResult.Immutable] => _cQueryCandleDataResult.invert(bytes).get
    case Some(c) if c == classOf[QueryDeposit.Immutable] => _cQueryDeposit.invert(bytes).get
    case Some(c) if c == classOf[QueryDepositResult.Immutable] => _cQueryDepositResult.invert(bytes).get
    case Some(c) if c == classOf[QueryMarketDepth.Immutable] => _cQueryMarketDepth.invert(bytes).get
    case Some(c) if c == classOf[QueryMarketDepthResult.Immutable] => _cQueryMarketDepthResult.invert(bytes).get
    case Some(c) if c == classOf[QueryOrder.Immutable] => _cQueryOrder.invert(bytes).get
    case Some(c) if c == classOf[QueryOrderResult.Immutable] => _cQueryOrderResult.invert(bytes).get
    case Some(c) if c == classOf[QueryTransaction.Immutable] => _cQueryTransaction.invert(bytes).get
    case Some(c) if c == classOf[QueryTransactionResult.Immutable] => _cQueryTransactionResult.invert(bytes).get
    case Some(c) if c == classOf[QueryWithdrawal.Immutable] => _cQueryWithdrawal.invert(bytes).get
    case Some(c) if c == classOf[QueryWithdrawalResult.Immutable] => _cQueryWithdrawalResult.invert(bytes).get
    case Some(c) if c == classOf[RegisterUserFailed.Immutable] => _cRegisterUserFailed.invert(bytes).get
    case Some(c) if c == classOf[RegisterUserSucceeded.Immutable] => _cRegisterUserSucceeded.invert(bytes).get
    case Some(c) if c == classOf[RequestCashDepositFailed.Immutable] => _cRequestCashDepositFailed.invert(bytes).get
    case Some(c) if c == classOf[RequestCashDepositSucceeded.Immutable] => _cRequestCashDepositSucceeded.invert(bytes).get
    case Some(c) if c == classOf[RequestCashWithdrawalFailed.Immutable] => _cRequestCashWithdrawalFailed.invert(bytes).get
    case Some(c) if c == classOf[RequestCashWithdrawalSucceeded.Immutable] => _cRequestCashWithdrawalSucceeded.invert(bytes).get
    case Some(c) if c == classOf[RequestPasswordResetFailed.Immutable] => _cRequestPasswordResetFailed.invert(bytes).get
    case Some(c) if c == classOf[RequestPasswordResetSucceeded.Immutable] => _cRequestPasswordResetSucceeded.invert(bytes).get
    case Some(c) if c == classOf[ResetPasswordFailed.Immutable] => _cResetPasswordFailed.invert(bytes).get
    case Some(c) if c == classOf[ResetPasswordSucceeded.Immutable] => _cResetPasswordSucceeded.invert(bytes).get
    case Some(c) if c == classOf[SubmitOrderFailed.Immutable] => _cSubmitOrderFailed.invert(bytes).get
    case Some(c) if c == classOf[TakeSnapshotNow.Immutable] => _cTakeSnapshotNow.invert(bytes).get
    case Some(c) if c == classOf[UpdateUserProfileFailed.Immutable] => _cUpdateUserProfileFailed.invert(bytes).get
    case Some(c) if c == classOf[UpdateUserProfileSucceeded.Immutable] => _cUpdateUserProfileSucceeded.invert(bytes).get
    case Some(c) if c == classOf[ValidatePasswordResetToken.Immutable] => _cValidatePasswordResetToken.invert(bytes).get
    case Some(c) if c == classOf[VerifyGoogleAuthCode.Immutable] => _cVerifyGoogleAuthCode.invert(bytes).get
    case Some(c) if c == classOf[TAccountState.Immutable] => _cTAccountState.invert(bytes).get
    case Some(c) if c == classOf[TApiSecretState.Immutable] => _cTApiSecretState.invert(bytes).get
    case Some(c) if c == classOf[TExportToMongoState.Immutable] => _cTExportToMongoState.invert(bytes).get
    case Some(c) if c == classOf[TMarketDepthState.Immutable] => _cTMarketDepthState.invert(bytes).get
    case Some(c) if c == classOf[TMarketState.Immutable] => _cTMarketState.invert(bytes).get
    case Some(c) if c == classOf[TUserState.Immutable] => _cTUserState.invert(bytes).get

    case Some(c) => throw new IllegalArgumentException("Cannot deserialize class: " + c.getCanonicalName)
    case None => throw new IllegalArgumentException("No class found in EventSerializer when deserializing array: " + bytes.mkString("").take(100))
  }
}
