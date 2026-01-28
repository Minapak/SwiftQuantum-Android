package com.swiftquantum.domain.repository

import com.swiftquantum.domain.model.Subscription
import com.swiftquantum.domain.model.SubscriptionProduct
import com.swiftquantum.domain.model.UserTier
import kotlinx.coroutines.flow.Flow

interface BillingRepository {
    val currentSubscription: Flow<Subscription?>
    val availableProducts: Flow<List<SubscriptionProduct>>

    suspend fun initializeBilling(): Result<Unit>
    suspend fun queryProducts(): Result<List<SubscriptionProduct>>
    suspend fun purchaseSubscription(productId: String): Result<Subscription>
    suspend fun restorePurchases(): Result<List<Subscription>>
    suspend fun cancelSubscription(): Result<Unit>
    suspend fun getCurrentSubscription(): Result<Subscription?>
    suspend fun verifyPurchase(purchaseToken: String): Result<Boolean>
    suspend fun acknowledgePurchase(purchaseToken: String): Result<Unit>
    fun getUserTier(): Flow<UserTier>
}
