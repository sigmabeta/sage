package net.sigmabeta.sage.wakelocks

interface WakeLockManager {
    fun keepScreenOn()

    fun allowScreenOff()
}
