package com.swiftquantum.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Subscription(
    val id: String,
    val userId: String,
    val tier: UserTier,
    val status: SubscriptionStatus = SubscriptionStatus.ACTIVE,
    val productId: String,
    val purchaseToken: String? = null,
    val startDate: String,
    val endDate: String? = null,
    val autoRenew: Boolean = true,
    val platform: String = "android"
)

@Serializable
enum class SubscriptionStatus {
    ACTIVE,
    CANCELLED,
    EXPIRED,
    PENDING,
    GRACE_PERIOD,
    ON_HOLD,
    PAUSED
}

data class SubscriptionProduct(
    val id: String,
    val tier: UserTier,
    val name: String,
    val description: String,
    val price: String,
    val period: String,
    val features: List<String>
) {
    companion object {
        val PRO_MONTHLY = SubscriptionProduct(
            id = "swiftquantum_pro_monthly",
            tier = UserTier.PRO,
            name = "Pro Monthly",
            description = "Unlock up to 30 qubits simulation",
            price = "$9.99/month",
            period = "P1M",
            features = listOf(
                "Up to 30 qubits simulation",
                "Unlimited simulations",
                "Save unlimited circuits",
                "Priority support"
            )
        )

        val PRO_YEARLY = SubscriptionProduct(
            id = "swiftquantum_pro_yearly",
            tier = UserTier.PRO,
            name = "Pro Yearly",
            description = "Unlock up to 30 qubits simulation - Save 17%",
            price = "$99.99/year",
            period = "P1Y",
            features = listOf(
                "Up to 30 qubits simulation",
                "Unlimited simulations",
                "Save unlimited circuits",
                "Priority support",
                "2 months free"
            )
        )

        val MASTER_MONTHLY = SubscriptionProduct(
            id = "swiftquantum_master_monthly",
            tier = UserTier.MASTER,
            name = "Master Monthly",
            description = "Full access including IBM Quantum hardware",
            price = "$29.99/month",
            period = "P1M",
            features = listOf(
                "40+ qubits simulation",
                "IBM Quantum hardware access",
                "Unlimited simulations",
                "Advanced analytics",
                "Priority hardware queue",
                "Premium support"
            )
        )

        val MASTER_YEARLY = SubscriptionProduct(
            id = "swiftquantum_master_yearly",
            tier = UserTier.MASTER,
            name = "Master Yearly",
            description = "Full access including IBM Quantum hardware - Save 17%",
            price = "$299.99/year",
            period = "P1Y",
            features = listOf(
                "40+ qubits simulation",
                "IBM Quantum hardware access",
                "Unlimited simulations",
                "Advanced analytics",
                "Priority hardware queue",
                "Premium support",
                "2 months free"
            )
        )

        val allProducts = listOf(PRO_MONTHLY, PRO_YEARLY, MASTER_MONTHLY, MASTER_YEARLY)
    }
}
