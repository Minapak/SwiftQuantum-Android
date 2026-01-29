package com.swiftquantum.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThreeDRotation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swiftquantum.R
import com.swiftquantum.presentation.ui.theme.QuantumCyan
import com.swiftquantum.presentation.ui.theme.QuantumGold
import com.swiftquantum.presentation.ui.theme.QuantumGreen
import com.swiftquantum.presentation.ui.theme.QuantumPink
import com.swiftquantum.presentation.ui.theme.QuantumPurple
import com.swiftquantum.presentation.viewmodel.SimulatorViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Cloud Visualization Screen with Magic Button
 *
 * Features:
 * - One-tap "Magic Button" for instant 3D visualization
 * - Bloch Sphere visualization
 * - Optical Lattice visualization
 * - Interactive rotation with touch gestures
 * - Cloud-rendered high-quality graphics
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloudVisualizationScreen(
    viewModel: SimulatorViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    var selectedVisualization by remember { mutableIntStateOf(0) }
    var isGenerating by remember { mutableStateOf(false) }
    var rotationX by remember { mutableFloatStateOf(0f) }
    var rotationY by remember { mutableFloatStateOf(0f) }

    val visualizationTypes = listOf(
        stringResource(R.string.cloud_viz_bloch_sphere),
        stringResource(R.string.cloud_viz_optical_lattice)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.cloud_viz_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = stringResource(R.string.share))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Visualization Type Selector
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                visualizationTypes.forEachIndexed { index, label ->
                    SegmentedButton(
                        selected = selectedVisualization == index,
                        onClick = { selectedVisualization = index },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = visualizationTypes.size
                        )
                    ) {
                        Text(label)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3D Visualization Canvas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                rotationY += dragAmount.x * 0.5f
                                rotationX += dragAmount.y * 0.5f
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    when (selectedVisualization) {
                        0 -> BlochSphere3D(
                            rotationX = rotationX,
                            rotationY = rotationY,
                            modifier = Modifier.fillMaxSize()
                        )
                        1 -> OpticalLattice3D(
                            rotationX = rotationX,
                            rotationY = rotationY,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Loading Overlay
                    AnimatedVisibility(
                        visible = isGenerating,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(
                                    color = QuantumPurple,
                                    strokeWidth = 3.dp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = stringResource(R.string.cloud_viz_loading),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Rotation Info
            Text(
                text = "Drag to rotate • X: ${rotationX.toInt()}° Y: ${rotationY.toInt()}°",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Magic Button
            MagicButton(
                onClick = {
                    isGenerating = true
                    // Simulate cloud rendering
                },
                isLoading = isGenerating,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        rotationX = 0f
                        rotationY = 0f
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = QuantumCyan)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reset View")
                }
                Button(
                    onClick = { /* Auto-rotate */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = QuantumGreen)
                ) {
                    Icon(Icons.Default.ThreeDRotation, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Auto Rotate")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Visualization Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudQueue,
                            contentDescription = null,
                            tint = QuantumPurple
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cloud-Rendered Visualization",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (selectedVisualization) {
                            0 -> "The Bloch sphere represents a single qubit's quantum state. " +
                                    "The north pole is |0⟩, south pole is |1⟩, and points on the equator " +
                                    "represent superposition states."
                            else -> "The optical lattice visualizes multiple qubits arranged in a 3D grid. " +
                                    "Colors represent probability amplitudes, and connections show entanglement."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }

    // Auto-dismiss loading after animation
    LaunchedEffect(isGenerating) {
        if (isGenerating) {
            kotlinx.coroutines.delay(2000)
            isGenerating = false
        }
    }
}

/**
 * Magic Button - One-tap visualization generator
 */
@Composable
private fun MagicButton(
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(1f) }
    val infiniteTransition = rememberInfiniteTransition(label = "magic")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            scale.animateTo(1f)
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Glow effect
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            QuantumPurple.copy(alpha = glowAlpha * 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        )

        Button(
            onClick = {
                onClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .scale(scale.value),
            colors = ButtonDefaults.buttonColors(
                containerColor = QuantumPurple
            ),
            shape = RoundedCornerShape(24.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.cloud_viz_magic_button),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * 3D Bloch Sphere Visualization
 */
@Composable
private fun BlochSphere3D(
    rotationX: Float,
    rotationY: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bloch")
    val stateRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "stateRotation"
    )

    Canvas(modifier = modifier.padding(16.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = minOf(size.width, size.height) / 2 * 0.8f

        // Apply rotation transformations
        rotate(rotationY, Offset(centerX, centerY)) {
            // Draw sphere wireframe
            // Equator
            drawCircle(
                color = QuantumCyan.copy(alpha = 0.5f),
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 2.dp.toPx())
            )

            // Vertical circle (meridian)
            val meridianPath = Path().apply {
                val steps = 36
                for (i in 0..steps) {
                    val angle = i * 2 * PI / steps
                    val x = centerX + cos(angle).toFloat() * radius * cos(rotationX * PI / 180).toFloat()
                    val y = centerY + sin(angle).toFloat() * radius
                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                }
            }
            drawPath(
                path = meridianPath,
                color = QuantumPurple.copy(alpha = 0.5f),
                style = Stroke(width = 2.dp.toPx())
            )

            // Horizontal circles (parallels)
            for (lat in listOf(-0.5f, 0.5f)) {
                val latRadius = radius * sqrt(1 - lat * lat)
                val latY = centerY - lat * radius * cos(rotationX * PI / 180).toFloat()
                drawCircle(
                    color = QuantumGreen.copy(alpha = 0.3f),
                    radius = latRadius,
                    center = Offset(centerX, latY),
                    style = Stroke(width = 1.dp.toPx())
                )
            }

            // Axes
            // Z-axis (|0⟩ to |1⟩)
            drawLine(
                color = Color.White,
                start = Offset(centerX, centerY - radius),
                end = Offset(centerX, centerY + radius),
                strokeWidth = 2.dp.toPx()
            )

            // X-axis
            drawLine(
                color = QuantumPink,
                start = Offset(centerX - radius, centerY),
                end = Offset(centerX + radius, centerY),
                strokeWidth = 2.dp.toPx()
            )

            // Y-axis (coming out of screen - represented by a dot)
            drawCircle(
                color = QuantumGold,
                radius = 4.dp.toPx(),
                center = Offset(centerX, centerY)
            )

            // State vector (animated)
            val theta = (45 + stateRotation * 0.1) * PI / 180
            val phi = stateRotation * PI / 180
            val stateX = centerX + sin(theta).toFloat() * cos(phi).toFloat() * radius
            val stateY = centerY - cos(theta).toFloat() * radius

            // State arrow
            drawLine(
                color = QuantumPurple,
                start = Offset(centerX, centerY),
                end = Offset(stateX, stateY),
                strokeWidth = 3.dp.toPx()
            )

            // State point
            drawCircle(
                color = QuantumPurple,
                radius = 8.dp.toPx(),
                center = Offset(stateX, stateY)
            )
            drawCircle(
                color = Color.White,
                radius = 4.dp.toPx(),
                center = Offset(stateX, stateY)
            )

            // Labels
            // |0⟩ at top
            drawCircle(
                color = QuantumGreen,
                radius = 6.dp.toPx(),
                center = Offset(centerX, centerY - radius - 10.dp.toPx())
            )
            // |1⟩ at bottom
            drawCircle(
                color = QuantumPink,
                radius = 6.dp.toPx(),
                center = Offset(centerX, centerY + radius + 10.dp.toPx())
            )
        }
    }
}

/**
 * 3D Optical Lattice Visualization
 */
@Composable
private fun OpticalLattice3D(
    rotationX: Float,
    rotationY: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "lattice")
    val pulsePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.padding(16.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val gridSize = 4
        val spacing = minOf(size.width, size.height) / (gridSize + 2)

        rotate(rotationY * 0.5f, Offset(centerX, centerY)) {
            // Draw lattice grid
            for (i in 0 until gridSize) {
                for (j in 0 until gridSize) {
                    val offsetX = (i - gridSize / 2 + 0.5f) * spacing
                    val offsetY = (j - gridSize / 2 + 0.5f) * spacing

                    // Apply pseudo-3D transformation
                    val depth = 1f + 0.1f * cos(rotationX * PI / 180).toFloat()
                    val x = centerX + offsetX * depth
                    val y = centerY + offsetY * depth

                    // Probability amplitude visualization
                    val phase = (i * 0.5f + j * 0.3f + pulsePhase) % (2 * PI.toFloat())
                    val amplitude = (0.5f + 0.5f * sin(phase))

                    // Color based on phase
                    val hue = (phase / (2 * PI.toFloat()) * 360).toInt()
                    val color = when {
                        hue < 60 -> QuantumPurple
                        hue < 120 -> QuantumCyan
                        hue < 180 -> QuantumGreen
                        hue < 240 -> QuantumGold
                        hue < 300 -> QuantumPink
                        else -> QuantumPurple
                    }

                    // Draw qubit node
                    val nodeRadius = (8 + amplitude * 12).dp.toPx()

                    // Glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                color.copy(alpha = amplitude * 0.5f),
                                Color.Transparent
                            ),
                            center = Offset(x, y),
                            radius = nodeRadius * 2
                        ),
                        radius = nodeRadius * 2,
                        center = Offset(x, y)
                    )

                    // Core
                    drawCircle(
                        color = color,
                        radius = nodeRadius,
                        center = Offset(x, y)
                    )

                    // Draw connections (entanglement visualization)
                    if (i < gridSize - 1) {
                        val nextX = centerX + ((i + 1) - gridSize / 2 + 0.5f) * spacing * depth
                        drawLine(
                            color = QuantumCyan.copy(alpha = 0.3f),
                            start = Offset(x, y),
                            end = Offset(nextX, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    if (j < gridSize - 1) {
                        val nextY = centerY + ((j + 1) - gridSize / 2 + 0.5f) * spacing * depth
                        drawLine(
                            color = QuantumPurple.copy(alpha = 0.3f),
                            start = Offset(x, y),
                            end = Offset(x, nextY),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
            }
        }
    }
}
