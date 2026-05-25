package net.sigmabeta.sage.ui.list

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

/**
 * Extra bottom inset that scrollable list/grid content should reserve, beyond the system
 * navigation bar inset — e.g. for a floating mini-player or other persistent bottom UI.
 *
 * Apps provide a value (typically an animated [androidx.compose.ui.unit.Dp]) near their
 * scaffold; [ListScreen] and [GridScreen] add it to their `contentPadding.bottom` so the
 * user can scroll the last items above whatever is floating below. Defaults to `0.dp`.
 */
val LocalListBottomInset = compositionLocalOf { 0.dp }
