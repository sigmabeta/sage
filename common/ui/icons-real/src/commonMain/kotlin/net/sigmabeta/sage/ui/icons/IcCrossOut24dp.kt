package net.sigmabeta.sage.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import net.sigmabeta.sage.ui.SageMaterialVectors

sealed interface CrossOutColor {
    data object Line : CrossOutColor
    data object Halo : CrossOutColor
}

fun SageMaterialVectors.IcCrossOut24dp(
    colors: Map<CrossOutColor, Color> = emptyMap(),
): ImageVector {
    val line = colors[CrossOutColor.Line] ?: Color.Black
    val halo = colors[CrossOutColor.Halo] ?: Color.Black
    return Builder(
        name = "IcCrossOut24dp",
        defaultWidth = 13.0.dp,
        defaultHeight = 13.0.dp,
        viewportWidth = 100.0f,
        viewportHeight = 100.0f,
    ).apply {
        path(
            fill = SolidColor(line),
            stroke = null,
            strokeLineWidth = 0.0f,
            strokeLineCap = Butt,
            strokeLineJoin = Miter,
            strokeLineMiter = 4.0f,
            pathFillType = NonZero,
        ) {
            moveTo(13.0f, 17.0f)
            lineTo(83.0f, 87.0f)
            lineTo(87.0f, 83.0f)
            lineTo(17.0f, 13.0f)
            close()
        }
        path(
            fill = SolidColor(halo),
            stroke = null,
            strokeLineWidth = 0.0f,
            strokeLineCap = Butt,
            strokeLineJoin = Miter,
            strokeLineMiter = 4.0f,
            pathFillType = NonZero,
        ) {
            moveTo(13.0f, 17.0f)
            lineTo(83.0f, 87.0f)
            lineTo(79.0f, 91.0f)
            lineTo(9.0f, 21.0f)
            close()
        }
    }.build()
}
