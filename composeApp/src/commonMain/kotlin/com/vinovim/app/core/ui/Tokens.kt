package com.vinovim.app.core.ui

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object VinovimTokens {
    object Space {
        val none = 0.dp
        val hairline = 0.5.dp
        val s1 = 1.dp
        val s2 = 2.dp
        val s3 = 3.dp
        val s4 = 4.dp
        val s5 = 5.dp
        val s6 = 6.dp
        val s7 = 7.dp
        val s8 = 8.dp
        val s10 = 10.dp
        val s12 = 12.dp
        val s14 = 14.dp
        val s16 = 16.dp
        val s18 = 18.dp
        val s20 = 20.dp
        val s22 = 22.dp
        val s24 = 24.dp
        val s28 = 28.dp
        val s32 = 32.dp
        val s36 = 36.dp
        val s40 = 40.dp
        val s48 = 48.dp
        val s56 = 56.dp
        val s64 = 64.dp
        val s72 = 72.dp
        val s80 = 80.dp
        val s96 = 96.dp
    }

    object Radius {
        val none = Space.none
        val xs = Space.s4
        val sm = Space.s6
        val md = Space.s8
        val lg = Space.s12
        val xl = Space.s16
        val xxl = Space.s24
        val full = 999.dp

        val card = xxl
        val compactCard = lg
        val sheet = xxl
        val dialog = Space.s28
        val button = xl
        val chip = full
        val poster = lg
        val avatar = full
        val playerPanel = xxl
    }

    object Border {
        val hairline = Space.hairline
        val thin = Space.s1
        val medium = Space.s2
    }

    object Elevation {
        val none = Space.none
        val raised = Space.s2
        val modal = Space.s8
        val overlay = Space.s12
        val playerControls = Space.s4
    }

    object Opacity {
        const val invisible = 0f
        const val disabled = 0.38f
        const val secondary = 0.70f
        const val muted = 0.60f
        const val selected = 0.15f
        const val hover = 0.08f
        const val pressed = 0.12f
        const val subtle = 0.06f
        const val medium = 0.52f
        const val strong = 0.75f
        const val overlayLight = 0.35f
        const val overlayMedium = 0.56f
        const val overlayHeavy = 0.82f
        const val visible = 1f
    }

    object Motion {
        const val instantMillis = 0
        const val fastMillis = 150
        const val normalMillis = 220
        const val sheetEnterMillis = 300
        const val sheetExitMillis = 250
        const val slowMillis = 400
        const val cinematicMillis = 700

        val standard: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
        val emphasized: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
        val decelerate: Easing = CubicBezierEasing(0f, 0f, 0f, 1f)
        val accelerate: Easing = CubicBezierEasing(0.3f, 0f, 1f, 1f)
    }

    object Icon {
        val xs = Space.s12
        val sm = Space.s16
        val md = Space.s20
        val lg = Space.s24
        val xl = Space.s32
        val xxl = Space.s40
    }

    object Type {
        val labelXs = 11.sp
        val labelSm = 12.sp
        val bodySm = 13.sp
        val bodyMd = 14.sp
        val bodyApp = 15.sp
        val bodyLg = 16.sp
        val titleSm = 18.sp
        val headline = 26.sp
        val titleMd = 22.sp
        val titleLg = 28.sp
        val displaySm = 32.sp
        val pageDisplay = 38.sp
        val displayMd = 48.sp
    }

    object LineHeight {
        val labelXs = 14.sp
        val labelSm = 15.sp
        val bodySm = 18.sp
        val bodyMd = 20.sp
        val bodyApp = 22.sp
        val bodyLg = 22.sp
        val titleSm = 22.sp
        val materialTitleLarge = 24.sp
        val headline = 30.sp
        val titleMd = 26.sp
        val titleLg = 32.sp
        val displaySm = 36.sp
        val pageDisplay = 42.sp
        val displayMd = 52.sp
    }

    object LetterSpacing {
        val none = 0.sp
        val pageDisplay = (-1.2).sp
        val headline = (-0.8).sp
        val label = 0.8.sp
    }

    object Breakpoint {
        val phone = 0.dp
        val largePhone = 420.dp
        val tablet = 600.dp
        val largeTablet = 840.dp
        val desktop = 1024.dp
        val playerWide = 1280.dp
    }

    object Z {
        const val base = 0f
        const val stickyHeader = 2f
        const val navigation = 4f
        const val sheet = 8f
        const val dialog = 10f
        const val playerOverlay = 12f
        const val toast = 16f
    }
}

