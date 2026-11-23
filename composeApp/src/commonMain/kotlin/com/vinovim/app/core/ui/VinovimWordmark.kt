package com.vinovim.app.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Gradiente da marca vinovim: violeta → rosa → coral
private val VinovimGradient = listOf(
    Color(0xFF8B5CFF),
    Color(0xFFFF4D94),
    Color(0xFFFF6A6A),
)

/**
 * Logo "V" do vinovim com gradiente da marca.
 * Usa Canvas para desenhar as duas hastes com gradiente linear.
 */
@Composable
fun VinovimLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    Canvas(modifier = modifier.width(size).height(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeW = w * 0.18f

        val gradientLeft = Brush.linearGradient(
            colors = listOf(Color(0xFF8B5CFF), Color(0xFFFF4D94)),
            start = Offset(w * 0.3f, 0f),
            end = Offset(w * 0.5f, h),
        )
        val gradientRight = Brush.linearGradient(
            colors = listOf(Color(0xFFFF4D94), Color(0xFFFF6A6A)),
            start = Offset(w * 0.5f, h),
            end = Offset(w * 0.7f, 0f),
        )

        // haste esquerda: top-left → center-bottom
        drawLine(
            brush = gradientLeft,
            start = Offset(w * 0.14f, h * 0.08f),
            end = Offset(w * 0.5f, h * 0.92f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round,
        )
        // haste direita: top-right → center-bottom
        drawLine(
            brush = gradientRight,
            start = Offset(w * 0.86f, h * 0.08f),
            end = Offset(w * 0.5f, h * 0.92f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round,
        )
    }
}

/**
 * Wordmark completo: logo V + texto "vinovim" com gradiente.
 */
@Composable
fun VinovimWordmark(
    modifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
    textSize: Float = 20f,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        VinovimLogoMark(size = iconSize)
        Spacer(modifier = Modifier.width(8.dp))
        Box {
            // texto base transparente para ocupar espaço
            Text(
                text = "vinovim",
                style = TextStyle(
                    fontSize = textSize.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.01f * textSize).sp,
                    color = Color.Transparent,
                ),
            )
            // texto com gradiente por cima
            Text(
                text = "vinovim",
                style = TextStyle(
                    fontSize = textSize.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.01f * textSize).sp,
                    brush = Brush.linearGradient(VinovimGradient),
                ),
            )
        }
    }
}
