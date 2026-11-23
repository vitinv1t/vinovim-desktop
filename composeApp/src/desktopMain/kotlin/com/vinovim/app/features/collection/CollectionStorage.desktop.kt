package com.vinovim.app.features.collection

import com.vinovim.app.core.storage.DesktopStorage
import com.vinovim.app.core.storage.ProfileScopedKey

internal actual object CollectionStorage {
    private val store = DesktopStorage.store("vinovim_collections")

    actual fun loadPayload(): String? =
        store.getString(ProfileScopedKey.of("collections"))

    actual fun savePayload(payload: String) {
        store.putString(ProfileScopedKey.of("collections"), payload)
    }
}
