package com.vinovim.app.features.home.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vinovim.app.core.format.formatReleaseDateForDisplay
import com.vinovim.app.core.ui.VinovimPosterCard
import com.vinovim.app.core.ui.VinovimPosterShape
import com.vinovim.app.core.ui.desktopCatalogShelfPosterBaseWidthDp
import com.vinovim.app.core.ui.rememberPosterCardStyleUiState
import com.vinovim.app.features.home.MetaPreview
import com.vinovim.app.features.home.PosterShape

@Composable
fun HomePosterCard(
    item: MetaPreview,
    modifier: Modifier = Modifier,
    useLandscapeBackdropMode: Boolean = false,
    isWatched: Boolean = false,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    showLandscapeOverlay: Boolean = true,
) {
    val posterCardStyle = rememberPosterCardStyleUiState()
    val isLandscapeMode = useLandscapeBackdropMode || posterCardStyle.catalogLandscapeModeEnabled
    val effectiveLandscapePoster = if (posterCardStyle.alwaysShowLandscapeClearlogo) null else item.landscapePoster
    val imageUrl = if (isLandscapeMode) {
        effectiveLandscapePoster ?: item.banner ?: item.poster
    } else item.poster
    val fallbackImageUrl = if (isLandscapeMode && !effectiveLandscapePoster.isNullOrBlank()) {
        // Landscape custom poster -> fall back to original backdrop, then portrait
        item.banner ?: item.rawPosterUrl
    } else {
        item.rawPosterUrl
    }

    HomePosterHoverPreview(
        item = item,
        isWatched = isWatched,
        onClick = onClick,
        onLongClick = onLongClick,
    ) { hoverModifier ->
        VinovimPosterCard(
            title = item.name,
            imageUrl = imageUrl,
            modifier = modifier.then(hoverModifier),
            fallbackImageUrl = fallbackImageUrl,
            basePosterWidthDp = desktopCatalogShelfPosterBaseWidthDp(posterCardStyle.widthDp),
            shape = if (isLandscapeMode) VinovimPosterShape.Landscape else item.posterShape.toVinovimPosterShape(),
            detailLine = if (isLandscapeMode || posterCardStyle.hideLabelsEnabled) null else item.releaseInfo?.let { formatReleaseDateForDisplay(it) },
            showTitleBelow = !posterCardStyle.hideLabelsEnabled,
            bottomLeftLogoUrl = if (isLandscapeMode && showLandscapeOverlay && effectiveLandscapePoster.isNullOrBlank()) item.logo else null,
            bottomLeftText = if (isLandscapeMode && showLandscapeOverlay && effectiveLandscapePoster.isNullOrBlank() && item.logo.isNullOrBlank() && !posterCardStyle.hideLabelsEnabled) item.name else null,
            isWatched = isWatched,
            onClick = onClick,
            onLongClick = onLongClick,
        )
    }
}

private fun PosterShape.toVinovimPosterShape(): VinovimPosterShape =
    when (this) {
        PosterShape.Poster -> VinovimPosterShape.Poster
        PosterShape.Square -> VinovimPosterShape.Square
        PosterShape.Landscape -> VinovimPosterShape.Landscape
    }
