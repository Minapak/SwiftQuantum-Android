package com.swiftquantum.presentation.ui.screen.admin

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swiftquantum.R
import com.swiftquantum.presentation.ui.theme.SwiftPurple

// Admin Tab Enum (iOS parity)
enum class AdminTab(
    val icon: ImageVector,
    val titleResId: Int
) {
    DASHBOARD(Icons.Default.Dashboard, R.string.admin_tab_dashboard),
    USERS(Icons.Default.People, R.string.admin_tab_users),
    CONTENT(Icons.Default.Description, R.string.admin_tab_content),
    TEAM(Icons.Default.Group, R.string.admin_tab_team),
    SETTINGS(Icons.Default.Settings, R.string.admin_tab_settings)
}

@Composable
fun AdminRootScreen(
    onNavigateBack: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(AdminTab.DASHBOARD) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0E1A),
                        Color(0xFF1A1F35),
                        Color(0xFF0A0E1A)
                    )
                )
            )
    ) {
        // Content Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (selectedTab) {
                AdminTab.DASHBOARD -> AdminDashboardScreen()
                AdminTab.USERS -> AdminUsersScreen()
                AdminTab.CONTENT -> AdminContentScreen()
                AdminTab.TEAM -> EnterpriseTeamScreen()
                AdminTab.SETTINGS -> AdminSettingsScreen(onNavigateBack = onNavigateBack)
            }
        }

        // Custom Tab Bar
        AdminTabBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}

@Composable
private fun AdminTabBar(
    selectedTab: AdminTab,
    onTabSelected: (AdminTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1F35).copy(alpha = 0.95f))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AdminTab.entries.forEach { tab ->
            AdminTabItem(
                tab = tab,
                isSelected = selectedTab == tab,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

@Composable
private fun AdminTabItem(
    tab: AdminTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        label = "scale"
    )

    val iconColor by animateColorAsState(
        targetValue = if (isSelected) SwiftPurple else Color.White.copy(alpha = 0.6f),
        label = "color"
    )

    Column(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = tab.icon,
            contentDescription = stringResource(tab.titleResId),
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = stringResource(tab.titleResId),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = iconColor,
            fontSize = 10.sp
        )

        // Selection indicator
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(SwiftPurple)
            )
        }
    }
}
