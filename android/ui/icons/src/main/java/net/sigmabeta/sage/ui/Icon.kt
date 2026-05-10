package net.sigmabeta.sage.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import net.sigmabeta.sage.ui.icons.IcAlbum24dp
import net.sigmabeta.sage.ui.icons.IcBarChart24dp
import net.sigmabeta.sage.ui.icons.IcCloudDone24dp
import net.sigmabeta.sage.ui.icons.IcCrossOut24dp
import net.sigmabeta.sage.ui.icons.IcDescription24dp
import net.sigmabeta.sage.ui.icons.IcFavoriteEmpty
import net.sigmabeta.sage.ui.icons.IcFavoriteFilled
import net.sigmabeta.sage.ui.icons.IcOutlineCloudDownload24dp
import net.sigmabeta.sage.ui.icons.IcPlayCircleFilled24
import net.sigmabeta.sage.ui.icons.IcRemove24dp
import net.sigmabeta.sage.ui.icons.IcTagBlack24dp

fun Icon.vector(): ImageVector = when (this) {
        Icon.ALBUM -> SageMaterialVectors.IcAlbum24dp
        Icon.BACK -> Icons.AutoMirrored.Default.ArrowBack
        Icon.BROWSE -> Icons.AutoMirrored.Default.List
        Icon.CALENDAR -> Icons.Default.DateRange
        Icon.CLEAR -> Icons.Default.Clear
        Icon.CROSSOUT -> SageMaterialVectors.IcCrossOut24dp
        Icon.DESCRIPTION -> SageMaterialVectors.IcDescription24dp
        Icon.DIFFICULTY -> SageMaterialVectors.IcBarChart24dp
        Icon.FORWARD -> Icons.AutoMirrored.Default.ArrowForward
        Icon.HOME -> Icons.Default.Home
        Icon.FAVORITE_EMPTY -> SageMaterialVectors.IcFavoriteEmpty
        Icon.FAVORITE_FILLED -> SageMaterialVectors.IcFavoriteFilled
        Icon.MINUS -> SageMaterialVectors.IcRemove24dp
        Icon.MUSIC_NOTE -> Icons.Default.MusicNote
        Icon.OFFLINE_FILLED -> SageMaterialVectors.IcCloudDone24dp
        Icon.OFFLINE_OUTLINE -> SageMaterialVectors.IcOutlineCloudDownload24dp
        Icon.PERSON -> Icons.Default.Person
        Icon.PLUS -> Icons.Default.Add
        Icon.REFRESH -> Icons.Default.Refresh
        Icon.SEARCH -> Icons.Default.Search
        Icon.SEARCH_YOUTUBE -> SageMaterialVectors.IcPlayCircleFilled24
        Icon.TAG -> SageMaterialVectors.IcTagBlack24dp
        Icon.WARNING -> Icons.Default.Warning
    }
