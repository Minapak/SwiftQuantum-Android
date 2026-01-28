package com.swiftquantum.domain.usecase

import com.swiftquantum.domain.model.Subscription
import com.swiftquantum.domain.model.SubscriptionProduct
import com.swiftquantum.domain.model.UserTier
import com.swiftquantum.domain.repository.BillingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSubscriptionProductsUseCase @Inject constructor(
    private val billingRepository: BillingRepository
) {
    suspend operator fun invoke(): Result<List<SubscriptionProduct>> {
        return billingRepository.queryProducts()
    }
}

class PurchaseSubscriptionUseCase @Inject constructor(
    private val billingRepository: BillingRepository
) {
    suspend operator fun invoke(productId: String): Result<Subscription> {
        return billingRepository.purchaseSubscription(productId)
    }
}

class RestorePurchasesUseCase @Inject constructor(
    private val billingRepository: BillingRepository
) {
    suspend operator fun invoke(): Result<List<Subscription>> {
        return billingRepository.restorePurchases()
    }
}

class GetCurrentSubscriptionUseCase @Inject constructor(
    private val billingRepository: BillingRepository
) {
    suspend operator fun invoke(): Result<Subscription?> {
        return billingRepository.getCurrentSubscription()
    }
}

class CancelSubscriptionUseCase @Inject constructor(
    private val billingRepository: BillingRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return billingRepository.cancelSubscription()
    }
}

class ObserveUserTierUseCase @Inject constructor(
    private val billingRepository: BillingRepository
) {
    operator fun invoke(): Flow<UserTier> {
        return billingRepository.getUserTier()
    }
}

class ObserveSubscriptionUseCase @Inject constructor(
    private val billingRepository: BillingRepository
) {
    operator fun invoke(): Flow<Subscription?> {
        return billingRepository.currentSubscription
    }
}
