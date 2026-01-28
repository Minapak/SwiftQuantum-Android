package com.swiftquantum.data.repository

import com.swiftquantum.data.api.AuthApi
import com.swiftquantum.data.dto.ForgotPasswordRequest
import com.swiftquantum.data.dto.LoginRequest
import com.swiftquantum.data.dto.RefreshTokenRequest
import com.swiftquantum.data.dto.RegisterRequest
import com.swiftquantum.data.dto.ResetPasswordRequest
import com.swiftquantum.data.dto.UpdateProfileRequest
import com.swiftquantum.data.local.TokenManager
import com.swiftquantum.data.local.UserPreferences
import com.swiftquantum.domain.model.AuthState
import com.swiftquantum.domain.model.User
import com.swiftquantum.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager,
    private val userPreferences: UserPreferences
) : AuthRepository {

    private val _authState = MutableStateFlow(AuthState())
    override val authState: Flow<AuthState> = _authState.asStateFlow()

    override val isAuthenticated: Flow<Boolean> = tokenManager.accessToken.map { it != null }

    override val currentUser: Flow<User?> = authState.map { it.user }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body()?.success == true) {
                val authResponse = response.body()?.data!!
                val user = authResponse.user.toDomain()

                tokenManager.saveTokens(
                    accessToken = authResponse.accessToken,
                    refreshToken = authResponse.refreshToken,
                    userId = user.id
                )

                userPreferences.setUserTier(user.tier)

                _authState.value = AuthState(
                    isAuthenticated = true,
                    user = user,
                    accessToken = authResponse.accessToken,
                    refreshToken = authResponse.refreshToken
                )

                Result.success(user)
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Login failed"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Login failed")
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, name: String): Result<User> {
        return try {
            val response = authApi.register(RegisterRequest(email, password, name))
            if (response.isSuccessful && response.body()?.success == true) {
                val authResponse = response.body()?.data!!
                val user = authResponse.user.toDomain()

                tokenManager.saveTokens(
                    accessToken = authResponse.accessToken,
                    refreshToken = authResponse.refreshToken,
                    userId = user.id
                )

                userPreferences.setUserTier(user.tier)

                _authState.value = AuthState(
                    isAuthenticated = true,
                    user = user,
                    accessToken = authResponse.accessToken,
                    refreshToken = authResponse.refreshToken
                )

                Result.success(user)
            } else {
                val errorMessage = response.body()?.error ?: response.message() ?: "Registration failed"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Registration failed")
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            authApi.logout()
            tokenManager.clearTokens()
            userPreferences.setUserTier(com.swiftquantum.domain.model.UserTier.FREE)
            _authState.value = AuthState()
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Logout failed")
            // Clear tokens anyway on logout
            tokenManager.clearTokens()
            _authState.value = AuthState()
            Result.success(Unit)
        }
    }

    override suspend fun refreshToken(): Result<String> {
        return try {
            val currentRefreshToken = tokenManager.getRefreshToken()
                ?: return Result.failure(Exception("No refresh token available"))

            val response = authApi.refreshToken(RefreshTokenRequest(currentRefreshToken))
            if (response.isSuccessful && response.body()?.success == true) {
                val tokenResponse = response.body()?.data!!
                tokenManager.updateAccessToken(tokenResponse.accessToken)
                tokenResponse.refreshToken?.let { newRefreshToken ->
                    tokenManager.saveTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = newRefreshToken,
                        userId = _authState.value.user?.id ?: ""
                    )
                }
                Result.success(tokenResponse.accessToken)
            } else {
                Result.failure(Exception("Token refresh failed"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Token refresh failed")
            Result.failure(e)
        }
    }

    override suspend fun forgotPassword(email: String): Result<Unit> {
        return try {
            val response = authApi.forgotPassword(ForgotPasswordRequest(email))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error ?: "Failed to send reset email"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Forgot password failed")
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> {
        return try {
            val response = authApi.resetPassword(ResetPasswordRequest(token, newPassword))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error ?: "Password reset failed"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Reset password failed")
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(name: String?, email: String?): Result<User> {
        return try {
            val response = authApi.updateProfile(UpdateProfileRequest(name, email))
            if (response.isSuccessful && response.body()?.success == true) {
                val user = response.body()?.data?.toDomain()!!
                _authState.value = _authState.value.copy(user = user)
                Result.success(user)
            } else {
                val errorMessage = response.body()?.error ?: "Profile update failed"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Update profile failed")
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            val response = authApi.deleteAccount()
            if (response.isSuccessful && response.body()?.success == true) {
                tokenManager.clearTokens()
                userPreferences.clearAll()
                _authState.value = AuthState()
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error ?: "Account deletion failed"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Delete account failed")
            Result.failure(e)
        }
    }

    override suspend fun getStoredToken(): String? {
        return tokenManager.getAccessToken()
    }

    override suspend fun clearStoredToken() {
        tokenManager.clearTokens()
    }
}
