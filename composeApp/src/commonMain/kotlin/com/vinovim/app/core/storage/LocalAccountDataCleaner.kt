package com.vinovim.app.core.storage

import com.vinovim.app.core.build.AppFeaturePolicy
import com.vinovim.app.core.sync.SyncManager
import com.vinovim.app.core.sync.ProfileSettingsSync
import com.vinovim.app.core.tracking.ensureTrackingProvidersRegistered
import com.vinovim.app.features.addons.AddonRepository
import com.vinovim.app.features.catalog.CatalogRepository
import com.vinovim.app.features.collection.CollectionMobileSettingsRepository
import com.vinovim.app.features.collection.CollectionRepository
import com.vinovim.app.features.details.MetaDetailsRepository
import com.vinovim.app.features.details.MetaScreenSettingsRepository
import com.vinovim.app.features.home.HomeCatalogSettingsRepository
import com.vinovim.app.features.home.HomeRepository
import com.vinovim.app.features.library.LibraryRepository
import com.vinovim.app.features.membership.MemberAccessRepository
import com.vinovim.app.features.library.LibraryDisplaySettingsRepository
import com.vinovim.app.features.notifications.EpisodeReleaseNotificationsRepository
import com.vinovim.app.features.player.PlayerLaunchStore
import com.vinovim.app.features.player.PlayerSettingsRepository
import com.vinovim.app.features.p2p.P2pSettingsRepository
import com.vinovim.app.features.plugins.PluginRepository
import com.vinovim.app.features.player.SubtitleRepository
import com.vinovim.app.features.profiles.ProfileRepository
import com.vinovim.app.features.profiles.MAX_PROFILES
import com.vinovim.app.features.search.SearchRepository
import com.vinovim.app.features.servers.ServerRepository
import com.vinovim.app.features.settings.ThemeSettingsRepository
import com.vinovim.app.features.streams.StreamContextStore
import com.vinovim.app.features.streams.StreamBadgeSettingsRepository
import com.vinovim.app.features.streams.StreamLaunchStore
import com.vinovim.app.features.streams.StreamsRepository
import com.vinovim.app.features.tracking.TrackingProviderRegistry
import com.vinovim.app.features.tracking.TrackingSettingsRepository
import com.vinovim.app.core.ui.CardDepthStyleRepository
import com.vinovim.app.core.ui.PosterCardStyleRepository
import com.vinovim.app.features.watchprogress.ContinueWatchingPreferencesRepository
import com.vinovim.app.features.watchprogress.ContinueWatchingEnrichmentCache
import com.vinovim.app.features.watchprogress.WatchProgressRepository
import com.vinovim.app.features.watchprogress.WatchProgressSourceCoordinator
import com.vinovim.app.features.watched.WatchedRepository

internal object LocalAccountDataCleaner {
    fun wipe() {
        ensureTrackingProvidersRegistered()
        TrackingProviderRegistry.removeStoredProfiles(1..MAX_PROFILES)
        SyncManager.cancelAccountSync()
        WatchProgressSourceCoordinator.clearLocalState()
        ProfileSettingsSync.clearAccountState()
        ContinueWatchingEnrichmentCache.clearLocalState()
        WatchProgressRepository.clearLocalState()
        WatchedRepository.clearLocalState()
        LibraryRepository.runAccountStorageWipe {
            wipePlatformStorage()
        }

        ProfileRepository.clearInMemory()
        MemberAccessRepository.clearLocalState()
        AddonRepository.clearLocalState()
        ServerRepository.clearLocalState()
        if (AppFeaturePolicy.pluginsEnabled) {
            PluginRepository.clearLocalState()
        }
        HomeRepository.clear()
        HomeCatalogSettingsRepository.clearLocalState()
        MetaScreenSettingsRepository.clearLocalState()
        com.vinovim.app.features.shuffle.EpisodeShuffleRepository.clearLocalState()
        LibraryRepository.clearLocalState()
        LibraryDisplaySettingsRepository.clearLocalState()
        ContinueWatchingPreferencesRepository.clearLocalState()
        EpisodeReleaseNotificationsRepository.clearLocalState()
        CollectionMobileSettingsRepository.clearLocalState()
        CollectionRepository.clearLocalState()
        ThemeSettingsRepository.clearLocalState()
        PosterCardStyleRepository.clearLocalState()
        CardDepthStyleRepository.clearLocalState()
        TrackingProviderRegistry.clearLocalState()
        TrackingSettingsRepository.clearLocalState()
        PlayerSettingsRepository.clearLocalState()
        StreamBadgeSettingsRepository.clearLocalState()
        P2pSettingsRepository.clearLocalState()
        CatalogRepository.clear()
        StreamsRepository.clear()
        MetaDetailsRepository.clear()
        SearchRepository.reset()
        SubtitleRepository.clear()
        PlayerLaunchStore.clear()
        StreamLaunchStore.clear()
        StreamContextStore.clear()
    }

    internal fun wipePlatformStorage(wipeStorage: () -> Unit = PlatformLocalAccountDataCleaner::wipe) {
        try {
            wipeStorage()
        } finally {
            ContinueWatchingEnrichmentCache.clearLocalState()
        }
    }
}

internal expect object PlatformLocalAccountDataCleaner {
    fun wipe()
}
