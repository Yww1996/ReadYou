package me.ash.reader.ui.component.glass

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.layer.drawLayer

/**
 * 液态玻璃：用 AGSL 运行时着色器对「底下的内容」做边缘折射 + 色散。
 *
 * 算法与 IT之家 Android 9.38 里 com.iruanmi.liquidglass 的实现同源，
 * 都是 RenderEffect.createRuntimeShaderEffect + 圆角矩形 SDF。
 * 只在 API 33+ 生效，低版本/取样层缺失时调用方退回普通 Surface。
 */
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
    half4 red = content.eval(refractedCoord + dispersedCoord);
    color.r += red.r / 3.5; color.a += red.a / 7.0;
    half4 orange = content.eval(refractedCoord + dispersedCoord * (2.0 / 3.0));
    color.r += orange.r / 3.5; color.g += orange.g / 7.0; color.a += orange.a / 7.0;
    half4 yellow = content.eval(refractedCoord + dispersedCoord * (1.0 / 3.0));
    color.r += yellow.r / 3.5; color.g += yellow.g / 3.5; color.a += yellow.a / 7.0;
    half4 green = content.eval(refractedCoord);
    color.g += green.g / 3.5; color.a += green.a / 7.0;
    half4 cyan = content.eval(refractedCoord - dispersedCoord * (1.0 / 3.0));
    color.g += cyan.g / 3.5; color.b += cyan.b / 3.0; color.a += cyan.a / 7.0;
    half4 blue = content.eval(refractedCoord - dispersedCoord * (2.0 / 3.0));
    color.b += blue.b / 3.0; color.a += blue.a / 7.0;
    half4 purple = content.eval(refractedCoord - dispersedCoord);
    color.r += purple.r / 7.0; color.b += purple.b / 3.0; color.a += purple.a / 7.0;
  }

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

val liquidGlassSupported: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun rememberLiquidGlassShader(): RuntimeShader = remember { RuntimeShader(GLASS_AGSL) }

/** 玻璃参数 */
data class LiquidGlassStyle(
    val cornerRadiusDp: Float,
    val refractionHeightDp: Float = 22f,
    val refractionAmountDp: Float = 11f,
    val chromatic: Float = 0.9f,
    val borderAlpha: Float = 0.55f,
    val isDark: Boolean = true,
)

/**
 * 把 [backdrop]（页面内容的录制层）按玻璃算法重绘到当前节点上。
 *
 * [sampleOrigin] 是当前节点左上角在被录制内容坐标系里的位置，
 * 平移到录制层原点后，着色器里的 coord 就直接对应到玻璃下方的内容。
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Modifier.liquidGlass(
    backdrop: GraphicsLayer?,
    shader: RuntimeShader?,
    style: LiquidGlassStyle,
    sampleOrigin: Offset,
    density: Float,
): Modifier = drawWithContent {
    if (backdrop == null || shader == null) {
        drawContent()
        return@drawWithContent
    }

    val radiusPx = style.cornerRadiusDp * density
    shader.setFloatUniform("size", size.width, size.height)
    shader.setFloatUniform("offset", 0f, 0f)
    shader.setFloatUniform("cornerRadii", radiusPx, radiusPx, radiusPx, radiusPx)
    shader.setFloatUniform("refractionHeight", style.refractionHeightDp * density)
    shader.setFloatUniform("refractionAmount", style.refractionAmountDp * density)
    shader.setFloatUniform("depthEffect", 0.22f)
    shader.setFloatUniform("chromaticAberration", style.chromatic)
    shader.setFloatUniform(
        "interiorMaskColor",
        1f, 1f, 1f,
        if (style.isDark) 0.10f else 0.20f,
    )
    shader.setFloatUniform("interiorMaskWidth", style.refractionHeightDp * density * 0.8f)
    shader.setFloatUniform("borderWidth", 1.1f * density)
    shader.setFloatUniform("borderAlpha", style.borderAlpha)
    shader.setFloatUniform("edgeShadowAlpha", if (style.isDark) 0.30f else 0.22f)
    shader.setFloatUniform("outerGlowAlpha", 0.14f)
    shader.setFloatUniform("isDark", if (style.isDark) 1f else 0f)

    val effect = RenderEffect.createRuntimeShaderEffect(shader, "content")
    val nativePaint = android.graphics.Paint()
    // 本机 SDK 的 stubs 里缺 Paint#setRenderEffect（API 31+ 实际存在），
    // 直接调用编译不过，走反射设置。
    runCatching {
        android.graphics.Paint::class.java
            .getMethod("setRenderEffect", android.graphics.RenderEffect::class.java)
            .invoke(nativePaint, effect)
    }
    val nc = drawContext.canvas.nativeCanvas

    nc.saveLayer(0f, 0f, size.width, size.height, nativePaint)
    nc.save()
    // 把录制层平移到与当前节点对齐：玻璃下方就是内容层里对应的那一块
    nc.translate(-sampleOrigin.x, -sampleOrigin.y)
    drawLayer(backdrop)
    nc.restore()
    nc.restore()

    drawContent()
}


/**
 * 把页面内容录进 [layer]，玻璃组件再把这一层按折射算法重绘出来。
 * 必须只包「内容」、不包玻璃条本身，否则会自己采样自己。
 */
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
