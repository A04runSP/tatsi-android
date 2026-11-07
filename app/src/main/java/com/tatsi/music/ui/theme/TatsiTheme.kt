package com.tatsi.music.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tatsi.music.model.TatsiMode
import com.tatsi.music.model.TatsiStyle

@Immutable
data class SwatchInfo(val name: String, val hex: String, val color: Color, val onColor: Color)

@Immutable
data class TatsiTokens(
    val style: TatsiStyle,
    val mode: TatsiMode,
    val themeName: String,
    val tagline: String,
    val background: Color,
    val surface: Color,
    val cardElevated: Color,
    val brand: Color,
    val accent: Color,
    val highlight: Color,
    val secondaryAccent: Color,
    val textPrimary: Color,
    val textMuted: Color,
    val divider: Color,
    val isWatercolor: Boolean,
    val swatches: List<SwatchInfo>
)

// 1. Default Premium Dark — Midnight Burgundy
val PremiumDarkTokens = TatsiTokens(
    style = TatsiStyle.PREMIUM,
    mode = TatsiMode.DARK,
    themeName = "Dark Theme — Midnight Burgundy",
    tagline = "YOUR MUSIC. YOUR WORLD.",
    background = Color(0xFF160D12),      // Midnight Ink
    surface = Color(0xFF321923),         // Dark Wine
    cardElevated = Color(0xFF412530),    // Elevated Wine
    brand = Color(0xFF843B50),           // Burgundy
    accent = Color(0xFFC17A83),          // Dusty Rose
    highlight = Color(0xFFD69AA0),       // Blush
    secondaryAccent = Color(0xFF843B50),
    textPrimary = Color(0xFFF1E4DC),     // Warm Ivory
    textMuted = Color(0xFFF1E4DC).copy(alpha = 0.65f),
    divider = Color(0xFFC17A83).copy(alpha = 0.18f),
    isWatercolor = false,
    swatches = listOf(
        SwatchInfo("Midnight Ink", "#160D12", Color(0xFF160D12), Color(0xFFF1E4DC)),
        SwatchInfo("Dark Wine", "#321923", Color(0xFF321923), Color(0xFFF1E4DC)),
        SwatchInfo("Elevated Wine", "#412530", Color(0xFF412530), Color(0xFFF1E4DC)),
        SwatchInfo("Burgundy", "#843B50", Color(0xFF843B50), Color(0xFFF1E4DC)),
        SwatchInfo("Dusty Rose", "#C17A83", Color(0xFFC17A83), Color(0xFF160D12)),
        SwatchInfo("Warm Ivory", "#F1E4DC", Color(0xFFF1E4DC), Color(0xFF160D12))
    )
)

// 2. Default Premium Light — Rose Paper
val PremiumLightTokens = TatsiTokens(
    style = TatsiStyle.PREMIUM,
    mode = TatsiMode.LIGHT,
    themeName = "Light Theme — Rose Paper",
    tagline = "YOUR MUSIC. YOUR WORLD.",
    background = Color(0xFFF7EDE8),      // Paper
    surface = Color(0xFFFFFEFC),         // Porcelain
    cardElevated = Color(0xFFEDE1DC),    // Rose Mist
    brand = Color(0xFF6F2D40),           // Deep Burgundy
    accent = Color(0xFFA95667),          // Rose
    highlight = Color(0xFFC17A83),
    secondaryAccent = Color(0xFF6F2D40),
    textPrimary = Color(0xFF2A171D),     // Ink
    textMuted = Color(0xFF2A171D).copy(alpha = 0.62f),
    divider = Color(0xFF6F2D40).copy(alpha = 0.14f),
    isWatercolor = false,
    swatches = listOf(
        SwatchInfo("Paper", "#F7EDE8", Color(0xFFF7EDE8), Color(0xFF2A171D)),
        SwatchInfo("Porcelain", "#FFFEFC", Color(0xFFFFFEFC), Color(0xFF2A171D)),
        SwatchInfo("Rose Mist", "#EDE1DC", Color(0xFFEDE1DC), Color(0xFF2A171D)),
        SwatchInfo("Deep Burgundy", "#6F2D40", Color(0xFF6F2D40), Color(0xFFF7EDE8)),
        SwatchInfo("Rose", "#A95667", Color(0xFFA95667), Color(0xFFFFFEFC)),
        SwatchInfo("Ink", "#2A171D", Color(0xFF2A171D), Color(0xFFF7EDE8))
    )
)

