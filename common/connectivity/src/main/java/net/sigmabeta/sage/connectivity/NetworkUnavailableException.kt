package net.sigmabeta.sage.connectivity

import java.io.IOException

class NetworkUnavailableException(
    val networkStatus: NetworkStatus,
    message: String,
) : IOException(message)
