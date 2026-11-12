package me.ash.reader.ui.component

import android.os.Build
import android.view.SoundEffectConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import me.ash.reader.domain.model.general.Filter
import me.ash.reader.infrastructure.preference.FlowFilterBarStylePreference
import me.ash.reader.infrastructure.preference.LocalDarkTheme
import me.ash.reader.infrastructure.preference.LocalThemeIndex
import me.ash.reader.ui.component.glass.LiquidGlassStyle
import me.ash.reader.ui.component.glass.drawLiquidGlass
import me.ash.reader.ui.component.glass.liquidGlassSupported
import me.ash.reader.ui.component.glass.liquidGlassSurface
import me.ash.reader.ui.component.glass.rememberLiquidGlassShader
import me.ash.reader.ui.ext.surfaceColorAtElevation
import me.ash.reader.ui.theme.palette.onDark

@Composable
fun FilterBar(
    modifier: Modifier = Modifier,
    filter: Filter,
    filterBarStyle: Int,
    filterBarFilled: Boolean,
    filterBarPadding: Dp,
    filterBarTonalElevation: Dp,
    /** 页面内容录制层：给了就用液态玻璃折射，不给就走原来的 Surface */
    backdrop: GraphicsLayer? = null,
    backdropOrigin: Offset = Offset.Zero,
    filterOnClick: (Filter) -> Unit = {},
) {
    val view = LocalView.current
    val themeIndex = LocalThemeIndex.current
    val indicatorColor = if (themeIndex == 5 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.primaryContainer
    } onDark MaterialTheme.colorScheme.secondaryContainer

    val useGlass = liquidGlassSupported && backdrop != null
    val containerHeight = when {
        useGlass && filterBarStyle == FlowFilterBarStylePreference.Icon.value -> 52.dp
        useGlass -> 60.dp
        filterBarStyle == FlowFilterBarStylePreference.Icon.value -> 64.dp
        else -> 80.dp
    }

    val shader = rememberLiquidGlassShader()
    val density = LocalDensity.current.density
    val isDark = LocalDarkTheme.current.isDarkTheme()

    // —— 滑动玻璃胶囊：跟随选中项做弹性位移，每帧按自身位置重采样背板 ——
    var selfOrigin by remember { mutableStateOf(Offset.Zero) }
    val itemBounds = remember { mutableStateMapOf<Filter, Rect>() }
    var pillLeft by remember { mutableFloatStateOf(0f) }
    var pillWidth by remember { mutableFloatStateOf(0f) }
    var pillHeight by remember { mutableFloatStateOf(0f) }
    val selectedBounds = itemBounds[filter]
    LaunchedEffect(selectedBounds, selfOrigin) {
        selectedBounds?.let {
            // 胶囊比 tab 稍小一圈，落在栏内不顶到上下边
            pillLeft = it.left - selfOrigin.x + it.width * 0.07f
            pillWidth = it.width * 0.86f
            pillHeight = it.height * 0.74f
        }
    }
    val pillSpec = spring<Float>(dampingRatio = 0.62f, stiffness = 300f)
    val animLeft by animateFloatAsState(pillLeft, pillSpec)
    val animWidth by animateFloatAsState(pillWidth, pillSpec)
    val pillBlurLayer = rememberGraphicsLayer()
    val pillPanelLayer = rememberGraphicsLayer()

    val glassModifier = if (useGlass) {
        Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 10.dp)
            .onGloballyPositioned { selfOrigin = it.positionInRoot() }
            .liquidGlassSurface(
                backdrop = backdrop,
                backdropOrigin = backdropOrigin,
                cornerRadius = containerHeight / 2,
                fallbackColor = MaterialTheme.colorScheme.surfaceContainer,
                beforeContent = {
                    if (shader != null && animWidth > 0f && pillHeight > 0f) {
                        val top = (size.height - pillHeight) / 2f
                        translate(animLeft, top) {
                            runCatching {
                                drawLiquidGlass(
                                    backdrop = backdrop!!,
                                    blurLayer = pillBlurLayer,
                                    panelLayer = pillPanelLayer,
                                    shader = shader,
                                    style =
                                        LiquidGlassStyle(
                                            cornerRadiusDp = pillHeight / density / 2f,
                                            refractionHeightDp = 12f,
                                            refractionAmountDp = 10f,
                                            chromaticAberration = 0.28f,
                                            blurRadiusDp = 1f,
                                            borderAlpha = 0.45f,
                                            interiorMaskAlpha = 0.05f,
                                            isDark = isDark,
                                        ),
                                    sampleOrigin =
                                        Offset(selfOrigin.x + animLeft, selfOrigin.y + top) -
                                            backdropOrigin,
                                    density = density,
                                    targetSize = Size(animWidth, pillHeight),
                                )
                            }
                        }
                        // 白底上玻璃本身看不出边界，补一根极细的对比描边（暗色下用亮边）
                        drawRoundRect(
                            color = if (isDark) Color.White.copy(alpha = 0.16f) else Color.Black.copy(alpha = 0.07f),
                            topLeft = Offset(animLeft, top),
                            size = Size(animWidth, pillHeight),
                            cornerRadius = CornerRadius(pillHeight / 2f),
                            style = Stroke(width = 1.dp.toPx()),
                        )
                    }
                },
            )
    } else Modifier

    Surface(
        color =
            if (useGlass) Color.Transparent
            else MaterialTheme.colorScheme.surfaceColorAtElevation(filterBarTonalElevation),
        modifier = modifier
    ) {
        Row(
            modifier =
            Modifier
                .then(if (useGlass) Modifier.navigationBarsPadding() else Modifier.windowInsetsPadding(NavigationBarDefaults.windowInsets))
                .then(glassModifier)
                .fillMaxWidth()
                .defaultMinSize(minHeight = containerHeight)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Spacer(modifier = Modifier.width(filterBarPadding))
            Filter.values.forEach { item ->
                NavigationBarItem(
                    modifier = Modifier
                        .height(containerHeight)
                        .onGloballyPositioned { itemBounds[item] = it.boundsInRoot() },
                    alwaysShowLabel = when (filterBarStyle) {
                        FlowFilterBarStylePreference.Icon.value -> false
                        FlowFilterBarStylePreference.IconLabel.value -> true
                        FlowFilterBarStylePreference.IconLabelOnlySelected.value -> false
                        else -> false
                    },
                    icon = {
                        Icon(
                            imageVector = if (filter == item && filterBarFilled) {
                                item.iconFilled
                            } else {
                                item.iconOutline
                            },
                            contentDescription = item.toName()
                        )
                    },
                    label = if (filterBarStyle == FlowFilterBarStylePreference.Icon.value) {
                        null
                    } else {
                        {
                            Text(
                                text = item.toName(),
//                            style = MaterialTheme.typography.labelLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    },
                    selected = filter == item,
                    onClick = {
//                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        filterOnClick(item)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = if (useGlass) Color.Transparent else indicatorColor,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedIconColor = MaterialTheme.colorScheme.contentColorFor(indicatorColor),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
            Spacer(modifier = Modifier.width(filterBarPadding))
        }
    }
}
