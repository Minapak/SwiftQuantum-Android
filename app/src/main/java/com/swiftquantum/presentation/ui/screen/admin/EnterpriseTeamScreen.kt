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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swiftquantum.R
import com.swiftquantum.presentation.ui.theme.SwiftPurple
import kotlinx.coroutines.launch

// Team Role Enum (iOS parity)
enum class TeamRole(val titleResId: Int, val icon: ImageVector, val color: Color) {
    OWNER(R.string.admin_role_owner, Icons.Default.Star, Color(0xFFFFD700)),
    ADMIN(R.string.admin_role_admin, Icons.Default.AdminPanelSettings, Color(0xFFF44336)),
    OPERATOR(R.string.admin_role_operator, Icons.Default.SupervisedUserCircle, Color(0xFF2196F3)),
    MEMBER(R.string.admin_role_member, Icons.Default.Person, Color(0xFF4CAF50))
}

// Team Member Model
data class TeamMember(
    val id: String,
    val email: String,
    val name: String,
    val role: TeamRole,
    val joinedAt: String,
    val lastActiveAt: String?
)

// Team Invite Model
data class TeamInvite(
    val id: String,
    val email: String,
    val role: TeamRole,
    val invitedAt: String,
    val expiresAt: String
)

// Audit Log Entry Model
data class AuditLogEntry(
    val id: String,
    val action: String,
    val performedBy: String,
    val targetUser: String?,
    val timestamp: String,
    val details: String?
)

// Team Tab Enum
enum class TeamTab(val titleResId: Int) {
    MEMBERS(R.string.admin_team_members),
    INVITES(R.string.admin_team_invites),
    SETTINGS(R.string.admin_team_settings),
    AUDIT_LOG(R.string.admin_team_audit_log)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterpriseTeamScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var members by remember { mutableStateOf(listOf<TeamMember>()) }
    var invites by remember { mutableStateOf(listOf<TeamInvite>()) }
    var auditLogs by remember { mutableStateOf(listOf<AuditLogEntry>()) }

    var showInviteSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val tabs = TeamTab.entries

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500)
        members = listOf(
            TeamMember("1", "alice@enterprise.com", "Alice Johnson", TeamRole.OWNER, "2024-01-15", "2026-01-31"),
            TeamMember("2", "bob@enterprise.com", "Bob Smith", TeamRole.ADMIN, "2024-06-20", "2026-01-30"),
            TeamMember("3", "charlie@enterprise.com", "Charlie Brown", TeamRole.OPERATOR, "2025-01-10", "2026-01-29"),
            TeamMember("4", "diana@enterprise.com", "Diana Prince", TeamRole.MEMBER, "2025-08-15", "2026-01-28"),
            TeamMember("5", "eve@enterprise.com", "Eve Wilson", TeamRole.MEMBER, "2025-11-01", "2026-01-25")
        )
        invites = listOf(
            TeamInvite("i1", "frank@enterprise.com", TeamRole.MEMBER, "2026-01-25", "2026-02-01"),
            TeamInvite("i2", "grace@enterprise.com", TeamRole.OPERATOR, "2026-01-28", "2026-02-04")
        )
        auditLogs = listOf(
            AuditLogEntry("a1", "Member invited", "alice@enterprise.com", "frank@enterprise.com", "2026-01-25 14:32", null),
            AuditLogEntry("a2", "Role changed", "alice@enterprise.com", "charlie@enterprise.com", "2026-01-20 09:15", "Member -> Operator"),
            AuditLogEntry("a3", "Member added", "alice@enterprise.com", "eve@enterprise.com", "2025-11-01 11:00", null),
            AuditLogEntry("a4", "Settings updated", "bob@enterprise.com", null, "2025-10-15 16:45", "Two-factor auth enabled")
        )
        isLoading = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = stringResource(R.string.admin_team_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = SwiftPurple,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = stringResource(tab.titleResId),
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        },
                        selectedContentColor = SwiftPurple,
                        unselectedContentColor = Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SwiftPurple)
                }
            } else {
                when (tabs[selectedTab]) {
                    TeamTab.MEMBERS -> TeamMembersView(members = members)
                    TeamTab.INVITES -> TeamInvitesView(invites = invites)
                    TeamTab.SETTINGS -> TeamSettingsView()
                    TeamTab.AUDIT_LOG -> AuditLogView(logs = auditLogs)
                }
            }
        }

        // FAB for inviting members
        if (selectedTab == 0 || selectedTab == 1) {
            FloatingActionButton(
                onClick = {
                    showInviteSheet = true
                    scope.launch { sheetState.show() }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 100.dp, end = 16.dp),
                containerColor = SwiftPurple
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }

    // Invite Member Sheet
    if (showInviteSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { sheetState.hide() }
                showInviteSheet = false
            },
            sheetState = sheetState,
            containerColor = Color(0xFF1A1F35)
        ) {
            InviteMemberSheet(
                onInvite = { email, role ->
                    invites = invites + TeamInvite(
                        id = "i${invites.size + 1}",
                        email = email,
                        role = role,
                        invitedAt = "Just now",
                        expiresAt = "In 7 days"
                    )
                    scope.launch { sheetState.hide() }
                    showInviteSheet = false
                },
                onDismiss = {
                    scope.launch { sheetState.hide() }
                    showInviteSheet = false
                }
            )
        }
    }
}

