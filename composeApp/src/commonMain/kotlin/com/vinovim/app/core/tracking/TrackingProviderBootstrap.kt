package com.vinovim.app.core.tracking

import com.vinovim.app.features.simkl.SimklAuthRepository
import com.vinovim.app.features.simkl.SimklMutationRepository
import com.vinovim.app.features.simkl.SimklLibraryRepository
import com.vinovim.app.features.simkl.SimklProgressRepository
import com.vinovim.app.features.simkl.SimklRatings
import com.vinovim.app.features.simkl.SimklTrackingLibraryProvider
import com.vinovim.app.features.simkl.SimklTrackingProgressProvider
import com.vinovim.app.features.simkl.SimklWatchedSyncAdapter
import com.vinovim.app.features.simkl.SimklSyncRepository
import com.vinovim.app.features.tracking.TrackingProviderRegistry
import com.vinovim.app.features.mdblist.MdbListTracker
import com.vinovim.app.features.trakt.TraktAuthRepository
import com.vinovim.app.features.trakt.TraktScrobbleRepository
import com.vinovim.app.features.trakt.TraktTrackingLibraryProvider
import com.vinovim.app.features.trakt.TraktTrackingProgressProvider
import com.vinovim.app.features.watching.sync.TraktWatchedSyncAdapter

fun ensureTrackingProvidersRegistered() {
    MdbListTracker.register()
    TraktAuthRepository.descriptor
    TraktScrobbleRepository.ensureRegistered()
    SimklAuthRepository.descriptor
    SimklSyncRepository.state
    SimklLibraryRepository.uiState
    SimklProgressRepository.uiState
    SimklMutationRepository.ensureRegistered()
    TrackingProviderRegistry.registerLibraryProvider(TraktTrackingLibraryProvider)
    TrackingProviderRegistry.registerLibraryProvider(SimklTrackingLibraryProvider)
    TrackingProviderRegistry.registerWatchedProvider(TraktWatchedSyncAdapter)
    TrackingProviderRegistry.registerWatchedProvider(SimklWatchedSyncAdapter)
    TrackingProviderRegistry.registerProgressProvider(TraktTrackingProgressProvider)
    TrackingProviderRegistry.registerProgressProvider(SimklTrackingProgressProvider)
    TrackingProviderRegistry.registerRatingProvider(SimklRatings)
}
