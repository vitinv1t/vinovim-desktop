package com.vinovim.app.features.downloads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vinovim.app.core.ui.VinovimActionLabel
import com.vinovim.app.core.ui.VinovimScreen
import com.vinovim.app.core.ui.VinovimScreenHeader
import com.vinovim.app.core.ui.VinovimSectionLabel
import com.vinovim.app.core.ui.VinovimStatusModal
import com.vinovim.app.core.ui.VinovimToastController
import com.vinovim.app.core.ui.VinovimTokens
import com.vinovim.app.core.ui.PlatformBackHandler
import com.vinovim.app.core.ui.vinovim
import com.vinovim.app.core.ui.vinovimTypeScale
import com.vinovim.app.features.watched.WatchedRepository
import com.vinovim.app.features.watchprogress.WatchProgressRepository
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.action_cancel
import com.vinovim.app.resources.action_delete
import com.vinovim.app.resources.action_delete_confirm_message
import com.vinovim.app.resources.compose_settings_root_downloads_title
import com.vinovim.app.resources.downloads_delete_message
import com.vinovim.app.resources.downloads_delete_title
import com.vinovim.app.resources.downloads_empty_message
import com.vinovim.app.resources.downloads_empty_title
import com.vinovim.app.resources.downloads_episodes
import com.vinovim.app.resources.downloads_open_directory
import com.vinovim.app.resources.downloads_open_directory_failed
import com.vinovim.app.resources.downloads_pause_all
import com.vinovim.app.resources.downloads_resume_all
import com.vinovim.app.resources.downloads_section_active
import com.vinovim.app.resources.downloads_section_downloaded
import com.vinovim.app.resources.downloads_show_downloads
import com.vinovim.app.resources.downloads_titles
import com.vinovim.app.resources.downloads_watched_count
import com.vinovim.app.resources.episodes_season
import com.vinovim.app.resources.episodes_specials
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DownloadsScreen(
    onBack: () -> Unit,
    onOpenDownload: (DownloadItem) -> Unit,
    onOpenDetails: (DownloadItem) -> Unit,
    initialShowId: String? = null,
    onNavigateToShow: ((showId: String, title: String) -> Unit)? = null,
    onBackFromShow: (() -> Unit)? = null,
) {
    val uiState by remember {
        DownloadsRepository.ensureLoaded()
        DownloadsRepository.uiState
    }.collectAsStateWithLifecycle()
    val watchProgressState by remember {
        WatchProgressRepository.ensureLoaded()
        WatchProgressRepository.uiState
    }.collectAsStateWithLifecycle()
    val watchedState by remember {
        WatchedRepository.ensureLoaded()
        WatchedRepository.uiState
    }.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.completedItems.size) {
        DownloadsRepository.markCompletedSeen()
    }

    var selectedShowId by rememberSaveable(initialShowId) { mutableStateOf(initialShowId) }
    var actionsItemId by rememberSaveable { mutableStateOf<String?>(null) }
    var deleteItemId by rememberSaveable { mutableStateOf<String?>(null) }
    val speeds = rememberDownloadSpeeds(uiState.items)
    val watchStatusOf = remember(watchProgressState, watchedState) {
        { item: DownloadItem -> item.watchStatus(watchProgressState) }
    }
    val openDownloadsDirectoryFailedText = stringResource(Res.string.downloads_open_directory_failed)
    val selectedShowTitle = remember(selectedShowId, uiState.items) {
        selectedShowId?.let { showId ->
            uiState.items.firstOrNull { it.isEpisode && it.parentMetaId == showId }?.title
        }
    }
    val onOpenActions: (DownloadItem) -> Unit = { actionsItemId = it.id }

    PlatformBackHandler(enabled = selectedShowId != null && onBackFromShow == null) {
        selectedShowId = null
    }

    VinovimScreen {
        stickyHeader {
            VinovimScreenHeader(
                title = if (selectedShowId == null) {
                    stringResource(Res.string.compose_settings_root_downloads_title)
                } else {
                    selectedShowTitle ?: stringResource(Res.string.downloads_show_downloads)
                },
                onBack = {
                    if (selectedShowId != null) {
                        onBackFromShow?.invoke() ?: run { selectedShowId = null }
                    } else {
                        onBack()
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (!DownloadsPlatformDownloader.openDownloadsDirectory()) {
                                VinovimToastController.show(openDownloadsDirectoryFailedText)
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Folder,
                            contentDescription = stringResource(Res.string.downloads_open_directory),
                        )
                    }
                },
            )
        }

        val showId = selectedShowId
        if (showId == null) {
            downloadsRootContent(
                items = uiState.items,
                speeds = speeds,
                watchStatusOf = watchStatusOf,
                onPlay = onOpenDownload,
                onOpenShow = { item ->
                    onNavigateToShow?.invoke(item.parentMetaId, item.title) ?: run { selectedShowId = item.parentMetaId }
                },
                onOpenActions = onOpenActions,
            )
        } else {
            downloadsShowContent(
                episodes = uiState.items
                    .filter { it.isEpisode && it.parentMetaId == showId }
                    .sortedForSeriesDownloads(),
                speeds = speeds,
                watchStatusOf = watchStatusOf,
                onPlay = onOpenDownload,
                onOpenActions = onOpenActions,
            )
        }
    }

    val actionsItem = actionsItemId?.let { id -> uiState.items.firstOrNull { it.id == id } }
    DownloadActionSheet(
        item = actionsItem,
        watchStatus = actionsItem?.let(watchStatusOf) ?: DownloadWatchStatus(),
        onDismiss = { actionsItemId = null },
        onPlay = { actionsItem?.let(onOpenDownload) },
        onToggleTransfer = { actionsItem?.toggleTransfer() },
        onOpenDetails = { actionsItem?.let(onOpenDetails) },
        onDelete = { deleteItemId = actionsItem?.id },
    )

    val deleteItem = deleteItemId?.let { id -> uiState.items.firstOrNull { it.id == id } }
    if (deleteItem != null) {
        DeleteDownloadDialog(
            item = deleteItem,
            onConfirm = {
                DownloadsRepository.cancelDownload(deleteItem.id)
                deleteItemId = null
            },
            onDismiss = { deleteItemId = null },
        )
    }
}

