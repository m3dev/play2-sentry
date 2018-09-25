package com.m3.play2.sentry

import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.Future

class SentryLoggingFilter extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    SentryDispatcher.httpInterfaceValue.value = Option(PlayHttpInterface(requestHeader))
    nextFilter(requestHeader)
  }

}
