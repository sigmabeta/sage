package net.sigmabeta.sage.connectivity

import java.io.IOException

class VglsHttpException(
    val code: Int,
    message: String,
) : IOException(message)
