package com.vinovim.app.core.ui

import com.vinovim.app.core.storage.DesktopStorage
import com.vinovim.app.core.storage.ProfileScopedKey

internal actual object PosterCardStyleStorage {
    private val store = DesktopStorage.store("vinovim_poster_card_style")

    actual fun loadPayload(): String? =
        store.getString(ProfileScopedKey.of("poster_card_style"))

    actual fun savePayload(payload: String) {
        store.putString(ProfileScopedKey.of("poster_card_style"), payload)
    }
}
