package com.idol.prank.call.chat.video

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd

import com.idol.prank.call.chat.video.AdsModule.TinyDBs
import com.idol.prank.call.chat.video.activities.PremiumManager
import java.util.Date



class FirstMainApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks,
    DefaultLifecycleObserver {

        lateinit var myTinyDBs: TinyDBs
        private lateinit var appOpenAdManager: AppOpenAdManager
        private var currentActivity: Activity? = null

    override fun onCreate() {
        super<MultiDexApplication>.onCreate()
        instance = this   // ✅ REQUIRED
        registerActivityLifecycleCallbacks(this)

        if (PremiumManager.shouldShowAds(this)) {
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
            appOpenAdManager = AppOpenAdManager()
        }

        myTinyDBs = TinyDBs(this)

    }

    // [END application_class]
    // [END application_class]

    /**
     * DefaultLifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    // [START lifecycle_observer_events]
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        currentActivity?.let {
            // Show the ad (if available) when the app moves to foreground.
            appOpenAdManager.showAdIfAvailable(it)
        }
    }

    // [END lifecycle_observer_events]

    /** ActivityLifecycleCallback methods. */
    // [START activity_lifecycle_callbacks]
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    // [END activity_lifecycle_callbacks]

    /**
     * Shows an app open ad.
     *
     * @param activity the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

    /**
     * Load an app open ad.
     *
     * @param activity the activity that shows the app open ad
     */
    fun loadAd(activity: Activity) {
        // We wrap the loadAd to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.loadAd(activity)
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete (i.e.
     * dismissed or fails to show).
     */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    /** Inner class that loads and shows app open ads. */
    // [START manager_class]
    private inner class AppOpenAdManager {

        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false

        /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
        private var loadTime: Long = 0

        // [END manager_class]

        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        fun loadAd(context: Context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return
            }
            isLoadingAd = true
            // [START load_ad]
            AppOpenAd.load(
                context,
                AD_UNIT_ID,
                AdRequest.Builder().build(),
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        // Called when an app open ad has loaded.
                        Log.d(LOG_TAG, "App open ad loaded.")

                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Called when an app open ad has failed to load.
                        Log.d(LOG_TAG, "App open ad failed to load with error: " + loadAdError.message)

                        isLoadingAd = false
                    }
                },
            )
            // [END load_ad]
        }

        // [START ad_expiration]
        /** Check if ad was loaded more than n hours ago. */
        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        /** Check if ad exists and can be shown. */
        private fun isAdAvailable(): Boolean {
            // For time interval details, see: https://support.google.com/admob/answer/9341964
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }

        // [END ad_expiration]

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        fun showAdIfAvailable(activity: Activity) {
            showAdIfAvailable(
                activity,
                object : OnShowAdCompleteListener {
                    override fun onShowAdComplete() {
                        // Empty because the user will go back to the activity that shows the ad.
                    }
                },
            )
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */
        fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.")
                return
            }

            // If the app open ad is not available yet, invoke the callback.
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                    loadAd(activity)

                return
            }

            Log.d(LOG_TAG, "Will show ad.")

            appOpenAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    /** Called when full screen content is dismissed. */
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        Log.d(LOG_TAG, "onAdDismissedFullScreenContent.")

                        onShowAdCompleteListener.onShowAdComplete()
                            loadAd(activity)

                    }

                    /** Called when fullscreen content failed to show. */
                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        appOpenAd = null
                        isShowingAd = false
                        Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.message)

                        onShowAdCompleteListener.onShowAdComplete()
                            loadAd(activity)

                    }

                    /** Called when fullscreen content is shown. */
                    override fun onAdShowedFullScreenContent() {
                        Log.d(LOG_TAG, "onAdShowedFullScreenContent.")
                    }
                }
            isShowingAd = true
            appOpenAd?.show(activity)
        }
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: FirstMainApplication

        val AD_UNIT_ID: String
            get() = instance.getString(R.string.app_open_ad_unit_id)
        // This is an ad unit ID for a test ad. Replace with your own app open ad unit ID.
//        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/9257395921"
        private const val LOG_TAG = "MyApplication"



    }
}