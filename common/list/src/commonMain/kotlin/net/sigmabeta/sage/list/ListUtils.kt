package net.sigmabeta.sage.list

import net.sigmabeta.sage.components.ErrorStateListModel
import net.sigmabeta.sage.components.HorizontalScrollerListModel
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.NoopListModel

fun checkForDupes(items: List<ListModel>) {
    val duplicateIds = items
        .groupingBy { it.dataId }
        .eachCount()
        .filter { it.value > 1 }

    if (duplicateIds.isNotEmpty()) {
        val duplicatesAsString = duplicateIds
            .toList()
            .joinToString("\n") { pair ->
                val item = items.firstOrNull { it.dataId == pair.first }
                "ID ${pair.first} - ${pair.second} times\n" +
                    "Details: $item"
            }

        val message = "Duplicate ids found.\n$duplicatesAsString"
        throw IllegalArgumentException(message)
    }

    val horizScrollers = items.filterIsInstance<HorizontalScrollerListModel>()
    horizScrollers.forEach {
        checkForDupes(it.scrollingItems)
    }
}

fun ifTrue(condition: Boolean, content: () -> ListModel): ListModel = if (condition) {
        content()
    } else {
        NoopListModel
    }

fun <InputType> ifNotNull(input: InputType?, content: (InputType) -> ListModel): ListModel = if (input != null) {
        content(input)
    } else {
        NoopListModel
    }

fun ListStateActual.getErrors(): List<ErrorStateListModel> {
    val horizScrollers = listItems.filterIsInstance<HorizontalScrollerListModel>()
    val scrollerErrorItems = horizScrollers
        .flatMap { it.scrollingItems.filterIsInstance<ErrorStateListModel>() }

    return listItems.filterIsInstance<ErrorStateListModel>() + scrollerErrorItems
}
