package com.swiftquantum.presentation.ui.screen.admin

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Webhook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swiftquantum.R
import com.swiftquantum.presentation.ui.theme.SwiftPurple

// Data Models (iOS parity)
data class AdminStats(
    val totalUsers: Int = 0,
    val activeToday: Int = 0,
    val circuitsCreated: Int = 0,
    val proSubscribers: Int = 0
)

data class SystemStatus(
    val name: String,
    val isHealthy: Boolean,
    val latency: Int? = null
)

data class AdminActivity(
    val id: String,
    val type: String,
    val description: String,
    val timestamp: String,
    val userName: String? = null
)

@Composable
fun AdminDashboardScreen() {
    var isLoading by remember { mutableStateOf(true) }
    var stats by remember { mutableStateOf(AdminStats()) }
    var systemStatuses by remember { mutableStateOf(listOf<SystemStatus>()) }
    var recentActivities by remember { mutableStateOf(listOf<AdminActivity>()) }

    LaunchedEffect(Unit) {
        // Simulate loading data
        kotlinx.coroutines.delay(500)
        stats = AdminStats(
            totalUsers = 1247,
            activeToday = 89,
            circuitsCreated = 3421,
            proSubscribers = 156
        )
        systemStatuses = listOf(
            SystemStatus("API", true, 45),
            SystemStatus("Database", true, 12),
            SystemStatus("CDN", true, 8),
            SystemStatus("Quantum Engine", true, 234)
        )
        recentActivities = listOf(
            AdminActivity("1", "user", "New user registered", "2 min ago", "john@example.com"),
            AdminActivity("2", "circuit", "Circuit saved", "5 min ago", "alice@example.com"),
            AdminActivity("3", "subscription", "Pro subscription started", "12 min ago", "bob@example.com"),
            AdminActivity("4", "simulation", "100-qubit simulation completed", "25 min ago", "charlie@example.com")
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
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            item {
                Text(
                    text = stringResource(R.string.admin_dashboard_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Stats Grid
            item {
                StatsGrid(stats = stats)
            }

            // System Status
            item {
                SystemStatusCard(statuses = systemStatuses)
            }

            // Quick Actions
            item {
                QuickActionsCard()
            }

            // Recent Activity
            item {
                Text(
                    text = stringResource(R.string.admin_recent_activity),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            items(recentActivities) { activity ->
                ActivityItem(activity = activity)
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun StatsGrid(stats: AdminStats) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AdminStatCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.admin_total_users),
                value = stats.totalUsers.toString(),
                icon = Icons.Default.People,
                color = Color(0xFF00BCD4)
            )
            AdminStatCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.admin_active_today),
                value = stats.activeToday.toString(),
                icon = Icons.Default.Person,
                color = Color(0xFF4CAF50)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AdminStatCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.admin_circuits_created),
                value = stats.circuitsCreated.toString(),
                icon = Icons.Default.Webhook,
                color = Color(0xFF9C27B0)
            )
            AdminStatCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.admin_pro_subscribers),
                value = stats.proSubscribers.toString(),
                icon = Icons.Default.Assessment,
                color = Color(0xFFFFD700)
            )
        }
    }
}

@Composable
private fun AdminStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
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
private fun SystemStatusCard(statuses: List<SystemStatus>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.admin_system_status),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            statuses.forEach { status ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (status.isHealthy) Color(0xFF4CAF50) else Color(0xFFF44336)
                                )
                        )
                        Text(
                            text = status.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                    status.latency?.let {
                        Text(
                            text = "${it}ms",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.admin_quick_actions),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.admin_add_circuit),
                    icon = Icons.Default.Add,
                    color = Color(0xFF00BCD4),
                    onClick = { }
                )
                QuickActionButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.admin_send_notification),
                    icon = Icons.Default.Notifications,
                    color = Color(0xFF9C27B0),
                    onClick = { }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            QuickActionButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.admin_view_reports),
                icon = Icons.Default.Assessment,
                color = Color(0xFFFFD700),
                onClick = { }
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

@Composable
private fun ActivityItem(activity: AdminActivity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.03f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SwiftPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (activity.type) {
                        "user" -> Icons.Default.Person
                        "circuit" -> Icons.Default.Webhook
                        "subscription" -> Icons.Default.Assessment
                        else -> Icons.Default.Storage
                    },
                    contentDescription = null,
                    tint = SwiftPurple,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                activity.userName?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            Text(
                text = activity.timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}
