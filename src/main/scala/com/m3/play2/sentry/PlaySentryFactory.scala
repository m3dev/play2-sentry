package com.m3.play2.sentry

import io.sentry._
import io.sentry.dsn.Dsn
import io.sentry.marshaller.Marshaller
import io.sentry.marshaller.json.JsonMarshaller

/**
 * Custom sentry client factory for Play
 *
 * You need specify this class in the sentry settings (default sentry.properties file on classpath)
 */
class PlaySentryFactory extends DefaultSentryClientFactory {

  override def createSentryClient(dsn: Dsn): SentryClient = {
    val sentry = super.createSentryClient(dsn)
    sentry.addBuilderHelper(new PlayHttpEventBuilderHelper)
    sentry
  }

  override protected def createMarshaller(dsn: Dsn): Marshaller = {
    val marshaller = super.createMarshaller(dsn).asInstanceOf[JsonMarshaller]
    marshaller.addInterfaceBinding(classOf[PlayHttpInterface], new PlayHttpInterfaceBinding)
    marshaller
  }
}