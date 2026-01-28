package com.swiftquantum.domain.repository

import com.swiftquantum.domain.model.AuthState
import com.swiftquantum.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<AuthState>
    val isAuthenticated: Flow<Boolean>
    val currentUser: Flow<User?>

    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String, name: String): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun refreshToken(): Result<String>
    suspend fun forgotPassword(email: String): Result<Unit>
    suspend fun resetPassword(token: String, newPassword: String): Result<Unit>
    suspend fun updateProfile(name: String?, email: String?): Result<User>
    suspend fun deleteAccount(): Result<Unit>
    suspend fun getStoredToken(): String?
    suspend fun clearStoredToken()
}
