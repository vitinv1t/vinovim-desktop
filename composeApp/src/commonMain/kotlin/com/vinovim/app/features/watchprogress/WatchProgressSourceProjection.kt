package com.vinovim.app.features.watchprogress

import com.vinovim.app.features.tracking.WatchProgressSource

internal fun projectWatchProgressSourceEntries(
    source: WatchProgressSource,
    vinovimEntries: Collection<WatchProgressEntry>,
    providerEntries: Collection<WatchProgressEntry>,
): List<WatchProgressEntry> = if (source.providerId == null) {
    vinovimEntries.toList()
} else {
    providerEntries.toList()
}
