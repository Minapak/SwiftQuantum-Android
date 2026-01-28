package com.swiftquantum.data.repository

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.acknowledgePurchase
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import com.swiftquantum.data.local.UserPreferences
import com.swiftquantum.domain.model.Subscription
import com.swiftquantum.domain.model.SubscriptionProduct
import com.swiftquantum.domain.model.SubscriptionStatus
import com.swiftquantum.domain.model.UserTier
import com.swiftquantum.domain.repository.BillingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class BillingRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferences: UserPreferences
) : BillingRepository, PurchasesUpdatedListener {

    private val _currentSubscription = MutableStateFlow<Subscription?>(null)
    override val currentSubscription: Flow<Subscription?> = _currentSubscription.asStateFlow()

    private val _availableProducts = MutableStateFlow<List<SubscriptionProduct>>(emptyList())
    override val availableProducts: Flow<List<SubscriptionProduct>> = _availableProducts.asStateFlow()

    private var billingClient: BillingClient? = null
    private var productDetailsList: List<ProductDetails> = emptyList()
    private var currentActivity: Activity? = null

    private val productIds = listOf(
        "swiftquantum_pro_monthly",
        "swiftquantum_pro_yearly",
        "swiftquantum_master_monthly",
        "swiftquantum_master_yearly"
    )

    override suspend fun initializeBilling(): Result<Unit> {
        return suspendCancellableCoroutine { continuation ->
            billingClient = BillingClient.newBuilder(context)
                .setListener(this)
                .enablePendingPurchases()
                .build()

            billingClient?.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        Timber.d("Billing client connected successfully")
                        continuation.resume(Result.success(Unit))
                    } else {
                        Timber.e("Billing client connection failed: ${billingResult.debugMessage}")
                        continuation.resume(Result.failure(Exception(billingResult.debugMessage)))
                    }
                }

                override fun onBillingServiceDisconnected() {
                    Timber.w("Billing service disconnected")
                }
            })
        }
    }

    override suspend fun queryProducts(): Result<List<SubscriptionProduct>> {
        return try {
            val client = billingClient ?: return Result.failure(Exception("Billing client not initialized"))

            val productList = productIds.map { productId ->
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(productId)
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            }

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()

            val result = client.queryProductDetails(params)

            if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                productDetailsList = result.productDetailsList ?: emptyList()
                val products = productDetailsList.mapNotNull { details ->
                    mapToSubscriptionProduct(details)
                }
                _availableProducts.value = products
                Result.success(products)
            } else {
                // Return default products if billing query fails
                _availableProducts.value = SubscriptionProduct.allProducts
                Result.success(SubscriptionProduct.allProducts)
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to query products")
            _availableProducts.value = SubscriptionProduct.allProducts
            Result.success(SubscriptionProduct.allProducts)
        }
    }

    override suspend fun purchaseSubscription(productId: String): Result<Subscription> {
        return try {
            val client = billingClient ?: return Result.failure(Exception("Billing client not initialized"))
            val activity = currentActivity ?: return Result.failure(Exception("Activity not set"))

            val productDetails = productDetailsList.find { it.productId == productId }
                ?: return Result.failure(Exception("Product not found"))

            val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken
                ?: return Result.failure(Exception("No offer available"))

            val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(offerToken)
                .build()

            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(productDetailsParams))
                .build()

            val result = client.launchBillingFlow(activity, billingFlowParams)

            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                // Purchase flow launched, result will come in onPurchasesUpdated
                Result.success(createPendingSubscription(productId))
            } else {
                Result.failure(Exception("Failed to launch purchase flow: ${result.debugMessage}"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to purchase subscription")
            Result.failure(e)
        }
    }

    override suspend fun restorePurchases(): Result<List<Subscription>> {
        return try {
            val client = billingClient ?: return Result.failure(Exception("Billing client not initialized"))

            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()

            val result = client.queryPurchasesAsync(params)

            if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val subscriptions = result.purchasesList.mapNotNull { purchase ->
                    handlePurchase(purchase)
                }
                Result.success(subscriptions)
            } else {
                Result.failure(Exception(result.billingResult.debugMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to restore purchases")
            Result.failure(e)
        }
    }

    override suspend fun cancelSubscription(): Result<Unit> {
        // Subscription cancellation is handled through Google Play
        // We can only provide a link to the subscription management page
        return Result.success(Unit)
    }

    override suspend fun getCurrentSubscription(): Result<Subscription?> {
        return try {
            val client = billingClient ?: return Result.success(null)

            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()

            val result = client.queryPurchasesAsync(params)

            if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val activePurchase = result.purchasesList.firstOrNull { purchase ->
                    purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                }
                val subscription = activePurchase?.let { handlePurchase(it) }
                _currentSubscription.value = subscription
                Result.success(subscription)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get current subscription")
            Result.success(null)
        }
    }

    override suspend fun verifyPurchase(purchaseToken: String): Result<Boolean> {
        // In a production app, you would verify the purchase with your backend
        return Result.success(true)
    }

    override suspend fun acknowledgePurchase(purchaseToken: String): Result<Unit> {
        return try {
            val client = billingClient ?: return Result.failure(Exception("Billing client not initialized"))

            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build()

            val result = client.acknowledgePurchase(params)

            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(result.debugMessage))
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to acknowledge purchase")
            Result.failure(e)
        }
    }

    override fun getUserTier(): Flow<UserTier> = userPreferences.userTier

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            purchases.forEach { purchase ->
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Timber.d("User cancelled the purchase")
        } else {
            Timber.e("Purchase failed: ${billingResult.debugMessage}")
        }
    }

    private fun handlePurchase(purchase: Purchase): Subscription? {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) {
            return null
        }

        val productId = purchase.products.firstOrNull() ?: return null
        val tier = getTierFromProductId(productId)

        val subscription = Subscription(
            id = purchase.orderId ?: "",
            userId = "",
            tier = tier,
            status = SubscriptionStatus.ACTIVE,
            productId = productId,
            purchaseToken = purchase.purchaseToken,
            startDate = Instant.ofEpochMilli(purchase.purchaseTime).toString(),
            autoRenew = purchase.isAutoRenewing
        )

        _currentSubscription.value = subscription

        // Update user tier
        kotlinx.coroutines.runBlocking {
            userPreferences.setUserTier(tier)
        }

        // Acknowledge purchase if not already acknowledged
        if (!purchase.isAcknowledged) {
            kotlinx.coroutines.runBlocking {
                acknowledgePurchase(purchase.purchaseToken)
            }
        }

        return subscription
    }

    private fun getTierFromProductId(productId: String): UserTier {
        return when {
            productId.contains("master") -> UserTier.MASTER
            productId.contains("pro") -> UserTier.PRO
            else -> UserTier.FREE
        }
    }

    private fun mapToSubscriptionProduct(details: ProductDetails): SubscriptionProduct? {
        val tier = getTierFromProductId(details.productId)
        val offerDetails = details.subscriptionOfferDetails?.firstOrNull() ?: return null
        val pricingPhase = offerDetails.pricingPhases.pricingPhaseList.firstOrNull() ?: return null

        val isYearly = pricingPhase.billingPeriod.contains("Y")

        return SubscriptionProduct(
            id = details.productId,
            tier = tier,
            name = details.title,
            description = details.description,
            price = pricingPhase.formattedPrice,
            period = pricingPhase.billingPeriod,
            features = SubscriptionProduct.allProducts.find { it.id == details.productId }?.features ?: emptyList()
        )
    }

    private fun createPendingSubscription(productId: String): Subscription {
        return Subscription(
            id = "",
            userId = "",
            tier = getTierFromProductId(productId),
            status = SubscriptionStatus.PENDING,
            productId = productId,
            startDate = Instant.now().toString()
        )
    }

    fun setActivity(activity: Activity) {
        currentActivity = activity
    }

    fun clearActivity() {
        currentActivity = null
    }
}
