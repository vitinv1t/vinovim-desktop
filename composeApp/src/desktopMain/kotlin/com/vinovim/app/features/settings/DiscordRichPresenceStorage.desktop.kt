package com.vinovim.app.features.settings

import com.vinovim.app.core.storage.DesktopStorage

internal actual object DiscordRichPresencePlatform {
    actual val isSupported: Boolean = true
}

internal actual object DiscordRichPresenceStorage {
    private const val enabledKey = "enabled"
    private val store = DesktopStorage.store("vinovim_discord_rich_presence")

    actual fun loadEnabled(): Boolean? =
        if (store.contains(enabledKey)) store.getBoolean(enabledKey) else null

    actual fun saveEnabled(enabled: Boolean) {
        store.putBoolean(enabledKey, enabled)
    }
}
