package com.vinovim.app.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Typography
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import com.vinovim.app.isDesktop
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.poppins_bold
import com.vinovim.app.resources.poppins_medium
import com.vinovim.app.resources.poppins_regular
import com.vinovim.app.resources.poppins_semibold
import com.vinovim.app.resources.inter_bold
import com.vinovim.app.resources.inter_semibold
import com.vinovim.app.resources.inter_regular
import org.jetbrains.compose.resources.Font

val LocalAppTheme = staticCompositionLocalOf { AppTheme.WHITE }
val LocalThemePalette = staticCompositionLocalOf { ThemeColors.White }

val MaterialTheme.themePalette: ThemeColorPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalThemePalette.current

internal val LocalVinovimPlatformDensity = staticCompositionLocalOf<Density> {
    error("Platform density is unavailable outside VinovimTheme")
}

val MaterialTheme.appTheme: AppTheme
    @Composable
    @ReadOnlyComposable
    get() = LocalAppTheme.current

private fun contentColorFor(background: Color): Color =
    if (background.luminance() > 0.5f) Color(0xFF111111) else Color(0xFFF5F7F8)

private fun buildColorScheme(palette: ThemeColorPalette, amoled: Boolean = false) = darkColorScheme(
    primary = palette.secondary,
    onPrimary = palette.onSecondary,
    primaryContainer = palette.focusBackground,
    onPrimaryContainer = contentColorFor(palette.focusBackground),
    secondary = palette.secondaryVariant,
    onSecondary = palette.onSecondaryVariant,
    background = if (amoled) Color.Black else palette.background,
    onBackground = Color(0xFFF5F7F8),
    surface = palette.backgroundElevated,
    onSurface = Color(0xFFF5F7F8),
    surfaceVariant = palette.backgroundCard,
    onSurfaceVariant = Color(0xFF969CA3),
    outline = Color(0xFF252A2A),
    error = Color(0xFFE36A8A),
    onError = Color(0xFFFCE5EC),
)

// Poppins — para títulos/display (vinovim brand)
private val Poppins: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.poppins_bold, FontWeight.Bold, FontStyle.Normal),
        Font(Res.font.poppins_semibold, FontWeight.SemiBold, FontStyle.Normal),
        Font(Res.font.poppins_medium, FontWeight.Medium, FontStyle.Normal),
        Font(Res.font.poppins_regular, FontWeight.Normal, FontStyle.Normal),
    )

// Inter — para corpo/labels (vinovim brand)
private val Inter: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.inter_bold, FontWeight.Bold, FontStyle.Normal),
        Font(Res.font.inter_semibold, FontWeight.SemiBold, FontStyle.Normal),
        Font(Res.font.inter_regular, FontWeight.Normal, FontStyle.Normal),
    )

private val VinovimTypography: Typography
    @Composable
    get() = Typography(
        displayLarge = TextStyle(
            fontFamily = Poppins,
            fontSize = VinovimTokens.Type.pageDisplay,
            lineHeight = VinovimTokens.LineHeight.pageDisplay,
            fontWeight = FontWeight.Bold,
            letterSpacing = VinovimTokens.LetterSpacing.pageDisplay,
        ),
        headlineLarge = TextStyle(
            fontFamily = Poppins,
            fontSize = VinovimTokens.Type.headline,
            lineHeight = VinovimTokens.LineHeight.headline,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = VinovimTokens.LetterSpacing.headline,
        ),
        titleLarge = TextStyle(
            fontFamily = Poppins,
            fontSize = VinovimTokens.Type.titleSm,
            lineHeight = VinovimTokens.LineHeight.materialTitleLarge,
            fontWeight = FontWeight.SemiBold,
        ),
        titleMedium = TextStyle(
            fontFamily = Poppins,
            fontSize = VinovimTokens.Type.bodyLg,
            lineHeight = VinovimTokens.LineHeight.bodyMd,
            fontWeight = FontWeight.SemiBold,
        ),
        bodyLarge = TextStyle(
            fontFamily = Inter,
            fontSize = VinovimTokens.Type.bodyApp,
            lineHeight = VinovimTokens.LineHeight.bodyApp,
            fontWeight = FontWeight.Normal,
        ),
        bodyMedium = TextStyle(
            fontFamily = Inter,
            fontSize = VinovimTokens.Type.bodyMd,
            lineHeight = VinovimTokens.LineHeight.bodyMd,
            fontWeight = FontWeight.Normal,
        ),
        labelLarge = TextStyle(
            fontFamily = Inter,
            fontSize = VinovimTokens.Type.bodyMd,
            lineHeight = VinovimTokens.LineHeight.bodySm,
            fontWeight = FontWeight.SemiBold,
        ),
        labelMedium = TextStyle(
            fontFamily = Inter,
            fontSize = VinovimTokens.Type.labelSm,
            lineHeight = VinovimTokens.LineHeight.labelXs,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = VinovimTokens.LetterSpacing.label,
        ),
    )

