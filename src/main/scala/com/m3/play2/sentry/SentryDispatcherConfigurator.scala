package com.m3.play2.sentry

import java.util.concurrent.TimeUnit

import akka.dispatch.{DispatcherPrerequisites, MessageDispatcher, MessageDispatcherConfigurator}
import com.typesafe.config.Config

import scala.concurrent.duration.Duration

/**
  * Custom akka.dispatch.DispatcherConfigurator to use SentryDispatcher instead of Dispatcher
  */
class SentryDispatcherConfigurator(config: Config, prerequisites: DispatcherPrerequisites)
  extends MessageDispatcherConfigurator(config, prerequisites) {

  // Copy from link akka.dispatch.DispatcherConfigurator
  private val instance = new SentryDispatcher(
    _configurator = this,
    id = config.getString("id"),
    throughput = config.getInt("throughput"),
    throughputDeadlineTime = Duration(config.getDuration("throughput-deadline-time", TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS),
    executorServiceFactoryProvider = configureExecutor(),
    shutdownTimeout = Duration(config.getDuration("shutdown-timeout", TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS))

  override def dispatcher(): MessageDispatcher = instance
}
