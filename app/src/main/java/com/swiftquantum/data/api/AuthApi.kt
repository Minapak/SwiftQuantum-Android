package com.swiftquantum.data.api

import com.swiftquantum.data.dto.ApiResponse
import com.swiftquantum.data.dto.AuthResponse
import com.swiftquantum.data.dto.ForgotPasswordRequest
import com.swiftquantum.data.dto.LoginRequest
import com.swiftquantum.data.dto.MessageResponse
import com.swiftquantum.data.dto.RefreshTokenRequest
import com.swiftquantum.data.dto.RefreshTokenResponse
import com.swiftquantum.data.dto.RegisterRequest
import com.swiftquantum.data.dto.ResetPasswordRequest
import com.swiftquantum.data.dto.UpdateProfileRequest
import com.swiftquantum.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth/logout")
    suspend fun logout(): Response<ApiResponse<MessageResponse>>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<ApiResponse<RefreshTokenResponse>>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ApiResponse<MessageResponse>>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ApiResponse<MessageResponse>>

    @GET("auth/me")
    suspend fun getCurrentUser(): Response<ApiResponse<UserDto>>

    @PATCH("auth/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<ApiResponse<UserDto>>

    @DELETE("auth/account")
    suspend fun deleteAccount(): Response<ApiResponse<MessageResponse>>
}
