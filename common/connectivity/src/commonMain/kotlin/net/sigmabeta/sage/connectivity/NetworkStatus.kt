package net.sigmabeta.sage.connectivity

enum class NetworkStatus {
    OFFLINE,

    /** Network transport is up but internet has not been validated (captive portal, broken backhaul, etc.). */
    ONLINE_NO_INTERNET,

    ONLINE,

    /** OS-validated internet, but the configured API failed to respond to a probe. */
    ONLINE_API_UNREACHABLE,
}

val NetworkStatus.allowsApiRequests: Boolean
    get() = this == NetworkStatus.ONLINE
