package net.sigmabeta.sage.components

// Open (not sealed) so downstream modules can contribute their own [ListModel] subclasses for
// feature-specific renderers without having to live in this package. ComposableMapping (and
// equivalent dispatchers in consumer modules) already fall through `else -> Spacer(...)` for
// unrecognised types, so the sealed-class compile-time guarantee was never load-bearing.
abstract class ListModel {
    abstract val dataId: Long
    abstract val columns: Int

    // Open so a wrapping/decorating model (e.g. a draggable container) can compose its own
    // layout identity with the wrapped content's — letting LazyList contentType discriminate by
    // inner type without aliasing the bare, undecorated version of that type.
    open fun layoutId(): String = runtimeClassName(this)

    companion object {
        const val COLUMNS_ALL = -1
    }
}
