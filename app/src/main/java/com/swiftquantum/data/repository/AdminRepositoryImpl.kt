package com.swiftquantum.data.repository

import com.swiftquantum.data.api.AdminApi
import com.swiftquantum.data.dto.*
import com.swiftquantum.domain.repository.AdminRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepositoryImpl @Inject constructor(
    private val adminApi: AdminApi
) : AdminRepository {

    // ============================================
    // Dashboard Stats
    // ============================================

    override suspend fun getDashboardStats(): Result<AdminStatsResponse> {
        return try {
            val response = adminApi.getDashboardStats()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: AdminStatsResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get dashboard stats"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get dashboard stats")
            Result.failure(e)
        }
    }

    // ============================================
    // User Management
    // ============================================

    override suspend fun getUsers(page: Int, limit: Int): Result<AdminUserListResponse> {
        return try {
            val response = adminApi.getUsers(page, limit)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: AdminUserListResponse(emptyList(), 0, 1, limit))
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get users"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get users")
            Result.failure(e)
        }
    }

    override suspend fun grantPremium(userId: Int, subscriptionType: String, days: Int): Result<Unit> {
        return try {
            val response = adminApi.grantPremium(GrantPremiumRequest(userId, subscriptionType, days))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to grant premium"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to grant premium")
            Result.failure(e)
        }
    }

    override suspend fun banUser(userId: Int, reason: String): Result<Unit> {
        return try {
            val response = adminApi.banUser(BanUserRequest(userId, reason))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to ban user"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to ban user")
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(userId: Int): Result<Unit> {
        return try {
            val response = adminApi.deleteUser(userId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to delete user"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to delete user")
            Result.failure(e)
        }
    }

    // ============================================
    // Daily Pulse / Content Management
    // ============================================

    override suspend fun getDailyPulseStats(): Result<DailyPulseStatsResponse> {
        return try {
            val response = adminApi.getDailyPulseStats()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: DailyPulseStatsResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get pulse stats"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get pulse stats")
            Result.failure(e)
        }
    }

    override suspend fun getPendingPulses(): Result<PendingPulseResponse> {
        return try {
            val response = adminApi.getPendingPulses()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: PendingPulseResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get pending pulses"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get pending pulses")
            Result.failure(e)
        }
    }

    override suspend fun getAllPulses(): Result<List<PulseItemDto>> {
        return try {
            val response = adminApi.getAllPulses()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get pulses"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get pulses")
            Result.failure(e)
        }
    }

    override suspend fun publishPulse(pulseId: Int, sendPush: Boolean): Result<PublishPulseResponse> {
        return try {
            val response = adminApi.publishPulse(pulseId, PublishPulseRequest(sendPush))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: PublishPulseResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to publish pulse"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to publish pulse")
            Result.failure(e)
        }
    }

    override suspend fun deletePulse(pulseId: String): Result<Unit> {
        return try {
            val response = adminApi.deletePulse(pulseId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to delete pulse"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to delete pulse")
            Result.failure(e)
        }
    }

    override suspend fun generateDailyPulse(): Result<GeneratePulseResponse> {
        return try {
            val response = adminApi.generateDailyPulse()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: GeneratePulseResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to generate pulse"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to generate pulse")
            Result.failure(e)
        }
    }

    // ============================================
    // AI Tutor Stats
    // ============================================

    override suspend fun getAITutorStats(): Result<AITutorStatsResponse> {
        return try {
            val response = adminApi.getAITutorStats()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: AITutorStatsResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get AI tutor stats"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get AI tutor stats")
            Result.failure(e)
        }
    }

    // ============================================
    // Notifications
    // ============================================

    override suspend fun sendNotification(title: String, body: String, topic: String): Result<SendNotificationResponse> {
        return try {
            val response = adminApi.sendNotification(SendNotificationRequest(title, body, topic))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: SendNotificationResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to send notification"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to send notification")
            Result.failure(e)
        }
    }

    // ============================================
    // Admin Settings
    // ============================================

    override suspend fun getFeatureFlags(): Result<FeatureFlagsResponse> {
        return try {
            val response = adminApi.getFeatureFlags()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: FeatureFlagsResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get feature flags"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get feature flags")
            Result.failure(e)
        }
    }

    override suspend fun updateFeatureFlag(flag: String, enabled: Boolean): Result<Unit> {
        return try {
            val response = adminApi.updateFeatureFlag(UpdateFeatureFlagRequest(flag, enabled))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to update feature flag"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to update feature flag")
            Result.failure(e)
        }
    }

    override suspend fun getEcsServices(): Result<EcsServicesResponse> {
        return try {
            val response = adminApi.getEcsServices()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: EcsServicesResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get ECS services"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get ECS services")
            Result.failure(e)
        }
    }

    override suspend fun getDatabaseStats(): Result<DatabaseStatsResponse> {
        return try {
            val response = adminApi.getDatabaseStats()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: DatabaseStatsResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get database stats"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get database stats")
            Result.failure(e)
        }
    }

    override suspend fun getSystemLogs(limit: Int): Result<List<SystemLogDto>> {
        return try {
            val response = adminApi.getSystemLogs(limit)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get system logs"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get system logs")
            Result.failure(e)
        }
    }

    override suspend fun getErrorReports(limit: Int): Result<List<ErrorReportDto>> {
        return try {
            val response = adminApi.getErrorReports(limit)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get error reports"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get error reports")
            Result.failure(e)
        }
    }

    // ============================================
    // Enterprise Team Management
    // ============================================

    override suspend fun getTeamMembers(): Result<TeamMembersResponse> {
        return try {
            val response = adminApi.getTeamMembers()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: TeamMembersResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get team members"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get team members")
            Result.failure(e)
        }
    }

    override suspend fun inviteTeamMember(email: String, role: String): Result<TeamInviteResponse> {
        return try {
            val response = adminApi.inviteTeamMember(TeamInviteRequest(email, role))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: TeamInviteResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to invite member"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to invite member")
            Result.failure(e)
        }
    }

    override suspend fun getTeamInvites(): Result<TeamInvitesResponse> {
        return try {
            val response = adminApi.getTeamInvites()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: TeamInvitesResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get invites"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get invites")
            Result.failure(e)
        }
    }

    override suspend fun cancelInvite(inviteId: Int): Result<Unit> {
        return try {
            val response = adminApi.cancelInvite(inviteId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to cancel invite"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to cancel invite")
            Result.failure(e)
        }
    }

    override suspend fun updateMemberRole(memberId: Int, role: String): Result<Unit> {
        return try {
            val response = adminApi.updateMemberRole(UpdateMemberRoleRequest(memberId, role))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to update role"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to update role")
            Result.failure(e)
        }
    }

    override suspend fun removeTeamMember(memberId: Int): Result<Unit> {
        return try {
            val response = adminApi.removeTeamMember(memberId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to remove member"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to remove member")
            Result.failure(e)
        }
    }

    override suspend fun getTeamAuditLog(limit: Int): Result<TeamAuditLogResponse> {
        return try {
            val response = adminApi.getTeamAuditLog(limit)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: TeamAuditLogResponse())
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Failed to get audit log"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get audit log")
            Result.failure(e)
        }
    }
}
