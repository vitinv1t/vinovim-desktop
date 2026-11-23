package com.vinovim.app.features.watched

import com.vinovim.app.core.storage.DesktopStorage

actual object WatchedStorage {
    private val store = DesktopStorage.store("vinovim_watched")

    actual fun loadPayload(profileId: Int): String? =
        store.getString("watched_$profileId")

    actual fun savePayload(profileId: Int, payload: String) {
        store.putString("watched_$profileId", payload)
    }
}
