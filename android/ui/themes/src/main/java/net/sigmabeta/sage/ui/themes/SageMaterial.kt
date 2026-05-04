package net.sigmabeta.sage.ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

@Composable
fun SageMaterial(
    lightColors: ColorScheme,
    darkColors: ColorScheme,
    typography: Typography,
    forceDark: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colors = if (!isSystemInDarkTheme() && !forceDark) {
        lightColors
    } else {
        darkColors
    }

    MaterialTheme(
        typography = typography,
        colorScheme = colors,
        content = content,
    )
}

@Composable
fun SageMaterialMenu(
    menuColors: ColorScheme,
    typography: Typography,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        typography = typography,
        colorScheme = menuColors,
        content = content,
    )
}
