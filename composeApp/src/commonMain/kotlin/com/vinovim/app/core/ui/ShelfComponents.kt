package com.vinovim.app.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.vinovim.app.isDesktop
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.home_view_all
import com.vinovim.app.resources.poster_logo_content_description
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs
import kotlin.math.roundToInt

enum class VinovimPosterShape {
    Poster,
    Square,
    Landscape,
}

enum class VinovimViewAllPillSize {
    Default,
    Compact,
}

/** Stable per-row group key so spatial Up/Down can jump between shelves predictably. */
private var nextShelfGroupId = 1L

@Composable
fun <T> VinovimShelfSection(
    title: String,
    entries: List<T>,
    modifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier,
    headerHorizontalPadding: Dp = 0.dp,
    rowContentPadding: PaddingValues = PaddingValues(0.dp),
    itemSpacing: Dp = 10.dp,
    onViewAllClick: (() -> Unit)? = null,
    onTitleClick: (() -> Unit)? = null,
    viewAllPillSize: VinovimViewAllPillSize = VinovimViewAllPillSize.Default,
    key: ((T) -> Any)? = null,
    animatePlacement: Boolean = false,
    state: LazyListState = rememberLazyListState(),
    itemContent: @Composable (T) -> Unit,
) {
    val tokens = MaterialTheme.vinovim
    val duplicateSafeEntries = remember(entries, key) {
        key?.let { entries.withDuplicateSafeLazyKeys(it) }
    }
    val shelfGroupKey = remember { "shelf_${nextShelfGroupId++}" }

    ScreenActivityEffect(state) { active ->
        if (!active) state.stopScroll()
    }
    com.vinovim.app.controller.ProvideControllerRowLazyListState(state) {
        androidx.compose.runtime.CompositionLocalProvider(
            com.vinovim.app.controller.LocalControllerGroupKey provides shelfGroupKey,
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(tokens.spacing.controlGap + VinovimTokens.Space.s2),
            ) {
                if (title.isNotBlank()) {
                    VinovimShelfSectionHeader(
                        title = title,
                        modifier = Modifier.padding(horizontal = headerHorizontalPadding),
                        onViewAllClick = onViewAllClick,
                        onTitleClick = onTitleClick,
                        viewAllPillSize = viewAllPillSize,
                    )
                }
                LazyRow(
                    state = state,
                    modifier = rowModifier
                        .vinovimHorizontalScrollBleed(LocalVerticalBarPadding.current)
                        .vinovimDesktopDragScroll(state),
                    contentPadding = rowContentPadding.withVerticalBar(),
                    horizontalArrangement = Arrangement.spacedBy(itemSpacing),
                ) {
                    if (duplicateSafeEntries != null) {
                        itemsIndexed(
                            items = duplicateSafeEntries,
                            key = { _, entry -> entry.lazyKey },
                            contentType = { _, _ -> "poster" },
                        ) { _, keyedEntry ->
                            if (animatePlacement) {
                                Box(modifier = Modifier.animateItem()) { itemContent(keyedEntry.value) }
                            } else {
                                itemContent(keyedEntry.value)
                            }
                        }
                    } else {
                        itemsIndexed(
                            items = entries,
                            contentType = { _, _ -> "poster" },
                        ) { _, entry ->
                            if (animatePlacement) {
                                Box(modifier = Modifier.animateItem()) { itemContent(entry) }
                            } else {
                                itemContent(entry)
                            }
                        }
                    }
                }
            }
        }
    }
}

internal fun Modifier.vinovimDesktopDragScroll(
    state: LazyListState,
): Modifier {
    if (!isDesktop) return this

    return pointerInput(state) {
        awaitEachGesture {
            val down = awaitFirstDown(pass = PointerEventPass.Initial)
            var totalDx = 0f
            var totalDy = 0f
            var dragging = false

            while (true) {
                val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                if (!change.pressed) break

                val delta = change.position - change.previousPosition
                totalDx += delta.x
                totalDy += delta.y

                if (!dragging) {
                    val horizontalDrag =
                        abs(totalDx) > viewConfiguration.touchSlop && abs(totalDx) > abs(totalDy)
                    val verticalDrag =
                        abs(totalDy) > viewConfiguration.touchSlop && abs(totalDy) > abs(totalDx)

                    when {
                        verticalDrag -> break
                        horizontalDrag -> dragging = true
                        else -> continue
                    }
                }

                state.dispatchRawDelta(-delta.x)
                change.consume()
            }
        }
    }
}