private val VinovimTypeTokens: VinovimTypeScale
    @Composable
    get() = VinovimTypeScale(
        labelXs = TextStyle(
            fontFamily = Inter,
            fontSize = VinovimTokens.Type.labelXs,
            lineHeight = VinovimTokens.LineHeight.labelXs,
            fontWeight = FontWeight.SemiBold,
        ),
        labelSm = TextStyle(
            fontFamily = Inter,
            fontSize = VinovimTokens.Type.labelSm,
            lineHeight = VinovimTokens.LineHeight.labelSm,
            fontWeight = FontWeight.SemiBold,
        ),
        bodySm = TextStyle(
            fontFamily = Inter,
            fontSize = VinovimTokens.Type.bodySm,
            lineHeight = VinovimTokens.LineHeight.bodySm,
            fontWeight = FontWeight.Normal,
        ),
        bodyMd = TextStyle(
            fontFamily = Inter,
            fontSize = VinovimTokens.Type.bodyMd,
            lineHeight = VinovimTokens.LineHeight.bodyMd,
            fontWeight = FontWeight.Normal,
        ),
        bodyLg = TextStyle(
            fontFamily = Inter,
            fontSize = VinovimTokens.Type.bodyLg,
            lineHeight = VinovimTokens.LineHeight.bodyLg,
            fontWeight = FontWeight.Normal,
        ),
        titleSm = TextStyle(
            fontFamily = Poppins,
            fontSize = VinovimTokens.Type.titleSm,
            lineHeight = VinovimTokens.LineHeight.titleSm,
            fontWeight = FontWeight.SemiBold,
        ),
        titleMd = TextStyle(
            fontFamily = Poppins,
            fontSize = VinovimTokens.Type.titleMd,
            lineHeight = VinovimTokens.LineHeight.titleMd,
            fontWeight = FontWeight.SemiBold,
        ),
        titleLg = TextStyle(
            fontFamily = Poppins,
            fontSize = VinovimTokens.Type.titleLg,
            lineHeight = VinovimTokens.LineHeight.titleLg,
            fontWeight = FontWeight.SemiBold,
        ),
        displaySm = TextStyle(
            fontFamily = Poppins,
            fontSize = VinovimTokens.Type.displaySm,
            lineHeight = VinovimTokens.LineHeight.displaySm,
            fontWeight = FontWeight.Bold,
        ),
        displayMd = TextStyle(
            fontFamily = Poppins,
            fontSize = VinovimTokens.Type.displayMd,
            lineHeight = VinovimTokens.LineHeight.displayMd,
            fontWeight = FontWeight.Bold,
        ),
    )

private val VinovimRippleConfiguration = RippleConfiguration(
    color = Color.Black,
)

private const val VinovimDesktopFontScale = 1.08f
private const val VinovimDesktopBaseWidthDp = 1280f
private const val VinovimDesktopBaseHeightDp = 820f
private const val VinovimDesktopMinUiScale = 1f
private const val VinovimDesktopMaxUiScale = 1.18f

internal fun desktopUiScaleForWindow(widthDp: Float, heightDp: Float): Float {
    if (!isDesktop || widthDp <= 0f || heightDp <= 0f) return VinovimDesktopMinUiScale

    val rawScale = minOf(
        widthDp / VinovimDesktopBaseWidthDp,
        heightDp / VinovimDesktopBaseHeightDp,
    )
    return rawScale.coerceIn(VinovimDesktopMinUiScale, VinovimDesktopMaxUiScale)
}

@Composable
fun VinovimTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    appTheme: AppTheme = AppTheme.WHITE,
    amoled: Boolean = false,
    desktopUiScale: Float = VinovimDesktopMinUiScale,
    customThemeColors: CustomThemeColors = CustomThemeColors.Default,
    content: @Composable () -> Unit,
) {
    val palette = remember(appTheme, customThemeColors) {
        ThemeColors.getColorPalette(appTheme, customThemeColors)
    }
    val colorScheme = buildColorScheme(palette, amoled = amoled)
    val tokens = defaultVinovimThemeTokens(palette, amoled = amoled, colorScheme = colorScheme)

    val density = LocalDensity.current
    val effectiveDesktopUiScale = if (isDesktop) {
        desktopUiScale.coerceIn(VinovimDesktopMinUiScale, VinovimDesktopMaxUiScale)
    } else {
        VinovimDesktopMinUiScale
    }
    CompositionLocalProvider(
        LocalVinovimPlatformDensity provides density,
        LocalDensity provides Density(
            density = density.density * effectiveDesktopUiScale,
            fontScale = if (isDesktop) VinovimDesktopFontScale else 1f,
        ),
        LocalVinovimThemeTokens provides tokens,
        LocalVinovimTypeScale provides VinovimTypeTokens,
        LocalRippleConfiguration provides VinovimRippleConfiguration,
        LocalAppTheme provides appTheme,
        LocalThemePalette provides palette,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = VinovimTypography,
        ) {
            SkeletonAnimationProvider(content = content)
        }
    }
}
