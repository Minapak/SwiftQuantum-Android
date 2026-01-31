package com.swiftquantum.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ============================================
// Admin Dashboard Stats
// ============================================

@Serializable
data class AdminStatsResponse(
    @SerialName("total_users")
    val totalUsers: Int = 0,
    @SerialName("active_users")
    val activeUsers: Int = 0,
    @SerialName("premium_users")
    val premiumUsers: Int = 0,
    @SerialName("new_users_today")
    val newUsersToday: Int = 0,
    @SerialName("monthly_revenue")
    val monthlyRevenue: Double = 0.0,
    @SerialName("api_requests_today")
    val apiRequestsToday: Int = 0,
    @SerialName("error_rate")
    val errorRate: Double = 0.0,
    @SerialName("avg_response_time")
    val avgResponseTime: Double = 0.0
)

// ============================================
// Admin Users
// ============================================

@Serializable
data class AdminUserListResponse(
    val users: List<AdminUserDto>,
    val total: Int,
    val page: Int,
    val limit: Int
)

@Serializable
data class AdminUserDto(
    val id: Int,
    val email: String? = null,
    val username: String? = null,
    @SerialName("is_active")
    val isActive: Boolean = true,
    @SerialName("is_admin")
    val isAdmin: Boolean = false,
    @SerialName("subscription_type")
    val subscriptionType: String? = null,
    @SerialName("subscription_expires_at")
    val subscriptionExpiresAt: String? = null,
    @SerialName("total_xp")
    val totalXp: Int = 0,
    @SerialName("current_level")
    val currentLevel: Int = 1,
    @SerialName("current_streak")
    val currentStreak: Int = 0,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class GrantPremiumRequest(
    @SerialName("user_id")
    val userId: Int,
    @SerialName("subscription_type")
    val subscriptionType: String,
    val days: Int
)

@Serializable
data class BanUserRequest(
    @SerialName("user_id")
    val userId: Int,
    val reason: String
)

// ============================================
// Admin Daily Pulse / Content
// ============================================

@Serializable
data class DailyPulseStatsResponse(
    @SerialName("total_pulses")
    val totalPulses: Int = 0,
    val published: Int = 0,
    @SerialName("pending_approval")
    val pendingApproval: Int = 0,
    @SerialName("total_views")
    val totalViews: Int = 0,
    @SerialName("total_quiz_attempts")
    val totalQuizAttempts: Int = 0,
    @SerialName("total_quiz_correct")
    val totalQuizCorrect: Int = 0,
    @SerialName("quiz_accuracy")
    val quizAccuracy: Double = 0.0
)

@Serializable
data class PendingPulseResponse(
    @SerialName("pending_count")
    val pendingCount: Int = 0,
    val pulses: List<PendingPulseDto> = emptyList()
)

@Serializable
data class PendingPulseDto(
    val id: Int,
    val title: String,
    val summary: String,
    val impact: String? = null,
    @SerialName("source_url")
    val sourceUrl: String? = null,
    @SerialName("quiz_question")
    val quizQuestion: String? = null,
    @SerialName("ai_model")
    val aiModel: String? = null,
    @SerialName("ai_confidence")
    val aiConfidence: Double? = null,
    @SerialName("target_date")
    val targetDate: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class PulseItemDto(
    val id: String,
    val title: String,
    val summary: String,
    val content: String = "",
    val category: String = "",
    @SerialName("is_active")
    val isActive: Boolean = false,
    @SerialName("scheduled_date")
    val scheduledDate: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class PublishPulseRequest(
    @SerialName("send_push")
    val sendPush: Boolean = true
)

@Serializable
data class PublishPulseResponse(
    val success: Boolean = false,
    val message: String? = null,
    @SerialName("push_sent")
    val pushSent: Boolean? = null
)

@Serializable
data class GeneratePulseResponse(
    val success: Boolean = false,
    @SerialName("generated_count")
    val generatedCount: Int? = null,
    val message: String? = null
)

// ============================================
// Admin AI Tutor Stats
// ============================================

@Serializable
data class AITutorStatsResponse(
    @SerialName("today_conversations")
    val todayConversations: Int = 0,
    @SerialName("today_active_users")
    val todayActiveUsers: Int = 0,
    @SerialName("total_conversations")
    val totalConversations: Int = 0,
    @SerialName("total_tokens_used")
    val totalTokensUsed: Int = 0,
    @SerialName("estimated_cost_usd")
    val estimatedCostUsd: Double = 0.0
)

// ============================================
// Admin Notifications
// ============================================

@Serializable
data class SendNotificationRequest(
    val title: String,
    val body: String,
    val topic: String
)

@Serializable
data class SendNotificationResponse(
    val success: Boolean = false,
    val message: String? = null
)

// ============================================
// Admin Settings
// ============================================

@Serializable
data class FeatureFlagsResponse(
    val flags: Map<String, Boolean> = emptyMap()
)

@Serializable
data class UpdateFeatureFlagRequest(
    val flag: String,
    val enabled: Boolean
)

@Serializable
data class EcsServicesResponse(
    val services: List<EcsServiceDto> = emptyList()
)

@Serializable
data class EcsServiceDto(
    val name: String,
    val status: String,
    @SerialName("running_count")
    val runningCount: Int = 0,
    @SerialName("desired_count")
    val desiredCount: Int = 0
)

@Serializable
data class DatabaseStatsResponse(
    @SerialName("total_connections")
    val totalConnections: Int = 0,
    @SerialName("active_connections")
    val activeConnections: Int = 0,
    @SerialName("database_size")
    val databaseSize: String? = null
)

@Serializable
data class SystemLogDto(
    val id: Int,
    val level: String,
    val message: String,
    val timestamp: String,
    val source: String? = null
)

@Serializable
data class ErrorReportDto(
    val id: Int,
    val error: String,
    val stackTrace: String? = null,
    val userId: Int? = null,
    val timestamp: String,
    val resolved: Boolean = false
)

// ============================================
// Enterprise Team
// ============================================

@Serializable
data class TeamMembersResponse(
    val members: List<TeamMemberDto> = emptyList(),
    val total: Int = 0
)

@Serializable
data class TeamMemberDto(
    val id: Int,
    val email: String,
    val name: String,
    val role: String,
    @SerialName("joined_at")
    val joinedAt: String? = null,
    @SerialName("last_active")
    val lastActive: String? = null
)

@Serializable
data class TeamInviteRequest(
    val email: String,
    val role: String
)

@Serializable
data class TeamInviteResponse(
    val success: Boolean = false,
    val message: String? = null,
    @SerialName("invite_code")
    val inviteCode: String? = null
)

@Serializable
data class TeamInvitesResponse(
    val invites: List<TeamInviteDto> = emptyList()
)

@Serializable
data class TeamInviteDto(
    val id: Int,
    val email: String,
    val role: String,
    val status: String,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("expires_at")
    val expiresAt: String? = null
)

@Serializable
data class TeamAuditLogResponse(
    val logs: List<TeamAuditLogDto> = emptyList()
)

@Serializable
data class TeamAuditLogDto(
    val id: Int,
    val action: String,
    @SerialName("performed_by")
    val performedBy: String,
    @SerialName("target_user")
    val targetUser: String? = null,
    val details: String? = null,
    val timestamp: String
)

@Serializable
data class UpdateMemberRoleRequest(
    @SerialName("member_id")
    val memberId: Int,
    val role: String
)
