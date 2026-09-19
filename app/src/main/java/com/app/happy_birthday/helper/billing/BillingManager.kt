package com.app.happy_birthday.helper.billing
import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.acknowledgePurchase
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BillingManager(private val context: Context) : PurchasesUpdatedListener {

    private var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .build()

    private val _productDetails = MutableLiveData<ProductDetails?>()
    val productDetails: LiveData<ProductDetails?> = _productDetails

    private val _isPremium = MutableLiveData<Boolean>(false)
    val isPremium: LiveData<Boolean> = _isPremium

    private val _purchaseStatus = MutableLiveData<String>()
    val purchaseStatus: LiveData<String> = _purchaseStatus

    init {
        startBillingConnection()
    }

    private fun startBillingConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                println("Here is my Billing::: ${billingResult.responseCode}")
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProductDetails()
                    checkExistingPurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request
                println("Here is my Billing::: onBillingServiceDisconnected")
            }
        })
    }

    private fun queryProductDetails() {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("premium_unlock_birthday")
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            val result = billingClient.queryProductDetails(params)
            println("Billing Query Response Code: ${result.billingResult.responseCode}")
            println("Billing Query Debug Message: ${result.billingResult.debugMessage}")
            println("Billing Products Found: ${result.productDetailsList?.size ?: 0}")

            if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                !result.productDetailsList.isNullOrEmpty()) {
                val details = result.productDetailsList!!.first()
                println("Product Loaded Successfully!")
                println("Title: ${details.title}")
                println("Price: ${details.oneTimePurchaseOfferDetails?.formattedPrice}")
                _productDetails.postValue(details)
            } else {
                println("No product details returned – product may not be visible yet")
                _productDetails.postValue(null)
            }
        }
    }

    private fun checkExistingPurchases() {
        CoroutineScope(Dispatchers.IO).launch {
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)

            val result = billingClient.queryPurchasesAsync(params.build())
            if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val purchased = result.purchasesList.any { it.products.contains("premium_unlock_birthday") && it.isAcknowledged }
                _isPremium.postValue(purchased)
            }
        }
    }

    fun launchPurchaseFlow(activity: Activity, productDetails: ProductDetails) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    if (purchase.products.contains("premium_unlock_birthday") && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        if (!purchase.isAcknowledged) {
                            acknowledgePurchase(purchase)
                        } else {
                            _isPremium.postValue(true)
                            _purchaseStatus.postValue("Premium unlocked! Thank you!")
                        }
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                _purchaseStatus.postValue("Purchase cancelled")
            }
            else -> {
                _purchaseStatus.postValue("Purchase failed: ${billingResult.debugMessage}")
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            billingClient.acknowledgePurchase(acknowledgePurchaseParams)
            _isPremium.postValue(true)
            _purchaseStatus.postValue("Premium unlocked! Thank you!")
        }
    }

    fun retryConnection() {
        startBillingConnection()
    }
}