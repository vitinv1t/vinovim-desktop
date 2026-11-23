package com.vinovim.app.features.profiles

import com.vinovim.app.core.ui.VinovimToastController
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.profile_already_active
import org.jetbrains.compose.resources.getString

internal fun routeProfileSelection(
    profile: VinovimProfile,
    isEditMode: Boolean,
    activeProfileIndex: Int? = null,
    onEditProfile: (VinovimProfile) -> Unit,
    onActiveProfileSelected: (VinovimProfile) -> Unit,
    onPinRequired: (VinovimProfile) -> Unit,
    onProfileSelected: (VinovimProfile) -> Unit,
) {
    when {
        isEditMode -> onEditProfile(profile)
        profile.profileIndex == activeProfileIndex -> onActiveProfileSelected(profile)
        profile.pinEnabled -> onPinRequired(profile)
        else -> onProfileSelected(profile)
    }
}

internal suspend fun showAlreadyActiveProfileToast(profile: VinovimProfile) {
    VinovimToastController.show(getString(Res.string.profile_already_active, profile.name))
}
