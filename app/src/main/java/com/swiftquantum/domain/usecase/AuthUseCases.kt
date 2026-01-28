package com.swiftquantum.domain.usecase

import com.swiftquantum.domain.model.User
import com.swiftquantum.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email cannot be empty"))
        }
        if (!email.contains("@")) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password cannot be empty"))
        }
        if (password.length < 8) {
            return Result.failure(IllegalArgumentException("Password must be at least 8 characters"))
        }
        return authRepository.login(email, password)
    }
}

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String,
        name: String
    ): Result<User> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email cannot be empty"))
        }
        if (!email.contains("@")) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password cannot be empty"))
        }
        if (password.length < 8) {
            return Result.failure(IllegalArgumentException("Password must be at least 8 characters"))
        }
        if (password != confirmPassword) {
            return Result.failure(IllegalArgumentException("Passwords do not match"))
        }
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Name cannot be empty"))
        }
        return authRepository.register(email, password, name)
    }
}

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.currentUser
}

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email cannot be empty"))
        }
        if (!email.contains("@")) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }
        return authRepository.forgotPassword(email)
    }
}

class UpdateProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String?, email: String?): Result<User> {
        if (email != null && !email.contains("@")) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }
        return authRepository.updateProfile(name, email)
    }
}
