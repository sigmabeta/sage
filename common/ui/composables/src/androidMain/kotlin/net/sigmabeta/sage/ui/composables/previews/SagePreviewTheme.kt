package net.sigmabeta.sage.ui.composables.previews

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import net.sigmabeta.sage.ui.SageComponentStringId
import net.sigmabeta.sage.ui.SageStringId
import net.sigmabeta.sage.ui.StringProvider
import net.sigmabeta.sage.ui.composables.ProvideSageRenderers

/**
 * Preview wrapper for the shared renderer suite.
 *
 * A Material 3 theme in light or dark — following the system when [forceDark] is `false` — plus the
 * [net.sigmabeta.sage.ui.composables.LocalSageStringProvider] the renderers read accessibility
 * labels from. The provider is preview-only: English for the eight [SageComponentStringId]s, and
 * `id.toString()` for anything else, so a preview renders without an app's real string table.
 */
@Composable
internal fun SagePreview(
    forceDark: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (forceDark || isSystemInDarkTheme()) darkColorScheme() else lightColorScheme(),
        typography = Typography(),
    ) {
        ProvideSageRenderers(stringProvider = PreviewStringProvider) {
            content()
        }
    }
}

/** The renderers' 8 labels, plus `id.toString()` for anything an app's own rows might ask for. */
private object PreviewStringProvider : StringProvider {
    private val strings: Map<SageStringId, String> = buildMap {
        put(SageComponentStringId.ACCY_OCL_CHECKBOX, "Toggle checkbox")
        put(SageComponentStringId.ACCY_OCL_DROPDOWN, "Expand dropdown")
        put(SageComponentStringId.ACCY_OCL_RATING, "Rate")
        put(SageComponentStringId.ACCY_OCL_SINGLE_LINE, "Open %1\$s")
        put(SageComponentStringId.ACCY_OCL_VALUE, "Open")
        put(SageComponentStringId.ACCY_ST_DESC_CHECKED, "Checked")
        put(SageComponentStringId.ACCY_ST_DESC_LOADING, "Loading")
        put(SageComponentStringId.ACCY_ST_DESC_UNCHECKED, "Unchecked")
    }

    override fun getString(string: SageStringId): String = strings[string] ?: string.toString()

    override fun getStringOneArg(string: SageStringId, arg: String): String =
        format(getString(string), listOf(arg))

    override fun getStringOneInt(string: SageStringId, arg: Int): String =
        format(getString(string), listOf(arg.toString()))

    override fun getStringTwoArgs(string: SageStringId, first: String, second: String): String =
        format(getString(string), listOf(first, second))
}

/** Substitutes the positional `%1$s` / `%1$d` specifiers the renderers use. */
private fun format(template: String, args: List<String>): String {
    var result = template
    args.forEachIndexed { index, arg ->
        result = result.replace("%${index + 1}\$s", arg).replace("%${index + 1}\$d", arg)
    }
    return result
}
