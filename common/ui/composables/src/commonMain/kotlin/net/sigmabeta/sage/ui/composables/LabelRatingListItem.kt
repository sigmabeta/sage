package net.sigmabeta.sage.ui.composables
import net.sigmabeta.sage.ui.SageComponentStringId

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import net.sigmabeta.sage.ui.composables.subs.LabeledThingy
import net.sigmabeta.sage.ui.composables.subs.Rating
import net.sigmabeta.sage.ui.composables.text
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.components.LabelRatingStarListModel

@Composable
fun LabelRatingListItem(
    model: LabelRatingStarListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    LabeledThingy(
        label = model.label,
        thingy = {
            Rating(
                score = model.value,
                modifier = Modifier
            )
        },
        onClick = { actionSink.sendAction(model.clickAction) },
        onClickLabel = SageComponentStringId.ACCY_OCL_RATING.text(),
        modifier = modifier.semantics {
            stateDescription = "${model.value} out of 4"
        },
        padding = padding,
    )
}
