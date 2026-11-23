package com.vinovim.app

import androidx.compose.runtime.Composable
import com.vinovim.app.features.downloads.DownloadItem
import com.vinovim.app.features.downloads.DownloadsScreen
import com.vinovim.app.navigation.DetailRoute
import com.vinovim.app.navigation.DownloadShowRoute
import com.vinovim.app.navigation.DownloadsRoute
import com.vinovim.app.navigation.VinovimNavigator

@Composable
internal fun DownloadsDestination(
    route: DownloadsRoute,
    navController: VinovimNavigator,
    useNativeNavigation: Boolean,
    onOpenDownload: (DownloadItem) -> Unit,
) {
    val onBack = rememberGuardedPopBackStack(navController, route)
    DownloadsScreen(
        onBack = onBack,
        onOpenDownload = onOpenDownload,
        onOpenDetails = navController::openDownloadDetails,
        onNavigateToShow = if (useNativeNavigation) {
            { showId, title -> navController.navigate(DownloadShowRoute(showId, title)) }
        } else {
            null
        },
    )
}

@Composable
internal fun DownloadShowDestination(
    route: DownloadShowRoute,
    navController: VinovimNavigator,
    onOpenDownload: (DownloadItem) -> Unit,
) {
    val onBack = rememberGuardedPopBackStack(navController, route)
    DownloadsScreen(
        onBack = onBack,
        onOpenDownload = onOpenDownload,
        onOpenDetails = navController::openDownloadDetails,
        initialShowId = route.showId,
        onBackFromShow = onBack,
    )
}

private fun VinovimNavigator.openDownloadDetails(item: DownloadItem) {
    navigate(
        DetailRoute(
            type = item.parentMetaType,
            id = item.parentMetaId,
            title = item.title,
        ),
    )
}