private fun LazyListScope.downloadsRootContent(
    items: List<DownloadItem>,
    speeds: Map<String, Long>,
    watchStatusOf: (DownloadItem) -> DownloadWatchStatus,
    onPlay: (DownloadItem) -> Unit,
    onOpenShow: (DownloadItem) -> Unit,
    onOpenActions: (DownloadItem) -> Unit,
) {
    if (items.isEmpty()) {
        item(key = "empty") {
            DownloadsEmptyState(
                title = stringResource(Res.string.downloads_empty_title),
                message = stringResource(Res.string.downloads_empty_message),
            )
        }
        return
    }

    val activeItems = items.filter { it.status != DownloadStatus.Completed }
    val entries = items.downloadedEntries()

    item(key = "summary") {
        val titleCount = items.distinctBy { it.parentMetaId }.size
        SummaryText(
            listOf(
                pluralStringResource(Res.plurals.downloads_titles, titleCount, titleCount),
                formatBytes(items.sumOf { it.sizeOnDiskBytes }),
            ),
        )
    }

    if (activeItems.isNotEmpty()) {
        item(key = "active_header") {
            val hasDownloading = activeItems.any { it.status == DownloadStatus.Downloading }
            val hasPaused = activeItems.any { it.status == DownloadStatus.Paused }
            SectionHeader(
                title = stringResource(Res.string.downloads_section_active),
                actionText = when {
                    activeItems.size < 2 -> null
                    hasDownloading -> stringResource(Res.string.downloads_pause_all)
                    hasPaused -> stringResource(Res.string.downloads_resume_all)
                    else -> null
                },
                onAction = {
                    if (hasDownloading) {
                        DownloadsRepository.pauseActiveDownloads()
                    } else {
                        DownloadsRepository.resumePausedDownloads()
                    }
                },
            )
        }
        items(items = activeItems, key = { it.id }) { item ->
            ActiveDownloadRow(
                item = item,
                speedBytesPerSecond = speeds[item.id],
                inShowPage = false,
                onToggle = item::toggleTransfer,
                onOpenActions = { onOpenActions(item) },
            )
        }
    }

    if (entries.isNotEmpty()) {
        item(key = "downloaded_header") {
            SectionHeader(title = stringResource(Res.string.downloads_section_downloaded))
        }
        items(items = entries, key = { it.key }) { entry ->
            when (entry) {
                is DownloadedEntry.Movie -> DownloadedMovieRow(
                    item = entry.item,
                    watchStatus = watchStatusOf(entry.item),
                    onPlay = { onPlay(entry.item) },
                    onOpenActions = { onOpenActions(entry.item) },
                )
                is DownloadedEntry.Show -> DownloadedShowRow(
                    cover = entry.episodes.first(),
                    episodeCount = entry.episodes.size,
                    sizeBytes = entry.episodes.sumOf { it.sizeOnDiskBytes },
                    downloadingCount = entry.downloadingCount,
                    onOpen = { onOpenShow(entry.episodes.first()) },
                )
            }
        }
    }
}

