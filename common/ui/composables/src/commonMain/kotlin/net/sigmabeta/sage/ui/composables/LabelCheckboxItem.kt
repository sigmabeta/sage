package net.sigmabeta.sage.ui.composables
import net.sigmabeta.sage.ui.SageComponentStringId

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import net.sigmabeta.sage.ui.composables.subs.LabeledThingy
import net.sigmabeta.sage.ui.composables.text
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.components.CheckableListModel

@Composable
fun LabelCheckboxItem(
    model: CheckableListModel,
    actionSink: ActionSink,
    modifier: Modifier,
    padding: PaddingValues,
) {
    val accyStateDescription = when (model.checked) {
        true -> SageComponentStringId.ACCY_ST_DESC_CHECKED.text()
        false -> SageComponentStringId.ACCY_ST_DESC_UNCHECKED.text()
        null -> SageComponentStringId.ACCY_ST_DESC_LOADING.text()
    }

    LabeledThingy(
        label = model.name,
        thingy = {
            Crossfade(
                targetState = model.checked,
                label = "CheckboxState"
            ) { checked ->
                when {
                    checked != null -> Checkbox(
                        checked = checked,
                        onCheckedChange = { actionSink.sendAction(model.clickAction) },
                        Modifier.clearAndSetSemantics { }
                    )

                    else -> CircularProgressIndicator(
                        modifier = Modifier
                            .padding(12.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        onClick = { actionSink.sendAction(model.clickAction) },
        onClickLabel = SageComponentStringId.ACCY_OCL_CHECKBOX.text(),
        accyStateDescription = accyStateDescription,
        modifier = modifier,
        padding = padding,
    )
}
