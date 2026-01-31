package com.swiftquantum.presentation.ui.screen.admin

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Webhook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.swiftquantum.data.dto.AdminUserDto
import com.swiftquantum.presentation.ui.theme.SwiftPurple
import com.swiftquantum.presentation.viewmodel.AdminViewModel
import com.swiftquantum.presentation.viewmodel.HealthStatus

@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel = hiltViewModel()
) {
    val dashboardState by viewModel.dashboardState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboardStats()
    }

    if (dashboardState.isLoading && dashboardState.stats.totalUsers == 0) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SwiftPurple)
        }
    } else if (dashboardState.error != null && dashboardState.stats.totalUsers == 0) {
        // Error state
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = dashboardState.error ?: "Error loading data",
                    color = Color.White.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.loadDashboardStats() }) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.admin_retry))
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title with refresh
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.admin_dashboard_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = { viewModel.loadDashboardStats() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = SwiftPurple
                        )
                    }
                }
            }

            // Stats Grid
            item {
                StatsGrid(
                    totalUsers = dashboardState.stats.totalUsers,
                    activeToday = dashboardState.stats.activeUsers,
                    circuitsCreated = dashboardState.stats.apiRequestsToday,
                    proSubscribers = dashboardState.stats.premiumUsers
                )
            }

            // System Status
            item {
                SystemStatusCard(
                    apiStatus = dashboardState.apiStatus,
                    dbStatus = dashboardState.dbStatus,
                    cdnStatus = dashboardState.cdnStatus,
                    engineStatus = dashboardState.engineStatus
                )
            }

            // Quick Actions
            item {
                QuickActionsCard(
                    onSendNotification = { title, body, topic ->
                        viewModel.sendNotification(title, body, topic)
                    }
                )
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

            items(dashboardState.recentUsers) { user ->
                UserActivityItem(user = user)
            }

            if (dashboardState.recentUsers.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.admin_no_recent_activity),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun StatsGrid(
    totalUsers: Int,
    activeToday: Int,
    circuitsCreated: Int,
    proSubscribers: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AdminStatCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.admin_total_users),
                value = totalUsers.toString(),
                icon = Icons.Default.People,
                color = Color(0xFF00BCD4)
            )
            AdminStatCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.admin_active_today),
                value = activeToday.toString(),
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
                value = circuitsCreated.toString(),
                icon = Icons.Default.Webhook,
                color = Color(0xFF9C27B0)
            )
            AdminStatCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.admin_pro_subscribers),
                value = proSubscribers.toString(),
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
private fun SystemStatusCard(
    apiStatus: HealthStatus,
    dbStatus: HealthStatus,
    cdnStatus: HealthStatus,
    engineStatus: HealthStatus
) {
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatusIndicator("API", apiStatus)
                StatusIndicator("DB", dbStatus)
                StatusIndicator("CDN", cdnStatus)
                StatusIndicator("Engine", engineStatus)
            }
        }
    }
}

@Composable
private fun StatusIndicator(name: String, status: HealthStatus) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(
                    when (status) {
                        HealthStatus.HEALTHY -> Color(0xFF4CAF50)
                        HealthStatus.DEGRADED -> Color(0xFFFF9800)
                        HealthStatus.DOWN -> Color(0xFFF44336)
                    }
                )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun QuickActionsCard(
    onSendNotification: (String, String, String) -> Unit
) {
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
private fun UserActivityItem(user: AdminUserDto) {
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
                Text(
                    text = (user.username?.firstOrNull() ?: 'U').uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SwiftPurple
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.username ?: "Unknown",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Text(
                    text = user.email ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

            // Subscription badge
            user.subscriptionType?.let { type ->
                val (badgeColor, badgeText) = when (type.lowercase()) {
                    "master", "career" -> Color(0xFFFFD700) to "Master"
                    "pro", "scholar" -> Color(0xFF9C27B0) to "Pro"
                    else -> Color.White.copy(alpha = 0.3f) to "Free"
                }
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier
                        .background(badgeColor, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