internal fun Modifier.vinovimDesktopDragScroll(
    state: ScrollState,
): Modifier {
    if (!isDesktop) return this

    return pointerInput(state) {
        awaitEachGesture {
            val down = awaitFirstDown(pass = PointerEventPass.Initial)
            var totalDx = 0f
            var totalDy = 0f
            var dragging = false

            while (true) {
                val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                if (!change.pressed) break

                val delta = change.position - change.previousPosition
                totalDx += delta.x
                totalDy += delta.y

                if (!dragging) {
                    val horizontalDrag =
                        abs(totalDx) > viewConfiguration.touchSlop && abs(totalDx) > abs(totalDy)
                    val verticalDrag =
                        abs(totalDy) > viewConfiguration.touchSlop && abs(totalDy) > abs(totalDx)

                    when {
                        verticalDrag -> break
                        horizontalDrag -> dragging = true
                        else -> continue
                    }
                }

                state.dispatchRawDelta(-delta.x)
                change.consume()
            }
        }
    }
}

@Composable
fun VinovimPosterCard(
    title: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    basePosterWidthDp: Int? = null,
    fallbackImageUrl: String? = null,
    shape: VinovimPosterShape = VinovimPosterShape.Poster,
    detailLine: String? = null,
    showTitleBelow: Boolean = true,
    bottomLeftLogoUrl: String? = null,
    bottomLeftText: String? = null,
    isWatched: Boolean = false,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
) {
    val posterCardStyle = rememberPosterCardStyleUiState()
    val tokens = MaterialTheme.vinovim
    val effectiveBasePosterWidthDp = basePosterWidthDp ?: posterCardStyle.widthDp
    val cardWidth = shape.cardWidth(basePosterWidthDp = effectiveBasePosterWidthDp)
    val cardShape = RoundedCornerShape(posterCardStyle.cornerRadiusDp.dp)
    val catalogLogoOverlaySize = catalogLogoOverlaySize(
        basePosterWidthDp = effectiveBasePosterWidthDp,
        shape = shape,
    )
    val shouldShowTitleBelow = showTitleBelow && !posterCardStyle.hideLabelsEnabled

    Column(
        modifier = Modifier
            .desktopPosterHoverScale()
            .then(modifier)
            .width(cardWidth),
        verticalArrangement = Arrangement.spacedBy(VinovimTokens.Space.s6),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(shape.aspectRatio)
                .clip(cardShape)
                .background(tokens.colors.surfaceCard)
                .vinovimCardDepth(
                    shape = cardShape,
                    surface = VinovimCardDepthSurface.Posters,
                )
                .posterCardClickable(
                    onClick = onClick,
                    onLongClick = onLongClick,
                    zoomImageUrl = imageUrl,
                    zoomCornerRadius = posterCardStyle.cornerRadiusDp.dp,
                    hoverScaleEnabled = false,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (imageUrl != null) {
                val platformContext = coil3.compose.LocalPlatformContext.current
                val hasFallback = !fallbackImageUrl.isNullOrBlank() && fallbackImageUrl != imageUrl
                val imageModel = remember(imageUrl, fallbackImageUrl, platformContext) {
                    if (hasFallback) {
                        coil3.request.ImageRequest.Builder(platformContext)
                            .data(imageUrl)
                            .memoryCacheKeyExtras(
                                mapOf(com.vinovim.app.core.poster.CustomPosterFallbackInterceptor.FALLBACK_URL_KEY to fallbackImageUrl!!)
                            )
                            .build()
                    } else {
                        imageUrl
                    }
                }
                VinovimAsyncImage(
                    model = imageModel,
                    contentDescription = title,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                    desktopAnimation = posterCardStyle.animatedArtworkMode.toDesktopAnimation(),
                )
            } else {
                Text(
                    text = title,
                    modifier = Modifier.padding(horizontal = VinovimTokens.Space.s14),
                    style = MaterialTheme.typography.titleMedium,
                    color = tokens.colors.textMuted,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (!bottomLeftLogoUrl.isNullOrBlank() || !bottomLeftText.isNullOrBlank()) {
                // Gradient scrim for readability — matching VinovimTV Modern Home style
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .drawWithCache {
                            val gradient = Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.0f to Color.Transparent,
                                    0.58f to Color.Transparent,
                                    1.0f to Color.Black.copy(alpha = 0.75f)
                                )
                            )
                            onDrawBehind { drawRect(gradient) }
                        }
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = VinovimTokens.Space.s10, vertical = VinovimTokens.Space.s10),
                ) {
                    if (!bottomLeftLogoUrl.isNullOrBlank()) {
                        VinovimAsyncImage(
                            model = bottomLeftLogoUrl,
                            contentDescription = stringResource(Res.string.poster_logo_content_description, title),
                            modifier = Modifier
                                .width(catalogLogoOverlaySize.width)
                                .height(catalogLogoOverlaySize.height),
                            contentScale = ContentScale.Fit,
                            alignment = Alignment.CenterStart,
                        )
                    } else {
                        Text(
                            text = bottomLeftText.orEmpty(),
                            style = MaterialTheme.typography.labelMedium,
                            color = tokens.colors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = catalogLogoOverlaySize.textMaxWidth),
                        )
                    }
                }
            }

            VinovimPosterWatchedOverlay(isWatched = isWatched)
        }
        if (shouldShowTitleBelow) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = tokens.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (!detailLine.isNullOrBlank()) {
                Text(
                    text = detailLine,
                    style = MaterialTheme.typography.labelSmall,
                    color = tokens.colors.textMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            } else {
                Box(modifier = Modifier.height(VinovimTokens.Space.none))
            }
        } else {
            Box(modifier = Modifier.height(VinovimTokens.Space.none))
        }
    }
}

