/**
 * Copyright (C) 2014 Coinport Inc. <http://www.coinport.com>
 *
 *
 * MarketManager is the maintainer of a Market. It executes new orders before
 * they are added into a market as pending orders. As execution results, a list
 * of Transactions are generated and returned.
 *
 * MarketManager can be used by an Akka persistent processor or a view
 * to reflect pending orders and market depth.
 *
 * Note this class does NOT depend on event-sourcing framework we choose. Please
 * keep it plain old scala/java.
 */

package com.coinport.coinex.users

import com.coinport.coinex.data._
import com.coinport.coinex.common.Manager
import com.coinport.coinex.util._
import com.google.common.io.BaseEncoding

class UserManager(googleAuthenticator: GoogleAuthenticator, secret: String = "") extends Manager[UserState](UserState()) {

  def getUser(email: String) = state.profileMap.get(computeUserId(email))
  def isEmailRegistered(email: String) = getUser(email).isDefined

  def regulateProfile(profile: UserProfile, password: String, salt: Long): UserProfile = {
    val id = computeUserId(profile.email)
    val verificationToken = computeHexToken(salt, profile.email)
    val passwordHash = computePassword(id, profile.email, password)
    val googleAuthenticatorSecret = googleAuthenticator.createSecret

    profile.copy(
      id = id,
      emailVerified = false,
      passwordHash = Some(passwordHash),
      passwordResetToken = None,
      verificationToken = Some(verificationToken),
      loginToken = None,
      googleAuthenticatorSecret = None,
      status = UserStatus.Normal)
  }

  def registerUser(profile: UserProfile): UserProfile = {
    state = state.addUserProfile(profile).addVerificationToken(profile.verificationToken.get, profile.id)
    profile
  }

  def updateUser(profile: UserProfile): UserProfile = {
    state = state.addUserProfile(profile)
    profile
  }

  def requestPasswordReset(email: String, salt: Long): UserProfile = {
    val id = computeUserId(email)
    val profile = state.profileMap(id)
    val token = computeHexToken(salt, email)
    val updatedProfile = profile.copy(passwordResetToken = Some(token))
    state = state.updateUserProfile(id)(_ => updatedProfile).addPasswordResetToken(token, id)
    updatedProfile
  }

  def resetPassword(email: String, password: String, passwordResetToken: Option[String]): UserProfile = {
    val id = computeUserId(email)
    val profile = state.profileMap(id)
    profile.passwordResetToken foreach { token => state = state.deletePasswordResetToken(token) }
    val passwordHash = computePassword(profile.id, profile.email, password)
    val updatedProfile = profile.copy(passwordHash = Some(passwordHash), passwordResetToken = None)
    state = state.updateUserProfile(id)(_ => updatedProfile)
    updatedProfile
  }

  def checkLogin(email: String, password: String): Either[ErrorCode, UserProfile] =
    getUser(email) match {
      case None => Left(ErrorCode.UserNotExist)
      case Some(profile) =>
        val passwordHash = computePassword(profile.id, profile.email, password)
        if (Some(passwordHash) == profile.passwordHash) Right(profile)
        else Left(ErrorCode.PasswordNotMatch)
    }

  private val userIdSecret = MHash.sha256Base64(secret + "userIdSecret")
  private val passwordSecret = MHash.sha256Base64(secret + "passwordSecret")
  private val hexTokenSecret = MHash.sha256Base64(secret + "hexTokenSecret")
  private val numericTokenSecret = MHash.sha256Base64(secret + "numericTokenSecret")

  private def computeUserId(email: String) = MHash.sha256ThenMurmur3(email + userIdSecret)

  private def computePassword(userId: Long, email: String, password: String) =
    MHash.sha256Base64(email + passwordSecret + MHash.sha256Base64(userId + password.trim + passwordSecret))

  private def computeHexToken(salt: Long, email: String): String =
    MHash.sha256Base32(email + salt + hexTokenSecret).substring(0, 40)

  private def newNumericToken(salt: Long, email: String): String = {
    val num = MHash.sha256ThenMurmur3(email + salt + numericTokenSecret)
    "%04d".format(Math.abs(num)).substring(0, 6)

  }
}
