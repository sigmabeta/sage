package net.sigmabeta.sage.events

import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.EventSink
import net.sigmabeta.sage.appcomm.SageEvent

class EventDispatcherReal(
    private val analytics: Analytics,
) : EventDispatcher {
    private val eventSinks = mutableSetOf<EventSink>()

    override val sendEvent: (SageEvent) -> Unit = { event ->
        analytics.logEvent(event)
        sendToSinks(event)
    }

    override fun addEventSink(sink: EventSink) {
        eventSinks.add(sink)
    }

    override fun removeEventSink(sink: EventSink) {
        eventSinks.remove(sink)
    }

    private fun sendToSinks(event: SageEvent) {
        eventSinks.forEach {
            it.sendEvent(event)
        }
    }
}
