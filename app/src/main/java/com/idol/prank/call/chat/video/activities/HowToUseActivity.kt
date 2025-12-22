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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_use)
        nativeAdContainerr = findViewById(R.id.nativeAdContainerr)
        viewPager = findViewById(R.id.viewPagerhowtouse)

        nextbtn = findViewById(R.id.btnnext)
        continuebtn = findViewById(R.id.btncontinue)
        pagerAdapter = ImagePagerAdapter(this)
        viewPager.adapter = pagerAdapter



        showProgressDialog()



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


}