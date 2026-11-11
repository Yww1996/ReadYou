package me.ash.reader.ui.component.glass

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.IntSize
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot

/**
 * 液态玻璃：对「底下的内容」做边缘折射 + 色散。
 *
 * 着色器是 IT之家 9.38 里 com.iruanmi.liquidglass 的那套 AGSL（原文抠自 classes.dex）：
 * 圆角矩形 SDF → 边缘折射带（circleMap 曲线）→ 7 点采样 RGB 色散 → 左上受光/右下暗边。
 *
 * 合成方式对齐 Android 原生做法：把背板录进 GraphicsLayer（底层是 RenderNode），
 * 给它挂一张 RenderEffect，再整体绘制。RuntimeShader 需要 API 33+。
 */
private const val TAG = "LiquidGlass"

val liquidGlassSupported: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

data class LiquidGlassStyle(
    val cornerRadiusDp: Float,
    val refractionHeightDp: Float = 18f,
    val refractionAmountDp: Float = 13f,
    val chromaticAberration: Float = 0.22f,
    /** 磨砂模糊半径（dp），模糊背板经折射和统一调色后一次合成。 */
    val blurRadiusDp: Float = 1.5f,
    val borderAlpha: Float = 0.18f,
    val interiorMaskAlpha: Float = 0.03f,
    val isDark: Boolean = true,
)

