package com.swiftquantum.presentation.viewmodel

import android.app.Application
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swiftquantum.data.auth.SharedAuthManager
import com.swiftquantum.data.local.UserPreferences
import com.swiftquantum.domain.model.User
import com.swiftquantum.domain.model.UserTier
import com.swiftquantum.domain.usecase.GetCurrentUserUseCase
import com.swiftquantum.domain.usecase.LogoutUseCase
import com.swiftquantum.domain.usecase.ObserveUserTierUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

data class SettingsUiState(
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val userTier: UserTier = UserTier.FREE,
    val currentLanguage: String = "en",
    val subscriptionEndDate: String? = null,
    val currentEngine: String = "Rust",
    val availableEngines: List<String> = listOf("Rust", "Python", "C++ HPC"),
    val appVersion: String = "1.0.0",
    val buildNumber: String = "1",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class SettingsEvent {
    data object LoggedOut : SettingsEvent()
    data class LanguageChanged(val languageCode: String) : SettingsEvent()
    data class Error(val message: String) : SettingsEvent()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val observeUserTierUseCase: ObserveUserTierUseCase,
    private val sharedAuthManager: SharedAuthManager,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SettingsEvent>()
    val events: SharedFlow<SettingsEvent> = _events.asSharedFlow()

    init {
        loadSettings()
        observeUser()
        observeUserTier()
        observeAuthState()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            // Load app version from package info
            try {
                val packageInfo = application.packageManager.getPackageInfo(application.packageName, 0)
                val versionName = packageInfo.versionName ?: "1.0.0"
                val versionCode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    packageInfo.longVersionCode.toString()
                } else {
                    @Suppress("DEPRECATION")
                    packageInfo.versionCode.toString()
                }

                _uiState.value = _uiState.value.copy(
                    appVersion = versionName,
                    buildNumber = versionCode
                )
            } catch (e: Exception) {
                // Use defaults
            }

            // Load current language
            val savedLanguage = userPreferences.getLanguage()
            _uiState.value = _uiState.value.copy(
                currentLanguage = savedLanguage ?: Locale.getDefault().language
            )

            // Load available engines based on tier
            updateAvailableEngines()
        }
    }

    private fun observeUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collectLatest { user ->
                _uiState.value = _uiState.value.copy(user = user)
            }
        }
    }

    private fun observeUserTier() {
        viewModelScope.launch {
            observeUserTierUseCase().collectLatest { tier ->
                _uiState.value = _uiState.value.copy(userTier = tier)
                updateAvailableEngines()
                updateCurrentEngine(tier)
            }
        }
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            sharedAuthManager.observeAuthState().collectLatest { authData ->
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = authData.isLoggedIn
                )
            }
        }
    }

    private fun updateAvailableEngines() {
        val tier = _uiState.value.userTier
        val engines = when (tier) {
            UserTier.FREE -> listOf("Python")
            UserTier.PRO -> listOf("Python", "Rust")
            UserTier.MASTER -> listOf("Python", "Rust", "C++ HPC", "IBM Quantum")
        }
        _uiState.value = _uiState.value.copy(availableEngines = engines)
    }

    private fun updateCurrentEngine(tier: UserTier) {
        val currentEngine = when (tier) {
            UserTier.FREE -> "Python"
            UserTier.PRO -> "Rust"
            UserTier.MASTER -> "C++ HPC"
        }
        _uiState.value = _uiState.value.copy(currentEngine = currentEngine)
    }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            try {
                // Save language preference
                userPreferences.setLanguage(languageCode)

                // Update locale
                val locale = Locale(languageCode)
                Locale.setDefault(locale)

                val config = Configuration(application.resources.configuration)
                config.setLocale(locale)

                @Suppress("DEPRECATION")
                application.resources.updateConfiguration(config, application.resources.displayMetrics)

                _uiState.value = _uiState.value.copy(currentLanguage = languageCode)
                _events.emit(SettingsEvent.LanguageChanged(languageCode))
            } catch (e: Exception) {
                _events.emit(SettingsEvent.Error(e.message ?: "Failed to change language"))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Clear shared auth
                sharedAuthManager.clearAuth()

                // Call API logout
                logoutUseCase()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = false,
                    user = null
                )
                _events.emit(SettingsEvent.LoggedOut)
            } catch (e: Exception) {
                // Even if API logout fails, clear local auth
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = false,
                    user = null
                )
                _events.emit(SettingsEvent.LoggedOut)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
