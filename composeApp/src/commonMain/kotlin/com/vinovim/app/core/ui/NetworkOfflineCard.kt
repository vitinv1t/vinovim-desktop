package com.vinovim.app.core.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vinovim.app.core.network.NetworkCondition
import com.vinovim.app.core.network.messageForEmptyState
import com.vinovim.app.core.network.titleForEmptyState
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.action_retry
import org.jetbrains.compose.resources.stringResource

@Composable
fun VinovimNetworkOfflineCard(
    condition: NetworkCondition,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
) {
    val tokens = MaterialTheme.vinovim
    VinovimSurfaceCard(modifier = modifier) {
        Text(
            text = condition.titleForEmptyState(),
            style = MaterialTheme.typography.titleLarge,
            color = tokens.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(tokens.spacing.controlGap))
        Text(
            text = condition.messageForEmptyState(),
            style = MaterialTheme.typography.bodyLarge,
            color = tokens.colors.textMuted,
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(tokens.spacing.screenHorizontal))
            VinovimPrimaryButton(
                text = stringResource(Res.string.action_retry),
                onClick = onRetry,
            )
        }
    }
}
