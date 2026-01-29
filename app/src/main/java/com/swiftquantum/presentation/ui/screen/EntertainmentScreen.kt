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
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swiftquantum.R
import com.swiftquantum.domain.model.ConfidenceLevel
import com.swiftquantum.domain.model.PuzzleDifficulty
import com.swiftquantum.domain.model.PuzzleGate
import com.swiftquantum.domain.model.QuantumArtData
import com.swiftquantum.presentation.ui.theme.QuantumCyan
import com.swiftquantum.presentation.ui.theme.QuantumGold
import com.swiftquantum.presentation.ui.theme.QuantumGreen
import com.swiftquantum.presentation.ui.theme.QuantumPink
import com.swiftquantum.presentation.ui.theme.QuantumPurple
import com.swiftquantum.presentation.viewmodel.ExperienceViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Entertainment Modules Screen
 * - Quantum Art Generator
 * - Qubit Puzzle Game
 * - Quantum Oracle
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntertainmentScreen(
    viewModel: ExperienceViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.entertainment_art),
        stringResource(R.string.entertainment_game),
        stringResource(R.string.entertainment_oracle)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.entertainment_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = QuantumPurple
            )
        }

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = QuantumPurple
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) },
                    icon = {
                        Icon(
                            imageVector = when (index) {
                                0 -> Icons.Default.Brush
                                1 -> Icons.Default.Extension
                                else -> Icons.Default.Casino
                            },
                            contentDescription = null
                        )
                    }
                )
            }
        }

        // Content
        when (selectedTab) {
            0 -> QuantumArtTab(viewModel)
            1 -> QubitPuzzleTab(viewModel)
            2 -> QuantumOracleTab(viewModel)
        }
    }
}

// ============================================================================
// Quantum Art Generator Tab
// ============================================================================

@Composable
private fun QuantumArtTab(viewModel: ExperienceViewModel) {
    val artData by viewModel.artData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.art_generator_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.art_generator_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Art Canvas
        QuantumArtCanvas(
            artData = artData,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Art Info
        artData?.let { data ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.art_quantum_signature),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = data.quantumSignature,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = QuantumPurple
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(stringResource(R.string.art_complexity), style = MaterialTheme.typography.labelSmall)
                            Text("${data.complexity}/10", fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text(stringResource(R.string.art_saturation), style = MaterialTheme.typography.labelSmall)
                            Text("${(data.saturation * 100).toInt()}%", fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text(stringResource(R.string.art_brightness), style = MaterialTheme.typography.labelSmall)
                            Text("${(data.brightness * 100).toInt()}%", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.loadArtFromSuperposition() },
                modifier = Modifier.weight(1f),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = QuantumPurple)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.art_generate))
                }
            }
            IconButton(
                onClick = { /* Share */ },
                modifier = Modifier
                    .background(QuantumCyan.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.Share, contentDescription = stringResource(R.string.share), tint = QuantumCyan)
            }
        }
    }
}

@Composable
private fun QuantumArtCanvas(
    artData: QuantumArtData?,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "art")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val primaryColor = artData?.composeColor ?: QuantumPurple
    val complexity = artData?.complexity ?: 5
    val contrast = artData?.contrast ?: 0.5

    Canvas(modifier = modifier.background(Color.Black)) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val maxRadius = minOf(size.width, size.height) / 2 * 0.9f

        // Draw quantum interference patterns
        for (i in 0 until complexity) {
            val radius = maxRadius * (i + 1) / complexity
            val alpha = (1f - i.toFloat() / complexity) * contrast.toFloat()

            drawCircle(
                color = primaryColor.copy(alpha = alpha.coerceIn(0.1f, 0.8f)),
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // Draw rotating quantum rays
        rotate(rotation, Offset(centerX, centerY)) {
            for (i in 0 until (complexity * 2)) {
                val angle = (i * 360f / (complexity * 2)) * PI / 180
                val endX = centerX + cos(angle).toFloat() * maxRadius
                val endY = centerY + sin(angle).toFloat() * maxRadius

                drawLine(
                    color = primaryColor.copy(alpha = 0.3f),
                    start = Offset(centerX, centerY),
                    end = Offset(endX, endY),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }

        // Draw central quantum core
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(primaryColor, primaryColor.copy(alpha = 0.3f), Color.Transparent),
                center = Offset(centerX, centerY),
                radius = maxRadius * 0.3f
            ),
            radius = maxRadius * 0.3f,
            center = Offset(centerX, centerY)
        )
    }
}

