package com.swiftquantum.data.dto

import com.swiftquantum.domain.model.User
import com.swiftquantum.domain.model.UserStats
import com.swiftquantum.domain.model.UserTier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)

@Serializable
data class AuthResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    val user: UserDto
)

/**
 * UserDto - Backend UserResponse와 매칭
 * v5.4.1: is_premium, subscription_type, subscription_tier, is_admin 필드 추가
 */
@Serializable
data class UserDto(
    val id: String,
    val email: String,
    val name: String,
    val tier: String = "free",
    @SerialName("is_admin")
    val isAdmin: Boolean = false,
    @SerialName("is_premium")
    val isPremium: Boolean = false,
    @SerialName("subscription_type")
    val subscriptionType: String? = null,
    @SerialName("subscription_tier")
    val subscriptionTier: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    val stats: UserStatsDto? = null
) {
    /**
     * v5.4.1: PRO 사용자 여부 확인
     */
    val isPro: Boolean
        get() {
            if (isAdmin) return true
            if (isPremium) return true
            val proTiers = listOf("pro", "master", "scholar", "career", "premium")
            if (proTiers.contains(tier.lowercase())) return true
            if (subscriptionType?.lowercase()?.let { proTiers.contains(it) } == true) return true
            if (subscriptionTier?.lowercase()?.let { proTiers.contains(it) } == true) return true
            return false
        }

    fun toDomain(): User = User(
        id = id,
        email = email,
        name = name,
        tier = try { UserTier.valueOf(tier.uppercase()) } catch (e: Exception) { UserTier.FREE },
        createdAt = createdAt,
        updatedAt = updatedAt,
        stats = stats?.toDomain()
    )
}

@Serializable
data class UserStatsDto(
    @SerialName("simulations_run")
    val simulationsRun: Int = 0,
    @SerialName("total_qubits_used")
    val totalQubitsUsed: Long = 0,
    @SerialName("hardware_jobs")
    val hardwareJobs: Int = 0,
    @SerialName("saved_circuits")
    val savedCircuits: Int = 0
) {
    fun toDomain(): UserStats = UserStats(
        simulationsRun = simulationsRun,
        totalQubitsUsed = totalQubitsUsed,
        hardwareJobs = hardwareJobs,
        savedCircuits = savedCircuits
    )
}

@Serializable
data class RefreshTokenRequest(
    @SerialName("refresh_token")
    val refreshToken: String
)

@Serializable
data class RefreshTokenResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String? = null
)

@Serializable
data class ForgotPasswordRequest(
    val email: String
)

@Serializable
data class ResetPasswordRequest(
    val token: String,
    @SerialName("new_password")
    val newPassword: String
)

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val email: String? = null
)

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: String? = null
)

@Serializable
data class MessageResponse(
    val message: String
)
