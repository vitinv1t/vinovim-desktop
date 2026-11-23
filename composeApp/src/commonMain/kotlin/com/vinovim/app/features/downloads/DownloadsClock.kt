package com.vinovim.app.features.downloads

internal expect object DownloadsClock {
    fun nowEpochMs(): Long
}
