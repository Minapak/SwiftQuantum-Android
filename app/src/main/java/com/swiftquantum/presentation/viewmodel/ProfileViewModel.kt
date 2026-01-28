package com.swiftquantum.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swiftquantum.data.auth.AuthData
import com.swiftquantum.data.auth.SharedAuthManager
import com.swiftquantum.domain.model.Subscription
import com.swiftquantum.domain.model.SubscriptionProduct
import com.swiftquantum.domain.model.User
import com.swiftquantum.domain.model.UserTier
import com.swiftquantum.domain.usecase.GetCurrentSubscriptionUseCase
import com.swiftquantum.domain.usecase.GetCurrentUserUseCase
import com.swiftquantum.domain.usecase.GetSubscriptionProductsUseCase
import com.swiftquantum.domain.usecase.LogoutUseCase
import com.swiftquantum.domain.usecase.ObserveSubscriptionUseCase
import com.swiftquantum.domain.usecase.ObserveUserTierUseCase
import com.swiftquantum.domain.usecase.PurchaseSubscriptionUseCase
import com.swiftquantum.domain.usecase.RestorePurchasesUseCase
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

data class ProfileUiState(
    val user: User? = null,
    val userTier: UserTier = UserTier.FREE,
    val subscription: Subscription? = null,
    val products: List<SubscriptionProduct> = emptyList(),
    val isLoading: Boolean = false,
    val isPurchasing: Boolean = false,
    val showUpgradeDialog: Boolean = false,
    val error: String? = null,
    // Shared auth state
    val isLoggedIn: Boolean = false,
    val authData: AuthData? = null,
    val showLogoutDialog: Boolean = false
)

sealed class ProfileEvent {
    data object LoggedOut : ProfileEvent()
    data object PurchaseSuccess : ProfileEvent()
    data object RestoreSuccess : ProfileEvent()
    data class Error(val message: String) : ProfileEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val observeUserTierUseCase: ObserveUserTierUseCase,
    private val observeSubscriptionUseCase: ObserveSubscriptionUseCase,
    private val getSubscriptionProductsUseCase: GetSubscriptionProductsUseCase,
    private val purchaseSubscriptionUseCase: PurchaseSubscriptionUseCase,
    private val restorePurchasesUseCase: RestorePurchasesUseCase,
    private val getCurrentSubscriptionUseCase: GetCurrentSubscriptionUseCase,
    private val sharedAuthManager: SharedAuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProfileEvent>()
    val events: SharedFlow<ProfileEvent> = _events.asSharedFlow()

    init {
        observeUser()
        observeUserTier()
        observeSubscription()
        observeAuthState()
        loadProducts()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            sharedAuthManager.observeAuthState().collectLatest { authData ->
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = authData.isLoggedIn,
                    authData = authData
                )
            }
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
            }
        }
    }

    private fun observeSubscription() {
        viewModelScope.launch {
            observeSubscriptionUseCase().collectLatest { subscription ->
                _uiState.value = _uiState.value.copy(subscription = subscription)
            }
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getSubscriptionProductsUseCase()
                .onSuccess { products ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        products = products
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
        }
    }

    fun loadCurrentSubscription() {
        viewModelScope.launch {
            getCurrentSubscriptionUseCase()
                .onSuccess { subscription ->
                    _uiState.value = _uiState.value.copy(subscription = subscription)
                }
        }
    }

    fun showUpgradeDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showUpgradeDialog = show)
    }

    fun purchaseSubscription(productId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPurchasing = true, error = null)

            purchaseSubscriptionUseCase(productId)
                .onSuccess { subscription ->
                    _uiState.value = _uiState.value.copy(
                        isPurchasing = false,
                        subscription = subscription,
                        showUpgradeDialog = false
                    )
                    _events.emit(ProfileEvent.PurchaseSuccess)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isPurchasing = false,
                        error = error.message
                    )
                    _events.emit(ProfileEvent.Error(error.message ?: "Purchase failed"))
                }
        }
    }

    fun restorePurchases() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            restorePurchasesUseCase()
                .onSuccess { subscriptions ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        subscription = subscriptions.firstOrNull()
                    )
                    _events.emit(ProfileEvent.RestoreSuccess)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                    _events.emit(ProfileEvent.Error(error.message ?: "Restore failed"))
                }
        }
    }

    fun showLogoutDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showLogoutDialog = show)
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Clear shared auth (affects all apps)
            sharedAuthManager.clearAuth()

            // Also call the API logout
            logoutUseCase()
                .onSuccess {
                    _uiState.value = ProfileUiState()
                    _events.emit(ProfileEvent.LoggedOut)
                }
                .onFailure { error ->
                    // Even if API logout fails, we've cleared local auth
                    _uiState.value = ProfileUiState()
                    _events.emit(ProfileEvent.LoggedOut)
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
