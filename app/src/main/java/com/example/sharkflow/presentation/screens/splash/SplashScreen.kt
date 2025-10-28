package com.example.sharkflow.presentation.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sharkflow.R
import com.example.sharkflow.core.common.Lang
import kotlinx.coroutines.delay
import kotlin.math.*

@Composable
fun SplashScreen(
    text: String,
    onFinish: () -> Unit,
    showSpinner: Boolean = true,
    showSubtitle: Boolean = true,
    durationMs: Long = 1800L
) {
    var visible by remember { mutableStateOf(true) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(450)
    )

    data class WaveParams(
        val amplitude: Float,
        val wavelength: Float,
        val speed: Float,
        val phase: Float,
        val verticalShift: Float,
        val alpha: Float
    )

    val waves = listOf(
        WaveParams(
            amplitude = 28f,
            wavelength = 420f,
            speed = 0.6f,
            phase = 0f,
            verticalShift = 0.5f,
            alpha = 0.18f
        ),
        WaveParams(
            amplitude = 18f,
            wavelength = 260f,
            speed = 1.1f,
            phase = 60f,
            verticalShift = 0.55f,
            alpha = 0.12f
        ),
        WaveParams(
            amplitude = 10f,
            wavelength = 180f,
            speed = 0.9f,
            phase = 140f,
            verticalShift = 0.62f,
            alpha = 0.09f
        )
    )

    val infinite = rememberInfiniteTransition()
    val t by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 3600f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing)
        )
    )

    LaunchedEffect(Unit) {
        delay(durationMs)
        visible = false
        delay(500)
        onFinish()
    }

    val bg = MaterialTheme.colorScheme.primary
    val fg = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(bottom = 0.dp)
            .zIndex(9999F)
            .graphicsLayer { alpha = alphaAnim },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App logo",
                modifier = Modifier.size(92.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = Lang.string(R.string.common_app_name),
                color = fg,
                style = MaterialTheme.typography.titleLarge
            )
            if (showSubtitle) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = text,
                    color = fg.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (showSpinner) {
                Spacer(modifier = Modifier.height(12.dp))
                CircularProgressIndicator(
                    color = fg,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.BottomCenter)
        ) {
            fun buildSmoothPath(points: List<Offset>): Path {
                val p = Path()
                if (points.isEmpty()) return p
                p.moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    val prev = points[i - 1]
                    val curr = points[i]
                    val midX = (prev.x + curr.x) / 2f
                    val midY = (prev.y + curr.y) / 2f
                    p.quadraticTo(prev.x, prev.y, midX, midY)
                }
                val last = points.last()
                p.lineTo(size.width, last.y)
                p.lineTo(size.width, size.height)
                p.lineTo(0f, size.height)
                p.close()
                return p
            }

            val steps = (size.width / 6).toInt().coerceAtLeast(40)
            waves.forEach { wp ->
                val pts = ArrayList<Offset>(steps + 1)
                val verticalBase = size.height * wp.verticalShift
                for (i in 0..steps) {
                    val x = i * (size.width / steps)
                    val phase = (x + t * wp.speed + wp.phase)
                    val y =
                        (wp.amplitude * sin((phase / wp.wavelength) * 2f * PI)).toFloat() + verticalBase
                    pts.add(Offset(x, y))
                }
                val path = buildSmoothPath(pts)
                drawPath(path = path, color = fg.copy(alpha = wp.alpha), style = Fill)
            }
        }
    }
}
