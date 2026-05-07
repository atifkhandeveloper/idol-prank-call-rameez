package com.idol.prank.call.chat.video.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.RadioButton
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.idol.prank.call.chat.video.BaseActivity
import com.idol.prank.call.chat.video.databinding.ActivityPaywallBinding
import com.idol.prank.call.chat.video.R

class PremiumActivity : BaseActivity() {

    private var interstitialAd: InterstitialAd? = null
    private var plans = emptyList<PlanUiModel>()

    private val handler = Handler(Looper.getMainLooper())
    private var isAdShownOnClose = false

    private lateinit var binding: ActivityPaywallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaywallBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        applyEdgeToEdgePadding(binding.root)
        setContentView(binding.root)

        // 🔥 Skip if already premium
        if (PremiumManager.isPremium(this)) {
            startActivity(Intent(this, Home::class.java))
            finish()
            return
        }

        initViews()
        initPlans()
        setupClicks()
        setupDefaultSelection()

        // 🔥 Load ads only for non-premium
        if (PremiumManager.shouldShowAds(this)) {
            loadAd()
        }

        showCloseButtonAfterDelay()

        BillingRepository.init(
            this,
            onReady = {
                runOnUiThread {
                    initPlans()   // 🔥 prices now guaranteed loaded
                }
            },
            onPremiumUnlocked = {
                setResult(RESULT_OK)
                startActivity(Intent(this, Home::class.java))
                finish()
            }
        )
    }



    // ---------------- INIT ----------------
    private fun initViews() {
        binding.btnClose.visibility = View.INVISIBLE
    }

    private fun showCloseButtonAfterDelay() {
        binding.btnClose.postDelayed({
            binding.btnClose.visibility = View.VISIBLE
        }, 3000)
    }

    private fun setupDefaultSelection() {
        binding.btnWeekly.isChecked = true
        updateButtonText("weekly_idol_call")
    }

    // ---------------- PLANS ----------------
//    private fun initPlans() {
//        plans = BillingRepository.getCachedPlans()
//        bindPlans()
//    }

    private fun initPlans() {

        val data = BillingRepository.getCachedPlans()

        if (data.isEmpty()) {
            binding.btnSubscribe.text = "Loading prices..."
            return
        }

        plans = data
        bindPlans()
    }

    private fun bindPlans() {
        binding.btnWeekly.text = formatPlan("weekly_idol_call")
        binding.btnYearly.text = formatPlan("monthly_idol_call")
        binding.lifeTime.text = formatPlan("yearly_idol_call")
    }

    private fun formatPlan(id: String): String {
        val plan = plans.find { it.id == id } ?: return "Loading..."

        return buildString {
            append(plan.title)
            append("\n")
            append(plan.price)

            if (plan.isBestValue) {
                append(" ⭐ BEST VALUE")
            }
        }
    }

    // ---------------- CLICKS ----------------
    private fun setupClicks() {

        binding.btnWeekly.setOnClickListener {
            selectOnly(binding.btnWeekly)
            updateButtonText("weekly_idol_call")
        }

        binding.btnYearly.setOnClickListener {
            selectOnly(binding.btnYearly)
            updateButtonText("monthly_idol_call")
        }

        binding.lifeTime.setOnClickListener {
            selectOnly(binding.lifeTime)
            updateButtonText("yearly_idol_call")
        }

        binding.btnSubscribe.setOnClickListener {

            val selected = when {
                binding.btnWeekly.isChecked ->
                    plans.find { it.id == "weekly_idol_call" }

                binding.btnYearly.isChecked ->
                    plans.find { it.id == "monthly_idol_call" }

                binding.lifeTime.isChecked ->
                    plans.find { it.id == "yearly_idol_call" }

                else -> null
            } ?: return@setOnClickListener

            BillingRepository.launchPurchase(this, selected)
        }

        binding.btnRestore.setOnClickListener {
            // BillingRepository.restorePurchases(this)
        }

        binding.btnClose.setOnClickListener {

            if (PremiumManager.isPremium(this)) {
                goHome()
                return@setOnClickListener
            }

            if (!isAdShownOnClose) {
                isAdShownOnClose = true
                showAdOnClose()
            } else {
                goHome()
            }
        }
    }

    private fun selectOnly(selected: RadioButton) {
        binding.btnWeekly.isChecked = selected == binding.btnWeekly
        binding.btnYearly.isChecked = selected == binding.btnYearly
        binding.lifeTime.isChecked = selected == binding.lifeTime
    }

    private fun updateButtonText(planId: String) {
        binding.btnSubscribe.text = when (planId) {

            "weekly_idol_call" -> "START FREE TRIAL"
            "monthly_idol_call" -> "SUBSCRIBE NOW"
            "yearly_idol_call" -> "BEST VALUE PLAN"

            else -> "SUBSCRIBE"
        }
    }

    // ---------------- ADS ----------------
    private fun loadAd() {

        InterstitialAd.load(
            this,
            getString(R.string.interstitial_id),
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad

                    interstitialAd?.setOnPaidEventListener { adValue ->
                        val revenue = adValue.valueMicros / 1_000_000.0
                        val currency = adValue.currencyCode

                        Log.d("Ads", "Revenue: $revenue $currency")

                        sendRevenueToFirebase(revenue, currency)
                    }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    fun sendRevenueToFirebase(value: Double, currency: String) {

        val bundle = Bundle().apply {
            putDouble("value", value)
            putString("currency", currency)
            putString("ad_platform", "admob")
            putString("ad_format", "interstitial")
        }

        FirebaseAnalytics.getInstance(this)
            .logEvent("ad_impression", bundle)
    }

    private fun showAdOnClose() {

        if (!PremiumManager.shouldShowAds(this)) {
            goHome()
            return
        }

        if (interstitialAd == null) {
            goHome()
            return
        }

        interstitialAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadAd()
                    goHome()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    loadAd()
                    goHome()
                }
            }

        interstitialAd?.show(this)
    }

    private fun goHome() {
        startActivity(Intent(this, Home::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onBackPressed() {
        // disabled
    }
}