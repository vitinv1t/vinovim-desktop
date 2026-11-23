package com.vinovim.app.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.vinovim.app.isWindows

private val SheetShape = RoundedCornerShape(VinovimTokens.Space.s28)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VinovimModalBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.vinovim.colors.surfaceSheet,
    contentColor: Color = MaterialTheme.vinovim.colors.textPrimary,
    showDragHandle: Boolean = true,
    fullHeight: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (usesNativeVinovimBottomSheet) {
        VinovimNativeModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            containerColor = containerColor,
            contentColor = contentColor,
            showDragHandle = showDragHandle,
            fullHeight = fullHeight,
            content = content,
        )
    } else {
        val tokens = MaterialTheme.vinovim
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            shape = RectangleShape,
            containerColor = Color.Transparent,
            contentColor = contentColor,
            scrimColor = tokens.colors.overlayScrim,
            dragHandle = null,
        ) {
            CompositionLocalProvider(LocalBottomInsetsConsumed provides true) {
                Column(
                    modifier = modifier
                        .padding(VinovimTokens.Space.s10)
                        .clip(SheetShape)
                        .background(containerColor)
                        .border(tokens.borders.thin, SurfaceEdge, SheetShape),
                ) {
                    if (showDragHandle) {
                        VinovimBottomSheetDragHandle(Modifier.align(Alignment.CenterHorizontally))
                    }
                    if (isWindows) {
                        // Inside the sheet's draggable, so content scrolls before
                        // the sheet can claim its drag. Keep the handle outside
                        // this scope so dragging it still moves the sheet.
                        Column(modifier = Modifier.platformTouchScroll(), content = content)
                    } else {
                        content()
                    }
                }
            }
        }
    }
}

@Composable
fun VinovimBottomSheetDivider(
    modifier: Modifier = Modifier,
) {
    val tokens = MaterialTheme.vinovim
    HorizontalDivider(
        modifier = modifier.padding(horizontal = tokens.spacing.screenHorizontal),
        thickness = tokens.borders.hairline,
        color = tokens.colors.borderDefault,
    )
}

@Composable
fun VinovimBottomSheetActionRow(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    selected: Boolean = false,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
) {
    val tokens = MaterialTheme.vinovim
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = VinovimTokens.Space.s8)
            .heightIn(min = VinovimTokens.Space.s56)
            .clip(tokens.shapes.compactCard)
            .background(if (selected) tokens.colors.accent.copy(alpha = tokens.opacity.hover) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(VinovimTokens.Space.s8),
        horizontalArrangement = Arrangement.spacedBy(VinovimTokens.Space.s14),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(VinovimTokens.Space.s36)
                    .clip(tokens.shapes.compactCard)
                    .background(tokens.colors.accent.copy(alpha = tokens.opacity.selected)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tokens.colors.accent,
                    modifier = Modifier.size(VinovimTokens.Icon.md),
                )
            }
        }
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (selected) FontWeight.SemiBold else null,
            color = tokens.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        trailingContent?.invoke(this)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
suspend fun dismissVinovimBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
) {
    if (usesNativeVinovimBottomSheet) {
        dismissNativeVinovimBottomSheet()
    } else if (sheetState.isVisible) {
        sheetState.hide()
    }
    onDismiss()
}

@Composable
private fun VinovimBottomSheetDragHandle(modifier: Modifier = Modifier) {
    val tokens = MaterialTheme.vinovim
    Box(
        modifier = modifier
            .padding(top = VinovimTokens.Space.s10, bottom = VinovimTokens.Space.s4)
            .size(width = VinovimTokens.Space.s36, height = VinovimTokens.Space.s4)
            .clip(tokens.shapes.chip)
            .background(tokens.colors.borderStrong),
    )
}
