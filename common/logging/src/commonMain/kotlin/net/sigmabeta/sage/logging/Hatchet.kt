package net.sigmabeta.sage.logging

interface Hatchet {
    fun v(message: String)

    fun d(message: String)

    fun i(message: String)

    fun w(message: String)

    fun e(message: String)
    fun log(severity: Int, message: String)

    /** Last 16 messages logged at severity ≥ ERROR, oldest first. Empty for impls that opt out. */
    val recentErrors: List<HatchetError>
        get() = emptyList()
}
