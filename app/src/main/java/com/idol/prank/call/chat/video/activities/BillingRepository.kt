package com.idol.prank.call.chat.video.activities

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*

object BillingRepository : PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient
    private lateinit var appContext: Context

    private var isConnected = false

    private var cachedPlans = emptyList<PlanUiModel>()

    private var premiumCallback: (() -> Unit)? = null
    private var plansCallback: (() -> Unit)? = null

    // ---------------- INIT ----------------
    fun init(
        context: Context,
        onReady: (() -> Unit)? = null,
        onPremiumUnlocked: (() -> Unit)? = null
    ) {

        appContext = context.applicationContext
        premiumCallback = onPremiumUnlocked
        plansCallback = onReady

        billingClient = BillingClient.newBuilder(appContext)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            .setListener(this)
            .build()

        billingClient.startConnection(object : BillingClientStateListener {

            override fun onBillingSetupFinished(result: BillingResult) {

                if (result.responseCode == BillingClient.BillingResponseCode.OK) {

                    isConnected = true
                    Log.d("BILLING", "Connected")

                    loadAllProducts()
                    restorePurchases(appContext)
                }
            }

            override fun onBillingServiceDisconnected() {
                isConnected = false
            }
        })
    }

    // ---------------- LOAD PRODUCTS ----------------
    private fun loadAllProducts() {

        val subs = listOf(
            product("weekly_idol_call", BillingClient.ProductType.SUBS),
            product("monthly_idol_call", BillingClient.ProductType.SUBS),
            product("yearly_idol_call", BillingClient.ProductType.SUBS)
        )

        queryProducts(subs) { subList ->

            cachedPlans = subList

            Log.d("BILLING", "Products loaded: ${cachedPlans.size}")

            // 🔥 IMPORTANT FIX
            plansCallback?.invoke()
        }
    }

    // ---------------- QUERY PRODUCTS ----------------
    private fun queryProducts(
        list: List<QueryProductDetailsParams.Product>,
        onResult: (List<PlanUiModel>) -> Unit
    ) {

        billingClient.queryProductDetailsAsync(
            QueryProductDetailsParams.newBuilder()
                .setProductList(list)
                .build()
        ) { result, data ->

            if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                onResult(emptyList())
                return@queryProductDetailsAsync
            }

            val mapped = data.productDetailsList.mapNotNull {
                it.toPlan()
            }

            onResult(mapped)
        }
    }

    // ---------------- PRODUCT ----------------
    private fun product(
        id: String,
        type: String
    ): QueryProductDetailsParams.Product {
        return QueryProductDetailsParams.Product.newBuilder()
            .setProductId(id)
            .setProductType(type)
            .build()
    }

    // ---------------- MAP PRODUCT ----------------
    private fun ProductDetails.toPlan(): PlanUiModel {

        val title = when (productId) {
            "weekly_idol_call" -> "Weekly Plan"
            "monthly_idol_call" -> "Monthly Plan"
            "yearly_idol_call" -> "Yearly Plan"
            else -> productId
        }

        val price =
            subscriptionOfferDetails
                ?.firstOrNull()
                ?.pricingPhases
                ?.pricingPhaseList
                ?.lastOrNull()
                ?.formattedPrice ?: "Loading..."

        return PlanUiModel(
            id = productId,
            title = title,
            price = price,
            hasFreeTrial = false,
            isBestValue = productId == "yearly_idol_call",
            product = this
        )
    }

    // ---------------- PURCHASE ----------------
    fun launchPurchase(activity: Activity, plan: PlanUiModel) {

        val product = plan.product

        val offer = product.subscriptionOfferDetails?.firstOrNull() ?: return

        val params = BillingFlowParams.ProductDetailsParams
            .newBuilder()
            .setProductDetails(product)
            .setOfferToken(offer.offerToken)
            .build()

        billingClient.launchBillingFlow(
            activity,
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(params))
                .build()
        )
    }

    // ---------------- CALLBACK ----------------
    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {

        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            purchases.forEach { handlePurchase(it) }
        }
    }

    // ---------------- HANDLE PURCHASE ----------------
    private fun handlePurchase(purchase: Purchase) {

        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return

        if (!purchase.isAcknowledged) {

            billingClient.acknowledgePurchase(
                AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
            ) { result ->

                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    unlockPremium()
                }
            }

        } else {
            unlockPremium()
        }
    }

    // ---------------- UNLOCK PREMIUM ----------------
    private fun unlockPremium() {

        PremiumManager.setPremium(appContext, true)

        Log.d("BILLING", "PREMIUM UNLOCKED")

        premiumCallback?.invoke()
    }

    // ---------------- RESTORE ----------------
    fun restorePurchases(context: Context) {

        if (!::billingClient.isInitialized) return

        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { _, purchases ->

            val hasPremium = purchases.any {
                it.purchaseState == Purchase.PurchaseState.PURCHASED
            }

            if (hasPremium) {
                unlockPremium()
            }
        }
    }

    // ---------------- CACHE ----------------
    fun getCachedPlans(): List<PlanUiModel> {
        return cachedPlans
    }

    // ---------------- DESTROY ----------------
    fun destroy() {
        if (::billingClient.isInitialized) {
            billingClient.endConnection()
        }
    }
}