package com.vinovim.app.features.downloads

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import com.vinovim.app.core.ui.VinovimTokens
import com.vinovim.app.core.ui.accentBrush
import com.vinovim.app.core.ui.gradientMask
import com.vinovim.app.core.ui.vinovim
import com.vinovim.app.core.ui.vinovimTypeScale
import com.vinovim.app.core.ui.themePalette
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.action_resume
import com.vinovim.app.resources.action_retry
import com.vinovim.app.resources.compose_action_pause
import com.vinovim.app.resources.details_actions_menu_label
import com.vinovim.app.resources.downloads_downloading_count
import com.vinovim.app.resources.downloads_episodes
import com.vinovim.app.resources.downloads_speed
import com.vinovim.app.resources.downloads_status_downloading
import com.vinovim.app.resources.downloads_status_failed
import com.vinovim.app.resources.downloads_status_paused
import com.vinovim.app.resources.episodes_cd_watched
import com.vinovim.app.resources.home_continue_watching_watched
import com.vinovim.app.resources.media_movie
import com.vinovim.app.resources.media_series
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ActiveDownloadRow(
    item: DownloadItem,
    speedBytesPerSecond: Long?,
    inShowPage: Boolean,
    onToggle: () -> Unit,
    onOpenActions: () -> Unit,
) {
    val tokens = MaterialTheme.vinovim
    val progress by animateFloatAsState(targetValue = item.progressFraction, label = "downloadProgress")
    DownloadRow(
        artwork = {
            DownloadThumbnail(imageUrl = item.landscapeArtwork())
        },
        title = if (inShowPage) item.episodeDisplayTitle() else item.title,
        subtitle = if (inShowPage) null else item.episodeLabel(),
        onClick = onOpenActions,
        onLongClick = onOpenActions,
        footer = {
            if (item.status == DownloadStatus.Failed) {
                StatusText(
                    text = item.errorMessage ?: stringResource(Res.string.downloads_status_failed),
                    color = tokens.colors.danger,
                )
            } else {
                StatusText(text = activeStatusText(item, speedBytesPerSecond))
            }
        },
        trailing = {
            TransferButton(item = item, progress = progress, onClick = onToggle)
        },
    )
}

@Composable
internal fun DownloadedMovieRow(
    item: DownloadItem,
    watchStatus: DownloadWatchStatus,
    onPlay: () -> Unit,
    onOpenActions: () -> Unit,
) {
    DownloadRow(
        artwork = {
            DownloadPoster(item = item)
        },
        title = item.title,
        subtitle = listOf(
            stringResource(Res.string.media_movie),
            formatBytes(item.sizeOnDiskBytes),
        ).joinToString(DownloadTextSeparator),
        onClick = onPlay,
        onLongClick = onOpenActions,
        footer = {
            watchStatusText(watchStatus)?.let { StatusText(text = it) }
        },
        trailing = {
            MoreActionsButton(onClick = onOpenActions)
        },
    )
}

@Composable
internal fun DownloadedShowRow(
    cover: DownloadItem,
    episodeCount: Int,
    sizeBytes: Long,
    downloadingCount: Int,
    onOpen: () -> Unit,
) {
    val tokens = MaterialTheme.vinovim
    DownloadRow(
        artwork = {
            DownloadPoster(item = cover)
        },
        title = cover.title,
        subtitle = listOf(
            stringResource(Res.string.media_series),
            pluralStringResource(Res.plurals.downloads_episodes, episodeCount, episodeCount),
            formatBytes(sizeBytes),
        ).joinToString(DownloadTextSeparator),
        onClick = onOpen,
        onLongClick = null,
        footer = {
            if (downloadingCount > 0) {
                StatusText(text = stringResource(Res.string.downloads_downloading_count, downloadingCount))
            }
        },
        trailing = {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = tokens.colors.textMuted,
                modifier = Modifier.padding(horizontal = VinovimTokens.Space.s12),
            )
        },
    )
}

@Composable
internal fun DownloadedEpisodeRow(
    item: DownloadItem,
    watchStatus: DownloadWatchStatus,
    onPlay: () -> Unit,
    onOpenActions: () -> Unit,
) {
    DownloadRow(
        artwork = {
            DownloadThumbnail(
                imageUrl = item.landscapeArtwork(),
                watchProgress = watchStatus.progress,
            )
        },
        title = item.episodeDisplayTitle(),
        subtitle = listOfNotNull(
            formatBytes(item.sizeOnDiskBytes),
            watchStatusText(watchStatus),
        ).joinToString(DownloadTextSeparator),
        onClick = onPlay,
        onLongClick = onOpenActions,
        trailing = {
            MoreActionsButton(onClick = onOpenActions)
        },
    )
}

