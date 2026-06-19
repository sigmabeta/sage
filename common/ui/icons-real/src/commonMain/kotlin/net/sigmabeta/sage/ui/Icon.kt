package net.sigmabeta.sage.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.FeaturedPlayList
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import net.sigmabeta.sage.ui.icons.IcAlbum24dp
import net.sigmabeta.sage.ui.icons.IcBarChart24dp
import net.sigmabeta.sage.ui.icons.IcCloudDone24dp
import net.sigmabeta.sage.ui.icons.IcCrossOut24dp
import net.sigmabeta.sage.ui.icons.IcCrossOutError24dp
import net.sigmabeta.sage.ui.icons.IcCrossOutMenu24dp
import net.sigmabeta.sage.ui.icons.IcDescription24dp
import net.sigmabeta.sage.ui.icons.IcFavoriteEmpty
import net.sigmabeta.sage.ui.icons.IcFavoriteFilled
import net.sigmabeta.sage.ui.icons.IcOutlineCloudDownload24dp
import net.sigmabeta.sage.ui.icons.IcPlayCircleFilled24
import net.sigmabeta.sage.ui.icons.IcRemove24dp
import net.sigmabeta.sage.ui.icons.IcTagBlack24dp

fun Icon.vector(): ImageVector = when (this) {
    is Icon.Album -> SageMaterialVectors.IcAlbum24dp
    is Icon.Back -> Icons.AutoMirrored.Default.ArrowBack
    is Icon.Browse -> Icons.AutoMirrored.Default.List
    is Icon.Calendar -> Icons.Default.DateRange
    is Icon.Caret -> Icons.Default.KeyboardArrowDown
    is Icon.Chip -> Icons.Default.Memory
    is Icon.Clear -> Icons.Default.Clear
    is Icon.Computer -> Icons.Default.Computer
    is Icon.CrossOut -> SageMaterialVectors.IcCrossOut24dp()
    is Icon.CrossOutError -> SageMaterialVectors.IcCrossOutError24dp()
    is Icon.CrossOutMenu -> SageMaterialVectors.IcCrossOutMenu24dp()
    is Icon.Description -> SageMaterialVectors.IcDescription24dp
    is Icon.Difficulty -> SageMaterialVectors.IcBarChart24dp
    is Icon.Forward -> Icons.AutoMirrored.Default.ArrowForward
    is Icon.Home -> Icons.Default.Home
    is Icon.FavoriteEmpty -> SageMaterialVectors.IcFavoriteEmpty
    is Icon.FavoriteFilled -> SageMaterialVectors.IcFavoriteFilled
    is Icon.Library -> Icons.Default.LibraryMusic
    is Icon.Menu -> Icons.Default.Menu
    is Icon.Minus -> SageMaterialVectors.IcRemove24dp
    is Icon.MusicNote -> Icons.Default.MusicNote
    is Icon.OfflineFilled -> SageMaterialVectors.IcCloudDone24dp
    is Icon.OfflineOutline -> SageMaterialVectors.IcOutlineCloudDownload24dp
    is Icon.Overflow -> Icons.Default.MoreVert
    is Icon.Pause -> Icons.Default.Pause
    is Icon.Person -> Icons.Default.Person
    is Icon.Play -> Icons.Default.PlayArrow
    is Icon.Plus -> Icons.Default.Add
    is Icon.QueueMusic -> Icons.AutoMirrored.Default.QueueMusic
    is Icon.Refresh -> Icons.Default.Refresh
    is Icon.Repeat -> Icons.Default.Repeat
    is Icon.RepeatOne -> Icons.Default.RepeatOne
    is Icon.Search -> Icons.Default.Search
    is Icon.SearchYoutube -> SageMaterialVectors.IcPlayCircleFilled24
    is Icon.Shuffle -> Icons.Default.Shuffle
    is Icon.SkipNext -> Icons.Default.SkipNext
    is Icon.SkipPrevious -> Icons.Default.SkipPrevious
    is Icon.Tag -> SageMaterialVectors.IcTagBlack24dp
    is Icon.Visibility -> Icons.Default.Visibility
    is Icon.VisibilityOff -> Icons.Default.VisibilityOff
    is Icon.Warning -> Icons.Default.Warning
}
