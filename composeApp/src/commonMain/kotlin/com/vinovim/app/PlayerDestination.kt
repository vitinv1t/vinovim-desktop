package com.vinovim.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.vinovim.app.core.ui.VinovimToastController
import com.vinovim.app.features.party.PartyMedia
import com.vinovim.app.features.player.ExternalPlayerIntentResult
import com.vinovim.app.features.player.ExternalPlayerPlatform
import com.vinovim.app.features.player.PlayerLaunch
import com.vinovim.app.features.player.PlayerLaunchStore
import com.vinovim.app.features.player.PlayerScreen
import com.vinovim.app.features.watchprogress.ResumePromptRepository
import com.vinovim.app.navigation.VinovimNavigator
import com.vinovim.app.navigation.PlayerRoute

@Composable
internal fun PlayerDestination(
    route: PlayerRoute,
    navController: VinovimNavigator,
    externalPlayerId: String?,
    externalPlayerNotConfiguredText: String,
    externalPlayerFailedText: String,
    onExternalPlayerLaunch: (PlayerLaunch) -> Unit,
    launchExternalPlayer: (ExternalPlayerIntentResult.Success) -> Boolean,
    openExternalStreamUrl: (String) -> Boolean,
    onSystemBackHandlerChanged: (PlayerRoute, (() -> Unit)?) -> Unit,
    onOpenPartyMedia: (PartyMedia) -> Unit,
) {
    val popBack = rememberGuardedPopBackStack(
        navController = navController,
        route = route,
        beforePop = ResumePromptRepository::markPlayerExitedNormally,
    )
    val launch = remember(route.launchId) { PlayerLaunchStore.get(route.launchId) }
    if (launch == null) {
        LaunchedEffect(route.launchId) {
            popBack()
        }
        Box(modifier = Modifier.fillMaxSize())
        return
    }
    val currentFullscreen = com.vinovim.app.core.ui.isFullscreenActionActive()
    val initialFullscreen = remember { currentFullscreen }
    val onBackBase = rememberGuardedPlayerPopBackStack(
        navController = navController,
        route = route,
        beforePop = ResumePromptRepository::markPlayerExitedNormally,
    )
    val onBack = remember(onBackBase, initialFullscreen, currentFullscreen) {
        { releaseBeforeBack: com.vinovim.app.features.player.PlayerReleaseBeforeBack ->
            if (currentFullscreen != initialFullscreen) {
                com.vinovim.app.core.ui.toggleFullscreenAction()
            }
            onBackBase(releaseBeforeBack)
        }
    }
    val registerSystemBack = remember(route, onSystemBackHandlerChanged) {
        { handler: (() -> Unit)? -> onSystemBackHandlerChanged(route, handler) }
    }
    LaunchedEffect(launch.videoId) {
        launch.videoId?.let { ResumePromptRepository.markPlayerEntered(it) }
    }
    PlayerScreen(
        profileId = launch.profileId,
        title = launch.title,
        sourceUrl = launch.sourceUrl,
        sourceAudioUrl = launch.sourceAudioUrl,
        sourceHeaders = launch.sourceHeaders,
        sourceResponseHeaders = launch.sourceResponseHeaders,
        externalSubtitles = launch.externalSubtitles,
        streamType = launch.streamType,
        logo = launch.logo,
        poster = launch.poster,
        background = launch.background,
        seasonNumber = launch.seasonNumber,
        episodeNumber = launch.episodeNumber,
        episodeTitle = launch.episodeTitle,
        episodeThumbnail = launch.episodeThumbnail,
        streamTitle = launch.streamTitle,
        streamSubtitle = launch.streamSubtitle,
        initialBingeGroup = launch.bingeGroup,
        pauseDescription = launch.pauseDescription,
        providerName = launch.providerName,
        providerAddonId = launch.providerAddonId,
        contentType = launch.contentType,
        videoId = launch.videoId,
        parentMetaId = launch.parentMetaId,
        parentMetaType = launch.parentMetaType,
        torrentInfoHash = launch.torrentInfoHash,
        torrentFileIdx = launch.torrentFileIdx,
        torrentFilename = launch.torrentFilename,
        torrentTrackers = launch.torrentTrackers,
        initialPositionMs = launch.initialPositionMs,
        initialProgressFraction = launch.initialProgressFraction,
        contentLanguage = launch.contentLanguage,
        subtitleExtras = launch.subtitleExtras,
        launchId = route.launchId,
        sourceFilename = launch.sourceFilename,
        stream = launch.stream,
        sourceFromLinkCache = launch.sourceFromLinkCache,
        sourceAutoSelected = launch.sourceAutoSelected,
        onBack = onBack,
        onSystemBackHandlerChanged = registerSystemBack,
        onOpenInExternalPlayer = if (externalPlayerSupported) { { request ->
            val playerLaunch = PlayerLaunch(
                profileId = launch.profileId,
                title = launch.title,
                sourceUrl = request.sourceUrl,
                sourceHeaders = request.sourceHeaders,
                logo = launch.logo,
                poster = launch.poster,
                background = launch.background,
                seasonNumber = launch.seasonNumber,
                episodeNumber = launch.episodeNumber,
                episodeTitle = launch.episodeTitle,
                episodeThumbnail = launch.episodeThumbnail,
                streamTitle = request.streamTitle ?: launch.streamTitle,
                streamSubtitle = launch.streamSubtitle,
                bingeGroup = launch.bingeGroup,
                pauseDescription = launch.pauseDescription,
                providerName = launch.providerName,
                providerAddonId = launch.providerAddonId,
                contentType = launch.contentType,
                videoId = launch.videoId,
                parentMetaId = launch.parentMetaId,
                parentMetaType = launch.parentMetaType,
                initialPositionMs = request.resumePositionMs,
                subtitleExtras = launch.subtitleExtras,
            )
            onExternalPlayerLaunch(playerLaunch)
            val intentResult = ExternalPlayerPlatform.buildIntent(
                request = request,
                playerId = externalPlayerId,
            )
            when (intentResult) {
                is ExternalPlayerIntentResult.Success -> {
                    val launched = launchExternalPlayer(intentResult)
                    if (!launched) {
                        VinovimToastController.show(externalPlayerFailedText)
                    } else if (externalPlayerId == "infuse") {
                        popBack()
                    }
                }
                ExternalPlayerIntentResult.NotConfigured -> {
                    VinovimToastController.show(externalPlayerNotConfiguredText)
                }
                ExternalPlayerIntentResult.Failed -> {
                    VinovimToastController.show(externalPlayerFailedText)
                }
            }
        } } else null,
        onOpenExternalUrl = { url ->
            openExternalStreamUrl(url)
        },
        onOpenPartyMedia = onOpenPartyMedia,
        modifier = Modifier.fillMaxSize(),
    )
}
