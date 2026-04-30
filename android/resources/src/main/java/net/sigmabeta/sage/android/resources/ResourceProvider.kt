package net.sigmabeta.sage.android.resources

interface ResourceProvider {
    fun getString(id: Int, vararg formatArgs: Any?): String
}
