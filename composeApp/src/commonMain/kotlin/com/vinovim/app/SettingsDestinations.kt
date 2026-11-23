package com.vinovim.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.vinovim.app.features.collection.CollectionEditorPage
import com.vinovim.app.features.collection.CollectionEditorScreen
import com.vinovim.app.features.collection.CollectionManagementScreen
import com.vinovim.app.features.collection.CollectionRepository
import com.vinovim.app.features.collection.FolderDetailRepository
import com.vinovim.app.features.collection.FolderDetailScreen
import com.vinovim.app.features.home.HomeCatalogSection
import com.vinovim.app.features.settings.SettingsScreen
import com.vinovim.app.navigation.AppRoute
import com.vinovim.app.navigation.CollectionEditorPageRoute
import com.vinovim.app.navigation.CollectionEditorRoute
import com.vinovim.app.navigation.CollectionsRoute
import com.vinovim.app.navigation.FolderDetailRoute
import com.vinovim.app.navigation.VinovimNavigator
import com.vinovim.app.navigation.PartyRoute
import com.vinovim.app.navigation.SettingsPageRoute
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.party_title
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingsDestination(
    route: AppRoute,
    navController: VinovimNavigator,
    content: @Composable (onBack: () -> Unit) -> Unit,
) {
    val onBack = rememberGuardedPopBackStack(navController, route)
    content(onBack)
}

@Composable
internal fun SettingsRootDestination(
    route: SettingsPageRoute,
    navController: VinovimNavigator,
    useNativeNavigation: Boolean,
    collectionsTitle: String,
    onCheckForUpdates: (() -> Unit)?,
    onTestUpdateBanner: (() -> Unit)?,
) {
    val onBack = rememberGuardedPopBackStack(navController, route)
    val watchPartyTitle = stringResource(Res.string.party_title)
    SettingsScreen(
        modifier = Modifier.fillMaxSize(),
        initialPageName = route.pageName,
        requestedPageName = route.pageName,
        onRequestedPageConsumed = {},
        rootActionsEnabled = false,
        onCollectionsClick = {
            navController.navigate(CollectionsRoute(collectionsTitle))
        },
        onWatchPartyClick = {
            navController.navigate(PartyRoute(title = watchPartyTitle))
        },
        onCheckForUpdatesClick = onCheckForUpdates,
        onTestUpdateBannerClick = onTestUpdateBanner,
    )
}

@Composable
internal fun CollectionsDestination(
    route: CollectionsRoute,
    navController: VinovimNavigator,
    newCollectionTitle: String,
) {
    val onBack = rememberGuardedPopBackStack(navController, route)
    CollectionManagementScreen(
        onBack = onBack,
        onNavigateToEditor = { collectionId ->
            val editorTitle = collectionId
                ?.let { id ->
                    CollectionRepository.collections.value.firstOrNull { it.id == id }?.title
                }
                .orEmpty()
            navController.navigate(
                CollectionEditorRoute(
                    collectionId = collectionId,
                    title = editorTitle.ifBlank { newCollectionTitle },
                ),
            )
        },
    )
}

@Composable
internal fun CollectionEditorDestination(
    route: CollectionEditorRoute,
    navController: VinovimNavigator,
    useNativeNavigation: Boolean,
) {
    val onBack = rememberGuardedPopBackStack(navController, route)
    CollectionEditorScreen(
        collectionId = route.collectionId,
        onBack = onBack,
        initialPage = if (useNativeNavigation) CollectionEditorPage.Root else null,
        onNavigateToPage = if (useNativeNavigation) {
            { page, title ->
                navController.navigate(
                    CollectionEditorPageRoute(
                        collectionId = route.collectionId,
                        pageName = page.name,
                        title = title,
                    ),
                )
            }
        } else {
            null
        },
    )
}

@Composable
internal fun CollectionEditorPageDestination(
    route: CollectionEditorPageRoute,
    navController: VinovimNavigator,
) {
    val page = remember(route.pageName) {
        runCatching { CollectionEditorPage.valueOf(route.pageName) }.getOrNull()
    }
    val onBack = rememberGuardedPopBackStack(navController, route)
    if (page == null || page == CollectionEditorPage.Root) {
        LaunchedEffect(route) { onBack() }
        return
    }
    CollectionEditorScreen(
        collectionId = route.collectionId,
        initialPage = page,
        initializeRepository = false,
        onBack = onBack,
        onNavigateToPage = { nextPage, title ->
            navController.navigate(
                CollectionEditorPageRoute(
                    collectionId = route.collectionId,
                    pageName = nextPage.name,
                    title = title,
                ),
            )
        },
    )
}

@Composable
internal fun FolderDestination(
    route: FolderDetailRoute,
    navController: VinovimNavigator,
    onCatalogClick: (HomeCatalogSection) -> Unit,
) {
    val onBack = rememberGuardedPopBackStack(navController, route)
    LaunchedEffect(route.collectionId, route.folderId) {
        FolderDetailRepository.initialize(route.collectionId, route.folderId)
    }
    FolderDetailScreen(
        onBack = onBack,
        onCatalogClick = onCatalogClick,
        onPosterClick = rememberOpenMeta(navController),
    )
}
