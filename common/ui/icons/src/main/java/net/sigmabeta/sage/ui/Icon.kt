package net.sigmabeta.sage.ui

sealed interface Icon {
    data object Album : Icon
    data object Back : Icon
    data object Browse : Icon
    data object Calendar : Icon
    data object Chip : Icon
    data object Clear : Icon
    data object Computer : Icon
    data object CrossOut : Icon
    data object CrossOutError : Icon
    data object CrossOutMenu : Icon
    data object Description : Icon
    data object Difficulty : Icon
    data object Forward : Icon
    data object Home : Icon
    data object FavoriteEmpty : Icon
    data object FavoriteFilled : Icon
    data object Minus : Icon
    data object MusicNote : Icon
    data object OfflineFilled : Icon
    data object OfflineOutline : Icon
    data object Person : Icon
    data object Plus : Icon
    data object Refresh : Icon
    data object Search : Icon
    data object SearchYoutube : Icon
    data object Shuffle : Icon
    data object Tag : Icon
    data object Warning : Icon
}
