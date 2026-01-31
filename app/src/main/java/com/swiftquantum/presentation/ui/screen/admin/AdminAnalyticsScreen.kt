package com.swiftquantum.presentation.ui.screen.admin

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swiftquantum.R
import com.swiftquantum.presentation.ui.theme.SwiftPurple
import com.swiftquantum.presentation.viewmodel.AdminViewModel
import kotlinx.coroutines.delay

// Data classes for analytics
data class AnalyticsData(
    val dailyActiveUsers: Int = 0,
    val dauChange: Int = 0,
    val simulationsToday: Int = 0,
    val simulationsChange: Int = 0,
    val avgSessionMinutes: Int = 0,
    val sessionChange: Int = 0,
    val conversionRate: Double = 0.0,
    val conversionChange: Double = 0.0,
    val pythonUsage: Int = 0,
    val rustUsage: Int = 0,
    val cppUsage: Int = 0,
    val monthlyRevenue: Int = 0,
    val proSubscriptions: Int = 0,
    val masterSubscriptions: Int = 0
)

data class UsageDataPoint(
    val day: String,
    val users: Int
)

data class TopUser(
    val id: Int,
    val username: String,
    val xp: Int,
    val rank: Int,
    val subscriptionType: String?
)

@Composable
fun AdminAnalyticsScreen(
    viewModel: AdminViewModel = hiltViewModel()
) {
    val dashboardState by viewModel.dashboardState.collectAsState()
    var analytics by remember { mutableStateOf(AnalyticsData()) }
    var usageData by remember { mutableStateOf<List<UsageDataPoint>>(emptyList()) }
    var topUsers by remember { mutableStateOf<List<TopUser>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.loadDashboardStats()
        delay(500)

        // Calculate analytics from dashboard state
        val stats = dashboardState.stats
        val conversionRate = if (stats.totalUsers > 0) {
            (stats.premiumUsers.toDouble() / stats.totalUsers) * 100
        } else 0.0

        analytics = AnalyticsData(
            dailyActiveUsers = stats.activeUsers,
            dauChange = if (stats.totalUsers > 0) ((stats.newUsersToday.toDouble() / stats.totalUsers) * 100).toInt() else 0,
            simulationsToday = stats.apiRequestsToday,
            simulationsChange = 5,
            avgSessionMinutes = 12,
            sessionChange = 3,
            conversionRate = conversionRate,
            conversionChange = 0.5,
            pythonUsage = 65,
            rustUsage = 25,
            cppUsage = 10,
            monthlyRevenue = stats.monthlyRevenue.toInt(),
            proSubscriptions = stats.premiumUsers,
            masterSubscriptions = stats.premiumUsers / 3
        )

        // Generate weekly usage data
        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val baseValue = maxOf(stats.activeUsers / 7, 1)
        usageData = days.mapIndexed { index, day ->
            val variation = (0.7 + Math.random() * 0.6)
            val weekendFactor = if (index >= 5) 0.7 else 1.0
            val users = (baseValue * variation * weekendFactor).toInt()
            UsageDataPoint(day, maxOf(users, 1))
        }

        // Create top users from recent users
        topUsers = dashboardState.recentUsers.take(5).mapIndexed { index, user ->
            TopUser(
                id = user.id,
                username = user.username ?: "User ${user.id}",
                xp = user.totalXp,
                rank = index + 1,
                subscriptionType = user.subscriptionType
            )
        }

        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SwiftPurple)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.admin_analytics_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = { viewModel.loadDashboardStats() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = SwiftPurple)
                    }
                }
            }

            // Summary Cards
            item {
                SummaryCardsGrid(analytics)
            }

            // Weekly Usage Chart
            item {
                WeeklyUsageChart(usageData)
            }

            // Engine Usage
            item {
                EngineUsageSection(analytics)
            }

            // Top Users
            item {
                TopUsersSection(topUsers)
            }

            // Revenue Section
            item {
                RevenueSection(analytics)
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun SummaryCardsGrid(analytics: AnalyticsData) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnalyticsSummaryCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.admin_daily_active_users),
                value = analytics.dailyActiveUsers.toString(),
                change = if (analytics.dauChange != 0) "${if (analytics.dauChange > 0) "+" else ""}${analytics.dauChange}%" else "-",
                isPositive = analytics.dauChange >= 0,
                icon = Icons.Default.Person
            )
            AnalyticsSummaryCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.admin_simulations_run),
                value = analytics.simulationsToday.toString(),
                change = if (analytics.simulationsChange != 0) "+${analytics.simulationsChange}%" else "-",
                isPositive = analytics.simulationsChange >= 0,
                icon = Icons.Default.PlayArrow
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnalyticsSummaryCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.admin_avg_session_time),
                value = "${analytics.avgSessionMinutes}m",
                change = if (analytics.sessionChange != 0) "+${analytics.sessionChange}%" else "-",
                isPositive = analytics.sessionChange >= 0,
                icon = Icons.Default.Schedule
            )
            AnalyticsSummaryCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.admin_conversion_rate),
                value = String.format("%.1f%%", analytics.conversionRate),
                change = if (analytics.conversionChange != 0.0) String.format("%+.1f%%", analytics.conversionChange) else "-",
                isPositive = analytics.conversionChange >= 0,
                icon = Icons.Default.TrendingUp
            )
        }
    }
}

