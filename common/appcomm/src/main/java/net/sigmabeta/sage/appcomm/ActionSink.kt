package net.sigmabeta.sage.appcomm

fun interface ActionSink {
    fun sendAction(action: SageAction)
}
