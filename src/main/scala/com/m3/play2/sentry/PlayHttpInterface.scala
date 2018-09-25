package com.m3.play2.sentry

import java.net.InetAddress

import io.sentry.event.interfaces.SentryInterface
import play.api.mvc.RequestHeader

import scala.collection.Seq
import scala.util.Try

/**
 * The HTTP interface of an Play2 HTTP request
 */
object PlayHttpInterface {
  private val HTTP_INTERFACE = "sentry.interfaces.Http"

  def apply(request: RequestHeader): PlayHttpInterface = {
    val localHostAddress: Try[InetAddress] = Try(InetAddress.getLocalHost)

    new PlayHttpInterface(
      requestUrl = getRequestFullURL(request),
      method = request.method,
      parameters = request.queryString,
      queryString = request.rawQueryString,
      cookies = request.cookies.map { cookie => (cookie.name, cookie.value) }.toSeq,
      headers = request.headers.toMap,
      remoteAddr = request.remoteAddress,
      serverName = request.host,
      localAddr = localHostAddress.map(_.getHostAddress).getOrElse(""),
      localName = localHostAddress.map(_.getHostName).getOrElse(""),
      secure = request.secure
    )
  }

  private def getRequestFullURL(request: RequestHeader) =
    (if (request.secure) "https" else "http") + "://" + request.host + request.uri
}

/**
 * Creates a an HTTP interface
 */
case class PlayHttpInterface(
        // arguments of HTTP interface
        requestUrl: String,
        method: String,
        parameters: Map[String, Seq[String]],
        queryString: String,
        cookies: Seq[(String, String)],
        headers: Map[String, Seq[String]],

        // environment data
        remoteAddr: String,
        serverName: String,
        localAddr: String,
        localName: String,
        secure: Boolean) extends SentryInterface {

  override def getInterfaceName: String = PlayHttpInterface.HTTP_INTERFACE

  override def toString: String =
    "PlayHttpInterface{" +
    "requestUrl='" + requestUrl + "', " +
    "method='" + method + "', " +
    "parameters=" + parameters + '}'

}