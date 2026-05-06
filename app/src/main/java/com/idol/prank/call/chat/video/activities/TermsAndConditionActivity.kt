package com.idol.prank.call.chat.video.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import com.idol.prank.call.chat.video.BaseActivity
import com.idol.prank.call.chat.video.R
import com.idol.prank.call.chat.video.databinding.ActivityPrivacyScreenBinding
import com.idol.prank.call.chat.video.databinding.ActivityTermsAndConditionBinding

class TermsAndConditionActivity : BaseActivity() {

    private var checkBoxtermsandcon: CheckBox? = null
    private var textcontinue: TextView? = null
    private var checkad: Boolean? = null
    lateinit var templateview: View

    lateinit var binding: ActivityTermsAndConditionBinding



    private var handlerRetryAd: Handler? = null
    private var retryAttempt = 0
    var applovin_native: String? = null
    var applovin_banner: String? = null
    private val nativeAdContainerView: ViewGroup? = null
    private var nativeAdContainerr: FrameLayout? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        applyEdgeToEdgePadding(binding.root)
        setContentView(binding.root)

        checkBoxtermsandcon = findViewById(R.id.checkBox)
        textcontinue = findViewById(R.id.continuedata)


        templateview = findViewById(R.id.relativeLayoutadmob)
        templateview.visibility = View.GONE

        showProgressDialog()
        loadnative()

        checkBoxtermsandcon!!.setOnCheckedChangeListener { _, isChecked ->
            textcontinue!!.visibility = if (isChecked) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        textcontinue!!.setOnClickListener {
            showInterstitialAd()

            }

    }

    // Call this method to show the ad
    private fun showInterstitialAd() {

            val i = Intent(this@TermsAndConditionActivity, HowToUseActivity::class.java)
            startActivity(i)
            finish()

    }

    private fun showProgressDialog() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        android.os.Handler().postDelayed({
            progressDialog.dismiss()
        }, 5000)
    }

    override fun onBackPressed() {
        return

    }

    private fun moveToNextActivity() {

            startActivity(
                Intent(
                    this@TermsAndConditionActivity,
                    HowToUseActivity::class.java
                )
            )

        finish()
    }

    private fun loadnative(){
        MobileAds.initialize(this)

// Create the ad loader
        val adLoader = AdLoader.Builder(this, resources.getString(R.string.nativead))
            .forNativeAd { nativeAd: NativeAd ->
                // Create template style
                val styles = NativeTemplateStyle.Builder()
                    .build()
                templateview.visibility = View.VISIBLE
                // Set template and native ad
                val template: TemplateView = findViewById(R.id.my_template)
                template.setStyles(styles)
                template.setNativeAd(nativeAd)

            }
            .build()

// Load the ad
        adLoader.loadAd(AdRequest.Builder().build())
    }




}