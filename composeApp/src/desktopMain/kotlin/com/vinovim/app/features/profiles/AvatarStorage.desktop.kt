package com.vinovim.app.features.profiles

import com.vinovim.app.core.storage.DesktopStorage

internal actual object AvatarStorage {
    private val store = DesktopStorage.store("vinovim_avatars")

    actual fun loadPayload(): String? = store.getString("avatars")

    actual fun savePayload(payload: String) {
        store.putString("avatars", payload)
    }
}
