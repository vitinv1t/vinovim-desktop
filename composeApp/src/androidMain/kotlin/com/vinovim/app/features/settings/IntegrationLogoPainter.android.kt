package com.vinovim.app.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.vinovim.app.R
import com.vinovim.app.features.simkl.SimklBrandAsset
import com.vinovim.app.features.simkl.simklBrandPainter
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.introdb_favicon
import com.vinovim.app.resources.rating_tmdb
import org.jetbrains.compose.resources.painterResource as composePainterResource

@Composable
internal actual fun integrationLogoPainter(logo: IntegrationLogo): Painter =
    when (logo) {
        IntegrationLogo.Tmdb -> composePainterResource(Res.drawable.rating_tmdb)
        IntegrationLogo.Trakt -> painterResource(id = R.drawable.trakt_tv_favicon)
        IntegrationLogo.Simkl -> simklBrandPainter(SimklBrandAsset.Glyph)
        IntegrationLogo.MdbList -> painterResource(id = R.drawable.mdblist_logo)
        IntegrationLogo.IntroDb -> composePainterResource(Res.drawable.introdb_favicon)
    }
