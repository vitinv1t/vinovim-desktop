package com.vinovim.app.features.watched

import com.vinovim.app.features.tracking.TrackingProviderId
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

internal class WatchedItemsStore {
    private val lock = SynchronizedObject()
    private val vinovimItems = mutableMapOf<String, WatchedItem>()
    private val providerItems = mutableMapOf<TrackingProviderId, MutableMap<String, WatchedItem>>()
    private val dirtyVinovimKeys = mutableSetOf<String>()
    private val dirtyProviderKeys = mutableMapOf<TrackingProviderId, MutableSet<String>>()

    fun <T> read(
        block: (
            vinovimItems: Map<String, WatchedItem>,
            providerItems: Map<TrackingProviderId, Map<String, WatchedItem>>,
            dirtyVinovimKeys: Set<String>,
            dirtyProviderKeys: Map<TrackingProviderId, Set<String>>,
        ) -> T,
    ): T = synchronized(lock) {
        block(vinovimItems, providerItems, dirtyVinovimKeys, dirtyProviderKeys)
    }

    fun <T> update(
        block: (
            vinovimItems: MutableMap<String, WatchedItem>,
            providerItems: MutableMap<TrackingProviderId, MutableMap<String, WatchedItem>>,
            dirtyVinovimKeys: MutableSet<String>,
            dirtyProviderKeys: MutableMap<TrackingProviderId, MutableSet<String>>,
        ) -> T,
    ): T = synchronized(lock) {
        block(vinovimItems, providerItems, dirtyVinovimKeys, dirtyProviderKeys)
    }
}
