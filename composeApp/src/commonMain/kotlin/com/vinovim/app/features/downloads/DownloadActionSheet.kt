package com.vinovim.app.features.downloads

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.vinovim.app.core.ui.VinovimBottomSheetActionRow
import com.vinovim.app.core.ui.VinovimBottomSheetDivider
import com.vinovim.app.core.ui.VinovimModalBottomSheet
import com.vinovim.app.core.ui.VinovimTokens
import com.vinovim.app.core.ui.dismissVinovimBottomSheet
import com.vinovim.app.core.ui.vinovim
import com.vinovim.app.core.ui.vinovimSafeBottomPadding
import com.vinovim.app.core.ui.vinovimTypeScale
import kotlinx.coroutines.launch
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.action_delete
import com.vinovim.app.resources.action_play
import com.vinovim.app.resources.action_resume
import com.vinovim.app.resources.action_retry
import com.vinovim.app.resources.compose_action_pause
import com.vinovim.app.resources.cw_action_go_to_details
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DownloadActionSheet(
    item: DownloadItem?,
    watchStatus: DownloadWatchStatus,
    onDismiss: () -> Unit,
    onPlay: () -> Unit,
    onToggleTransfer: () -> Unit,
    onOpenDetails: () -> Unit,
    onDelete: () -> Unit,
) {
    if (item == null) return
    val tokens = MaterialTheme.vinovim
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    fun dismissAfter(action: () -> Unit) {
        action()
        coroutineScope.launch {
            dismissVinovimBottomSheet(sheetState = sheetState, onDismiss = onDismiss)
        }
    }

    VinovimModalBottomSheet(
        onDismissRequest = {
            coroutineScope.launch {
                dismissVinovimBottomSheet(sheetState = sheetState, onDismiss = onDismiss)
            }
        },
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = vinovimSafeBottomPadding(tokens.spacing.screenHorizontal)),
        ) {
            DownloadSheetHeader(item = item, watchStatus = watchStatus)
            VinovimBottomSheetDivider()
            when (item.status) {
                DownloadStatus.Completed -> VinovimBottomSheetActionRow(
                    icon = Icons.Rounded.PlayArrow,
                    title = stringResource(
                        if (watchStatus.progress > 0f) Res.string.action_resume else Res.string.action_play,
                    ),
                    onClick = { dismissAfter(onPlay) },
                )
                DownloadStatus.Downloading -> VinovimBottomSheetActionRow(
                    icon = Icons.Rounded.Pause,
                    title = stringResource(Res.string.compose_action_pause),
                    onClick = { dismissAfter(onToggleTransfer) },
                )
                DownloadStatus.Paused -> VinovimBottomSheetActionRow(
                    icon = Icons.Rounded.Download,
                    title = stringResource(Res.string.action_resume),
                    onClick = { dismissAfter(onToggleTransfer) },
                )
                DownloadStatus.Failed -> VinovimBottomSheetActionRow(
                    icon = Icons.Rounded.Refresh,
                    title = stringResource(Res.string.action_retry),
                    onClick = { dismissAfter(onToggleTransfer) },
                )
            }
            VinovimBottomSheetActionRow(
                icon = Icons.Rounded.Info,
                title = stringResource(Res.string.cw_action_go_to_details),
                onClick = { dismissAfter(onOpenDetails) },
            )
            VinovimBottomSheetActionRow(
                icon = Icons.Rounded.DeleteOutline,
                title = stringResource(Res.string.action_delete),
                onClick = { dismissAfter(onDelete) },
            )
        }
    }
}

@Composable
private fun DownloadSheetHeader(
    item: DownloadItem,
    watchStatus: DownloadWatchStatus,
) {
    val tokens = MaterialTheme.vinovim
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = tokens.spacing.screenHorizontal, vertical = VinovimTokens.Space.s14),
        horizontalArrangement = Arrangement.spacedBy(VinovimTokens.Space.s14),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DownloadPoster(item = item)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(VinovimTokens.Space.s4),
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge,
                color = tokens.colors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            item.episodeLabel()?.let { label ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = tokens.colors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = listOfNotNull(
                    formatBytes(item.sizeOnDiskBytes).takeIf { item.sizeOnDiskBytes > 0L },
                    item.providerName.takeIf { it.isNotBlank() },
                ).joinToString(DownloadTextSeparator),
                style = MaterialTheme.vinovimTypeScale.bodySm,
                color = tokens.colors.textMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
