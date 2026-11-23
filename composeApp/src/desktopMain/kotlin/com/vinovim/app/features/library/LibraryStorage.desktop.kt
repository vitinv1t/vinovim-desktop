package com.vinovim.app.features.library

import com.vinovim.app.core.storage.DesktopStorage

internal actual object LibraryStorage {
    private val store = DesktopStorage.store("vinovim_library")

    actual fun loadPayload(profileId: Int): String? =
        store.getString("library_$profileId")

    actual fun savePayload(profileId: Int, payload: String) {
        store.putString("library_$profileId", payload)
    }
}
