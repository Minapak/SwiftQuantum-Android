package com.swiftquantum.data.api

import com.swiftquantum.data.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Admin API Interface
 * Backend endpoints for admin dashboard, user management, content management, and team management
 */
interface AdminApi {

    // ============================================
    // Dashboard Stats
    // ============================================

    @GET("admin/stats")
    suspend fun getDashboardStats(): Response<ApiResponse<AdminStatsResponse>>

    // ============================================
    // User Management
    // ============================================

    @GET("admin/users")
    suspend fun getUsers(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<ApiResponse<AdminUserListResponse>>

    @POST("admin/users/grant-premium")
    suspend fun grantPremium(
        @Body request: GrantPremiumRequest
    ): Response<ApiResponse<MessageResponse>>

    @POST("admin/users/ban")
    suspend fun banUser(
        @Body request: BanUserRequest
    ): Response<ApiResponse<MessageResponse>>

    @DELETE("admin/users/{userId}")
    suspend fun deleteUser(
        @Path("userId") userId: Int
    ): Response<ApiResponse<MessageResponse>>

    // ============================================
    // Daily Pulse / Content Management
    // ============================================

    @GET("admin/daily-pulse/stats")
    suspend fun getDailyPulseStats(): Response<ApiResponse<DailyPulseStatsResponse>>

    @GET("admin/daily-pulse/pending")
    suspend fun getPendingPulses(): Response<ApiResponse<PendingPulseResponse>>

    @GET("admin/pulses")
    suspend fun getAllPulses(): Response<ApiResponse<List<PulseItemDto>>>

    @POST("admin/daily-pulse/{pulseId}/publish")
    suspend fun publishPulse(
        @Path("pulseId") pulseId: Int,
        @Body request: PublishPulseRequest
    ): Response<ApiResponse<PublishPulseResponse>>

    @DELETE("admin/pulses/{pulseId}")
    suspend fun deletePulse(
        @Path("pulseId") pulseId: String
    ): Response<ApiResponse<MessageResponse>>

    @POST("admin/daily-pulse/generate")
    suspend fun generateDailyPulse(): Response<ApiResponse<GeneratePulseResponse>>

    // ============================================
    // AI Tutor Stats
    // ============================================

    @GET("admin/ai-tutor/stats")
    suspend fun getAITutorStats(): Response<ApiResponse<AITutorStatsResponse>>

    // ============================================
    // Notifications
    // ============================================

    @POST("admin/notifications/send")
    suspend fun sendNotification(
        @Body request: SendNotificationRequest
    ): Response<ApiResponse<SendNotificationResponse>>

    // ============================================
    // Admin Settings
    // ============================================

    @GET("admin/feature-flags")
    suspend fun getFeatureFlags(): Response<ApiResponse<FeatureFlagsResponse>>

    @POST("admin/feature-flags")
    suspend fun updateFeatureFlag(
        @Body request: UpdateFeatureFlagRequest
    ): Response<ApiResponse<MessageResponse>>

    @GET("admin/aws/ecs/services")
    suspend fun getEcsServices(): Response<ApiResponse<EcsServicesResponse>>

    @GET("admin/database-stats")
    suspend fun getDatabaseStats(): Response<ApiResponse<DatabaseStatsResponse>>

    @GET("admin/system-logs")
    suspend fun getSystemLogs(
        @Query("limit") limit: Int = 100
    ): Response<ApiResponse<List<SystemLogDto>>>

    @GET("admin/error-reports")
    suspend fun getErrorReports(
        @Query("limit") limit: Int = 50
    ): Response<ApiResponse<List<ErrorReportDto>>>

    // ============================================
    // Enterprise Team Management
    // ============================================

    @GET("admin/team/members")
    suspend fun getTeamMembers(): Response<ApiResponse<TeamMembersResponse>>

    @POST("admin/team/invite")
    suspend fun inviteTeamMember(
        @Body request: TeamInviteRequest
    ): Response<ApiResponse<TeamInviteResponse>>

    @GET("admin/team/invites")
    suspend fun getTeamInvites(): Response<ApiResponse<TeamInvitesResponse>>

    @DELETE("admin/team/invites/{inviteId}")
    suspend fun cancelInvite(
        @Path("inviteId") inviteId: Int
    ): Response<ApiResponse<MessageResponse>>

    @POST("admin/team/members/role")
    suspend fun updateMemberRole(
        @Body request: UpdateMemberRoleRequest
    ): Response<ApiResponse<MessageResponse>>

    @DELETE("admin/team/members/{memberId}")
    suspend fun removeTeamMember(
        @Path("memberId") memberId: Int
    ): Response<ApiResponse<MessageResponse>>

    @GET("admin/team/audit-log")
    suspend fun getTeamAuditLog(
        @Query("limit") limit: Int = 100
    ): Response<ApiResponse<TeamAuditLogResponse>>
}
