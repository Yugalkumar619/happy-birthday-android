import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import com.app.happy_birthday.helper.AppController
import com.app.happy_birthday.helper.Global.isTestModeEnabled
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

// File: AdsManager.kt  (Create this single file and never touch ads again)

object AdsManager {

    private const val TAG = "AdsManager"

    // TEST IDs (use now)
    private const val TEST_BANNER       = "ca-app-pub-3940256099942544/9214589741"
    private const val TEST_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"
    private const val TEST_REWARDED     = "ca-app-pub-3940256099942544/5224354917"

    // YOUR REAL IDs (replace these before publishing)
    private const val BANNER_ID       = "ca-app-pub-2088375739935408/5876506535"
    private const val INTERSTITIAL_ID = "ca-app-pub-2088375739935408/9240553970"
    private const val REWARDED_ID     = "ca-app-pub-2088375739935408/2464604082"

    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    // Check if user is premium using your BillingManager
    private fun isUserPremium(context: Context): Boolean {
        val app = context.applicationContext as? AppController
        return app?.billingManager?.isPremium?.value == true
    }


    // 1. SMALL BANNER (for ALL listing pages)
    fun loadBanner(activity: Activity, containerId: Int) {
        /** Enable later for premium app */
        if (isUserPremium(activity)) return

        val container = activity.findViewById<FrameLayout>(containerId) ?: return
        val adView = AdView(activity).apply {
            adUnitId = if (isTestModeEnabled) TEST_BANNER else BANNER_ID
            setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, 320))
        }
        container.removeAllViews()
        container.addView(adView)
        adView.loadAd(AdRequest.Builder().build())
    }

    fun loadRectangleBanner(activity: Activity, containerId: Int) {
        /** Enable later for premium app */
        if (isUserPremium(activity)) return

        val container = activity.findViewById<FrameLayout>(containerId) ?: return
        val adView = AdView(activity).apply {
            adUnitId = if (isTestModeEnabled) TEST_BANNER else BANNER_ID
            setAdSize(AdSize.MEDIUM_RECTANGLE)
        }
        container.removeAllViews()
        container.addView(adView)
        adView.loadAd(AdRequest.Builder().build())
    }

    // 2. INTERSTITIAL (after Set Wallpaper / Set Ringtone)
    fun loadInterstitial(context: Context) {
        /** Enable later for premium app */
        if (isUserPremium(context)) {
            interstitialAd = null // Clear any loaded ad
            return
        }

        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, if (isTestModeEnabled) TEST_INTERSTITIAL else INTERSTITIAL_ID,
            adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad

                }
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let { Log.d(ContentValues.TAG, it) }
                    interstitialAd = null
                }

            })

        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(ContentValues.TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(ContentValues.TAG, "Ad dismissed fullscreen content.")
                interstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when ad fails to show.
                super.onAdFailedToShowFullScreenContent(adError)
                Log.e(ContentValues.TAG, "Ad failed to show fullscreen content.")
                InterstitialAd.load(
                    context,
                    if (isTestModeEnabled) TEST_INTERSTITIAL else INTERSTITIAL_ID,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            adError.toString().let { Log.d(ContentValues.TAG, it) }
                            interstitialAd = null
                        }

                        override fun onAdLoaded(interstitial: InterstitialAd) {
                            Log.d(ContentValues.TAG, "Ad was loaded.")
                            interstitialAd = interstitial
                        }
                    })
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(ContentValues.TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(ContentValues.TAG, "Ad showed fullscreen content.")
            }
        }
    }

    fun showInterstitial(activity: Activity, onDismiss: () -> Unit = {}) {
        /** Enable later for premium app */
        if (isUserPremium(activity) || interstitialAd == null) {
            onDismiss() // Premium or no ad → continue immediately
            return
        }

        // SAFE CHECK – this prevents the crash
        if (interstitialAd != null) {
            interstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("AdsManager", "Interstitial dismissed")
                    interstitialAd = null
                    onDismiss.invoke()
                    loadInterstitial(activity)  // Preload next ad
                }

                override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                    Log.e("AdsManager", "Interstitial failed to show: ${adError.message}")
                    interstitialAd = null
                    onDismiss.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("AdsManager", "Interstitial shown")
                }
            }
            interstitialAd!!.show(activity)
        } else {
            Log.d("AdsManager", "Interstitial not ready yet")
            onDismiss.invoke()  // Continue even if no ad
        }
    }

    // 3. REWARDED (Watch ad → Unlock premium wallpaper/bhajan)
    fun loadRewarded(context: Context) {
        /** Enable later for premium app */
        if (isUserPremium(context)) {
            rewardedAd = null
            return
        }


        RewardedAd.load(context, if (isTestModeEnabled) TEST_REWARDED else REWARDED_ID,
            AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }
            })
    }

    fun showRewarded(activity: Activity, onReward: () -> Unit, onError: () -> Unit) {
        /** Enable later for premium app */
        if (isUserPremium(activity)) {
            // Premium users get FREE unlock without watching ad
            onReward()
            return
        }

        if (rewardedAd == null) {
           onError()
        }else{
            rewardedAd!!.show(activity) {
                onReward() // User watched full ad → unlock content
            }
            rewardedAd = null
            loadRewarded(activity)
        }

    }
}