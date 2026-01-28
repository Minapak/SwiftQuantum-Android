package com.swiftquantum.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swiftquantum.domain.model.Subscription
import com.swiftquantum.domain.model.SubscriptionProduct
import com.swiftquantum.domain.model.UserTier
import com.swiftquantum.domain.usecase.GetSubscriptionProductsUseCase
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

data class PaywallUiState(
    val userTier: UserTier = UserTier.FREE,
    val currentSubscription: Subscription? = null,
    val products: List<SubscriptionProduct> = emptyList(),
    val isLoading: Boolean = false,
    val isPurchasing: Boolean = false,
    val isRestoring: Boolean = false,
    val error: String? = null,
    val purchaseSuccess: Boolean = false
)

sealed class PaywallEvent {
    data class PurchaseSuccess(val subscription: Subscription) : PaywallEvent()
    data object RestoreSuccess : PaywallEvent()
    data class Error(val message: String) : PaywallEvent()
    data object NoSubscriptionsToRestore : PaywallEvent()
}

@HiltViewModel
class PaywallViewModel @Inject constructor(
    private val getSubscriptionProductsUseCase: GetSubscriptionProductsUseCase,
    private val purchaseSubscriptionUseCase: PurchaseSubscriptionUseCase,
    private val restorePurchasesUseCase: RestorePurchasesUseCase,
    private val observeSubscriptionUseCase: ObserveSubscriptionUseCase,
    private val observeUserTierUseCase: ObserveUserTierUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaywallUiState())
    val uiState: StateFlow<PaywallUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<PaywallEvent>()
    val events: SharedFlow<PaywallEvent> = _events.asSharedFlow()

    init {
        loadProducts()
        observeUserTier()
        observeSubscription()
    }

    private fun loadProducts() {
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
                _uiState.value = _uiState.value.copy(currentSubscription = subscription)
            }
        }
    }

    fun purchaseSubscription(productId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isPurchasing = true,
                error = null
            )

            purchaseSubscriptionUseCase(productId)
                .onSuccess { subscription ->
                    _uiState.value = _uiState.value.copy(
                        isPurchasing = false,
                        currentSubscription = subscription,
                        purchaseSuccess = true
                    )
                    _events.emit(PaywallEvent.PurchaseSuccess(subscription))
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isPurchasing = false,
                        error = error.message
                    )
                    _events.emit(PaywallEvent.Error(error.message ?: "Purchase failed"))
                }
        }
    }

    fun restorePurchases() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isRestoring = true,
                error = null
            )

            restorePurchasesUseCase()
                .onSuccess { subscriptions ->
                    _uiState.value = _uiState.value.copy(
                        isRestoring = false,
                        currentSubscription = subscriptions.firstOrNull()
                    )

                    if (subscriptions.isNotEmpty()) {
                        _events.emit(PaywallEvent.RestoreSuccess)
                    } else {
                        _events.emit(PaywallEvent.NoSubscriptionsToRestore)
                    }
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isRestoring = false,
                        error = error.message
                    )
                    _events.emit(PaywallEvent.Error(error.message ?: "Restore failed"))
                }
        }
    }

    fun getProMonthlyProduct(): SubscriptionProduct? {
        return _uiState.value.products.find { it.id == "swiftquantum_pro_monthly" }
            ?: SubscriptionProduct.PRO_MONTHLY
    }

    fun getProYearlyProduct(): SubscriptionProduct? {
        return _uiState.value.products.find { it.id == "swiftquantum_pro_yearly" }
            ?: SubscriptionProduct.PRO_YEARLY
    }

    fun getMasterMonthlyProduct(): SubscriptionProduct? {
        return _uiState.value.products.find { it.id == "swiftquantum_master_monthly" }
            ?: SubscriptionProduct.MASTER_MONTHLY
    }

    fun getMasterYearlyProduct(): SubscriptionProduct? {
        return _uiState.value.products.find { it.id == "swiftquantum_master_yearly" }
            ?: SubscriptionProduct.MASTER_YEARLY
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetPurchaseSuccess() {
        _uiState.value = _uiState.value.copy(purchaseSuccess = false)
    }
}
