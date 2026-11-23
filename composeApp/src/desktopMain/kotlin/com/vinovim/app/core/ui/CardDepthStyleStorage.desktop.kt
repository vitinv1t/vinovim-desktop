package com.vinovim.app.core.ui

import com.vinovim.app.core.storage.DesktopStorage
import com.vinovim.app.core.storage.ProfileScopedKey

internal actual object CardDepthStyleStorage {
    private const val payloadKey = "card_depth_style_payload"
    private val store = DesktopStorage.store("vinovim_card_depth_style")

    actual fun loadPayload(): String? =
        store.getString(ProfileScopedKey.of(payloadKey))

    actual fun savePayload(payload: String) {
        store.putString(ProfileScopedKey.of(payloadKey), payload)
    }
}
