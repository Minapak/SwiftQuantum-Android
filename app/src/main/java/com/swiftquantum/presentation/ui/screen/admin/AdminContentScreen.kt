package com.swiftquantum.presentation.ui.screen.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swiftquantum.R
import com.swiftquantum.presentation.ui.theme.SwiftPurple

// Content Section (iOS parity)
enum class ContentSection(val titleResId: Int) {
    DAILY_PULSE(R.string.admin_section_daily_pulse),
    PENDING(R.string.admin_section_pending),
    STATS(R.string.admin_section_stats)
}

// Daily Pulse Model
data class AdminDailyPulse(
    val id: String,
    val title: String,
    val summary: String,
    val isPublished: Boolean,
    val targetDate: String,
    val viewCount: Int = 0,
    val quizAttemptCount: Int = 0,
    val aiConfidence: Double? = null
)

// Pulse Stats Model
data class PulseStats(
    val totalPulses: Int = 0,
    val published: Int = 0,
    val pendingApproval: Int = 0,
    val totalViews: Int = 0,
    val totalQuizAttempts: Int = 0,
    val quizAccuracy: Double = 0.0
)

@Composable
fun AdminContentScreen() {
    var selectedSection by remember { mutableIntStateOf(0) }
    val sections = ContentSection.entries

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = stringResource(R.string.admin_content_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Section Tabs
        TabRow(
            selectedTabIndex = selectedSection,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSection]),
                    color = SwiftPurple,
                    height = 3.dp
                )
            }
        ) {
            sections.forEachIndexed { index, section ->
                Tab(
                    selected = selectedSection == index,
                    onClick = { selectedSection = index },
                    text = {
                        Text(
                            text = stringResource(section.titleResId),
                            fontWeight = if (selectedSection == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selectedContentColor = SwiftPurple,
                    unselectedContentColor = Color.White.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content
        when (sections[selectedSection]) {
            ContentSection.DAILY_PULSE -> DailyPulseListView()
            ContentSection.PENDING -> PendingPulseListView()
            ContentSection.STATS -> PulseStatsView()
        }
    }
}

@Composable
private fun DailyPulseListView() {
    var isLoading by remember { mutableStateOf(true) }
    var pulses by remember { mutableStateOf(listOf<AdminDailyPulse>()) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500)
        pulses = listOf(
            AdminDailyPulse("1", "Quantum Computing Breakthrough", "New error correction method achieves 99.9% fidelity", true, "2026-01-31", 1245, 324),
            AdminDailyPulse("2", "IBM Quantum Roadmap Update", "IBM announces 100,000 qubit goal by 2033", true, "2026-01-30", 892, 156),
            AdminDailyPulse("3", "Google's Willow Chip", "Latest quantum chip demonstrates time crystal", true, "2026-01-29", 2341, 567),
            AdminDailyPulse("4", "Quantum Internet Progress", "First quantum network established between cities", true, "2026-01-28", 1567, 289)
        )
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SwiftPurple)
        }
    } else if (pulses.isEmpty()) {
        EmptyContentView(
            icon = Icons.Default.Newspaper,
            title = stringResource(R.string.admin_no_pulses),
            subtitle = stringResource(R.string.admin_no_pulses_desc)
        )
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pulses) { pulse ->
                DailyPulseRow(pulse = pulse)
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun DailyPulseRow(pulse: AdminDailyPulse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pulse.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = pulse.summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = pulse.viewCount.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.QuestionMark,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = pulse.quizAttemptCount.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (pulse.isPublished) Color(0xFF4CAF50) else Color.Gray
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (pulse.isPublished) stringResource(R.string.admin_published) else stringResource(R.string.admin_draft),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pulse.targetDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun PendingPulseListView() {
    var isLoading by remember { mutableStateOf(true) }
    var pendingPulses by remember { mutableStateOf(listOf<AdminDailyPulse>()) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500)
        pendingPulses = listOf(
            AdminDailyPulse("5", "Microsoft Azure Quantum Update", "New ion trap system available", false, "2026-02-01", 0, 0, 0.92),
            AdminDailyPulse("6", "Quantum Algorithms in Finance", "JPMorgan's latest quantum portfolio optimization", false, "2026-02-02", 0, 0, 0.87),
            AdminDailyPulse("7", "Superconducting Qubit Advances", "New coherence time record achieved", false, "2026-02-03", 0, 0, 0.95)
        )
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SwiftPurple)
        }
    } else if (pendingPulses.isEmpty()) {
        EmptyContentView(
            icon = Icons.Default.CheckCircle,
            title = stringResource(R.string.admin_no_pending),
            subtitle = stringResource(R.string.admin_no_pending_desc)
        )
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pendingPulses) { pulse ->
                PendingPulseRow(
                    pulse = pulse,
                    onPublish = {
                        pendingPulses = pendingPulses.filter { it.id != pulse.id }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun PendingPulseRow(
    pulse: AdminDailyPulse,
    onPublish: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = pulse.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pulse.summary,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))

            pulse.aiConfidence?.let { confidence ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.admin_ai_confidence, (confidence * 100).toInt()),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.5f)
                    )

                    Button(
                        onClick = onPublish,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.admin_publish),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PulseStatsView() {
    var isLoading by remember { mutableStateOf(true) }
    var stats by remember { mutableStateOf(PulseStats()) }
    var isGenerating by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500)
        stats = PulseStats(
            totalPulses = 156,
            published = 142,
            pendingApproval = 14,
            totalViews = 45678,
            totalQuizAttempts = 12345,
            quizAccuracy = 78.5
        )
        isLoading = false
    }

    // Prepare stats data
    val totalPulsesLabel = stringResource(R.string.admin_total_pulses)
    val publishedLabel = stringResource(R.string.admin_published)
    val pendingLabel = stringResource(R.string.admin_pending)
    val totalViewsLabel = stringResource(R.string.admin_total_views)
    val quizAttemptsLabel = stringResource(R.string.admin_quiz_attempts)
    val quizAccuracyLabel = stringResource(R.string.admin_quiz_accuracy)

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SwiftPurple)
        }
    } else {
        val statsData = listOf(
            Triple(totalPulsesLabel, stats.totalPulses.toString(), Color(0xFF00BCD4)),
            Triple(publishedLabel, stats.published.toString(), Color(0xFF4CAF50)),
            Triple(pendingLabel, stats.pendingApproval.toString(), Color(0xFFFFC107)),
            Triple(totalViewsLabel, formatNumber(stats.totalViews), Color(0xFF9C27B0)),
            Triple(quizAttemptsLabel, formatNumber(stats.totalQuizAttempts), Color(0xFFFF5722)),
            Triple(quizAccuracyLabel, "${stats.quizAccuracy.toInt()}%", Color(0xFF2196F3))
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Stats Grid
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(280.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(statsData) { (title, value, color) ->
                        PulseStatCard(title = title, value = value, color = color)
                    }
                }
            }

            // Generate Button
            item {
                Button(
                    onClick = {
                        isGenerating = true
                        // Simulate generation
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isGenerating,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFFFD700), Color(0xFFFFA500))
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.admin_generate_pulse),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // Generating indicator
            item {
                AnimatedVisibility(visible = isGenerating) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color(0xFFFFD700),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.admin_generating_ai),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun PulseStatCard(
    title: String,
    value: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun EmptyContentView(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

private fun formatNumber(number: Int): String {
    return when {
        number >= 1000000 -> String.format("%.1fM", number / 1000000.0)
        number >= 1000 -> String.format("%.1fK", number / 1000.0)
        else -> number.toString()
    }
}
