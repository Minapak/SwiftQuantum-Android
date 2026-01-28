package com.swiftquantum.presentation.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swiftquantum.domain.model.ExecutionResult

@Composable
fun ResultHistogram(
    result: ExecutionResult,
    modifier: Modifier = Modifier,
    maxBarsToShow: Int = 16,
    barColor: Color = Color(0xFF6366F1),
    barGradient: List<Color> = listOf(Color(0xFF6366F1), Color(0xFF22D3EE)),
    animated: Boolean = true
) {
    val sortedResults = remember(result) {
        result.probabilities.entries
            .sortedByDescending { it.value }
            .take(maxBarsToShow)
    }

    val maxProbability = remember(sortedResults) {
        sortedResults.maxOfOrNull { it.value } ?: 1.0
    }

    var animationTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        animationTriggered = true
    }

    Column(modifier = modifier) {
        // Title and stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Measurement Results",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "${result.shots} shots",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Histogram bars
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            val scrollState = rememberScrollState()

            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sortedResults.forEachIndexed { index, (state, probability) ->
                    val count = result.counts[state] ?: 0

                    val animatedHeight by animateFloatAsState(
                        targetValue = if (animationTriggered) (probability / maxProbability).toFloat() else 0f,
                        animationSpec = tween(
                            durationMillis = if (animated) 500 else 0,
                            delayMillis = if (animated) index * 50 else 0
                        ),
                        label = "bar_height_$state"
                    )

                    HistogramBar(
                        state = state,
                        probability = probability,
                        count = count,
                        heightFraction = animatedHeight,
                        gradient = barGradient,
                        modifier = Modifier.width(60.dp)
                    )
                }
            }
        }

        // Legend
        if (sortedResults.size < result.probabilities.size) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Showing top $maxBarsToShow of ${result.probabilities.size} states",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun HistogramBar(
    state: String,
    probability: Double,
    count: Int,
    heightFraction: Float,
    gradient: List<Color>,
    modifier: Modifier = Modifier
) {
    val maxHeight = 150.dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Probability label
        Text(
            text = String.format("%.1f%%", probability * 100),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Bar container
        Box(
            modifier = Modifier
                .height(maxHeight)
                .width(40.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Background bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Filled bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(heightFraction)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = gradient.reversed()
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Count label
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(2.dp))

        // State label
        Text(
            text = "|$state⟩",
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CompactHistogram(
    probabilities: Map<String, Double>,
    modifier: Modifier = Modifier,
    barColor: Color = Color(0xFF6366F1),
    maxBars: Int = 8
) {
    val sortedEntries = remember(probabilities) {
        probabilities.entries
            .sortedByDescending { it.value }
            .take(maxBars)
    }

    val maxProb = sortedEntries.maxOfOrNull { it.value } ?: 1.0

    Row(
        modifier = modifier
            .height(60.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        sortedEntries.forEach { (state, prob) ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight((prob / maxProb).toFloat())
                            .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                            .background(barColor)
                    )
                }
                Text(
                    text = state,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ProbabilityTable(
    result: ExecutionResult,
    modifier: Modifier = Modifier,
    maxRows: Int = 10
) {
    val sortedResults = remember(result) {
        result.probabilities.entries
            .sortedByDescending { it.value }
            .take(maxRows)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "State",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Prob",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Count",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            sortedResults.forEach { (state, probability) ->
                val count = result.counts[state] ?: 0

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "|$state⟩",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = String.format("%.2f%%", probability * 100),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = count.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}