@Immutable
data class VinovimColorTokens(
    val background: Color,
    val backgroundAmoled: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val surfaceCard: Color,
    val surfaceSheet: Color,
    val surfaceDialog: Color,
    val surfacePopover: Color,
    val nativeChrome: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val textDisabled: Color,
    val textInverse: Color,
    val accent: Color,
    val accentStrong: Color,
    val onAccent: Color,
    val focusRing: Color,
    val focusBackground: Color,
    val borderSubtle: Color,
    val borderDefault: Color,
    val borderStrong: Color,
    val borderFocus: Color,
    val borderSelected: Color,
    val success: Color,
    val warning: Color,
    val danger: Color,
    val info: Color,
    val neutral: Color,
    val overlayScrim: Color,
    val overlayHover: Color,
    val overlayPressed: Color,
    val overlaySelected: Color,
    val overlayDisabled: Color,
    val shimmer: Color,
    val skeleton: Color,
    val playerControlsBackground: Color,
    val playerControlsForeground: Color,
    val playerTimelineTrack: Color,
    val playerTimelineFill: Color,
    val playerSubtitlePreview: Color,
    val playerBuffering: Color,
    val parentalGuide: Color,
)

@Immutable
data class VinovimShapeTokens(
    val card: Shape,
    val compactCard: Shape,
    val sheet: Shape,
    val dialog: Shape,
    val button: Shape,
    val chip: Shape,
    val poster: Shape,
    val avatar: Shape,
    val playerPanel: Shape,
)

@Immutable
data class VinovimSpacingTokens(
    val screenHorizontal: Dp,
    val screenTop: Dp,
    val screenBottom: Dp,
    val sectionGap: Dp,
    val listGap: Dp,
    val railGap: Dp,
    val cardPadding: Dp,
    val cardPaddingCompact: Dp,
    val controlGap: Dp,
    val dialogPadding: Dp,
    val sheetPadding: Dp,
    val playerOverlayPadding: Dp,
)

@Immutable
data class VinovimBorderTokens(
    val hairline: Dp,
    val thin: Dp,
    val medium: Dp,
)

@Immutable
data class VinovimElevationTokens(
    val flat: Dp,
    val raised: Dp,
    val modal: Dp,
    val overlay: Dp,
    val playerControls: Dp,
)

@Immutable
data class VinovimMotionTokens(
    val instantMillis: Int,
    val fastMillis: Int,
    val normalMillis: Int,
    val sheetEnterMillis: Int,
    val sheetExitMillis: Int,
    val slowMillis: Int,
    val cinematicMillis: Int,
    val standard: Easing,
    val emphasized: Easing,
    val decelerate: Easing,
    val accelerate: Easing,
)

@Immutable
data class VinovimOpacityTokens(
    val invisible: Float,
    val disabled: Float,
    val secondary: Float,
    val muted: Float,
    val selected: Float,
    val hover: Float,
    val pressed: Float,
    val subtle: Float,
    val medium: Float,
    val strong: Float,
    val overlayLight: Float,
    val overlayMedium: Float,
    val overlayHeavy: Float,
    val visible: Float,
)

@Immutable
data class VinovimIconTokens(
    val xs: Dp,
    val sm: Dp,
    val md: Dp,
    val lg: Dp,
    val xl: Dp,
    val xxl: Dp,
)

@Immutable
data class VinovimBreakpointTokens(
    val phone: Dp,
    val largePhone: Dp,
    val tablet: Dp,
    val largeTablet: Dp,
    val desktop: Dp,
    val playerWide: Dp,
)

@Immutable
data class VinovimComponentTokens(
    val navItemShape: Shape,
    val navIconSize: Dp,
    val navItemMaxWidth: Dp,
    val sheetMaxWidth: Dp,
    val dialogMaxWidth: Dp,
    val chipHorizontalPadding: Dp,
    val chipVerticalPadding: Dp,
    val posterRadius: Dp,
    val avatarSize: Dp,
    val playerPanelMaxWidth: Dp,
)