private const val GLASS_AGSL = """
uniform shader content;
uniform float2 size;
uniform float2 offset;
uniform float4 cornerRadii;
uniform float refractionHeight;
uniform float refractionAmount;
uniform float depthEffect;
uniform float chromaticAberration;
uniform float4 interiorMaskColor;
uniform float4 panelTint;
uniform float interiorMaskWidth;
uniform float borderWidth;
uniform float borderAlpha;
uniform float edgeShadowAlpha;
uniform float outerGlowAlpha;
uniform float isDark;
float radiusAt(float2 coord, float4 radii) {
  if (coord.x >= 0.0) { if (coord.y <= 0.0) return radii.y; else return radii.z; }
  else { if (coord.y <= 0.0) return radii.x; else return radii.w; }
}
float sdRoundedRect(float2 coord, float2 halfSize, float radius) {
  float2 cornerCoord = abs(coord) - (halfSize - float2(radius));
  float outside = length(max(cornerCoord, 0.0)) - radius;
  float inside = min(max(cornerCoord.x, cornerCoord.y), 0.0);
  return outside + inside;
}
float2 gradSdRoundedRect(float2 coord, float2 halfSize, float radius) {
  float2 cornerCoord = abs(coord) - (halfSize - float2(radius));
  if (cornerCoord.x >= 0.0 || cornerCoord.y >= 0.0) return sign(coord) * normalize(max(cornerCoord, 0.0));
  float gradX = step(cornerCoord.y, cornerCoord.x);
  return sign(coord) * float2(gradX, 1.0 - gradX);
}
float circleMap(float x) { return 1.0 - sqrt(max(1.0 - x * x, 0.0)); }
half4 main(float2 coord) {
  float2 halfSize = size * 0.5;
  float2 centeredCoord = (coord + offset) - halfSize;
  float radius = radiusAt(centeredCoord, cornerRadii);
  float sd = sdRoundedRect(centeredCoord, halfSize, radius);
  float outerWidth = max(borderWidth * 1.85, 0.001);
  if (sd > outerWidth) return half4(0.0);
  float outerLip = borderWidth > 0.001 ? 1.0 - smoothstep(0.0, borderWidth * 0.9, max(sd, 0.0)) : 0.0;
  float outerGlow = borderWidth > 0.001 ? (1.0 - smoothstep(borderWidth * 0.9, outerWidth, max(sd, 0.0))) : 0.0;
  if (sd >= 0.0) {
    float alpha = max(outerLip * borderAlpha, outerGlow * outerGlowAlpha);
    return half4(0.0, 0.0, 0.0, alpha);
  }
  half4 color;
  if (refractionHeight <= 0.001 || -sd >= refractionHeight) {
    color = content.eval(coord);
  } else {
    float d = circleMap(1.0 - -sd / refractionHeight) * refractionAmount;
    float gradRadius = min(radius * 1.5, min(halfSize.x, halfSize.y));
    float2 grad = normalize(gradSdRoundedRect(centeredCoord, halfSize, gradRadius) + depthEffect * normalize(centeredCoord));
    float2 refractedCoord = coord + d * grad;
    float dispersionIntensity = chromaticAberration * ((centeredCoord.x * centeredCoord.y) / (halfSize.x * halfSize.y));
    float2 dispersedCoord = d * grad * dispersionIntensity;
    color = half4(0.0);
    half4 red = content.eval(refractedCoord + dispersedCoord); color.r += red.r / 3.5; color.a += red.a / 7.0;
    half4 orange = content.eval(refractedCoord + dispersedCoord * (2.0 / 3.0)); color.r += orange.r / 3.5; color.g += orange.g / 7.0; color.a += orange.a / 7.0;
    half4 yellow = content.eval(refractedCoord + dispersedCoord * (1.0 / 3.0)); color.r += yellow.r / 3.5; color.g += yellow.g / 3.5; color.a += yellow.a / 7.0;
    half4 green = content.eval(refractedCoord); color.g += green.g / 3.5; color.a += green.a / 7.0;
    half4 cyan = content.eval(refractedCoord - dispersedCoord * (1.0 / 3.0)); color.g += cyan.g / 3.5; color.b += cyan.b / 3.0; color.a += cyan.a / 7.0;
    half4 blue = content.eval(refractedCoord - dispersedCoord * (2.0 / 3.0)); color.b += blue.b / 3.0; color.a += blue.a / 7.0;
    half4 purple = content.eval(refractedCoord - dispersedCoord); color.r += purple.r / 7.0; color.b += purple.b / 3.0; color.a += purple.a / 7.0;
  }
  color = half4(mix(color.rgb, panelTint.rgb, panelTint.a), max(color.a, panelTint.a));
  float interiorMask = (1.0 - smoothstep(0.0, interiorMaskWidth, -sd)) * interiorMaskColor.a;
  color.rgb = mix(color.rgb, half3(interiorMaskColor.rgb), interiorMask);
  float innerLip = borderWidth > 0.001 ? 1.0 - smoothstep(0.0, borderWidth, -sd) : 0.0;
  float2 normal = normalize(gradSdRoundedRect(centeredCoord, halfSize, radius));
  float topLeftLight = max(dot(normal, normalize(float2(-0.42, -0.91))), 0.0);
  float bottomRightShade = max(dot(normal, normalize(float2(0.42, 0.91))), 0.0);
  float innerHighlight = innerLip * borderAlpha * (0.38 + 0.62 * topLeftLight);
  float innerShadow = innerLip * edgeShadowAlpha * (0.35 + 0.65 * bottomRightShade) * mix(1.0, 0.55, isDark);
  color.rgb = mix(color.rgb, half3(1.0), innerHighlight);
  color.rgb = mix(color.rgb, half3(0.0), innerShadow);
  color.a = max(color.a, max(innerHighlight, innerShadow));
  return color;
}
"""

@Composable
fun rememberLiquidGlassShader(): RuntimeShader? =
    remember(Build.VERSION.SDK_INT) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            null
        } else {
            runCatching { RuntimeShader(GLASS_AGSL) }
                .onFailure { Log.e(TAG, "AGSL 编译失败: ${it.message}") }
                .getOrNull()
        }
    }

/** 背板层：页面内容录进这一层，玻璃条再带着折射效果重绘它。 */
@Composable
fun GlassBackdrop(
    layer: GraphicsLayer,
    onOrigin: (Offset) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier
            .fillMaxSize()
            .onGloballyPositioned { onOrigin(it.positionInRoot()) }
            .drawWithContent {
                layer.record { this@drawWithContent.drawContent() }
                drawLayer(layer)
            }
    ) { content() }
}

/**
 * 玻璃绘制：先模糊背板，再对整个面板统一折射、调色，最终只合成一次。
 * 必须先 record 再设 renderEffect 再 drawLayer —— 效果作用在图层内容上。
 */
