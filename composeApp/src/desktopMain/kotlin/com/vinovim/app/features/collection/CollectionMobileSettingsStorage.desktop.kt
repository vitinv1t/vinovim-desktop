package com.vinovim.app.features.collection

import com.vinovim.app.core.storage.DesktopStorage
import com.vinovim.app.core.storage.ProfileScopedKey

internal actual object CollectionMobileSettingsStorage {
    private val store = DesktopStorage.store("vinovim_collection_mobile_settings")

    actual fun loadPayload(): String? =
        store.getString(ProfileScopedKey.of("collection_mobile_settings"))

    actual fun savePayload(payload: String) {
        store.putString(ProfileScopedKey.of("collection_mobile_settings"), payload)
    }
}
