package com.vinovim.app.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.vinovim.app.features.simkl.SimklBrandAsset
import com.vinovim.app.features.simkl.simklBrandPainter
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.introdb_favicon
import com.vinovim.app.resources.mdblist_logo
import com.vinovim.app.resources.rating_tmdb
import com.vinovim.app.resources.trakt_tv_favicon
import org.jetbrains.compose.resources.painterResource

@Composable
internal actual fun integrationLogoPainter(logo: IntegrationLogo): Painter =
    when (logo) {
        IntegrationLogo.Tmdb -> painterResource(Res.drawable.rating_tmdb)
        IntegrationLogo.Trakt -> painterResource(Res.drawable.trakt_tv_favicon)
        IntegrationLogo.Simkl -> simklBrandPainter(SimklBrandAsset.Glyph)
        IntegrationLogo.MdbList -> painterResource(Res.drawable.mdblist_logo)
        IntegrationLogo.IntroDb -> painterResource(Res.drawable.introdb_favicon)
    }
