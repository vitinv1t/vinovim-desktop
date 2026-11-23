package com.vinovim.app

import android.content.Intent
import android.content.res.Configuration
import com.vinovim.app.features.autosync.AutoSyncPreferencesAndroid
import android.os.Bundle
import androidx.activity.compose.setContent

import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.vinovim.app.core.auth.AuthStorage
import com.vinovim.app.core.network.ServerConfigurationStorage
import com.vinovim.app.core.diagnostics.SentryInitializer
import com.vinovim.app.core.deeplink.handleAppUrl
import com.vinovim.app.core.storage.PlatformLocalAccountDataCleaner
import com.vinovim.app.core.sync.SyncClientIdentityStorage
import com.vinovim.app.features.addons.AddonHttpClientProvider
import com.vinovim.app.features.addons.AddonStorage
import com.vinovim.app.features.collection.CollectionMobileSettingsStorage
import com.vinovim.app.features.collection.CollectionStorage
import com.vinovim.app.features.debrid.DebridSettingsStorage
import com.vinovim.app.features.downloads.DownloadsLiveStatusPlatform
import com.vinovim.app.features.downloads.DownloadsPlatformDownloader
import com.vinovim.app.features.downloads.DownloadsStorage
import com.vinovim.app.features.library.LibraryDisplaySettingsStorage
import com.vinovim.app.features.membership.MemberAssetStorage
import com.vinovim.app.features.library.LibraryStorage
import com.vinovim.app.features.details.MetaScreenSettingsStorage
import com.vinovim.app.features.home.HomeCatalogSettingsStorage
import com.vinovim.app.features.mdblist.MdbListSettingsStorage
import com.vinovim.app.features.notifications.EpisodeReleaseNotificationPlatform
import com.vinovim.app.features.notifications.EpisodeReleaseNotificationsStorage
import com.vinovim.app.features.player.PlayerSettingsStorage
import com.vinovim.app.features.player.PlayerTrackPreferenceStorage
import com.vinovim.app.features.player.ExternalPlayerPlatform
import com.vinovim.app.features.player.SubtitleFileCache
import com.vinovim.app.features.player.PlayerPictureInPictureManager
import com.vinovim.app.features.player.PipRemoteActionReceiver
import com.vinovim.app.features.p2p.P2pSettingsStorage
import com.vinovim.app.features.p2p.P2pStreamingEngine
import com.vinovim.app.features.plugins.PluginStorage
import com.vinovim.app.features.profiles.AvatarStorage
import com.vinovim.app.features.profiles.ProfilePinCacheStorage
import com.vinovim.app.features.profiles.ProfileStorage
import com.vinovim.app.features.details.SeasonViewModeStorage
import com.vinovim.app.features.search.DiscoverSelectionStorage
import com.vinovim.app.features.search.SearchHistoryStorage
import com.vinovim.app.features.settings.SentrySettingsStorage
import com.vinovim.app.features.settings.AppIconPlatform
import com.vinovim.app.features.settings.ThemeSettingsStorage
import com.vinovim.app.features.trakt.TraktAuthStorage
import com.vinovim.app.features.trakt.TraktCommentsStorage
import com.vinovim.app.features.trakt.TraktLibraryStorage
import com.vinovim.app.features.trakt.TraktSettingsStorage
import com.vinovim.app.features.mdblist.PlatformMdbListAuthPersistence
import com.vinovim.app.features.mdblist.PlatformMdbListSyncStorage
import com.vinovim.app.features.servers.ServerStorage
import com.vinovim.app.features.simkl.SimklAuthStorage
import com.vinovim.app.features.simkl.SimklSyncStorage
import com.vinovim.app.features.tmdb.TmdbSettingsStorage
import com.vinovim.app.features.updater.AndroidAppUpdaterPlatform
import com.vinovim.app.core.ui.CardDepthStyleStorage
import com.vinovim.app.core.ui.PosterCardStyleStorage
import com.vinovim.app.core.poster.CustomPosterUrlStorage
import com.vinovim.app.features.watched.WatchedStorage
import com.vinovim.app.features.streams.StreamLinkCacheStorage
import com.vinovim.app.features.streams.StreamBadgeSettingsStorage
import com.vinovim.app.features.streams.BingeGroupCacheStorage
import com.vinovim.app.features.watchprogress.ContinueWatchingEnrichmentStorage
import com.vinovim.app.features.watchprogress.ContinueWatchingPreferencesStorage
import com.vinovim.app.features.watchprogress.ResumePromptStorage
import com.vinovim.app.features.watchprogress.WatchProgressStorage

