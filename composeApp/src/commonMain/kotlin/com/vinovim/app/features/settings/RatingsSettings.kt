package com.vinovim.app.features.settings

import androidx.compose.runtime.Composable
import com.vinovim.app.core.ui.Chip
import com.vinovim.app.features.details.EpisodeRatingsVisibility
import com.vinovim.app.features.details.MetaScreenSettingsRepository
import com.vinovim.app.features.details.MetaScreenSettingsUiState
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.layout_episode_ratings
import com.vinovim.app.resources.layout_episode_ratings_sub
import com.vinovim.app.resources.layout_overall_ratings
import com.vinovim.app.resources.layout_overall_ratings_sub_off
import com.vinovim.app.resources.layout_overall_ratings_sub_on
import com.vinovim.app.resources.layout_ratings_hide
import com.vinovim.app.resources.layout_ratings_hide_unwatched
import com.vinovim.app.resources.layout_ratings_show
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RatingsSettings(
    isTablet: Boolean,
    uiState: MetaScreenSettingsUiState,
) {
    SettingsSwitchRow(
        title = stringResource(Res.string.layout_overall_ratings),
        description = stringResource(
            if (uiState.showOverallRatings) Res.string.layout_overall_ratings_sub_on
            else Res.string.layout_overall_ratings_sub_off,
        ),
        checked = uiState.showOverallRatings,
        isTablet = isTablet,
        onCheckedChange = MetaScreenSettingsRepository::setShowOverallRatings,
    )
    SettingsGroupDivider(isTablet = isTablet)
    SettingsChipRow(
        title = stringResource(Res.string.layout_episode_ratings),
        description = stringResource(Res.string.layout_episode_ratings_sub),
        isTablet = isTablet,
    ) {
        EpisodeRatingsVisibility.entries.forEach { visibility ->
            Chip(
                label = stringResource(
                    when (visibility) {
                        EpisodeRatingsVisibility.SHOW_ALL -> Res.string.layout_ratings_show
                        EpisodeRatingsVisibility.HIDE_EPISODES -> Res.string.layout_ratings_hide
                        EpisodeRatingsVisibility.HIDE_UNWATCHED_EPISODES -> Res.string.layout_ratings_hide_unwatched
                    },
                ),
                selected = uiState.episodeRatingsVisibility == visibility,
                onClick = { MetaScreenSettingsRepository.setEpisodeRatingsVisibility(visibility) },
            )
        }
    }
}
