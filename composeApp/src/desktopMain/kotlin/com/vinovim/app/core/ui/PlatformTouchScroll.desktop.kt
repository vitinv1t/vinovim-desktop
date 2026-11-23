package com.vinovim.app.core.ui

import androidx.compose.ui.Modifier
import com.vinovim.app.windowsTouchScroll

internal actual fun Modifier.platformTouchScroll(): Modifier = windowsTouchScroll()
