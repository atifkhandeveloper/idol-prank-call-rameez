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
import com.idol.prank.call.chat.video.R

class TermsAndConditionActivity : AppCompatActivity() {

    private var checkBoxtermsandcon: CheckBox? = null
    private var textcontinue: TextView? = null
    private var checkad: Boolean? = null

    private var handlerRetryAd: Handler? = null
    private var retryAttempt = 0
    var applovin_native: String? = null
    var applovin_banner: String? = null
    private val nativeAdContainerView: ViewGroup? = null
    private var nativeAdContainerr: FrameLayout? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_condition)

        checkBoxtermsandcon = findViewById(R.id.checkBox)
        textcontinue = findViewById(R.id.continuedata)

        showProgressDialog()

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



}