@Composable
private fun TeamMembersView(members: List<TeamMember>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(members) { member ->
            MemberRow(member = member)
        }

        item {
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
private fun MemberRow(member: TeamMember) {
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
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(member.role.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = member.role.icon,
                    contentDescription = null,
                    tint = member.role.color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = member.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Role Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(member.role.color.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = stringResource(member.role.titleResId),
                            style = MaterialTheme.typography.labelSmall,
                            color = member.role.color,
                            fontSize = 10.sp
                        )
                    }
                }

                Text(
                    text = member.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Last active
            member.lastActiveAt?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Composable
private fun TeamInvitesView(invites: List<TeamInvite>) {
    if (invites.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.admin_no_pending_invites),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.admin_no_pending_invites_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(invites) { invite ->
                InviteRow(invite = invite)
            }

            item {
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

@Composable
private fun InviteRow(invite: TeamInvite) {
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
            // Email Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(SwiftPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = SwiftPurple,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = invite.email,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(invite.role.titleResId),
                        style = MaterialTheme.typography.bodySmall,
                        color = invite.role.color
                    )
                    Text(
                        text = stringResource(R.string.admin_expires, invite.expiresAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }

            // Status
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFFC107).copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.admin_pending),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFFFC107)
                )
            }
        }
    }
}

@Composable
private fun TeamSettingsView() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            TeamSettingCard(
                title = stringResource(R.string.admin_team_name),
                subtitle = "SwiftQuantum Enterprise",
                icon = Icons.Default.Shield
            )
        }

        item {
            TeamSettingCard(
                title = stringResource(R.string.admin_member_limit),
                subtitle = "5 / 25 members",
                icon = Icons.Default.Person
            )
        }

        item {
            TeamSettingCard(
                title = stringResource(R.string.admin_two_factor),
                subtitle = stringResource(R.string.admin_enabled),
                icon = Icons.Default.AdminPanelSettings
            )
        }

        item {
            TeamSettingCard(
                title = stringResource(R.string.admin_sso),
                subtitle = stringResource(R.string.admin_not_configured),
                icon = Icons.Default.Settings
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun TeamSettingCard(
    title: String,
    subtitle: String,
    icon: ImageVector
) {
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SwiftPurple,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun AuditLogView(logs: List<AuditLogEntry>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(logs) { log ->
            AuditLogRow(log = log)
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun AuditLogRow(log: AuditLogEntry) {
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
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = log.action,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.admin_by, log.performedBy),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
                log.targetUser?.let {
                    Text(
                        text = stringResource(R.string.admin_target, it),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
                log.details?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = SwiftPurple.copy(alpha = 0.8f)
                    )
                }
            }

            Text(
                text = log.timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
private fun InviteMemberSheet(
    onInvite: (String, TeamRole) -> Unit,
    onDismiss: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(TeamRole.MEMBER) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.admin_invite_member),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = stringResource(R.string.email),
                    color = Color.White.copy(alpha = 0.6f)
                )
            },
            placeholder = {
                Text(
                    text = "user@example.com",
                    color = Color.White.copy(alpha = 0.4f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = SwiftPurple
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SwiftPurple,
                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = SwiftPurple
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Role Selection
        Text(
            text = stringResource(R.string.admin_select_role),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TeamRole.entries.filter { it != TeamRole.OWNER }.forEach { role ->
                RoleOptionItem(
                    role = role,
                    isSelected = selectedRole == role,
                    onClick = { selectedRole = role }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Invite Button
        Button(
            onClick = {
                if (email.isNotBlank()) {
                    onInvite(email, selectedRole)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = email.isNotBlank() && email.contains("@"),
            colors = ButtonDefaults.buttonColors(
                containerColor = SwiftPurple,
                disabledContainerColor = SwiftPurple.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.admin_send_invite),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun RoleOptionItem(
    role: TeamRole,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) role.color.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = role.icon,
                contentDescription = null,
                tint = role.color,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(role.titleResId),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) role.color else Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(role.color),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
