package com.vinovim.app.core.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents

internal actual val vinovimPlatformExtraTopPadding: Dp = 0.dp
internal actual val vinovimPlatformExtraBottomPadding: Dp = 0.dp
internal actual val vinovimBottomNavigationExtraVerticalPadding: Dp = 0.dp

@Composable
internal actual fun vinovimBottomNavigationBarInsets(): WindowInsets =
	WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun platformPhysicalTopInset(): Dp {
    val physicalTop = LocalUIViewController.current.view.window
        ?.safeAreaInsets
        ?.useContents { top.toFloat() }

    return physicalTop?.dp
        ?: WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
}

@Composable
internal actual fun ResizeForKeyboard() = Unit
