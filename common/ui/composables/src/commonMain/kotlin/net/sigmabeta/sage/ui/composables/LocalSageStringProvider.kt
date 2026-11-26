package net.sigmabeta.sage.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import net.sigmabeta.sage.ui.SageStringId
import net.sigmabeta.sage.ui.StringProvider

/**
 * Composable access to the app's shared [StringProvider].
 *
 * Each app's composition root installs its provider just above the navigation host, so a renderer
 * can resolve an accessibility label without reaching into DI. The local is static because the
 * provider never changes for the lifetime of the tree.
 */
val LocalSageStringProvider = staticCompositionLocalOf<StringProvider> {
    error(
        "LocalSageStringProvider not provided. Wrap the Compose tree in " +
            "CompositionLocalProvider(LocalSageStringProvider provides stringProvider) { ... }",
    )
}

@Composable
@ReadOnlyComposable
fun SageStringId.text(): String = LocalSageStringProvider.current.getString(this)

@Composable
@ReadOnlyComposable
fun SageStringId.text(arg: String): String =
    LocalSageStringProvider.current.getStringOneArg(this, arg)

@Composable
@ReadOnlyComposable
fun SageStringId.textInt(arg: Int): String =
    LocalSageStringProvider.current.getStringOneInt(this, arg)

@Composable
@ReadOnlyComposable
fun SageStringId.text(first: String, second: String): String =
    LocalSageStringProvider.current.getStringTwoArgs(this, first, second)
