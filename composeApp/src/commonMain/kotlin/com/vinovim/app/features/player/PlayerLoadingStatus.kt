package com.vinovim.app.features.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.player_loading_buffering
import com.vinovim.app.resources.player_loading_building
import com.vinovim.app.resources.player_loading_starting
import com.vinovim.app.resources.player_loading_subtitles
import com.vinovim.app.resources.player_loading_subtitles_from
import com.vinovim.app.resources.player_loading_subtitles_progress
import com.vinovim.app.resources.player_loading_subtitles_addon
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * The player start-up steps, as VinovimTV shows them: the engine is created, then it opens the
 * source and reads its tracks, then it buffers until it can play.
 */
internal fun playerLoadingStatusResource(
    showStatus: Boolean,
    controllerReady: Boolean,
    snapshot: PlayerPlaybackSnapshot,
): StringResource? = when {
    !showStatus -> null
    !controllerReady -> Res.string.player_loading_building
    snapshot.isLoading && snapshot.isOpening -> Res.string.player_loading_starting
    else -> Res.string.player_loading_buffering
}

@Composable
internal fun playerLoadingStatusMessage(
    showStatus: Boolean,
    controllerReady: Boolean,
    snapshot: PlayerPlaybackSnapshot,
): String? = playerLoadingStatusResource(showStatus, controllerReady, snapshot)
    ?.let { stringResource(it) }

@Composable
internal fun subtitleLoadingStatusMessage(): String {
    val progress by SubtitleRepository.loadingProgress.collectAsStateWithLifecycle()
    return subtitleLoadingStatusMessage(progress)
}

@Composable
internal fun subtitleLoadingStatusMessage(progress: SubtitleLoadingProgress?): String = when {
    progress == null -> stringResource(Res.string.player_loading_subtitles)
    progress.completed == 0 -> stringResource(Res.string.player_loading_subtitles_from, progress.total)
    !progress.addonName.isNullOrBlank() -> stringResource(
        Res.string.player_loading_subtitles_addon,
        progress.addonName,
        progress.completed,
        progress.total,
    )
    else -> stringResource(Res.string.player_loading_subtitles_progress, progress.completed, progress.total)
}
