package com.vinovim.app.core.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal expect val vinovimPlatformExtraTopPadding: Dp
internal expect val vinovimPlatformExtraBottomPadding: Dp
internal expect val vinovimBottomNavigationExtraVerticalPadding: Dp
@Composable
internal expect fun vinovimBottomNavigationBarInsets(): WindowInsets

/** Physical display-safe top inset, excluding any enclosing native toolbar. */
@Composable
internal expect fun platformPhysicalTopInset(): Dp

@Composable
internal expect fun ResizeForKeyboard()

internal val LocalVinovimBottomNavigationOverlayPadding = staticCompositionLocalOf { 0.dp }

internal val LocalBottomInsetsConsumed = staticCompositionLocalOf { false }

/** CompositionLocal providing the shared [VinovimNavBarScrollState] so child screens can attach the nestedScrollConnection. */
val LocalVinovimNavBarScrollState = staticCompositionLocalOf<VinovimNavBarScrollState?> { null }

@Composable
internal fun vinovimSafeBottomPadding(extra: Dp = 0.dp): Dp {
	if (LocalBottomInsetsConsumed.current) return extra
	val navigationBarBottom = vinovimBottomNavigationBarInsets()
		.asPaddingValues()
		.calculateBottomPadding()
	return navigationBarBottom.coerceAtLeast(vinovimPlatformExtraBottomPadding) +
		LocalVinovimBottomNavigationOverlayPadding.current +
		extra
}
