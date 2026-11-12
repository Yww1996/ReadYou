package me.ash.reader.ui.component.glass

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import me.ash.reader.infrastructure.preference.LocalDarkTheme

/** Shared glass treatment for navigation and reading controls, with an opaque fallback. */
@Composable
fun Modifier.liquidGlassSurface(
    backdrop: GraphicsLayer?,
    backdropOrigin: Offset,
    cornerRadius: Dp,
    fallbackColor: Color,
): Modifier {
    val shader = rememberLiquidGlassShader()
    val blurLayer = rememberGraphicsLayer()
    val panelLayer = rememberGraphicsLayer()
    val density = LocalDensity.current.density
    val isDark = LocalDarkTheme.current.isDarkTheme()
    var origin by remember { mutableStateOf(Offset.Zero) }
    if (!liquidGlassSupported || backdrop == null || shader == null) {
        return background(fallbackColor, RoundedCornerShape(cornerRadius))
    }
    return onGloballyPositioned { origin = it.positionInRoot() }
        .drawWithContent {
            runCatching {
                drawLiquidGlass(
                    backdrop = backdrop,
                    blurLayer = blurLayer,
                    panelLayer = panelLayer,
                    shader = shader,
                    style = LiquidGlassStyle(cornerRadiusDp = cornerRadius.value, isDark = isDark),
                    sampleOrigin = origin - backdropOrigin,
                    density = density,
                )
            }.onFailure {
                Log.e("LiquidGlass", "Panel rendering failed", it)
                drawRoundRect(
                    color = fallbackColor,
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                )
            }
            drawContent()
        }
}
