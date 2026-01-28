package com.swiftquantum.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val tier: UserTier = UserTier.FREE,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val stats: UserStats? = null
)

@Serializable
enum class UserTier {
    FREE,
    PRO,
    MASTER;

    val maxQubits: Int
        get() = when (this) {
            FREE -> 20
            PRO -> 30
            MASTER -> 40
        }

    val hasHardwareAccess: Boolean
        get() = this == MASTER

    val displayName: String
        get() = name
}

@Serializable
data class UserStats(
    val simulationsRun: Int = 0,
    val totalQubitsUsed: Long = 0,
    val hardwareJobs: Int = 0,
    val savedCircuits: Int = 0
)

data class AuthState(
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)
