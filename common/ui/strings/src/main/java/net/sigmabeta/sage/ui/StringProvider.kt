package net.sigmabeta.sage.ui

interface StringProvider {
    fun getString(string: SageStringId): String
    fun getStringOneArg(string: SageStringId, arg: String): String
    fun getStringOneInt(string: SageStringId, arg: Int): String
    fun getStringTwoArgs(string: SageStringId, first: String, second: String): String
}
