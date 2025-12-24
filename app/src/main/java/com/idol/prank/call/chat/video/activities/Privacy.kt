package com.idol.prank.call.chat.video.activities

import android.R.id.background
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import com.idol.prank.call.chat.video.R
import com.idol.prank.call.chat.video.adapter.FirstViewPagerAdapter
import com.idol.prank.call.chat.video.utils.SharedPref
import me.relex.circleindicator.CircleIndicator3
class Privacy : AppCompatActivity() {

    var user: SharedPref? = null
    var boolean: Boolean? = null
    private var handlerRetryAd: Handler? = null
    private var retryAttempt = 0
    private val nativeAdContainerView: ViewGroup? = null
    lateinit var templateview: View

    var isConnected = false

    var progressBar: ProgressBar? = null
    var viewpager2: ViewPager2? = null
    var circleIndicator3: CircleIndicator3? = null
    private var sharedPref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    private var nativeAdContainerr: FrameLayout? = null
    companion object {
        var skip: TextView? = null
        var accept: TextView? = null


        public fun buttonenable() {
            skip!!.visibility = View.GONE
            accept!!.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_screen)
        supportActionBar!!.hide()
        showProgressDialog()

        templateview = findViewById(R.id.relativeLayoutadmob)
        templateview.visibility = View.GONE

        progressBar = findViewById(R.id.progressBar)
        handlerRetryAd = Handler()

        user = SharedPref(this)

        skip = findViewById(R.id.skip)
        sharedPref = getSharedPreferences("privacy_prefrs", Context.MODE_PRIVATE)
        editor = sharedPref!!.edit()
        var adapter =
            FirstViewPagerAdapter(
                this
            )

        loadnative()

        Handler(Looper.getMainLooper()).postDelayed({
            // Hide the progress bar and show the button
            progressBar!!.visibility = View.GONE
            skip!!.visibility = View.VISIBLE
        }, 3000)
        skip!!.setOnClickListener {
            showInterstitialAd()


            boolean = true

        }

    }

    private fun showInterstitialAd() {

        val i = Intent(this@Privacy, TermsAndConditionActivity::class.java)
        startActivity(i)
        finish()

    }
    override fun onDestroy() {
        handlerRetryAd!!.removeCallbacksAndMessages(null)
        super.onDestroy()
    }



    private fun startedWed() {


        if (user!!.getStoreValue().equals("true")) {
            startActivity(Intent(this, SettingOption::class.java))
            finish()
        } else {
            user?.setStoreValue("true")
            editor!!.putBoolean("PrivacySelection", true);
            editor!!.apply();
            sharedPref!!.edit().putBoolean("firstTimeclog_prefrs", false).apply()
            startActivity(Intent(this, TermsAndConditionActivity::class.java))
            finish()
        }
    }


    override fun onBackPressed() {

        return

    }
    private fun showProgressDialog() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        android.os.Handler().postDelayed({
            progressDialog.dismiss()
        }, 3000)
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