@Composable
private fun DownloadRow(
    artwork: @Composable () -> Unit,
    title: String,
    subtitle: String?,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)?,
    footer: @Composable ColumnScope.() -> Unit = {},
    trailing: @Composable () -> Unit = {},
) {
    val tokens = MaterialTheme.vinovim
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        horizontalArrangement = Arrangement.spacedBy(VinovimTokens.Space.s12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        artwork()
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(VinovimTokens.Space.s2),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = tokens.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.vinovimTypeScale.bodySm,
                    color = tokens.colors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            footer()
        }
        trailing()
    }
}

@Composable
private fun TransferButton(
    item: DownloadItem,
    progress: Float,
    onClick: () -> Unit,
) {
    val tokens = MaterialTheme.vinovim
    IconButton(onClick = onClick) {
        if (item.status == DownloadStatus.Failed) {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = stringResource(Res.string.action_retry),
                tint = tokens.colors.danger,
            )
        } else {
            val isDownloading = item.status == DownloadStatus.Downloading
            val trackColor = tokens.colors.borderStrong
            val strokeWidth = VinovimTokens.Space.s2
            val ringColor = if (isDownloading) Color.White else tokens.colors.textMuted
            val ringModifier = Modifier
                .size(VinovimTokens.Space.s32)
                .then(
                    if (isDownloading) {
                        Modifier.gradientMask(MaterialTheme.themePalette.accentBrush())
                    } else {
                        Modifier
                    },
                )
            Box(
                modifier = Modifier
                    .size(VinovimTokens.Space.s32)
                    .drawBehind {
                        val strokePx = strokeWidth.toPx()
                        drawCircle(
                            color = trackColor,
                            radius = (size.minDimension - strokePx) / 2f,
                            style = Stroke(width = strokePx),
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (isDownloading && !item.hasKnownSize) {
                    CircularProgressIndicator(
                        modifier = ringModifier,
                        color = ringColor,
                        strokeWidth = strokeWidth,
                        trackColor = Color.Transparent,
                    )
                } else {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = ringModifier,
                        color = ringColor,
                        strokeWidth = strokeWidth,
                        trackColor = Color.Transparent,
                        gapSize = VinovimTokens.Space.none,
                    )
                }
                Icon(
                    imageVector = if (isDownloading) Icons.Rounded.Pause else Icons.Rounded.Download,
                    contentDescription = stringResource(
                        if (isDownloading) Res.string.compose_action_pause else Res.string.action_resume,
                    ),
                    tint = tokens.colors.textPrimary,
                    modifier = Modifier.size(VinovimTokens.Icon.sm),
                )
            }
        }
    }
}

@Composable
private fun MoreActionsButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = stringResource(Res.string.details_actions_menu_label),
            tint = MaterialTheme.vinovim.colors.textSecondary,
        )
    }
}

@Composable
private fun StatusText(
    text: String,
    color: Color = MaterialTheme.vinovim.colors.textMuted,
) {
    Text(
        text = text,
        style = MaterialTheme.vinovimTypeScale.bodySm,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun activeStatusText(item: DownloadItem, speedBytesPerSecond: Long?): String {
    val total = item.totalBytes?.takeIf { it > 0L }
    val transferred = if (total != null) {
        "${formatBytes(item.downloadedBytes)} / ${formatBytes(total)}"
    } else {
        formatBytes(item.downloadedBytes)
    }
    if (item.status == DownloadStatus.Paused) {
        return stringResource(Res.string.downloads_status_paused, transferred)
    }
    val percent = "${(item.progressFraction * 100f).toInt()}%"
    val speed = speedBytesPerSecond?.takeIf { it > 0L }
    if (speed == null) {
        return if (total != null) {
            listOf(percent, transferred).joinToString(DownloadTextSeparator)
        } else {
            stringResource(Res.string.downloads_status_downloading, transferred)
        }
    }
    val speedText = stringResource(Res.string.downloads_speed, formatBytes(speed))
    if (total == null) {
        return listOf(transferred, speedText).joinToString(DownloadTextSeparator)
    }
    val timeLeft = formatTimeLeft((total - item.downloadedBytes).coerceAtLeast(0L) * 1_000L / speed)
    return listOf(percent, speedText, timeLeft).joinToString(DownloadTextSeparator)
}

@Composable
private fun watchStatusText(status: DownloadWatchStatus): String? = when {
    status.isWatched -> stringResource(Res.string.episodes_cd_watched)
    status.remainingMs > 0L -> formatTimeLeft(status.remainingMs)
    status.progress > 0f -> stringResource(
        Res.string.home_continue_watching_watched,
        "${(status.progress * 100f).toInt()}%",
    )
    else -> null
}