open class MainActivity : AppCompatActivity() {
    private var pipRemoteActionReceiver: PipRemoteActionReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(
                scrim = 0xFF020404.toInt(),
            ),
        )
        ThemeSettingsStorage.initialize(applicationContext)
        AppIconPlatform.initialize(applicationContext)
        SentrySettingsStorage.initialize(applicationContext)
        SentryInitializer.start(application)
        super.onCreate(savedInstanceState)
        AutoSyncPreferencesAndroid.initialize(applicationContext) // AutoSync hook
        window.setBackgroundDrawableResource(R.color.vinovim_background)
        pipRemoteActionReceiver = PipRemoteActionReceiver.register(this)
        SyncClientIdentityStorage.initialize(applicationContext)
        AddonHttpClientProvider.initialize(applicationContext)
        AddonStorage.initialize(applicationContext)
        AuthStorage.initialize(applicationContext)
        ServerConfigurationStorage.initialize(applicationContext)
        LibraryStorage.initialize(applicationContext)
        WatchedStorage.initialize(applicationContext)
        MetaScreenSettingsStorage.initialize(applicationContext)
        com.vinovim.app.features.shuffle.EpisodeShuffleStorage.initialize(applicationContext)
        HomeCatalogSettingsStorage.initialize(applicationContext)
        PlayerSettingsStorage.initialize(applicationContext)
        PlayerTrackPreferenceStorage.initialize(applicationContext)
        P2pSettingsStorage.initialize(applicationContext)
        P2pStreamingEngine.initialize(applicationContext)
        ExternalPlayerPlatform.initialize(applicationContext)
        SubtitleFileCache.initialize(applicationContext)
        ProfileStorage.initialize(applicationContext)
        AvatarStorage.initialize(applicationContext)
        ProfilePinCacheStorage.initialize(applicationContext)
        MemberAssetStorage.initialize(applicationContext)
        DiscoverSelectionStorage.initialize(applicationContext)
        SearchHistoryStorage.initialize(applicationContext)
        SeasonViewModeStorage.initialize(applicationContext)
        PosterCardStyleStorage.initialize(applicationContext)
        CustomPosterUrlStorage.initialize(applicationContext)
        CardDepthStyleStorage.initialize(applicationContext)
        DebridSettingsStorage.initialize(applicationContext)
        TmdbSettingsStorage.initialize(applicationContext)
        MdbListSettingsStorage.initialize(applicationContext)
        TraktAuthStorage.initialize(applicationContext)
        TraktCommentsStorage.initialize(applicationContext)
        TraktLibraryStorage.initialize(applicationContext)
        TraktSettingsStorage.initialize(applicationContext)
        PlatformMdbListAuthPersistence.initialize(applicationContext)
        ServerStorage.initialize(applicationContext)
        PlatformMdbListSyncStorage.initialize(applicationContext)
        SimklAuthStorage.initialize(applicationContext)
        SimklSyncStorage.initialize(applicationContext)
        LibraryDisplaySettingsStorage.initialize(applicationContext)
        ContinueWatchingPreferencesStorage.initialize(applicationContext)
        ResumePromptStorage.initialize(applicationContext)
        ContinueWatchingEnrichmentStorage.initialize(applicationContext)
        EpisodeReleaseNotificationsStorage.initialize(applicationContext)
        WatchProgressStorage.initialize(applicationContext)
        StreamLinkCacheStorage.initialize(applicationContext)
        StreamBadgeSettingsStorage.initialize(applicationContext)
        BingeGroupCacheStorage.initialize(applicationContext)
        PluginStorage.initialize(applicationContext)
        CollectionMobileSettingsStorage.initialize(applicationContext)
        CollectionStorage.initialize(applicationContext)
        DownloadsStorage.initialize(applicationContext)
        DownloadsPlatformDownloader.initialize(applicationContext)
        DownloadsLiveStatusPlatform.initialize(applicationContext)
        AndroidAppUpdaterPlatform.initialize(applicationContext)
        PlatformLocalAccountDataCleaner.initialize(applicationContext)
        EpisodeReleaseNotificationPlatform.initialize(applicationContext)
        EpisodeReleaseNotificationPlatform.bindActivity(this)
        handleIncomingAppIntent(intent)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingAppIntent(intent)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        PlayerPictureInPictureManager.onUserLeaveHint(this)
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration,
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        PlayerPictureInPictureManager.onPictureInPictureModeChanged(this, isInPictureInPictureMode)
    }

    override fun onDestroy() {
        EpisodeReleaseNotificationPlatform.unbindActivity(this)
        val receiver = pipRemoteActionReceiver
        if (receiver != null) {
            runCatching { unregisterReceiver(receiver) }
            pipRemoteActionReceiver = null
        }
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (EpisodeReleaseNotificationPlatform.handlePermissionRequestResult(requestCode, grantResults)) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun handleIncomingAppIntent(intent: Intent?) {
        val appUrl = (intent?.dataString ?: intent?.getStringExtra(LinkActivity.EXTRA_LINK))?.trim().orEmpty()
        if (appUrl.isBlank()) return
        handleAppUrl(appUrl)
    }
}
