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

/**
 * UserTier - Backend tier와 매칭
 * v5.4.1: SCHOLAR, CAREER 티어 추가 (QuantumNative Edu 플랜)
 */
@Serializable
enum class UserTier {
    FREE,
    PRO,
    MASTER,
    SCHOLAR,  // v5.4.1: QuantumNative Edu
    CAREER;   // v5.4.1: QuantumNative Edu Pro

    val maxQubits: Int
        get() = when (this) {
            FREE -> 20
            PRO, SCHOLAR -> 30
            MASTER, CAREER -> 40
        }

    val hasHardwareAccess: Boolean
        get() = this == MASTER || this == CAREER

    val displayName: String
        get() = name

    /**
     * v5.4.1: PRO급 이상인지 확인
     */
    val isPro: Boolean
        get() = this != FREE
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
