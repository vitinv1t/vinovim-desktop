package com.vinovim.app.features.addons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.vinovim.app.core.ui.DialogButton
import com.vinovim.app.core.ui.DialogButtonStyle
import com.vinovim.app.core.ui.DialogButtons
import com.vinovim.app.core.ui.DialogSurface
import com.vinovim.app.core.ui.VinovimTokens
import com.vinovim.app.core.ui.vinovim
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.action_cancel
import com.vinovim.app.resources.addons_install_button
import com.vinovim.app.resources.addons_tracking_consent_body
import com.vinovim.app.resources.addons_tracking_consent_checkbox
import com.vinovim.app.resources.addons_tracking_consent_title
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TrackingAddonConsentDialog(
    manifest: AddonManifest,
    onInstall: () -> Unit,
    onDismiss: () -> Unit,
) {
    var accepted by rememberSaveable(manifest.transportUrl) { mutableStateOf(false) }
    val tokens = MaterialTheme.vinovim
    DialogSurface(
        onDismissRequest = onDismiss,
        title = stringResource(Res.string.addons_tracking_consent_title),
    ) {
        Text(
            text = stringResource(Res.string.addons_tracking_consent_body, manifest.name),
            style = MaterialTheme.typography.bodyMedium,
            color = tokens.colors.textSecondary,
            modifier = Modifier
                .heightIn(max = 320.dp)
                .verticalScroll(rememberScrollState()),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = accepted,
                    role = Role.Checkbox,
                    onValueChange = { accepted = it },
                ),
            horizontalArrangement = Arrangement.spacedBy(VinovimTokens.Space.s8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = accepted,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(
                    checkedColor = tokens.colors.accent,
                    checkmarkColor = tokens.colors.onAccent,
                ),
            )
            Text(
                text = stringResource(Res.string.addons_tracking_consent_checkbox),
                style = MaterialTheme.typography.bodyMedium,
                color = tokens.colors.textPrimary,
                modifier = Modifier.weight(1f),
            )
        }
        DialogButtons {
            DialogButton(
                text = stringResource(Res.string.action_cancel),
                onClick = onDismiss,
            )
            DialogButton(
                text = stringResource(Res.string.addons_install_button),
                onClick = onInstall,
                style = DialogButtonStyle.Primary,
                enabled = accepted,
            )
        }
    }
}
