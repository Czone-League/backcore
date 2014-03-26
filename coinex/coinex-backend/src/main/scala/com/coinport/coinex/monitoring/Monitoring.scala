/**
 * Copyright (C) 2014 Coinport Inc. <http://www.coinport.com>
 *
 */

package com.coinport.coinex.monitoring

import scala.concurrent.duration._
import akka.actor._
import akka.util.Timeout
import scala.util.{ Success, Failure }
import akka.pattern._
import spray.routing._
import spray.routing.directives._
import spray.can.Http
import spray.httpx.marshalling.Marshaller
import spray.httpx.encoding.Gzip
import spray.util._
import spray.http._
import MediaTypes._
import HttpHeaders._
import reflect.ClassTag

import org.json4s._
import native.Serialization.{ read, write => swrite }

import com.coinport.coinex.LocalRouters
import com.coinport.coinex.data._

/**
 * TODO(d): finish this class.
 */
class Monitor(actorPaths: List[ActorPath]) extends Actor with HttpService with spray.httpx.SprayJsonSupport {
  val actorRefFactory = context
  implicit def executionContext = context.dispatcher
  implicit val formats = native.Serialization.formats(NoTypeHints)
  implicit val timeout: Timeout = 1 second

  def receive = runRoute(route)

  val route: Route = {
    get {
      pathSingleSlash {
        val lists = actorPaths.map { path =>
          val s = path.toString
          "<li><a href=\"/stats/actor?path=%s\">%s</a></li>".format(s, s)
        }.mkString

        val html = "<html><body><ui>" + lists + "</ui></body></html>"
        respondWithMediaType(`text/html`) { complete(html) }

      } ~ path("stats" / "actor") {
        parameter("path") { path =>
          respondWithMediaType(`application/json`) {
            onComplete(context.actorSelection(path) ? QueryActorStats) {
              case Success(stats: AnyRef) => complete(swrite(stats))
              case Success(v) => complete("" + v)
              case Failure(e) => failWith(e)
            }
          }
        }
      } ~ path("config") {
        complete("TODO")
      }
    }
  }
}