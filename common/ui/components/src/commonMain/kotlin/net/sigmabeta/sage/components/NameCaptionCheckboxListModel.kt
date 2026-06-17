package net.sigmabeta.sage.components

import net.sigmabeta.sage.appcomm.SageAction

/**
 * A captioned settings toggle: [name] over a smaller [caption], with a trailing checkbox driven by
 * [checked]. Combines [NameCaptionListModel]'s two-line text block with [CheckableListModel]'s
 * checkbox affordance, for settings whose on/off meaning needs a line of explanation.
 *
 * [checked] is nullable so the row can show a loading spinner (`null`) before the backing
 * preference has emitted. Tapping anywhere on the row — label or checkbox — dispatches
 * [clickAction]; the consumer flips the stored value and emits a new model.
 */
data class NameCaptionCheckboxListModel(
    val settingId: String,
    val name: String,
    val caption: String,
    val checked: Boolean?,
    val clickAction: SageAction,
) : ListModel() {
    override val dataId: Long = settingId.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
