package com.vinovim.app.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vinovim.app.isDesktop
import kotlinx.coroutines.launch

data class VinovimDropdownOption(
    val key: String,
    val label: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VinovimDropdownChip(
    title: String,
    label: String,
    selectedKey: String?,
    options: List<VinovimDropdownOption>,
    enabled: Boolean = true,
    onSelected: (VinovimDropdownOption) -> Unit,
    modifier: Modifier = Modifier,
    fillWidth: Boolean = false,
) {
    val tokens = MaterialTheme.vinovim
    var isSheetVisible by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier) {
        Row(
            modifier = (if (fillWidth) Modifier.fillMaxWidth() else Modifier)
                .clip(tokens.shapes.compactCard)
                .background(tokens.colors.surface)
                .then(
                    if (enabled) {
                        Modifier.clickable { isSheetVisible = true }
                    } else {
                        Modifier
                    },
                )
                .padding(horizontal = VinovimTokens.Space.s12, vertical = tokens.components.chipVerticalPadding),
            horizontalArrangement = Arrangement.spacedBy(VinovimTokens.Space.s6),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (enabled) tokens.colors.textPrimary else tokens.colors.textDisabled,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = if (fillWidth) Modifier.weight(1f) else Modifier,
            )
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(VinovimTokens.Icon.sm + VinovimTokens.Space.s2),
                tint = if (enabled) tokens.colors.textMuted else tokens.colors.borderDefault,
            )
        }

        if (isDesktop) {
            DropdownMenu(
                expanded = isSheetVisible,
                onDismissRequest = { isSheetVisible = false },
                modifier = Modifier.platformTouchScroll().widthIn(min = 180.dp, max = 360.dp),
                containerColor = tokens.colors.surfacePopover,
                shape = tokens.shapes.compactCard,
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.label,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        trailingIcon = {
                            if (option.key == selectedKey) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                    tint = tokens.colors.accent,
                                    modifier = Modifier.size(tokens.icons.md),
                                )
                            }
                        },
                        onClick = {
                            onSelected(option)
                            isSheetVisible = false
                        },
                    )
                }
            }
        }
    }

    if (isSheetVisible && !isDesktop) {
        VinovimDropdownOptionsSheet(
            title = title,
            options = options,
            selectedKey = selectedKey,
            sheetState = sheetState,
            onDismiss = {
                coroutineScope.launch {
                    dismissVinovimBottomSheet(
                        sheetState = sheetState,
                        onDismiss = { isSheetVisible = false },
                    )
                }
            },
            onSelected = { option ->
                onSelected(option)
                coroutineScope.launch {
                    dismissVinovimBottomSheet(
                        sheetState = sheetState,
                        onDismiss = { isSheetVisible = false },
                    )
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VinovimDropdownOptionsSheet(
    title: String,
    options: List<VinovimDropdownOption>,
    selectedKey: String?,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSelected: (VinovimDropdownOption) -> Unit,
) {
    val tokens = MaterialTheme.vinovim
    VinovimModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = vinovimSafeBottomPadding(tokens.spacing.screenHorizontal)),
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(horizontal = tokens.spacing.screenHorizontal, vertical = VinovimTokens.Space.s14),
                style = MaterialTheme.typography.titleLarge,
                color = tokens.colors.textPrimary,
            )
            VinovimBottomSheetDivider()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = tokens.breakpoints.largePhone),
            ) {
                items(options) { option ->
                    VinovimBottomSheetActionRow(
                        title = option.label,
                        onClick = { onSelected(option) },
                        selected = option.key == selectedKey,
                        trailingContent = {
                            if (option.key == selectedKey) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                    tint = tokens.colors.accent,
                                    modifier = Modifier.size(tokens.icons.md),
                                )
                            }
                        },
                    )
                }
            }
        }
    }
}
