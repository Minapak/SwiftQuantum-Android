package com.swiftquantum.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swiftquantum.data.dto.*
import com.swiftquantum.domain.repository.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ============================================
// UI State Models
// ============================================

data class AdminDashboardUiState(
    val stats: AdminStatsResponse = AdminStatsResponse(),
    val recentUsers: List<AdminUserDto> = emptyList(),
    val apiStatus: HealthStatus = HealthStatus.HEALTHY,
    val dbStatus: HealthStatus = HealthStatus.HEALTHY,
    val cdnStatus: HealthStatus = HealthStatus.HEALTHY,
    val engineStatus: HealthStatus = HealthStatus.HEALTHY,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class AdminUsersUiState(
    val users: List<AdminUserDto> = emptyList(),
    val totalUsers: Int = 0,
    val currentPage: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class AdminContentUiState(
    val pulses: List<PulseItemDto> = emptyList(),
    val pendingPulses: List<PendingPulseDto> = emptyList(),
    val stats: DailyPulseStatsResponse = DailyPulseStatsResponse(),
    val isLoading: Boolean = false,
    val isGenerating: Boolean = false,
    val error: String? = null
)

data class AdminSettingsUiState(
    val featureFlags: Map<String, Boolean> = emptyMap(),
    val ecsServices: List<EcsServiceDto> = emptyList(),
    val databaseStats: DatabaseStatsResponse = DatabaseStatsResponse(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class EnterpriseTeamUiState(
    val members: List<TeamMemberDto> = emptyList(),
    val invites: List<TeamInviteDto> = emptyList(),
    val auditLogs: List<TeamAuditLogDto> = emptyList(),
    val isLoading: Boolean = false,
    val isInviting: Boolean = false,
    val error: String? = null
)

enum class HealthStatus {
    HEALTHY, DEGRADED, DOWN
}

// ============================================
// Events
// ============================================

sealed class AdminEvent {
    data object UserBanned : AdminEvent()
    data object UserDeleted : AdminEvent()
    data object PulsePublished : AdminEvent()
    data object PulseGenerated : AdminEvent()
    data object NotificationSent : AdminEvent()
    data object MemberInvited : AdminEvent()
    data object MemberRemoved : AdminEvent()
    data class Error(val message: String) : AdminEvent()
}

// ============================================
// ViewModel
// ============================================

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    // Dashboard State
    private val _dashboardState = MutableStateFlow(AdminDashboardUiState())
    val dashboardState: StateFlow<AdminDashboardUiState> = _dashboardState.asStateFlow()

    // Users State
    private val _usersState = MutableStateFlow(AdminUsersUiState())
    val usersState: StateFlow<AdminUsersUiState> = _usersState.asStateFlow()

    // Content State
    private val _contentState = MutableStateFlow(AdminContentUiState())
    val contentState: StateFlow<AdminContentUiState> = _contentState.asStateFlow()

    // Settings State
    private val _settingsState = MutableStateFlow(AdminSettingsUiState())
    val settingsState: StateFlow<AdminSettingsUiState> = _settingsState.asStateFlow()

    // Team State
    private val _teamState = MutableStateFlow(EnterpriseTeamUiState())
    val teamState: StateFlow<EnterpriseTeamUiState> = _teamState.asStateFlow()

    // Events
    private val _events = MutableSharedFlow<AdminEvent>()
    val events: SharedFlow<AdminEvent> = _events.asSharedFlow()

    // ============================================
    // Dashboard Functions
    // ============================================

    fun loadDashboardStats() {
        viewModelScope.launch {
            _dashboardState.value = _dashboardState.value.copy(isLoading = true, error = null)

            adminRepository.getDashboardStats()
                .onSuccess { stats ->
                    _dashboardState.value = _dashboardState.value.copy(
                        stats = stats,
                        apiStatus = HealthStatus.HEALTHY,
                        dbStatus = if (stats.totalUsers > 0) HealthStatus.HEALTHY else HealthStatus.DEGRADED,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _dashboardState.value = _dashboardState.value.copy(
                        isLoading = false,
                        error = error.message,
                        apiStatus = HealthStatus.DOWN,
                        dbStatus = HealthStatus.DEGRADED
                    )
                }

            // Also load recent users for activity
            adminRepository.getUsers(1, 5)
                .onSuccess { response ->
                    _dashboardState.value = _dashboardState.value.copy(
                        recentUsers = response.users
                    )
                }
        }
    }

    // ============================================
    // User Management Functions
    // ============================================

    fun loadUsers(page: Int = 1) {
        viewModelScope.launch {
            _usersState.value = _usersState.value.copy(isLoading = true, error = null)

            adminRepository.getUsers(page)
                .onSuccess { response ->
                    _usersState.value = _usersState.value.copy(
                        users = response.users,
                        totalUsers = response.total,
                        currentPage = response.page,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _usersState.value = _usersState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun banUser(userId: Int, reason: String = "Admin action") {
        viewModelScope.launch {
            adminRepository.banUser(userId, reason)
                .onSuccess {
                    _events.emit(AdminEvent.UserBanned)
                    loadUsers(_usersState.value.currentPage)
                }
                .onFailure { error ->
                    _events.emit(AdminEvent.Error(error.message ?: "Failed to ban user"))
                }
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            adminRepository.deleteUser(userId)
                .onSuccess {
                    _events.emit(AdminEvent.UserDeleted)
                    loadUsers(_usersState.value.currentPage)
                }
                .onFailure { error ->
                    _events.emit(AdminEvent.Error(error.message ?: "Failed to delete user"))
                }
        }
    }

    fun grantPremium(userId: Int, subscriptionType: String, days: Int) {
        viewModelScope.launch {
            adminRepository.grantPremium(userId, subscriptionType, days)
                .onSuccess {
                    loadUsers(_usersState.value.currentPage)
                }
                .onFailure { error ->
                    _events.emit(AdminEvent.Error(error.message ?: "Failed to grant premium"))
                }
        }
    }

    // ============================================
    // Content Management Functions
    // ============================================

    fun loadContentStats() {
        viewModelScope.launch {
            _contentState.value = _contentState.value.copy(isLoading = true, error = null)

            adminRepository.getDailyPulseStats()
                .onSuccess { stats ->
                    _contentState.value = _contentState.value.copy(
                        stats = stats,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _contentState.value = _contentState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun loadPulses() {
        viewModelScope.launch {
            _contentState.value = _contentState.value.copy(isLoading = true, error = null)

            adminRepository.getAllPulses()
                .onSuccess { pulses ->
                    _contentState.value = _contentState.value.copy(
                        pulses = pulses,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _contentState.value = _contentState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun loadPendingPulses() {
        viewModelScope.launch {
            _contentState.value = _contentState.value.copy(isLoading = true, error = null)

            adminRepository.getPendingPulses()
                .onSuccess { response ->
                    _contentState.value = _contentState.value.copy(
                        pendingPulses = response.pulses,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _contentState.value = _contentState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun publishPulse(pulseId: Int, sendPush: Boolean = true) {
        viewModelScope.launch {
            adminRepository.publishPulse(pulseId, sendPush)
                .onSuccess {
                    _events.emit(AdminEvent.PulsePublished)
                    loadPendingPulses()
                    loadContentStats()
                }
                .onFailure { error ->
                    _events.emit(AdminEvent.Error(error.message ?: "Failed to publish pulse"))
                }
        }
    }

    fun generateDailyPulse() {
        viewModelScope.launch {
            _contentState.value = _contentState.value.copy(isGenerating = true)

            adminRepository.generateDailyPulse()
                .onSuccess {
                    _contentState.value = _contentState.value.copy(isGenerating = false)
                    _events.emit(AdminEvent.PulseGenerated)
                    loadPendingPulses()
                    loadContentStats()
                }
                .onFailure { error ->
                    _contentState.value = _contentState.value.copy(isGenerating = false)
                    _events.emit(AdminEvent.Error(error.message ?: "Failed to generate pulse"))
                }
        }
    }

    fun deletePulse(pulseId: String) {
        viewModelScope.launch {
            adminRepository.deletePulse(pulseId)
                .onSuccess {
                    loadPulses()
                }
                .onFailure { error ->
                    _events.emit(AdminEvent.Error(error.message ?: "Failed to delete pulse"))
                }
        }
    }

    // ============================================
    // Notification Functions
    // ============================================

    fun sendNotification(title: String, body: String, topic: String) {
        viewModelScope.launch {
            adminRepository.sendNotification(title, body, topic)
                .onSuccess {
                    _events.emit(AdminEvent.NotificationSent)
                }
                .onFailure { error ->
                    _events.emit(AdminEvent.Error(error.message ?: "Failed to send notification"))
                }
        }
    }

    // ============================================
    // Settings Functions
    // ============================================

    fun loadSettings() {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(isLoading = true, error = null)

            // Load feature flags
            adminRepository.getFeatureFlags()
                .onSuccess { response ->
                    _settingsState.value = _settingsState.value.copy(
                        featureFlags = response.flags
                    )
                }

            // Load ECS services
            adminRepository.getEcsServices()
                .onSuccess { response ->
                    _settingsState.value = _settingsState.value.copy(
                        ecsServices = response.services
                    )
                }

            // Load database stats
            adminRepository.getDatabaseStats()
                .onSuccess { stats ->
                    _settingsState.value = _settingsState.value.copy(
                        databaseStats = stats,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _settingsState.value = _settingsState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun updateFeatureFlag(flag: String, enabled: Boolean) {
        viewModelScope.launch {
            adminRepository.updateFeatureFlag(flag, enabled)
                .onSuccess {
                    loadSettings()
                }
                .onFailure { error ->
                    _events.emit(AdminEvent.Error(error.message ?: "Failed to update feature flag"))
                }
        }
    }

    // ============================================
    // Team Management Functions
    // ============================================

    fun loadTeamMembers() {
        viewModelScope.launch {
            _teamState.value = _teamState.value.copy(isLoading = true, error = null)

            adminRepository.getTeamMembers()
                .onSuccess { response ->
                    _teamState.value = _teamState.value.copy(
                        members = response.members,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _teamState.value = _teamState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun loadTeamInvites() {
        viewModelScope.launch {
            adminRepository.getTeamInvites()
                .onSuccess { response ->
                    _teamState.value = _teamState.value.copy(
                        invites = response.invites
                    )
                }
        }
    }

    fun loadTeamAuditLog() {
        viewModelScope.launch {
            adminRepository.getTeamAuditLog()
                .onSuccess { response ->
                    _teamState.value = _teamState.value.copy(
                        auditLogs = response.logs
                    )
                }
        }
    }

    fun inviteTeamMember(email: String, role: String) {
        viewModelScope.launch {
            _teamState.value = _teamState.value.copy(isInviting = true)

            adminRepository.inviteTeamMember(email, role)
                .onSuccess {
                    _teamState.value = _teamState.value.copy(isInviting = false)
                    _events.emit(AdminEvent.MemberInvited)
                    loadTeamInvites()
                }
                .onFailure { error ->
                    _teamState.value = _teamState.value.copy(isInviting = false)
                    _events.emit(AdminEvent.Error(error.message ?: "Failed to invite member"))
                }
        }
    }

    fun cancelInvite(inviteId: Int) {
        viewModelScope.launch {
            adminRepository.cancelInvite(inviteId)
                .onSuccess {
                    loadTeamInvites()
                }
                .onFailure { error ->
                    _events.emit(AdminEvent.Error(error.message ?: "Failed to cancel invite"))
                }
        }
    }

    fun updateMemberRole(memberId: Int, role: String) {
        viewModelScope.launch {
            adminRepository.updateMemberRole(memberId, role)
                .onSuccess {
                    loadTeamMembers()
                }
                .onFailure { error ->
                    _events.emit(AdminEvent.Error(error.message ?: "Failed to update role"))
                }
        }
    }

    fun removeTeamMember(memberId: Int) {
        viewModelScope.launch {
            adminRepository.removeTeamMember(memberId)
                .onSuccess {
                    _events.emit(AdminEvent.MemberRemoved)
                    loadTeamMembers()
                }
                .onFailure { error ->
                    _events.emit(AdminEvent.Error(error.message ?: "Failed to remove member"))
                }
        }
    }

    // ============================================
    // Utility Functions
    // ============================================

    fun clearDashboardError() {
        _dashboardState.value = _dashboardState.value.copy(error = null)
    }

    fun clearUsersError() {
        _usersState.value = _usersState.value.copy(error = null)
    }

    fun clearContentError() {
        _contentState.value = _contentState.value.copy(error = null)
    }

    fun clearSettingsError() {
        _settingsState.value = _settingsState.value.copy(error = null)
    }

    fun clearTeamError() {
        _teamState.value = _teamState.value.copy(error = null)
    }
}
