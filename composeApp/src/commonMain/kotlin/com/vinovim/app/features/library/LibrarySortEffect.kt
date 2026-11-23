package com.vinovim.app.features.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vinovim.app.core.ui.VinovimToastController
import com.vinovim.app.core.ui.ScreenActivityEffect
import com.vinovim.app.features.profiles.ProfileRepository
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.library_error_sort_failed
import org.jetbrains.compose.resources.getString

@Composable
internal fun rememberLibraryProviderOrders(
    sourceMode: LibrarySourceMode,
    listKeys: List<String>,
    sortOption: LibrarySortOption,
): LibraryProviderOrders {
    val profileState by ProfileRepository.state.collectAsStateWithLifecycle()
    val profileId = profileState.activeProfile?.profileIndex ?: ProfileRepository.activeProfileId
    var orders by remember(profileId, sourceMode, listKeys, sortOption) { mutableStateOf(LibraryProviderOrders()) }
    ScreenActivityEffect(profileId, sourceMode, listKeys, sortOption) { active ->
        if (!active) return@ScreenActivityEffect
        observeLibraryProviderOrders(sourceMode.librarySorter(), listKeys, sortOption).collect { orders = it }
    }
    ScreenActivityEffect(orders.failed) { active ->
        if (active && orders.failed) {
            val message = getString(Res.string.library_error_sort_failed)
            LibraryDisplaySettingsRepository.setSortOption(LibrarySortOption.DEFAULT)
            VinovimToastController.show(message)
        }
    }
    return orders
}
