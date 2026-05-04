package net.sigmabeta.sage.connectivity

import java.io.IOException

class HttpException(
    val code: Int,
    message: String,
) : IOException(message)