private fun LazyListScope.downloadsShowContent(
    episodes: List<DownloadItem>,
    speeds: Map<String, Long>,
    watchStatusOf: (DownloadItem) -> DownloadWatchStatus,
    onPlay: (DownloadItem) -> Unit,
    onOpenActions: (DownloadItem) -> Unit,
) {
    if (episodes.isEmpty()) {
        item(key = "empty") {
            DownloadsEmptyState(title = stringResource(Res.string.downloads_empty_title))
        }
        return
    }

    item(key = "summary") {
        val watchedCount = episodes.count { watchStatusOf(it).isWatched }
        SummaryText(
            listOfNotNull(
                pluralStringResource(Res.plurals.downloads_episodes, episodes.size, episodes.size),
                formatBytes(episodes.sumOf { it.sizeOnDiskBytes }),
                stringResource(Res.string.downloads_watched_count, watchedCount).takeIf { watchedCount > 0 },
            ),
        )
    }

    episodes
        .groupBy { it.seasonNumber ?: 0 }
        .toList()
        .sortedBy { (season, _) -> season }
        .forEach { (season, seasonEpisodes) ->
            item(key = "season_$season") {
                SectionHeader(
                    title = if (season == 0) {
                        stringResource(Res.string.episodes_specials)
                    } else {
                        stringResource(Res.string.episodes_season, season)
                    },
                    detail = listOf(
                        pluralStringResource(Res.plurals.downloads_episodes, seasonEpisodes.size, seasonEpisodes.size),
                        formatBytes(seasonEpisodes.sumOf { it.sizeOnDiskBytes }),
                    ).joinToString(DownloadTextSeparator),
                )
            }
            items(items = seasonEpisodes, key = { it.id }) { item ->
                if (item.status == DownloadStatus.Completed) {
                    DownloadedEpisodeRow(
                        item = item,
                        watchStatus = watchStatusOf(item),
                        onPlay = { onPlay(item) },
                        onOpenActions = { onOpenActions(item) },
                    )
                } else {
                    ActiveDownloadRow(
                        item = item,
                        speedBytesPerSecond = speeds[item.id],
                        inShowPage = true,
                        onToggle = item::toggleTransfer,
                        onOpenActions = { onOpenActions(item) },
                    )
                }
            }
        }
}

@Composable
private fun SummaryText(parts: List<String>) {
    Text(
        text = parts.joinToString(DownloadTextSeparator),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.vinovim.colors.textSecondary,
    )
}

@Composable
private fun SectionHeader(
    title: String,
    detail: String? = null,
    actionText: String? = null,
    onAction: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = VinovimTokens.Space.s8),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        VinovimSectionLabel(text = title)
        when {
            actionText != null -> VinovimActionLabel(text = actionText, onClick = onAction)
            detail != null -> Text(
                text = detail,
                style = MaterialTheme.vinovimTypeScale.bodySm,
                color = MaterialTheme.vinovim.colors.textMuted,
            )
        }
    }
}

@Composable
private fun DownloadsEmptyState(
    title: String,
    message: String? = null,
) {
    val tokens = MaterialTheme.vinovim
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = VinovimTokens.Space.s24, vertical = VinovimTokens.Space.s64),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(VinovimTokens.Space.s8),
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = VinovimTokens.Space.s8)
                .size(VinovimTokens.Space.s64)
                .clip(tokens.shapes.avatar)
                .background(tokens.colors.surfaceCard),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.Download,
                contentDescription = null,
                tint = tokens.colors.textMuted,
                modifier = Modifier.size(VinovimTokens.Icon.xl),
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = tokens.colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        if (message != null) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = tokens.colors.textMuted,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun DeleteDownloadDialog(
    item: DownloadItem,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val name = listOfNotNull(item.title, item.episodeCode()).joinToString(" ")
    val sizeBytes = item.sizeOnDiskBytes
    VinovimStatusModal(
        title = stringResource(Res.string.downloads_delete_title, name),
        message = if (sizeBytes > 0L) {
            stringResource(Res.string.downloads_delete_message, formatBytes(sizeBytes))
        } else {
            stringResource(Res.string.action_delete_confirm_message)
        },
        isVisible = true,
        destructive = true,
        confirmText = stringResource(Res.string.action_delete),
        dismissText = stringResource(Res.string.action_cancel),
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}

private sealed interface DownloadedEntry {
    val key: String
    val addedAtEpochMs: Long

    data class Movie(val item: DownloadItem) : DownloadedEntry {
        override val key: String get() = item.id
        override val addedAtEpochMs: Long get() = item.createdAtEpochMs
    }

    data class Show(
        val episodes: List<DownloadItem>,
        val downloadingCount: Int,
    ) : DownloadedEntry {
        override val key: String get() = "show:${episodes.first().parentMetaId}"
        override val addedAtEpochMs: Long get() = episodes.maxOf { it.createdAtEpochMs }
    }
}

private fun List<DownloadItem>.downloadedEntries(): List<DownloadedEntry> {
    val completed = filter { it.status == DownloadStatus.Completed }
    val downloadingByShow = filter { it.isEpisode && it.status != DownloadStatus.Completed }
        .groupingBy { it.parentMetaId }
        .eachCount()
    val movies = completed
        .filterNot { it.isEpisode }
        .map { DownloadedEntry.Movie(it) }
    val shows = completed
        .filter { it.isEpisode }
        .groupBy { it.parentMetaId }
        .map { (showId, episodes) ->
            DownloadedEntry.Show(
                episodes = episodes.sortedForSeriesDownloads(),
                downloadingCount = downloadingByShow[showId] ?: 0,
            )
        }
    return (movies + shows).sortedByDescending { it.addedAtEpochMs }
}

private fun DownloadItem.toggleTransfer() {
    when (status) {
        DownloadStatus.Downloading -> DownloadsRepository.pauseDownload(id)
        DownloadStatus.Paused -> DownloadsRepository.resumeDownload(id)
        DownloadStatus.Failed -> DownloadsRepository.retryDownload(id)
        DownloadStatus.Completed -> Unit
    }
}
