package net.sigmabeta.sage.list

import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.TitleBarModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class ListStateActual(
    val columnType: ColumnType = ColumnType.One,
    val title: TitleBarModel = TitleBarModel(),
    val listItems: ImmutableList<ListModel> = emptyList<ListModel>().toImmutableList(),
    val paginationType: PaginationType = PaginationType.None,
)
