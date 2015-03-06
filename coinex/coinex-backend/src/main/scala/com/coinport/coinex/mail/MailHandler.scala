/**
 * Copyright (C) 2014 Coinport Inc. <http://www.coinport.com>
 *
 */

package com.coinport.coinex.mail

trait MailHandler {
  def sendRegistrationEmailConfirmation(to: String, params: Seq[(String, String)], v: Option[String], lang: Option[String]): Unit
  def sendLoginToken(to: String, params: Seq[(String, String)]): Unit
  def sendPasswordReset(to: String, params: Seq[(String, String)], v: Option[String], lang: Option[String]): Unit
  def sendMonitor(to: String, params: Seq[(String, String)]): Unit
  def sendVerificationCodeEmail(to: String, params: Seq[(String, String)], v: Option[String], lang: Option[String]): Unit
  def sendWithdrawalNotification(to: String, params: Seq[(String, String)], v: Option[String], lang: Option[String]): Unit
}
