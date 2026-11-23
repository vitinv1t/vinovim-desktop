package com.vinovim.app.features.watched

import com.vinovim.app.features.tracking.TrackingProviderId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class WatchedItemsStoreTest {
    @Test
    fun `concurrent updates publish coherent item snapshots`() = runBlocking {
        val store = WatchedItemsStore()

        coroutineScope {
            repeat(4) { writer ->
                launch(Dispatchers.Default) {
                    repeat(500) { index ->
                        val key = "$writer:$index"
                        val item = WatchedItem(
                            id = key,
                            type = "movie",
                            name = key,
                            markedAtEpochMs = index.toLong(),
                        )
                        store.update { vinovimItems, providerItems, dirtyVinovimKeys, dirtyProviderKeys ->
                            vinovimItems[key] = item
                            providerItems
                                .getOrPut(TrackingProviderId.TRAKT, ::mutableMapOf)[key] = item
                            dirtyVinovimKeys += key
                            dirtyProviderKeys
                                .getOrPut(TrackingProviderId.TRAKT, ::mutableSetOf) += key
                        }
                    }
                }
            }
            repeat(4) {
                launch(Dispatchers.Default) {
                    repeat(500) {
                        store.read { vinovimItems, providerItems, dirtyVinovimKeys, dirtyProviderKeys ->
                            val vinovimKeys = vinovimItems.keys.toSet()
                            assertEquals(vinovimKeys, providerItems[TrackingProviderId.TRAKT].orEmpty().keys)
                            assertEquals(vinovimKeys, dirtyVinovimKeys)
                            assertEquals(vinovimKeys, dirtyProviderKeys[TrackingProviderId.TRAKT].orEmpty())
                        }
                    }
                }
            }
        }

        store.read { vinovimItems, providerItems, dirtyVinovimKeys, dirtyProviderKeys ->
            assertEquals(2_000, vinovimItems.size)
            assertEquals(vinovimItems.keys, providerItems[TrackingProviderId.TRAKT].orEmpty().keys)
            assertEquals(vinovimItems.keys, dirtyVinovimKeys)
            assertEquals(vinovimItems.keys, dirtyProviderKeys[TrackingProviderId.TRAKT])
        }
    }
}
