package me.ash.reader.ui.component

import android.os.Build
import android.view.SoundEffectConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import me.ash.reader.ui.component.glass.LiquidGlassStyle
import me.ash.reader.ui.component.glass.liquidGlass
import me.ash.reader.ui.component.glass.liquidGlassSupported
import me.ash.reader.ui.component.glass.rememberLiquidGlassShader
import me.ash.reader.infrastructure.preference.LocalDarkTheme
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.ash.reader.domain.model.general.Filter
import me.ash.reader.infrastructure.preference.FlowFilterBarStylePreference
import me.ash.reader.infrastructure.preference.LocalThemeIndex
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

    val containerHeight = when (filterBarStyle) {
        FlowFilterBarStylePreference.Icon.value -> 64.dp
        else -> 80.dp
    }

    val density = LocalDensity.current.density
    val isDark = LocalDarkTheme.current.isDarkTheme()
    var selfOrigin by remember { mutableStateOf(Offset.Zero) }
    val useGlass = liquidGlassSupported && backdrop != null
    val glassModifier =
        if (useGlass) {
            val shader = rememberLiquidGlassShader()
            Modifier
                .onGloballyPositioned { selfOrigin = it.positionInRoot() }
                .liquidGlass(
                    backdrop = backdrop,
                    shader = shader,
                    style =
                        LiquidGlassStyle(
                            cornerRadiusDp = 0f,
                            refractionHeightDp = 26f,
                            refractionAmountDp = 12f,
                            chromatic = 0.9f,
                            borderAlpha = 0.5f,
                            isDark = isDark,
                        ),
                    sampleOrigin = selfOrigin - backdropOrigin,
                    density = density,
                )
        } else Modifier

    Surface(
        color =
            if (useGlass) Color.Transparent
            else MaterialTheme.colorScheme.surfaceColorAtElevation(filterBarTonalElevation),
        modifier = modifier.then(glassModifier)
    ) {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(NavigationBarDefaults.windowInsets)
                .defaultMinSize(minHeight = containerHeight)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Spacer(modifier = Modifier.width(filterBarPadding))
            Filter.values.forEach { item ->
                NavigationBarItem(
                    modifier = Modifier.height(containerHeight),
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
                        indicatorColor = indicatorColor,
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
