package com.vinovim.app.core.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import com.vinovim.app.resources.Res
import com.vinovim.app.resources.action_back
import com.vinovim.app.resources.action_ok
import org.jetbrains.compose.resources.stringResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.vinovim.app.isDesktop
import com.vinovim.app.navigation.LocalNativeNavigationBarHidden
import com.vinovim.app.navigation.LocalUseNativeNavigation

@Composable
fun VinovimScreen(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = MaterialTheme.vinovim.spacing.screenHorizontal,
    topPadding: Dp? = null,
    listState: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit,
) {
    val tokens = MaterialTheme.vinovim
    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val scrollViewportModifier = if (isDesktop) {
        com.vinovim.app.controller.RegisterControllerVerticalScroll(lazyListState = listState)
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(tokens.colors.background),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .then(scrollViewportModifier),
            contentPadding = PaddingValues(
                start = horizontalPadding,
                top = topPadding ?: tokens.spacing.screenTop + statusBarTop + vinovimPlatformExtraTopPadding,
                end = horizontalPadding,
                bottom = vinovimSafeBottomPadding(tokens.spacing.screenBottom),
            ).withVerticalBar(),
            verticalArrangement = Arrangement.spacedBy(tokens.spacing.listGap),
            content = content,
        )
        VinovimDesktopVerticalScrollbar(
            state = listState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(vertical = 8.dp, horizontal = 4.dp),
        )
    }
}

internal fun Modifier.vinovimConsumePointerEvents(): Modifier =
    pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                awaitPointerEvent(PointerEventPass.Final).changes.forEach { change ->
                    change.consume()
                }
            }
        }
    }

@Composable
fun VinovimSurfaceCard(
    modifier: Modifier = Modifier,
    tonalElevation: Int = 0,
    content: @Composable ColumnScope.() -> Unit,
) {
    val tokens = MaterialTheme.vinovim
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = tokens.colors.surface,
        shape = tokens.shapes.card,
        tonalElevation = tonalElevation.dp,
        shadowElevation = tokens.elevation.flat,
    ) {
        Column(
            modifier = Modifier.padding(tokens.spacing.cardPadding),
            content = content,
        )
    }
}

@Composable
fun VinovimScreenHeader(
    title: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.vinovim.colors.background,
    includeStatusBarPadding: Boolean = true,
    topPadding: Dp? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val tokens = MaterialTheme.vinovim
    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val nativeDetailNavigation = LocalUseNativeNavigation.current &&
        !LocalNativeNavigationBarHidden.current &&
        onBack != null
    if (nativeDetailNavigation) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = VinovimTokens.Space.s4),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = actions,
        )
        return
    }
    val resolvedTopPadding = topPadding ?: if (includeStatusBarPadding) statusBarTop else VinovimTokens.Space.none
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .matchParentSize()
                .background(backgroundColor)
                .vinovimConsumePointerEvents(),
        ) {}
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = resolvedTopPadding, bottom = VinovimTokens.Space.s4),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(tokens.spacing.controlGap),
            ) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(Res.string.action_back),
                            tint = tokens.colors.textPrimary,
                        )
                    }
                }
                AnimatedContent(
                    targetState = title,
                    modifier = Modifier.weight(1f),
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "screen_header_title",
                ) { currentTitle ->
                    Text(
                        text = currentTitle,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.displayLarge,
                        color = tokens.colors.textPrimary,
                        textAlign = TextAlign.Start,
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(VinovimTokens.Space.s2),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FullscreenActionButton(
                    buttonSize = 48.dp,
                    iconSize = 24.dp,
                    contentColor = tokens.colors.textPrimary,
                )
                actions()
            }
        }
    }
}

@Composable
fun VinovimSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.vinovim.colors.textMuted,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun VinovimActionLabel(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Text(
        text = text,
        modifier = modifier.then(
            if (onClick != null) {
                Modifier.clickable(onClick = onClick)
            } else {
                Modifier
            }
        ),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.vinovim.colors.accent,
    )
}

@Composable
fun VinovimIconActionButton(
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.vinovim.colors.textPrimary,
    onClick: () -> Unit = {},
) {
    val tokens = MaterialTheme.vinovim
    IconButton(
        modifier = modifier
            .background(
                color = tokens.colors.background.copy(alpha = 0.001f),
                shape = tokens.shapes.avatar,
            ),
        onClick = onClick,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}

@Composable
fun VinovimBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.vinovim.shapes.avatar,
    containerColor: Color = MaterialTheme.vinovim.colors.surface,
    showContainerOnDesktop: Boolean = false,
    contentColor: Color = MaterialTheme.vinovim.colors.textPrimary,
    buttonSize: Dp = VinovimTokens.Space.s40,
    iconSize: Dp = VinovimTokens.Icon.md,
    contentDescription: String = stringResource(Res.string.action_back),
) {
    if (LocalUseNativeNavigation.current && !LocalNativeNavigationBarHidden.current) return
    val effectiveContainerColor = if (isDesktop && !showContainerOnDesktop) Color.Transparent else containerColor

    Box(
        modifier = modifier
            .size(buttonSize)
            .clip(shape)
            .background(effectiveContainerColor)
            .vinovimFocusBorder(shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier.size(iconSize),
        )
    }
}

