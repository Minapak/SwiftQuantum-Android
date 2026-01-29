package com.swiftquantum.presentation.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swiftquantum.presentation.ui.theme.QuantumCyan
import com.swiftquantum.presentation.ui.theme.QuantumPink
import com.swiftquantum.presentation.ui.theme.QuantumPurple
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(
    appVersion: String = "1.0.0",
    onSplashComplete: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    val logoScale = remember { Animatable(0.3f) }
    val logoAlpha = remember { Animatable(0f) }

    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800, delayMillis = 500),
        label = "text_alpha"
    )

    val versionAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 800),
        label = "version_alpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true

        // Animate logo scale
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )
    }

    LaunchedEffect(Unit) {
        // Animate logo alpha
        logoAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600)
        )
    }

    LaunchedEffect(Unit) {
        // Wait for animations and then navigate
        delay(2000)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0A0F),
                        Color(0xFF0D0D1A),
                        Color(0xFF0A0A0F)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Background animated particles
        AnimatedParticles()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Logo with Pulsing Rings
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value),
                contentAlignment = Alignment.Center
            ) {
                // Pulsing Rings
                PulsingRings()

                // Core Logo
                QuantumLogo()
            }

            Spacer(modifier = Modifier.height(40.dp))

            // App Name (hardcoded English for splash)
            Text(
                text = "SwiftQuantum",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.alpha(textAlpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline (hardcoded English for splash)
            Text(
                text = "Professional Quantum Computing IDE",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.alpha(textAlpha)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Version (hardcoded English for splash)
            Text(
                text = "Version $appVersion",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.alpha(versionAlpha)
            )
        }
    }
}

@Composable
private fun PulsingRings() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    // Ring 1 - Outer
    val ring1Scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring1_scale"
    )

    val ring1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring1_alpha"
    )

    // Ring 2 - Middle
    val ring2Scale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, delayMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring2_scale"
    )

    val ring2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, delayMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring2_alpha"
    )

    // Ring 3 - Inner
    val ring3Scale by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, delayMillis = 300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring3_scale"
    )

    val ring3Alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, delayMillis = 300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring3_alpha"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.minDimension / 2

        // Ring 1 (Outer - Purple)
        drawCircle(
            color = QuantumPurple.copy(alpha = ring1Alpha),
            radius = maxRadius * ring1Scale,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        // Ring 2 (Middle - Cyan)
        drawCircle(
            color = QuantumCyan.copy(alpha = ring2Alpha),
            radius = maxRadius * ring2Scale,
            center = center,
            style = Stroke(width = 3.dp.toPx())
        )

        // Ring 3 (Inner - Pink)
        drawCircle(
            color = QuantumPink.copy(alpha = ring3Alpha),
            radius = maxRadius * ring3Scale,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Composable
private fun QuantumLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "logo")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val orbitRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_rotation"
    )

    Canvas(modifier = Modifier.size(100.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 3

        // Draw orbital paths
        rotate(rotation, center) {
            // Horizontal ellipse
            drawOval(
                color = QuantumPurple.copy(alpha = 0.5f),
                topLeft = Offset(center.x - radius * 1.2f, center.y - radius * 0.4f),
                size = androidx.compose.ui.geometry.Size(radius * 2.4f, radius * 0.8f),
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        rotate(rotation + 60, center) {
            // Tilted ellipse 1
            drawOval(
                color = QuantumCyan.copy(alpha = 0.5f),
                topLeft = Offset(center.x - radius * 1.1f, center.y - radius * 0.5f),
                size = androidx.compose.ui.geometry.Size(radius * 2.2f, radius * 1f),
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        rotate(rotation - 60, center) {
            // Tilted ellipse 2
            drawOval(
                color = QuantumPink.copy(alpha = 0.5f),
                topLeft = Offset(center.x - radius * 1.0f, center.y - radius * 0.6f),
                size = androidx.compose.ui.geometry.Size(radius * 2f, radius * 1.2f),
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        // Central core (nucleus)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, QuantumPurple),
                center = center,
                radius = radius * 0.4f
            ),
            radius = radius * 0.3f,
            center = center
        )

        // Orbiting electrons
        val electronRadius = 6.dp.toPx()

        // Electron 1
        val e1Angle = orbitRotation * PI / 180
        val e1X = center.x + radius * 1.1f * cos(e1Angle).toFloat()
        val e1Y = center.y + radius * 0.35f * sin(e1Angle).toFloat()
        drawCircle(
            color = QuantumCyan,
            radius = electronRadius,
            center = Offset(e1X, e1Y)
        )

        // Electron 2
        val e2Angle = (orbitRotation + 120) * PI / 180
        val e2X = center.x + radius * 1.0f * cos(e2Angle).toFloat()
        val e2Y = center.y + radius * 0.45f * sin(e2Angle).toFloat()
        drawCircle(
            color = QuantumPink,
            radius = electronRadius,
            center = Offset(e2X, e2Y)
        )

        // Electron 3
        val e3Angle = (orbitRotation + 240) * PI / 180
        val e3X = center.x + radius * 0.9f * cos(e3Angle).toFloat()
        val e3Y = center.y + radius * 0.55f * sin(e3Angle).toFloat()
        drawCircle(
            color = QuantumPurple,
            radius = electronRadius,
            center = Offset(e3X, e3Y)
        )
    }
}

@Composable
private fun AnimatedParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")

    val particleOffsets = remember {
        List(20) {
            Pair(
                (Math.random() * 400 - 200).toFloat(),
                (Math.random() * 800 - 400).toFloat()
            )
        }
    }

    val particleAlphas = List(20) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.1f,
            targetValue = 0.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = (1500 + index * 100),
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "particle_alpha_$index"
        )
    }

    val particleYOffsets = List(20) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 50f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = (2000 + index * 150),
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "particle_y_$index"
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        particleOffsets.forEachIndexed { index, (offsetX, offsetY) ->
            val alpha by particleAlphas[index]
            val yOffset by particleYOffsets[index]

            val particleSize = (2 + index % 4).dp.toPx()

            drawCircle(
                color = when (index % 3) {
                    0 -> QuantumPurple.copy(alpha = alpha)
                    1 -> QuantumCyan.copy(alpha = alpha)
                    else -> QuantumPink.copy(alpha = alpha)
                },
                radius = particleSize,
                center = Offset(
                    centerX + offsetX,
                    centerY + offsetY + yOffset
                )
            )
        }
    }
}