@Composable
private fun VinovimShelfSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    onViewAllClick: (() -> Unit)? = null,
    onTitleClick: (() -> Unit)? = null,
    viewAllPillSize: VinovimViewAllPillSize = VinovimViewAllPillSize.Default,
) {
    val tokens = MaterialTheme.vinovim
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(tokens.spacing.controlGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .then(if (onTitleClick != null) {
                        Modifier.vinovimFocusBorder(RoundedCornerShape(VinovimTokens.Radius.md))
                            .clickable(onClick = onTitleClick)
                    } else Modifier),
                style = MaterialTheme.typography.titleLarge,
                color = tokens.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            val viewAllPlaceholderModifier = if (onViewAllClick == null) {
                Modifier
                    .alpha(0f)
                    .clearAndSetSemantics { }
            } else {
                Modifier
            }
            VinovimViewAllPill(
                onClick = onViewAllClick,
                size = viewAllPillSize,
                modifier = viewAllPlaceholderModifier,
            )
        }
    }
}

@Composable
private fun VinovimViewAllPill(
    onClick: (() -> Unit)?,
    size: VinovimViewAllPillSize,
    modifier: Modifier = Modifier,
) {
    val tokens = MaterialTheme.vinovim
    val actionSize = if (size == VinovimViewAllPillSize.Compact) VinovimTokens.Space.s32 else VinovimTokens.Space.s40
    val iconSize = if (size == VinovimViewAllPillSize.Compact) VinovimTokens.Icon.sm else tokens.icons.md
    val viewAllText = stringResource(Res.string.home_view_all)

    val pillShape = RoundedCornerShape(VinovimTokens.Radius.xl)
    Box(
        modifier = modifier
            .size(actionSize)
            .background(color = tokens.colors.surfaceCard, shape = pillShape)
            .then(if (onClick != null) {
                Modifier.vinovimFocusBorder(pillShape).clickable(onClick = onClick)
            } else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = viewAllText,
            tint = tokens.colors.textMuted,
            modifier = Modifier.size(iconSize),
        )
    }
}

internal const val VinovimDesktopCatalogShelfPosterScale = 1.4f
private const val DesktopPosterHoverScale = 1.045f

internal fun desktopCatalogShelfPosterBaseWidthDp(
    basePosterWidthDp: Int,
): Int =
    if (isDesktop) {
        (basePosterWidthDp * VinovimDesktopCatalogShelfPosterScale).roundToInt()
    } else {
        basePosterWidthDp
    }

