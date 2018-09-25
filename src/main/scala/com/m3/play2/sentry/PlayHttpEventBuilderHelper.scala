package com.m3.play2.sentry

import io.sentry.event.EventBuilder
import io.sentry.event.helper.EventBuilderHelper

class PlayHttpEventBuilderHelper() extends EventBuilderHelper {

  override def helpBuildingEvent(eventBuilder: EventBuilder): Unit = {
    val hiOpt = SentryDispatcher.httpInterfaceValue.value
    hiOpt.foreach( eventBuilder.withSentryInterface(_, false) )
  }
}