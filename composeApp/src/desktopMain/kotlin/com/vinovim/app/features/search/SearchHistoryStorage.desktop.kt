package com.vinovim.app.features.search

import com.vinovim.app.core.storage.DesktopStorage
import com.vinovim.app.core.storage.ProfileScopedKey

internal actual object SearchHistoryStorage {
    private val store = DesktopStorage.store("vinovim_search_history")
    private const val enabledKey = "recent_searches_enabled"

    actual fun loadPayload(): String? =
        store.getString(ProfileScopedKey.of("search_history"))

    actual fun savePayload(payload: String) {
        store.putString(ProfileScopedKey.of("search_history"), payload)
    }

    actual fun loadEnabled(): Boolean? = store.getBoolean(ProfileScopedKey.of(enabledKey))

    actual fun saveEnabled(enabled: Boolean) {
        store.putBoolean(ProfileScopedKey.of(enabledKey), enabled)
    }
}
