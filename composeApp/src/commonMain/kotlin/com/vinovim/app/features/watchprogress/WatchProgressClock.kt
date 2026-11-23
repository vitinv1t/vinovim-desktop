package com.vinovim.app.features.watchprogress

internal expect object WatchProgressClock {
    fun nowEpochMs(): Long
}