// 3. Optional Settings Theme: Watercolor v1.0 Light (Warm Paper Wash)
val WatercolorLightTokens = TatsiTokens(
    style = TatsiStyle.WATERCOLOR,
    mode = TatsiMode.LIGHT,
    themeName = "Watercolor v1.0 — Light Paper Wash",
    tagline = "Calm your soul ♥",
    background = Color(0xFFF9F3EA),      // Warm Ivory
    surface = Color(0xFFFFF9F2),         // Soft Paper Wash
    cardElevated = Color(0xFFF4C7D0),    // Blush Pink
    brand = Color(0xFF7A2E3A),           // Burgundy
    accent = Color(0xFFC7888E),          // Dusty Rose
    highlight = Color(0xFFF4C7D0),       // Blush Pink
    secondaryAccent = Color(0xFFA4B8A0), // Sage Leaf
    textPrimary = Color(0xFF3B282A),     // Plum Brown
    textMuted = Color(0xFF3B282A).copy(alpha = 0.68f),
    divider = Color(0xFFC7888E).copy(alpha = 0.32f),
    isWatercolor = true,
    swatches = listOf(
        SwatchInfo("Burgundy", "#7A2E3A", Color(0xFF7A2E3A), Color(0xFFF9F3EA)),
        SwatchInfo("Dusty Rose", "#C7888E", Color(0xFFC7888E), Color(0xFF3B282A)),
        SwatchInfo("Blush Pink", "#F4C7D0", Color(0xFFF4C7D0), Color(0xFF3B282A)),
        SwatchInfo("Warm Ivory", "#F9F3EA", Color(0xFFF9F3EA), Color(0xFF3B282A)),
        SwatchInfo("Plum Brown", "#3B282A", Color(0xFF3B282A), Color(0xFFF9F3EA)),
        SwatchInfo("Sage Leaf", "#A4B8A0", Color(0xFFA4B8A0), Color(0xFF3B282A))
    )
)

// 4. Optional Settings Theme: Watercolor v1.0 Dark (Deep Starry Wash)
val WatercolorDarkTokens = TatsiTokens(
    style = TatsiStyle.WATERCOLOR,
    mode = TatsiMode.DARK,
    themeName = "Watercolor v1.0 — Dark Starry Wash",
    tagline = "Calm your soul ♥",
    background = Color(0xFF1E1216),      // Deep Plum Night
    surface = Color(0xFF3B282A),         // Plum Brown
    cardElevated = Color(0xFF56232D),    // Deep Pigment Wash
    brand = Color(0xFF7A2E3A),           // Burgundy
    accent = Color(0xFFC7888E),          // Dusty Rose
    highlight = Color(0xFFF4C7D0),       // Blush Pink
    secondaryAccent = Color(0xFFA4B8A0), // Sage Leaf
    textPrimary = Color(0xFFF9F3EA),     // Warm Ivory
    textMuted = Color(0xFFF9F3EA).copy(alpha = 0.70f),
    divider = Color(0xFFC7888E).copy(alpha = 0.26f),
    isWatercolor = true,
    swatches = listOf(
        SwatchInfo("Burgundy", "#7A2E3A", Color(0xFF7A2E3A), Color(0xFFF9F3EA)),
        SwatchInfo("Dusty Rose", "#C7888E", Color(0xFFC7888E), Color(0xFF1E1216)),
        SwatchInfo("Blush Pink", "#F4C7D0", Color(0xFFF4C7D0), Color(0xFF3B282A)),
        SwatchInfo("Warm Ivory", "#F9F3EA", Color(0xFFF9F3EA), Color(0xFF3B282A)),
        SwatchInfo("Plum Brown", "#3B282A", Color(0xFF3B282A), Color(0xFFF9F3EA)),
        SwatchInfo("Sage Leaf", "#A4B8A0", Color(0xFFA4B8A0), Color(0xFF1E1216))
    )
)

val LocalTatsiTokens = staticCompositionLocalOf { PremiumDarkTokens }

// Typography Pairing: Playfair Display (Expressive Serif) + Inter (Clean Sans-Serif)
val TatsiTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = 0.5.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 0.8.sp
    )
)

