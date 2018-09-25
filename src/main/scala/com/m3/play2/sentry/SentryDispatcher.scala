package com.m3.play2.sentry

import java.util

import akka.dispatch._
import org.slf4j.MDC

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util.DynamicVariable

object SentryDispatcher {

  val httpInterfaceValue = new DynamicVariable[Option[PlayHttpInterface]](None)

}

/**
  * Custom Akka dispatcher to propagate thread local values for logging
  */
class SentryDispatcher(
                       _configurator: MessageDispatcherConfigurator,
                       override val id: String,
                       override val throughput: Int,
                       override val throughputDeadlineTime: Duration,
                       executorServiceFactoryProvider: ExecutorServiceFactoryProvider,
                       override val shutdownTimeout: FiniteDuration)
  extends Dispatcher(_configurator, id, throughput, throughputDeadlineTime, executorServiceFactoryProvider, shutdownTimeout) {

  self =>

  override def prepare(): ExecutionContext = new ExecutionContext {
    private val currentHttpInterface = SentryDispatcher.httpInterfaceValue.value
    private val currentMdcContext = Option(MDC.getCopyOfContextMap)

    def execute(r: Runnable): Unit = self.execute(new Runnable {
      def run(): Unit = {
        val oldMDCContext = Option(MDC.getCopyOfContextMap)

        SentryDispatcher.httpInterfaceValue.value = currentHttpInterface
        setContextMap(currentMdcContext)
        try {
          r.run()
        } finally {
          setContextMap(oldMDCContext)
        }
      }
    })

    def reportFailure(t: Throwable): Unit = self.reportFailure(t)
  }

  private[this] def setContextMap(context: Option[util.Map[String, String]]): Unit =
    context.fold(MDC.clear()) { map => MDC.setContextMap(map) }

}