fun androidx.compose.ui.graphics.drawscope.DrawScope.drawLiquidGlass(
    backdrop: GraphicsLayer,
    blurLayer: GraphicsLayer,
    panelLayer: GraphicsLayer,
    shader: RuntimeShader,
    style: LiquidGlassStyle,
    sampleOrigin: Offset,
    density: Float,
) {
    val barSize = size
    if (barSize.width <= 0f || barSize.height <= 0f) return

    val radiusPx = style.cornerRadiusDp * density
    val refractionAmountPx = abs(style.refractionAmountDp * density)
    val blurRadiusPx = style.blurRadiusDp * density
    // Include the furthest chromatic sample and the blur kernel, plus a 2px guard.
    val padding = ceil(
        max(refractionAmountPx * (1f + abs(style.chromaticAberration)), blurRadiusPx * 3f) + 2f
    ).toInt()
    val paddingPx = padding.toFloat()
    val captureSize = IntSize(
        ceil(barSize.width).toInt() + 2 * padding,
        ceil(barSize.height).toInt() + 2 * padding,
    )
    val captureOrigin = sampleOrigin - Offset(paddingPx, paddingPx)
    val barPath = Path().apply {
        addRoundRect(RoundRect(Rect(Offset.Zero, barSize), CornerRadius(radiusPx)))
    }

    // 先录制带留白的背板，供整个面板的折射着色器采样。
    blurLayer.record(size = captureSize) {
        translate(-captureOrigin.x, -captureOrigin.y) { drawLayer(backdrop) }
    }
    blurLayer.renderEffect =
        RenderEffect.createBlurEffect(
                blurRadiusPx,
                blurRadiusPx,
                android.graphics.Shader.TileMode.CLAMP,
            )
            .asComposeRenderEffect()
    // 折射边缘和中央采样同一张模糊背板，并在同一遍着色中应用 panelTint。
    // 不再单独绘制未调色的中央区域，避免夜间出现偏浅的内层玻璃。
    panelLayer.record(size = captureSize) {
        drawLayer(blurLayer)
    }

    // coord is in padded-layer space; subtract padding to get visible-bar space.
    // Keep size equal to the visible bar so the SDF does not expand into padding.
    shader.setFloatUniform("size", barSize.width, barSize.height)
    shader.setFloatUniform("offset", -paddingPx, -paddingPx)
    shader.setFloatUniform("cornerRadii", radiusPx, radiusPx, radiusPx, radiusPx)
    shader.setFloatUniform("refractionHeight", style.refractionHeightDp * density)
    shader.setFloatUniform("refractionAmount", refractionAmountPx)
    shader.setFloatUniform("depthEffect", 0.22f)
    shader.setFloatUniform("chromaticAberration", style.chromaticAberration)
    shader.setFloatUniform(
        "interiorMaskColor",
        1f, 1f, 1f,
        if (style.isDark) style.interiorMaskAlpha * 0.35f else style.interiorMaskAlpha * 2f,
    )
    shader.setFloatUniform("interiorMaskWidth", style.refractionHeightDp * density * 0.35f)
    if (style.isDark) {
        shader.setFloatUniform("panelTint", 0.055f, 0.06f, 0.07f, 0.86f)
    } else {
        shader.setFloatUniform("panelTint", 1f, 1f, 1f, 0.52f)
    }
    shader.setFloatUniform("borderWidth", 1.2f * density)
    shader.setFloatUniform("borderAlpha", style.borderAlpha)
    shader.setFloatUniform("edgeShadowAlpha", if (style.isDark) 0.30f else 0.22f)
    shader.setFloatUniform("outerGlowAlpha", 0.14f)
    shader.setFloatUniform("isDark", if (style.isDark) 1f else 0f)

    panelLayer.renderEffect =
        RenderEffect.createRuntimeShaderEffect(shader, "content").asComposeRenderEffect()
    // 必须裁到可见圆角矩形，否则带 padding 的图层会溢出到栏外，把上面的内容也扭曲掉
    clipPath(barPath) {
        translate(-paddingPx, -paddingPx) { drawLayer(panelLayer) }
    }
}