internal fun Modifier.vinovimShelfHoverOverdraw(inset: Dp): Modifier {
    if (inset == 0.dp) return this

    // Expand the measured viewport, then place it negatively so edge items keep
    // their visual alignment while desktop hover scale can draw into the gutter.
    return layout { measurable, constraints ->
        val insetPx = inset.roundToPx()
        val horizontalInset = insetPx * 2
        val verticalInset = insetPx * 2
        val expandedMaxWidth = if (constraints.hasBoundedWidth) {
            constraints.maxWidth + horizontalInset
        } else {
            constraints.maxWidth
        }
        val expandedMinWidth = if (constraints.hasBoundedWidth) {
            (constraints.minWidth + horizontalInset).coerceAtMost(expandedMaxWidth)
        } else {
            constraints.minWidth
        }
        val expandedMaxHeight = if (constraints.hasBoundedHeight) {
            constraints.maxHeight + verticalInset
        } else {
            constraints.maxHeight
        }
        val expandedConstraints = constraints.copy(
            minWidth = expandedMinWidth,
            maxWidth = expandedMaxWidth,
            minHeight = 0,
            maxHeight = expandedMaxHeight,
        )
        val placeable = measurable.measure(expandedConstraints)
        val width = (placeable.width - horizontalInset)
            .coerceIn(constraints.minWidth, constraints.maxWidth)
        val height = (placeable.height - verticalInset)
            .coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(width, height) {
            placeable.placeRelative(-insetPx, -insetPx)
        }
    }
}

private val VinovimPosterShape.aspectRatio: Float
    get() = when (this) {
        VinovimPosterShape.Poster -> 0.675f
        VinovimPosterShape.Square -> 1f
        VinovimPosterShape.Landscape -> PosterLandscapeAspectRatio
    }

private data class CatalogLogoOverlaySize(
    val width: Dp,
    val height: Dp,
    val textMaxWidth: Dp,
)

private fun catalogLogoOverlaySize(
    basePosterWidthDp: Int,
    shape: VinovimPosterShape,
): CatalogLogoOverlaySize =
    if (shape == VinovimPosterShape.Landscape) {
        when {
            basePosterWidthDp <= 108 -> CatalogLogoOverlaySize(width = 92.dp, height = 24.dp, textMaxWidth = 120.dp)
            basePosterWidthDp <= 120 -> CatalogLogoOverlaySize(width = 104.dp, height = 28.dp, textMaxWidth = 132.dp)
            basePosterWidthDp <= 132 -> CatalogLogoOverlaySize(width = 116.dp, height = 30.dp, textMaxWidth = 144.dp)
            else -> CatalogLogoOverlaySize(width = 128.dp, height = 34.dp, textMaxWidth = 156.dp)
        }
    } else {
        when {
            basePosterWidthDp <= 108 -> CatalogLogoOverlaySize(width = 72.dp, height = 18.dp, textMaxWidth = 92.dp)
            basePosterWidthDp <= 120 -> CatalogLogoOverlaySize(width = 80.dp, height = 20.dp, textMaxWidth = 104.dp)
            basePosterWidthDp <= 132 -> CatalogLogoOverlaySize(width = 88.dp, height = 22.dp, textMaxWidth = 112.dp)
            else -> CatalogLogoOverlaySize(width = 96.dp, height = 24.dp, textMaxWidth = 124.dp)
        }
    }

private fun VinovimPosterShape.cardWidth(basePosterWidthDp: Int): Dp =
    when (this) {
        VinovimPosterShape.Poster -> basePosterWidthDp.dp
        VinovimPosterShape.Square -> basePosterWidthDp.dp
        VinovimPosterShape.Landscape -> landscapePosterWidth(basePosterWidthDp)
    }
@Composable
internal fun Modifier.desktopPosterHoverScale(
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
): Modifier {
    if (!enabled || !isDesktop) return this

    val hovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (hovered) DesktopPosterHoverScale else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "desktop_poster_hover_scale",
    )

    val isScaling = hovered || scale != 1f

    return this
        .then(
            if (isScaling) {
                Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .zIndex(1f)
            } else {
                Modifier
            },
        )
        .hoverable(interactionSource)
}