// ============================================================================
// Qubit Puzzle Game Tab
// ============================================================================

@Composable
private fun QubitPuzzleTab(viewModel: ExperienceViewModel) {
    val puzzleState by viewModel.puzzleState.collectAsState()
    val currentLevel by viewModel.currentLevel.collectAsState()
    val totalScore by viewModel.totalScore.collectAsState()

    LaunchedEffect(Unit) {
        if (puzzleState == null) {
            viewModel.startPuzzle(1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Score and Level
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(R.string.game_level),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Text(
                    text = "$currentLevel / ${ExperienceViewModel.MAX_LEVELS}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = QuantumPurple
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(R.string.game_total_score),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Text(
                    text = "$totalScore",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = QuantumGold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        puzzleState?.let { state ->
            val level = ExperienceViewModel.puzzleLevels.getOrNull(currentLevel - 1)

            // Level Info
            level?.let {
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = it.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                DifficultyBadge(it.difficulty)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Qubit State Visualization
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Current State
                QubitStateCard(
                    title = stringResource(R.string.game_current_state),
                    probability0 = state.currentState.probability0(),
                    probability1 = state.currentState.probability1(),
                    color = QuantumCyan
                )

                // Target State
                QubitStateCard(
                    title = stringResource(R.string.game_target_state),
                    probability0 = state.targetState.probability0(),
                    probability1 = state.targetState.probability1(),
                    color = QuantumGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Moves Counter
            Text(
                text = stringResource(R.string.game_moves_format, state.moves.size, state.maxMoves),
                style = MaterialTheme.typography.bodyLarge,
                color = if (state.moves.size > state.maxMoves * 0.75)
                    QuantumPink else MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gate Buttons
            level?.let { lvl ->
                Text(
                    text = stringResource(R.string.game_available_gates),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(lvl.allowedGates) { gate ->
                        GateButton(
                            gate = gate,
                            onClick = { viewModel.applyGate(gate) },
                            enabled = !state.isComplete
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Completion or Reset
            if (state.isComplete) {
                CompletionCard(
                    stars = state.stars,
                    score = state.score,
                    onNextLevel = { viewModel.nextLevel() },
                    onReplay = { viewModel.resetPuzzle() },
                    isLastLevel = currentLevel >= ExperienceViewModel.MAX_LEVELS
                )
            } else {
                Button(
                    onClick = { viewModel.resetPuzzle() },
                    colors = ButtonDefaults.buttonColors(containerColor = QuantumPink)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.game_reset))
                }
            }
        }
    }
}

@Composable
private fun QubitStateCard(
    title: String,
    probability0: Double,
    probability1: Double,
    color: Color
) {
    Card(
        modifier = Modifier.width(140.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "|0⟩: ${(probability0 * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "|1⟩: ${(probability1 * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GateButton(
    gate: PuzzleGate,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val gateColor = when (gate) {
        PuzzleGate.H -> QuantumPurple
        PuzzleGate.X -> QuantumPink
        PuzzleGate.Y -> QuantumGreen
        PuzzleGate.Z -> QuantumCyan
        PuzzleGate.S -> QuantumGold
        PuzzleGate.T -> Color(0xFFFF6B6B)
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = gateColor),
        modifier = Modifier.size(60.dp)
    ) {
        Text(
            text = gate.symbol,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Composable
private fun DifficultyBadge(difficulty: PuzzleDifficulty) {
    val (color, text) = when (difficulty) {
        PuzzleDifficulty.BEGINNER -> QuantumGreen to stringResource(R.string.difficulty_beginner)
        PuzzleDifficulty.EASY -> QuantumCyan to stringResource(R.string.difficulty_easy)
        PuzzleDifficulty.MEDIUM -> QuantumGold to stringResource(R.string.difficulty_medium)
        PuzzleDifficulty.HARD -> QuantumPink to stringResource(R.string.difficulty_hard)
        PuzzleDifficulty.EXPERT -> QuantumPurple to stringResource(R.string.difficulty_expert)
    }

    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CompletionCard(
    stars: Int,
    score: Int,
    onNextLevel: () -> Unit,
    onReplay: () -> Unit,
    isLastLevel: Boolean
) {
    val scale = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = tween(500, easing = FastOutSlowInEasing))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale.value),
        colors = CardDefaults.cardColors(containerColor = QuantumGreen.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.game_level_complete),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = QuantumGreen
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Stars
            Row {
                repeat(3) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (index < stars) QuantumGold else Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.game_score_format, score),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onReplay,
                    colors = ButtonDefaults.buttonColors(containerColor = QuantumCyan)
                ) {
                    Text(stringResource(R.string.game_replay))
                }
                if (!isLastLevel) {
                    Button(
                        onClick = onNextLevel,
                        colors = ButtonDefaults.buttonColors(containerColor = QuantumPurple)
                    ) {
                        Text(stringResource(R.string.game_next_level))
                    }
                }
            }
        }
    }
}

// ============================================================================
// Quantum Oracle Tab
// ============================================================================

@Composable
private fun QuantumOracleTab(viewModel: ExperienceViewModel) {
    val oracleResult by viewModel.oracleResult.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var question by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Oracle Header
        Icon(
            imageVector = Icons.Default.Casino,
            contentDescription = null,
            tint = QuantumPurple,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = stringResource(R.string.oracle_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.oracle_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Question Input
        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            label = { Text(stringResource(R.string.oracle_question_hint)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Consult Button
        Button(
            onClick = {
                if (question.isNotBlank()) {
                    viewModel.consultOracle(question)
                }
            },
            enabled = question.isNotBlank() && !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = QuantumPurple),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.oracle_consult))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Oracle Result
        AnimatedVisibility(
            visible = oracleResult != null,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut()
        ) {
            oracleResult?.let { result ->
                OracleResultCard(result)
            }
        }
    }
}

@Composable
private fun OracleResultCard(result: com.swiftquantum.domain.model.OracleResult) {
    val resultColor = if (result.isYes) QuantumGreen else QuantumPink
    val resultIcon = if (result.isYes) Icons.Default.ThumbUp else Icons.Default.ThumbDown

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = resultColor.copy(alpha = 0.1f)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(listOf(resultColor, resultColor.copy(alpha = 0.5f)))
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Answer Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(resultColor.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = resultIcon,
                    contentDescription = null,
                    tint = resultColor,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Answer Text
            Text(
                text = result.answerString.uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = resultColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Confidence
            Text(
                text = stringResource(R.string.oracle_confidence_format, result.confidencePercentage.toInt()),
                style = MaterialTheme.typography.bodyLarge
            )

            // Confidence Level Badge
            val confidenceColor = when (result.confidenceLevel) {
                ConfidenceLevel.HIGH -> QuantumGreen
                ConfidenceLevel.MEDIUM -> QuantumGold
                ConfidenceLevel.LOW -> QuantumPink
            }
            val confidenceText = when (result.confidenceLevel) {
                ConfidenceLevel.HIGH -> stringResource(R.string.oracle_confidence_high)
                ConfidenceLevel.MEDIUM -> stringResource(R.string.oracle_confidence_medium)
                ConfidenceLevel.LOW -> stringResource(R.string.oracle_confidence_low)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .background(confidenceColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = confidenceText,
                    color = confidenceColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quantum State
            Text(
                text = stringResource(R.string.oracle_quantum_state),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = result.quantumState,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = QuantumCyan
            )

            // Remaining tokens (if applicable)
            result.remainingTokens?.let { tokens ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.oracle_remaining_tokens, tokens),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}
