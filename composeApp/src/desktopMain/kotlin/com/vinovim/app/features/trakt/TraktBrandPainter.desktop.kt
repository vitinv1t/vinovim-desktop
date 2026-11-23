package com.vinovim.app.features.trakt

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.trakt_logo_wordmark
import com.vinovim.app.resources.trakt_tv_favicon
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun traktBrandPainter(asset: TraktBrandAsset): Painter =
    painterResource(
        when (asset) {
            TraktBrandAsset.Glyph -> Res.drawable.trakt_tv_favicon
            TraktBrandAsset.Wordmark -> Res.drawable.trakt_logo_wordmark
        },
    )
