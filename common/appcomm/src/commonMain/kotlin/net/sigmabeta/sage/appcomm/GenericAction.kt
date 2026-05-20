package net.sigmabeta.sage.appcomm

import kotlinx.serialization.Serializable

@Serializable
data class GenericAction(
    val type: String,
    val argIdOne: Long? = null,
    val argIdTwo: Long? = null,
    val argString: String? = null,
)
