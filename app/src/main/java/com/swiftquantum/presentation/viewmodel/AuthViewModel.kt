package com.swiftquantum.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swiftquantum.domain.model.User
import com.swiftquantum.domain.usecase.ForgotPasswordUseCase
import com.swiftquantum.domain.usecase.GetCurrentUserUseCase
import com.swiftquantum.domain.usecase.LoginUseCase
import com.swiftquantum.domain.usecase.LogoutUseCase
import com.swiftquantum.domain.usecase.RegisterUseCase
import com.swiftquantum.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

sealed class AuthEvent {
    data object LoginSuccess : AuthEvent()
    data object RegisterSuccess : AuthEvent()
    data object LogoutSuccess : AuthEvent()
    data class Error(val message: String) : AuthEvent()
    data object PasswordResetSent : AuthEvent()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>()
    val events: SharedFlow<AuthEvent> = _events.asSharedFlow()

    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collectLatest { user ->
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = user != null,
                    user = user
                )
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            loginUseCase(email, password)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        user = user
                    )
                    _events.emit(AuthEvent.LoginSuccess)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                    _events.emit(AuthEvent.Error(error.message ?: "Login failed"))
                }
        }
    }

    fun register(email: String, password: String, confirmPassword: String, name: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            registerUseCase(email, password, confirmPassword, name)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        user = user
                    )
                    _events.emit(AuthEvent.RegisterSuccess)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                    _events.emit(AuthEvent.Error(error.message ?: "Registration failed"))
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            logoutUseCase()
                .onSuccess {
                    _uiState.value = AuthUiState()
                    _events.emit(AuthEvent.LogoutSuccess)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                    _events.emit(AuthEvent.Error(error.message ?: "Logout failed"))
                }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            forgotPasswordUseCase(email)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _events.emit(AuthEvent.PasswordResetSent)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                    _events.emit(AuthEvent.Error(error.message ?: "Failed to send reset email"))
                }
        }
    }

    fun updateProfile(name: String?, email: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            updateProfileUseCase(name, email)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = user
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                    _events.emit(AuthEvent.Error(error.message ?: "Failed to update profile"))
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
