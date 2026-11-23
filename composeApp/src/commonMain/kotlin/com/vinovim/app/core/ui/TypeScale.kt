package com.vinovim.app.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

data class VinovimTypeScale(
    val labelXs: TextStyle,
    val labelSm: TextStyle,
    val bodySm: TextStyle,
    val bodyMd: TextStyle,
    val bodyLg: TextStyle,
    val titleSm: TextStyle,
    val titleMd: TextStyle,
    val titleLg: TextStyle,
    val displaySm: TextStyle,
    val displayMd: TextStyle,
)

internal val LocalVinovimTypeScale = staticCompositionLocalOf {
    VinovimTypeScale(
        labelXs = TextStyle(fontSize = VinovimTokens.Type.labelXs, lineHeight = VinovimTokens.LineHeight.labelXs, fontWeight = FontWeight.Medium),
        labelSm = TextStyle(fontSize = VinovimTokens.Type.labelSm, lineHeight = VinovimTokens.LineHeight.labelSm, fontWeight = FontWeight.Medium),
        bodySm = TextStyle(fontSize = VinovimTokens.Type.bodySm, lineHeight = VinovimTokens.LineHeight.bodySm, fontWeight = FontWeight.Normal),
        bodyMd = TextStyle(fontSize = VinovimTokens.Type.bodyMd, lineHeight = VinovimTokens.LineHeight.bodyMd, fontWeight = FontWeight.Normal),
        bodyLg = TextStyle(fontSize = VinovimTokens.Type.bodyLg, lineHeight = VinovimTokens.LineHeight.bodyLg, fontWeight = FontWeight.Medium),
        titleSm = TextStyle(fontSize = VinovimTokens.Type.titleSm, lineHeight = VinovimTokens.LineHeight.titleSm, fontWeight = FontWeight.Bold),
        titleMd = TextStyle(fontSize = VinovimTokens.Type.titleMd, lineHeight = VinovimTokens.LineHeight.titleMd, fontWeight = FontWeight.Bold),
        titleLg = TextStyle(fontSize = VinovimTokens.Type.titleLg, lineHeight = VinovimTokens.LineHeight.titleLg, fontWeight = FontWeight.Bold),
        displaySm = TextStyle(fontSize = VinovimTokens.Type.displaySm, lineHeight = VinovimTokens.LineHeight.displaySm, fontWeight = FontWeight.ExtraBold),
        displayMd = TextStyle(fontSize = VinovimTokens.Type.displayMd, lineHeight = VinovimTokens.LineHeight.displayMd, fontWeight = FontWeight.ExtraBold),
    )
}

val MaterialTheme.vinovimTypeScale: VinovimTypeScale
    @Composable
    get() = LocalVinovimTypeScale.current
