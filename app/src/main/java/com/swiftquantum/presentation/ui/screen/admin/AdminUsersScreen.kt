package com.swiftquantum.presentation.ui.screen.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swiftquantum.R
import com.swiftquantum.presentation.ui.theme.SwiftPurple
import kotlinx.coroutines.launch

// User Filter (iOS parity)
enum class UserFilter(val titleResId: Int) {
    ALL(R.string.admin_filter_all),
    ACTIVE(R.string.admin_filter_active),
    PRO(R.string.admin_filter_pro),
    MASTER(R.string.admin_filter_master)
}

// Admin User Model
data class AdminUser(
    val id: String,
    val email: String,
    val username: String?,
    val tier: String,
    val isActive: Boolean,
    val createdAt: String,
    val lastLoginAt: String?,
    val simulationsRun: Int = 0,
    val circuitsCreated: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen() {
    var isLoading by remember { mutableStateOf(true) }
    var users by remember { mutableStateOf(listOf<AdminUser>()) }
    var filteredUsers by remember { mutableStateOf(listOf<AdminUser>()) }
    var selectedFilter by remember { mutableStateOf(UserFilter.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedUser by remember { mutableStateOf<AdminUser?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var userToDelete by remember { mutableStateOf<AdminUser?>(null) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Simulate loading users
        kotlinx.coroutines.delay(500)
        users = listOf(
            AdminUser("1", "john@example.com", "john_doe", "PRO", true, "2025-01-15", "2026-01-31", 145, 23),
            AdminUser("2", "alice@example.com", "alice_q", "MASTER", true, "2024-11-20", "2026-01-30", 892, 156),
            AdminUser("3", "bob@example.com", "bob_smith", "FREE", true, "2026-01-01", "2026-01-29", 12, 3),
            AdminUser("4", "charlie@quantum.io", "charlie", "PRO", false, "2025-06-15", "2025-12-15", 234, 45),
            AdminUser("5", "diana@example.com", "diana_q", "MASTER", true, "2024-08-10", "2026-01-31", 1567, 312),
            AdminUser("6", "eve@research.org", "eve_r", "FREE", true, "2026-01-20", null, 5, 1)
        )
        isLoading = false
    }

    // Apply filters
    LaunchedEffect(users, selectedFilter, searchQuery) {
        filteredUsers = users.filter { user ->
            val matchesFilter = when (selectedFilter) {
                UserFilter.ALL -> true
                UserFilter.ACTIVE -> user.isActive
                UserFilter.PRO -> user.tier == "PRO"
                UserFilter.MASTER -> user.tier == "MASTER"
            }
            val matchesSearch = searchQuery.isEmpty() ||
                    user.email.contains(searchQuery, ignoreCase = true) ||
                    user.username?.contains(searchQuery, ignoreCase = true) == true

            matchesFilter && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = stringResource(R.string.admin_users_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(R.string.admin_search_users),
                    color = Color.White.copy(alpha = 0.5f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.6f)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
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

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(UserFilter.entries) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = {
                        Text(
                            text = stringResource(filter.titleResId),
                            fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SwiftPurple,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White.copy(alpha = 0.05f),
                        labelColor = Color.White.copy(alpha = 0.8f)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = Color.White.copy(alpha = 0.2f),
                        selectedBorderColor = SwiftPurple,
                        enabled = true,
                        selected = selectedFilter == filter
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User count
        Text(
            text = stringResource(R.string.admin_users_count, filteredUsers.size),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Users List
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SwiftPurple)
            }
        } else if (filteredUsers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.admin_no_users_found),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredUsers) { user ->
                    AdminUserRow(
                        user = user,
                        onClick = {
                            selectedUser = user
                            scope.launch { sheetState.show() }
                        },
                        onDelete = {
                            userToDelete = user
                            showDeleteDialog = true
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    // User Detail Bottom Sheet
    if (selectedUser != null) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { sheetState.hide() }
                selectedUser = null
            },
            sheetState = sheetState,
            containerColor = Color(0xFF1A1F35)
        ) {
            AdminUserDetailSheet(
                user = selectedUser!!,
                onActivate = {
                    users = users.map {
                        if (it.id == selectedUser!!.id) it.copy(isActive = true) else it
                    }
                },
                onDeactivate = {
                    users = users.map {
                        if (it.id == selectedUser!!.id) it.copy(isActive = false) else it
                    }
                },
                onDelete = {
                    userToDelete = selectedUser
                    showDeleteDialog = true
                },
                onDismiss = {
                    scope.launch { sheetState.hide() }
                    selectedUser = null
                }
            )
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && userToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                userToDelete = null
            },
            title = {
                Text(
                    text = stringResource(R.string.admin_delete_user_title),
                    color = Color.White
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.admin_delete_user_message, userToDelete!!.email),
                    color = Color.White.copy(alpha = 0.8f)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        users = users.filter { it.id != userToDelete!!.id }
                        showDeleteDialog = false
                        userToDelete = null
                        selectedUser = null
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = Color(0xFFF44336)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        userToDelete = null
                    }
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            },
            containerColor = Color(0xFF1A1F35)
        )
    }
}

@Composable
private fun AdminUserRow(
    user: AdminUser,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                    .background(
                        when (user.tier) {
                            "MASTER" -> Color(0xFFFFD700).copy(alpha = 0.2f)
                            "PRO" -> SwiftPurple.copy(alpha = 0.2f)
                            else -> Color.White.copy(alpha = 0.1f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (user.tier == "MASTER") Icons.Default.Star else Icons.Default.Person,
                    contentDescription = null,
                    tint = when (user.tier) {
                        "MASTER" -> Color(0xFFFFD700)
                        "PRO" -> SwiftPurple
                        else -> Color.White.copy(alpha = 0.6f)
                    },
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
                        text = user.username ?: user.email.substringBefore("@"),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Tier Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when (user.tier) {
                                    "MASTER" -> Color(0xFFFFD700)
                                    "PRO" -> SwiftPurple
                                    else -> Color.Gray
                                }
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = user.tier,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (user.tier == "MASTER") Color.Black else Color.White,
                            fontSize = 10.sp
                        )
                    }
                }

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Status Indicator
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (user.isActive) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
            )
        }
    }
}

@Composable
private fun AdminUserDetailSheet(
    user: AdminUser,
    onActivate: () -> Unit,
    onDeactivate: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
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
                text = stringResource(R.string.admin_user_details),
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

        // User Info
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(SwiftPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = SwiftPurple,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = user.username ?: "No username",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            UserStatItem(
                label = stringResource(R.string.admin_simulations),
                value = user.simulationsRun.toString()
            )
            UserStatItem(
                label = stringResource(R.string.admin_circuits),
                value = user.circuitsCreated.toString()
            )
            UserStatItem(
                label = stringResource(R.string.admin_tier),
                value = user.tier
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Info Rows
        InfoRow(label = stringResource(R.string.admin_created_at), value = user.createdAt)
        InfoRow(label = stringResource(R.string.admin_last_login), value = user.lastLoginAt ?: "Never")
        InfoRow(
            label = stringResource(R.string.admin_status),
            value = if (user.isActive) stringResource(R.string.admin_active) else stringResource(R.string.admin_inactive),
            valueColor = if (user.isActive) Color(0xFF4CAF50) else Color(0xFFF44336)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (user.isActive) {
                Button(
                    onClick = onDeactivate,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800).copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Block,
                        contentDescription = null,
                        tint = Color(0xFFFF9800)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.admin_deactivate),
                        color = Color(0xFFFF9800)
                    )
                }
            } else {
                Button(
                    onClick = onActivate,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50).copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.admin_activate),
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            Button(
                onClick = onDelete,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336).copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color(0xFFF44336)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.delete),
                    color = Color(0xFFF44336)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun UserStatItem(
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = SwiftPurple
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    valueColor: Color = Color.White.copy(alpha = 0.8f)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = valueColor
        )
    }
}
