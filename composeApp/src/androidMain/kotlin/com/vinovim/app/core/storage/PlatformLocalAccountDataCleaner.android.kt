package com.vinovim.app.core.storage

import android.content.Context

internal actual object PlatformLocalAccountDataCleaner {
    private val preferenceNames = listOf(
        "episode_shuffle",
        "vinovim_addons",
        "vinovim_library",
        "vinovim_library_display_settings",
        "vinovim_home_catalog_settings",
        "vinovim_player_settings",
        "torrent_settings",
        "vinovim_profile_cache",
        "vinovim_avatar_cache",
        "vinovim_profile_pin_cache",
        "vinovim_theme_settings",
        "vinovim_poster_card_style",
        "vinovim_debrid_settings",
        "vinovim_mdblist_settings",
        "vinovim_mdblist_auth",
        "vinovim_mdblist_sync",
        "vinovim_auth",
        "vinovim_trakt_auth",
        "vinovim_simkl_auth",
        "vinovim_simkl_sync",
        "vinovim_trakt_library",
        "vinovim_trakt_settings",
        "vinovim_watched",
        "vinovim_stream_link_cache",
        "vinovim_stream_badge_settings",
        "vinovim_continue_watching_preferences",
        "vinovim_cw_enrichment",
        "vinovim_episode_release_notifications",
        "vinovim_episode_release_notifications_platform",
        "vinovim_watch_progress",
        "vinovim_discover_selection",
        "vinovim_collection_mobile_settings",
        "vinovim_collections",
        "vinovim_plugins",
        "vinovim_member_access",
    )

    private var appContext: Context? = null

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    actual fun wipe() {
        val context = appContext ?: return
        preferenceNames.forEach { name ->
            context.getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()
        }
        context.filesDir.resolve("vinovim_plugin_scrapers").deleteRecursively()
    }
}
