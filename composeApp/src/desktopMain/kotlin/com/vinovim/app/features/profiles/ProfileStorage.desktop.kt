package com.vinovim.app.features.profiles

import com.vinovim.app.core.storage.DesktopStorage

internal actual object ProfileStorage {
    private val store = DesktopStorage.store("vinovim_profiles")

    actual fun loadPayload(): String? = store.getString("profiles")

    actual fun savePayload(payload: String) {
        store.putString("profiles", payload)
    }
}
