package net.sigmabeta.sage.analytics

import net.sigmabeta.sage.appcomm.SageAction

fun SageAction.isInitAction() = when (this) {
    is SageAction.InitNoArgs,
    is SageAction.InitWithId,
    is SageAction.InitWithString,
    is SageAction.InitWithPageNumber -> true

    else -> false
}

fun SageAction.getDetails(): String? {
    if (!isInitAction()) {
        return null
    }

    return when (this) {
        is SageAction.InitNoArgs -> null
        is SageAction.InitWithId -> id.toString()
        is SageAction.InitWithString -> arg
        is SageAction.InitWithPageNumber -> "$id:$pageNumber"
        else -> null
    }
}
