package net.sigmabeta.sage.components

sealed class ListModel {
    abstract val dataId: Long
    abstract val columns: Int

    fun layoutId(): String = this.javaClass.name

    companion object {
        const val COLUMNS_ALL = -1
    }
}
