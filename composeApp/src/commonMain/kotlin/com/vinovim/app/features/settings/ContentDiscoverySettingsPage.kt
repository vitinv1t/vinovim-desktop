package com.vinovim.app.features.settings

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vinovim.app.core.build.AppFeaturePolicy
import com.vinovim.app.features.search.SearchHistoryRepository
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.compose_settings_page_addons
import com.vinovim.app.resources.compose_settings_page_media_servers
import com.vinovim.app.resources.compose_settings_page_plugins
import com.vinovim.app.resources.settings_content_discovery_addons_description
import com.vinovim.app.resources.settings_content_discovery_addons_description_appstore
import com.vinovim.app.resources.settings_content_discovery_media_servers_description
import com.vinovim.app.resources.settings_content_discovery_plugins_description
import com.vinovim.app.resources.settings_content_discovery_section_sources
import com.vinovim.app.resources.settings_content_discovery_section_search
import com.vinovim.app.resources.settings_content_discovery_recent_searches
import com.vinovim.app.resources.settings_content_discovery_recent_searches_description
import org.jetbrains.compose.resources.stringResource

internal fun LazyListScope.contentDiscoveryContent(
    isTablet: Boolean,
    showPluginsEntry: Boolean,
    onAddonsClick: () -> Unit,
    onPluginsClick: () -> Unit,
    onMediaServersClick: () -> Unit,
) {
    item {
        val recentSearchesEnabled by remember {
            SearchHistoryRepository.ensureLoaded()
            SearchHistoryRepository.enabled
        }.collectAsStateWithLifecycle()

        SettingsSection(
            title = stringResource(Res.string.settings_content_discovery_section_search),
            isTablet = isTablet,
        ) {
            SettingsGroup(isTablet = isTablet) {
                SettingsSwitchRow(
                    title = stringResource(Res.string.settings_content_discovery_recent_searches),
                    description = stringResource(Res.string.settings_content_discovery_recent_searches_description),
                    checked = recentSearchesEnabled,
                    isTablet = isTablet,
                    onCheckedChange = SearchHistoryRepository::setEnabled,
                )
            }
        }
    }

    item {
        SettingsSection(
            title = stringResource(Res.string.settings_content_discovery_section_sources),
            isTablet = isTablet,
        ) {
            SettingsGroup(isTablet = isTablet) {
                SettingsNavigationRow(
                    title = stringResource(Res.string.compose_settings_page_addons),
                    description = stringResource(
                        if (AppFeaturePolicy.personalMediaAddonCopyEnabled) {
                            Res.string.settings_content_discovery_addons_description_appstore
                        } else {
                            Res.string.settings_content_discovery_addons_description
                        },
                    ),
                    isTablet = isTablet,
                    onClick = onAddonsClick,
                )
                SettingsNavigationRow(
                    title = stringResource(Res.string.compose_settings_page_media_servers),
                    description = stringResource(Res.string.settings_content_discovery_media_servers_description),
                    isTablet = isTablet,
                    onClick = onMediaServersClick,
                )
                if (showPluginsEntry) {
                    SettingsNavigationRow(
                        title = stringResource(Res.string.compose_settings_page_plugins),
                        description = stringResource(Res.string.settings_content_discovery_plugins_description),
                        isTablet = isTablet,
                        onClick = onPluginsClick,
                    )
                }
            }
        }
    }
}