@Composable
fun TatsiTheme(
    style: TatsiStyle = TatsiStyle.PREMIUM,
    mode: TatsiMode = TatsiMode.DARK,
    content: @Composable () -> Unit
) {
    val tokens = when (style) {
        TatsiStyle.PREMIUM -> if (mode == TatsiMode.DARK) PremiumDarkTokens else PremiumLightTokens
        TatsiStyle.WATERCOLOR -> if (mode == TatsiMode.DARK) WatercolorDarkTokens else WatercolorLightTokens
    }

    val colorScheme = if (mode == TatsiMode.DARK) {
        darkColorScheme(
            background = tokens.background,
            surface = tokens.surface,
            primary = tokens.brand,
            secondary = tokens.accent,
            tertiary = tokens.secondaryAccent,
            onBackground = tokens.textPrimary,
            onSurface = tokens.textPrimary
        )
    } else {
        lightColorScheme(
            background = tokens.background,
            surface = tokens.surface,
            primary = tokens.brand,
            secondary = tokens.accent,
            tertiary = tokens.secondaryAccent,
            onBackground = tokens.textPrimary,
            onSurface = tokens.textPrimary
        )
    }

    CompositionLocalProvider(LocalTatsiTokens provides tokens) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = TatsiTypography,
            content = content
        )
    }
}

fun Modifier.tatsiScreenBackground(tokens: TatsiTokens): Modifier = this.drawWithCache {
    onDrawBehind {
        drawRect(color = tokens.background)
        if (tokens.isWatercolor) {
            val bloomAlpha = if (tokens.mode == TatsiMode.LIGHT) 0.45f else 0.30f
            val sageAlpha = if (tokens.mode == TatsiMode.LIGHT) 0.25f else 0.15f

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(tokens.highlight.copy(alpha = bloomAlpha), Color.Transparent),
                    center = Offset(size.width * 0.15f, size.height * 0.12f),
                    radius = size.width * 0.65f
                ),
                center = Offset(size.width * 0.15f, size.height * 0.12f),
                radius = size.width * 0.65f
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(tokens.accent.copy(alpha = bloomAlpha * 0.85f), Color.Transparent),
                    center = Offset(size.width * 0.88f, size.height * 0.38f),
                    radius = size.width * 0.6f
                ),
                center = Offset(size.width * 0.88f, size.height * 0.38f),
                radius = size.width * 0.6f
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(tokens.secondaryAccent.copy(alpha = sageAlpha), Color.Transparent),
                    center = Offset(size.width * 0.3f, size.height * 0.78f),
                    radius = size.width * 0.7f
                ),
                center = Offset(size.width * 0.3f, size.height * 0.78f),
                radius = size.width * 0.7f
            )
        }
    }
}

fun Modifier.tatsiCardSurface(
    tokens: TatsiTokens,
    isActive: Boolean = false,
    cornerRadius: Dp = 18.dp
): Modifier {
    val shape = RoundedCornerShape(cornerRadius)
    return this
        .clip(shape)
        .drawWithCache {
            onDrawBehind {
                if (!tokens.isWatercolor) {
                    drawRect(color = if (isActive) tokens.cardElevated else tokens.surface)
                } else {
                    val startColor = if (isActive) {
                        tokens.accent.copy(alpha = if (tokens.mode == TatsiMode.LIGHT) 0.55f else 0.45f)
                    } else {
                        tokens.surface.copy(alpha = 0.90f)
                    }
                    val endColor = if (isActive) {
                        tokens.highlight.copy(alpha = if (tokens.mode == TatsiMode.LIGHT) 0.65f else 0.35f)
                    } else {
                        tokens.cardElevated.copy(alpha = if (tokens.mode == TatsiMode.LIGHT) 0.42f else 0.55f)
                    }
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(startColor, endColor),
                            start = Offset.Zero,
                            end = Offset(size.width, size.height)
                        )
                    )
                    drawRoundRect(
                        color = tokens.accent.copy(alpha = if (isActive) 0.7f else 0.35f),
                        style = Stroke(
                            width = if (isActive) 3.5f else 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(28f, 6f), 0f)
                        )
                    )
                }
            }
        }
        .border(
            width = 1.dp,
            color = if (isActive) tokens.accent else tokens.divider,
            shape = shape
        )
}