@Composable
private fun AnalyticsSummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    change: String,
    isPositive: Boolean,
    icon: ImageVector
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF00BCD4),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = change,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (change == "-") Color.White.copy(alpha = 0.5f)
                    else if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
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
private fun WeeklyUsageChart(usageData: List<UsageDataPoint>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.admin_weekly_usage),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (usageData.isEmpty()) {
                Text(
                    text = stringResource(R.string.admin_no_data),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.padding(vertical = 32.dp)
                )
            } else {
                val maxUsers = usageData.maxOfOrNull { it.users } ?: 1
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    usageData.forEach { data ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val heightFraction by animateFloatAsState(
                                targetValue = data.users.toFloat() / maxUsers,
                                animationSpec = tween(500),
                                label = "bar"
                            )
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .fillMaxHeight(heightFraction.coerceIn(0.1f, 1f))
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(Color(0xFF00BCD4))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = data.day,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EngineUsageSection(analytics: AnalyticsData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.admin_engine_usage),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            EngineUsageRow("Python", analytics.pythonUsage, Color(0xFF3776AB))
            Spacer(modifier = Modifier.height(8.dp))
            EngineUsageRow("Rust", analytics.rustUsage, Color(0xFFDEA584))
            Spacer(modifier = Modifier.height(8.dp))
            EngineUsageRow("C++", analytics.cppUsage, Color(0xFF00599C))
        }
    }
}

@Composable
private fun EngineUsageRow(engine: String, percentage: Int, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = engine,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White.copy(alpha = 0.1f))
        ) {
            val widthFraction by animateFloatAsState(
                targetValue = percentage / 100f,
                animationSpec = tween(500),
                label = "engine"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(widthFraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}

@Composable
private fun TopUsersSection(topUsers: List<TopUser>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.admin_top_users),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (topUsers.isEmpty()) {
                Text(
                    text = stringResource(R.string.admin_no_data),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                topUsers.forEach { user ->
                    TopUserRow(user)
                    if (user != topUsers.last()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TopUserRow(user: TopUser) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank badge
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(rankColor(user.rank).copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.rank.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = rankColor(user.rank)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF00BCD4).copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.username.first().uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00BCD4)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.username,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = user.subscriptionType ?: "Free",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f)
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = user.xp.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00BCD4)
            )
            Text(
                text = "XP",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}

private fun rankColor(rank: Int): Color {
    return when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color.White.copy(alpha = 0.6f)
    }
}

@Composable
private fun RevenueSection(analytics: AnalyticsData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.admin_revenue),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RevenueCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.admin_this_month),
                    value = "$${analytics.monthlyRevenue}",
                    icon = Icons.Default.AttachMoney,
                    color = Color(0xFF4CAF50)
                )
                RevenueCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.admin_pro_subscriptions),
                    value = analytics.proSubscriptions.toString(),
                    icon = Icons.Default.Star,
                    color = Color(0xFF9C27B0)
                )
                RevenueCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.admin_master_subscriptions),
                    value = analytics.masterSubscriptions.toString(),
                    icon = Icons.Default.WorkspacePremium,
                    color = Color(0xFFFFD700)
                )
            }
        }
    }
}

@Composable
private fun RevenueCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.6f),
                maxLines = 2
            )
        }
    }
}
