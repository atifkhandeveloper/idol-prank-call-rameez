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
import androidx.viewpager.widget.ViewPager
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import com.idol.prank.call.chat.video.R
import com.idol.prank.call.chat.video.adapter.ImagePagerAdapter

class HowToUseActivity : AppCompatActivity()
{
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: ImagePagerAdapter
    private var nextbtn: Button? = null
    private var continuebtn: Button? = null
    private var checkad: Boolean? = null
    private var handlerRetryAd: Handler? = null
    private var retryAttempt = 0
    private val nativeAdContainerView: ViewGroup? = null
    private var nativeAdContainerr: FrameLayout? = null
    lateinit var templateview: View

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_use)
        viewPager = findViewById(R.id.viewPagerhowtouse)

        nextbtn = findViewById(R.id.btnnext)
        continuebtn = findViewById(R.id.btncontinue)
        pagerAdapter = ImagePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        templateview = findViewById(R.id.relativeLayoutadmob)
        templateview.visibility = View.GONE



        showProgressDialog()
        loadnative()



        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                setButtonVisibility(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })



        nextbtn!!.setOnClickListener {
            moveViewPager(1)
        }

        continuebtn!!.setOnClickListener {


            showInterstitialAd()

        }


        setButtonVisibility(0)
    }

    private fun showInterstitialAd() {

            val i = Intent(this@HowToUseActivity, AppWelcome::class.java)
            startActivity(i)
            finish()

    }


    private fun moveViewPager(offset: Int) {
        val currentPosition = viewPager.currentItem
        val newPosition = (currentPosition + offset).coerceIn(0, pagerAdapter.count - 1)
        viewPager.setCurrentItem(newPosition, true)
    }

    private fun setButtonVisibility(currentPosition: Int) {
        if (currentPosition == pagerAdapter.count - 1) {
            continuebtn!!.visibility = View.VISIBLE
            nextbtn!!.visibility = View.GONE
        } else {
            nextbtn!!.visibility = View.VISIBLE
            continuebtn!!.visibility = View.GONE
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
        }, 2000)
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