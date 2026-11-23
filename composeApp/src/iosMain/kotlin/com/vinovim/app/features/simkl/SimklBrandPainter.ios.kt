package com.vinovim.app.features.simkl

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.simkl_logo_glyph
import com.vinovim.app.resources.simkl_logo_wordmark
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun simklBrandPainter(asset: SimklBrandAsset): Painter =
    when (asset) {
        SimklBrandAsset.Glyph -> painterResource(Res.drawable.simkl_logo_glyph)
        SimklBrandAsset.Wordmark -> painterResource(Res.drawable.simkl_logo_wordmark)
    }
