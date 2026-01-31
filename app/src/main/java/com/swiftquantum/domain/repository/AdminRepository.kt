package com.swiftquantum.domain.repository

import com.swiftquantum.data.dto.*

/**
 * Admin Repository Interface
 * Defines all admin operations for dashboard, user management, content, and team
 */
interface AdminRepository {

    // Dashboard Stats
    suspend fun getDashboardStats(): Result<AdminStatsResponse>

    // User Management
    suspend fun getUsers(page: Int = 1, limit: Int = 50): Result<AdminUserListResponse>
    suspend fun grantPremium(userId: Int, subscriptionType: String, days: Int): Result<Unit>
    suspend fun banUser(userId: Int, reason: String): Result<Unit>
    suspend fun deleteUser(userId: Int): Result<Unit>

    // Daily Pulse / Content Management
    suspend fun getDailyPulseStats(): Result<DailyPulseStatsResponse>
    suspend fun getPendingPulses(): Result<PendingPulseResponse>
    suspend fun getAllPulses(): Result<List<PulseItemDto>>
    suspend fun publishPulse(pulseId: Int, sendPush: Boolean = true): Result<PublishPulseResponse>
    suspend fun deletePulse(pulseId: String): Result<Unit>
    suspend fun generateDailyPulse(): Result<GeneratePulseResponse>

    // AI Tutor Stats
    suspend fun getAITutorStats(): Result<AITutorStatsResponse>

    // Notifications
    suspend fun sendNotification(title: String, body: String, topic: String): Result<SendNotificationResponse>

    // Admin Settings
    suspend fun getFeatureFlags(): Result<FeatureFlagsResponse>
    suspend fun updateFeatureFlag(flag: String, enabled: Boolean): Result<Unit>
    suspend fun getEcsServices(): Result<EcsServicesResponse>
    suspend fun getDatabaseStats(): Result<DatabaseStatsResponse>
    suspend fun getSystemLogs(limit: Int = 100): Result<List<SystemLogDto>>
    suspend fun getErrorReports(limit: Int = 50): Result<List<ErrorReportDto>>

    // Enterprise Team Management
    suspend fun getTeamMembers(): Result<TeamMembersResponse>
    suspend fun inviteTeamMember(email: String, role: String): Result<TeamInviteResponse>
    suspend fun getTeamInvites(): Result<TeamInvitesResponse>
    suspend fun cancelInvite(inviteId: Int): Result<Unit>
    suspend fun updateMemberRole(memberId: Int, role: String): Result<Unit>
    suspend fun removeTeamMember(memberId: Int): Result<Unit>
    suspend fun getTeamAuditLog(limit: Int = 100): Result<TeamAuditLogResponse>
}
