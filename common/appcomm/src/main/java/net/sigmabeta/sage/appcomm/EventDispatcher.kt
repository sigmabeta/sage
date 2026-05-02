package net.sigmabeta.sage.appcomm

interface EventDispatcher {
    val sendEvent: (SageEvent) -> Unit
    fun addEventSink(sink: EventSink)
    fun removeEventSink(sink: EventSink)
}