@Composable
fun VinovimPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    val tokens = MaterialTheme.vinovim
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(VinovimTokens.Space.s48 + VinovimTokens.Space.s4)
            .vinovimFocusBorder(tokens.shapes.button),
        enabled = enabled,
        shape = tokens.shapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = tokens.colors.accent,
            contentColor = tokens.colors.onAccent,
            disabledContainerColor = tokens.colors.accent.copy(alpha = tokens.opacity.disabled),
            disabledContentColor = tokens.colors.onAccent.copy(alpha = tokens.opacity.disabled),
        ),
    ) {
        AnimatedContent(
            targetState = text,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "buttonText",
        ) { animatedText ->
            Text(
                text = animatedText,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun VinovimInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.vinovim.shapes.compactCard,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    trailingContent: (@Composable (() -> Unit))? = null,
) {
    val tokens = MaterialTheme.vinovim
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = shape,
        placeholder = {
            Text(
                text = placeholder,
                color = tokens.colors.textMuted,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        textStyle = textStyle.copy(color = tokens.colors.textPrimary),
        trailingIcon = trailingContent,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = tokens.colors.borderFocus,
            unfocusedBorderColor = tokens.colors.borderDefault,
            focusedContainerColor = tokens.colors.surfaceCard,
            unfocusedContainerColor = tokens.colors.surfaceCard,
            cursorColor = tokens.colors.accent,
        ),
    )
}

@Composable
fun VinovimInfoBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    val tokens = MaterialTheme.vinovim
    Box(
        modifier = modifier
            .background(
                color = tokens.colors.surfaceCard,
                shape = tokens.shapes.chip,
            )
            .padding(horizontal = VinovimTokens.Space.s10, vertical = VinovimTokens.Space.s6),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = tokens.colors.textMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun VinovimInlineMetadata(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.vinovim.colors.textMuted,
        )
        Spacer(modifier = Modifier.width(VinovimTokens.Space.s6))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.vinovim.colors.textPrimary,
        )
    }
}

@Composable
fun VinovimStatusModal(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isBusy: Boolean = false,
    destructive: Boolean = false,
    confirmText: String = stringResource(Res.string.action_ok),
    dismissText: String? = null,
    onConfirm: () -> Unit,
    onDismiss: (() -> Unit)? = null,
) {
    if (!isVisible) return

    DialogSurface(
        onDismissRequest = {
            if (!isBusy) {
                onDismiss?.invoke() ?: onConfirm()
            }
        },
        modifier = modifier,
        title = title,
        message = message,
    ) {
        DialogButtons {
            if (!isBusy && dismissText != null && onDismiss != null) {
                DialogButton(text = dismissText, onClick = onDismiss)
            }
            DialogButton(
                text = confirmText,
                onClick = onConfirm,
                style = if (destructive) DialogButtonStyle.Destructive else DialogButtonStyle.Primary,
                loading = isBusy,
            )
        }
    }
}

@Composable
fun VinovimToastHost(
    modifier: Modifier = Modifier,
) {
    val tokens = MaterialTheme.vinovim
    val toast by VinovimToastController.currentToast.collectAsState()
    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val visibilityState = remember { MutableTransitionState(false) }
    var renderedToast by remember { mutableStateOf<VinovimToastMessage?>(null) }

    LaunchedEffect(toast?.id) {
        val currentToast = toast
        if (currentToast != null) {
            renderedToast = currentToast
            visibilityState.targetState = true
            delay(currentToast.durationMillis)
            VinovimToastController.dismiss(currentToast.id)
        } else {
            visibilityState.targetState = false
        }
    }

    LaunchedEffect(
        visibilityState.currentState,
        visibilityState.targetState,
        visibilityState.isIdle,
    ) {
        if (visibilityState.isIdle && !visibilityState.currentState && !visibilityState.targetState) {
            renderedToast = null
        }
    }

    AnimatedVisibility(
        visibleState = visibilityState,
        modifier = modifier,
        enter = fadeIn() + slideInVertically { -it },
        exit = fadeOut() + slideOutVertically { -it },
    ) {
        val currentToast = renderedToast ?: return@AnimatedVisibility
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = statusBarTop + tokens.spacing.listGap)
                .padding(horizontal = tokens.spacing.screenHorizontal),
            contentAlignment = Alignment.TopCenter,
        ) {
            Surface(
                shape = RoundedCornerShape(VinovimTokens.Radius.xl),
                color = tokens.colors.surfacePopover,
                tonalElevation = tokens.elevation.raised,
                shadowElevation = tokens.elevation.overlay,
            ) {
                Text(
                    text = currentToast.message,
                    modifier = Modifier.padding(horizontal = VinovimTokens.Space.s16, vertical = VinovimTokens.Space.s12),
                    style = MaterialTheme.typography.bodyMedium,
                    color = tokens.colors.textPrimary,
                )
            }
        }
    }
}

data class VinovimToastMessage(
    val id: Long,
    val message: String,
    val durationMillis: Long,
)

object VinovimToastController {
    private val _currentToast = MutableStateFlow<VinovimToastMessage?>(null)
    val currentToast = _currentToast.asStateFlow()
    private var nextToastId = 0L

    fun show(
        message: String,
        durationMillis: Long = 2500L,
    ) {
        nextToastId += 1L
        _currentToast.value = VinovimToastMessage(
            id = nextToastId,
            message = message,
            durationMillis = durationMillis,
        )
    }

    fun dismiss(id: Long? = null) {
        val activeToast = _currentToast.value ?: return
        if (id == null || activeToast.id == id) {
            _currentToast.value = null
        }
    }
}
