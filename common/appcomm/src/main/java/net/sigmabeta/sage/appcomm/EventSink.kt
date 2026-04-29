package net.sigmabeta.sage.appcomm

fun interface EventSink {
    fun sendEvent(event: VglsEvent)
}