@Immutable
data class VinovimThemeTokens(
    val colors: VinovimColorTokens,
    val spacing: VinovimSpacingTokens,
    val shapes: VinovimShapeTokens,
    val borders: VinovimBorderTokens,
    val elevation: VinovimElevationTokens,
    val motion: VinovimMotionTokens,
    val opacity: VinovimOpacityTokens,
    val icons: VinovimIconTokens,
    val breakpoints: VinovimBreakpointTokens,
    val components: VinovimComponentTokens,
)

internal val LocalVinovimThemeTokens = staticCompositionLocalOf {
    defaultVinovimThemeTokens(ThemeColors.White, amoled = false, colorScheme = null)
}

val MaterialTheme.vinovim: VinovimThemeTokens
    @Composable
    @Stable
    get() = LocalVinovimThemeTokens.current

internal fun defaultVinovimThemeTokens(
    palette: ThemeColorPalette,
    amoled: Boolean,
    colorScheme: ColorScheme?,
): VinovimThemeTokens {
    val background = if (amoled) Color.Black else palette.background
    val textPrimary = Color(0xFFF5F7F8)
    val textSecondary = Color(0xFFB8BEC5)
    val textMuted = Color(0xFF969CA3)
    val surface = palette.backgroundElevated
    val surfaceCard = palette.backgroundCard
    val accent = palette.secondary
    val accentStrong = palette.secondaryVariant
    val borderSubtle = Color(0xFF252A2A).copy(alpha = 0.55f)
    val borderDefault = Color(0xFF252A2A)
    val overlayScrim = Color.Black.copy(alpha = VinovimTokens.Opacity.overlayMedium)

    return VinovimThemeTokens(
        colors = VinovimColorTokens(
            background = background,
            backgroundAmoled = Color.Black,
            surface = surface,
            surfaceElevated = surface,
            surfaceCard = surfaceCard,
            surfaceSheet = surface,
            surfaceDialog = surface,
            surfacePopover = surfaceCard,
            nativeChrome = background,
            textPrimary = textPrimary,
            textSecondary = textSecondary,
            textMuted = textMuted,
            textDisabled = textMuted.copy(alpha = VinovimTokens.Opacity.disabled),
            textInverse = Color(0xFF111111),
            accent = accent,
            accentStrong = accentStrong,
            onAccent = palette.onSecondary,
            focusRing = palette.focusRing,
            focusBackground = palette.focusBackground,
            borderSubtle = borderSubtle,
            borderDefault = borderDefault,
            borderStrong = Color(0xFF3A4040),
            borderFocus = palette.focusRing,
            borderSelected = accent.copy(alpha = VinovimTokens.Opacity.strong),
            success = Color(0xFF66BB6A),
            warning = Color(0xFFFFC857),
            danger = colorScheme?.error ?: Color(0xFFE36A8A),
            info = Color(0xFF42A5F5),
            neutral = textMuted,
            overlayScrim = overlayScrim,
            overlayHover = Color.White.copy(alpha = VinovimTokens.Opacity.hover),
            overlayPressed = Color.White.copy(alpha = VinovimTokens.Opacity.pressed),
            overlaySelected = Color.White.copy(alpha = VinovimTokens.Opacity.selected),
            overlayDisabled = Color.Black.copy(alpha = VinovimTokens.Opacity.disabled),
            shimmer = Color.White.copy(alpha = 0.10f),
            skeleton = Color.White.copy(alpha = 0.06f),
            playerControlsBackground = Color.Black.copy(alpha = 0.72f),
            playerControlsForeground = Color.White,
            playerTimelineTrack = Color.White.copy(alpha = 0.30f),
            playerTimelineFill = accent,
            playerSubtitlePreview = Color.Black.copy(alpha = 0.55f),
            playerBuffering = accent,
            parentalGuide = Color(0xFF5D1F1F),
        ),
        spacing = VinovimSpacingTokens(
            screenHorizontal = VinovimTokens.Space.s16,
            screenTop = VinovimTokens.Space.s10,
            screenBottom = VinovimTokens.Space.s18,
            sectionGap = VinovimTokens.Space.s24,
            listGap = VinovimTokens.Space.s12,
            railGap = VinovimTokens.Space.s14,
            cardPadding = VinovimTokens.Space.s18,
            cardPaddingCompact = VinovimTokens.Space.s14,
            controlGap = VinovimTokens.Space.s8,
            dialogPadding = VinovimTokens.Space.s20,
            sheetPadding = VinovimTokens.Space.s20,
            playerOverlayPadding = VinovimTokens.Space.s24,
        ),
        shapes = VinovimShapeTokens(
            card = RoundedCornerShape(VinovimTokens.Radius.card),
            compactCard = RoundedCornerShape(VinovimTokens.Radius.compactCard),
            sheet = RoundedCornerShape(VinovimTokens.Radius.sheet),
            dialog = RoundedCornerShape(VinovimTokens.Radius.dialog),
            button = RoundedCornerShape(VinovimTokens.Radius.button),
            chip = RoundedCornerShape(VinovimTokens.Radius.chip),
            poster = RoundedCornerShape(VinovimTokens.Radius.poster),
            avatar = RoundedCornerShape(VinovimTokens.Radius.avatar),
            playerPanel = RoundedCornerShape(VinovimTokens.Radius.playerPanel),
        ),
        borders = VinovimBorderTokens(
            hairline = VinovimTokens.Border.hairline,
            thin = VinovimTokens.Border.thin,
            medium = VinovimTokens.Border.medium,
        ),
        elevation = VinovimElevationTokens(
            flat = VinovimTokens.Elevation.none,
            raised = VinovimTokens.Elevation.raised,
            modal = VinovimTokens.Elevation.modal,
            overlay = VinovimTokens.Elevation.overlay,
            playerControls = VinovimTokens.Elevation.playerControls,
        ),
        motion = VinovimMotionTokens(
            instantMillis = VinovimTokens.Motion.instantMillis,
            fastMillis = VinovimTokens.Motion.fastMillis,
            normalMillis = VinovimTokens.Motion.normalMillis,
            sheetEnterMillis = VinovimTokens.Motion.sheetEnterMillis,
            sheetExitMillis = VinovimTokens.Motion.sheetExitMillis,
            slowMillis = VinovimTokens.Motion.slowMillis,
            cinematicMillis = VinovimTokens.Motion.cinematicMillis,
            standard = VinovimTokens.Motion.standard,
            emphasized = VinovimTokens.Motion.emphasized,
            decelerate = VinovimTokens.Motion.decelerate,
            accelerate = VinovimTokens.Motion.accelerate,
        ),
        opacity = VinovimOpacityTokens(
            invisible = VinovimTokens.Opacity.invisible,
            disabled = VinovimTokens.Opacity.disabled,
            secondary = VinovimTokens.Opacity.secondary,
            muted = VinovimTokens.Opacity.muted,
            selected = VinovimTokens.Opacity.selected,
            hover = VinovimTokens.Opacity.hover,
            pressed = VinovimTokens.Opacity.pressed,
            subtle = VinovimTokens.Opacity.subtle,
            medium = VinovimTokens.Opacity.medium,
            strong = VinovimTokens.Opacity.strong,
            overlayLight = VinovimTokens.Opacity.overlayLight,
            overlayMedium = VinovimTokens.Opacity.overlayMedium,
            overlayHeavy = VinovimTokens.Opacity.overlayHeavy,
            visible = VinovimTokens.Opacity.visible,
        ),
        icons = VinovimIconTokens(
            xs = VinovimTokens.Icon.xs,
            sm = VinovimTokens.Icon.sm,
            md = VinovimTokens.Icon.md,
            lg = VinovimTokens.Icon.lg,
            xl = VinovimTokens.Icon.xl,
            xxl = VinovimTokens.Icon.xxl,
        ),
        breakpoints = VinovimBreakpointTokens(
            phone = VinovimTokens.Breakpoint.phone,
            largePhone = VinovimTokens.Breakpoint.largePhone,
            tablet = VinovimTokens.Breakpoint.tablet,
            largeTablet = VinovimTokens.Breakpoint.largeTablet,
            desktop = VinovimTokens.Breakpoint.desktop,
            playerWide = VinovimTokens.Breakpoint.playerWide,
        ),
        components = VinovimComponentTokens(
            navItemShape = RoundedCornerShape(VinovimTokens.Radius.xl),
            navIconSize = VinovimTokens.Icon.xl,
            navItemMaxWidth = 150.dp,
            sheetMaxWidth = 520.dp,
            dialogMaxWidth = 460.dp,
            chipHorizontalPadding = VinovimTokens.Space.s14,
            chipVerticalPadding = VinovimTokens.Space.s8,
            posterRadius = VinovimTokens.Radius.poster,
            avatarSize = VinovimTokens.Space.s48,
            playerPanelMaxWidth = 600.dp,
        ),
    )
}
