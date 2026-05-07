package com.idol.prank.call.chat.video.activities


import com.android.billingclient.api.ProductDetails

data class PlanUiModel(
    val id: String,
    val title: String,
    val price: String,
    val hasFreeTrial: Boolean,
    val isBestValue: Boolean,
    val product: ProductDetails
)