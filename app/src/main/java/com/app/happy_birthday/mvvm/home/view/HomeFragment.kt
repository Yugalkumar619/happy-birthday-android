package com.app.happy_birthday.mvvm.home.view

import AdsManager
import AppNavigation.navigateToCategoryListing
import AppNavigation.navigateToRingtoneListing
import AppNavigation.navigateToSavedListing
import AppNavigation.navigateToWallpaperListing
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.FragmentHomeBinding
import com.app.happy_birthday.helper.AppController
import com.app.happy_birthday.helper.BaseFragment
import com.app.happy_birthday.helper.Constants.ACCESS_DURATION_HOURS
import com.app.happy_birthday.helper.Constants.KEY_ACCESS_EXPIRY
import com.app.happy_birthday.helper.Constants.KEY_AD_COUNT
import com.app.happy_birthday.helper.Constants.KEY_LAST_AD_DAY
import com.app.happy_birthday.helper.Constants.KEY_UNLOCKED_COUNT
import com.app.happy_birthday.helper.Constants.MAX_ADS_PER_DAY
import com.app.happy_birthday.helper.Constants.MAX_UNLOCKABLE
import com.app.happy_birthday.helper.Constants.PREF_NAME
import com.app.happy_birthday.helper.Constants.UNLOCK_PER_AD
import com.app.happy_birthday.helper.Extensions.getUnlockedWallpaperCount
import com.app.happy_birthday.helper.Extensions.openMyAppOnPlayStore
import com.app.happy_birthday.helper.Global.isMessageShown
import com.app.happy_birthday.helper.billing.BillingManager
import com.app.happy_birthday.helper.helper_model.BirthdayPerson
import com.app.happy_birthday.mvvm.category.view_model.CategoryListingObj
import com.app.happy_birthday.mvvm.wallpaper.view_model.WallpaperListingObj


class HomeFragment : BaseFragment() {

    private lateinit var billingManager: BillingManager
    private lateinit var mActivity: HomeActivity
    private lateinit var binding: FragmentHomeBinding
    private val sharedPrefs by lazy { mActivity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize BillingManager
        billingManager = (requireActivity().application as AppController).billingManager

        initializeFields()
        initOnClickListener()

        // NEW: Observe premium status from purchase
        billingManager.isPremium.observe(viewLifecycleOwner) { isPremium ->
            if (isPremium) {
                onUserBecamePremium()
            } else {
                onUserNotPremium()
            }
        }

        // NEW: Show price on premium section
        billingManager.productDetails.observe(viewLifecycleOwner) { details ->
            if (details != null) {
                val price = details.oneTimePurchaseOfferDetails?.formattedPrice ?: "₹49"
                binding.btnPurchasePremium.text = getString(R.string.label_go_premium_price, price)
            }
        }

        // NEW: Show purchase messages
        billingManager.purchaseStatus.observe(viewLifecycleOwner) { message ->
            if(isMessageShown == false){
                Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show()
                isMessageShown = true
            }
        }

    }


    private fun initializeFields(){

        binding.linPremiumFeature.isVisible = sharedPrefs.getUnlockedWallpaperCount(mActivity) > 0
//        binding.root.post {
//            AdsManager.loadRectangleBanner(requireActivity(), R.id.banner_container)
//        }

        // Wallpaper Section
        binding.ilImages.conBg.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.color_red)
        binding.ilImages.ivOption.setImageResource(R.drawable.ic_wallpaper)
        binding.ilImages.txtTitle.text = getString(R.string.label_images)

        // Saved Section
        binding.ilSaved.conBg.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.color_green)
        binding.ilSaved.ivOption.setImageResource(R.drawable.ic_saved)
        binding.ilSaved.txtTitle.text = getString(R.string.label_saved)

        // Bhajan Section
        binding.ilWishes.conBg.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.color_blue)
        binding.ilWishes.ivOption.setImageResource(R.drawable.ic_bhajan)
        binding.ilWishes.txtTitle.text = getString(R.string.label_wishes)

        // Ringtone Section
        binding.ilRingtone.conBg.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.color_pink)
        binding.ilRingtone.ivOption.setImageResource(R.drawable.ic_ringtone)
        binding.ilRingtone.txtTitle.text = getString(R.string.label_ringtone)

        // More Apps Section
        binding.ilPremium.conBg.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.color_yellow)
        binding.ilPremium.ivOption.setImageResource(R.drawable.ic_more_apps)
        binding.ilPremium.txtTitle.text = getString(R.string.label_premium_images)

        // Rate Us Section
        binding.ilRateUs.conBg.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.color_blue_light)
        binding.ilRateUs.ivOption.setImageResource(R.drawable.ic_rate_us)
        binding.ilRateUs.txtTitle.text = getString(R.string.label_rate_us)


//        binding.btnUnlock.isVisible = canWatchAd()

        // Hide unlock button if user is already premium
        updateUnlockButtonVisibility()

        billingManager.productDetails.observe(viewLifecycleOwner) { details ->
            if (details != null) {
                val price = details.oneTimePurchaseOfferDetails?.formattedPrice ?: "₹299"
                binding.btnPurchasePremium.text = "Go Premium – $price"
            } else {
                // Still loading
                binding.btnPurchasePremium.text = "Go Premium – Loading..."
                // Optional: Show progress or disable click temporarily
            }
        }
    }

    private fun initOnClickListener(){

        binding.ivMore.setOnClickListener {
            mActivity.binding.rootLayout.open()
        }

        binding.ilImages.conBg.setOnClickListener {
//            navigateToWallpaperListing()
            navigateToCategoryListing()
        }

        binding.ilSaved.conBg.setOnClickListener {
            navigateToSavedListing()
        }
        binding.ilWishes.conBg.setOnClickListener {
//            navigateToBhajanListing()
            navigateToCategoryListing(CategoryListingObj(isWishes = true))
        }

        binding.ilRingtone.conBg.setOnClickListener {
            navigateToRingtoneListing()
        }

        binding.ilPremium.conBg.setOnClickListener {
//            openPublisherOnPlayStore(mActivity)
            navigateToWallpaperListing(WallpaperListingObj(true,sharedPrefs.getUnlockedWallpaperCount(mActivity), BirthdayPerson.PREMIUM))
        }

        binding.ilRateUs.conBg.setOnClickListener {
            openMyAppOnPlayStore(mActivity)
        }

        binding.btnUnlock.setOnClickListener {
            if (!canWatchAd()) {
                Toast.makeText(mActivity,
                    getString(R.string.label_daily_limit_reached_max_5_ads), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            AdsManager.showRewarded(mActivity,
                onReward = {
                    incrementAdCount()
                    grantPremiumAccess()
                    Toast.makeText(mActivity,
                        getString(R.string.label_premium_unlocked_enjoy_10), Toast.LENGTH_LONG).show()
//                    updateUnlockButton()
                },
                onError = {
                    Toast.makeText(mActivity,
                        getString(R.string.label_ad_not_ready_yet), Toast.LENGTH_SHORT).show()
                })

        }

        // CHANGE: Premium section now launches purchase
        binding.btnPurchasePremium.setOnClickListener {
            isMessageShown = false
            val productDetails = billingManager.productDetails.value
            if (productDetails == null) {
                // Still loading → show message and do nothing
                Toast.makeText(mActivity, "Loading premium options... Please wait a moment", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ready → launch purchase
            billingManager.launchPurchaseFlow(mActivity, productDetails)
        }


    }

    // NEW: Called when user buys premium
    private fun onUserBecamePremium() {
        // Remove all ad limits and timers
        sharedPrefs.edit()
            .remove(KEY_ACCESS_EXPIRY)
            .remove(KEY_AD_COUNT)
            .remove(KEY_LAST_AD_DAY)
            .putInt(KEY_UNLOCKED_COUNT, Int.MAX_VALUE) // Unlock everything forever
            .apply()

        // Hide rewarded unlock button
        binding.btnUnlock.isVisible = false
        binding.btnPurchasePremium.isVisible = false

        // Show full premium access
        binding.linPremiumFeature.isVisible = true
        grantPremiumPurchaseAccess()

        // Optional: Change premium section text
        binding.btnPurchasePremium.text = getString(R.string.label_premium_unlocked)
        binding.btnPurchasePremium.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.color_green)
    }

    // NEW: Normal free user state
    private fun onUserNotPremium() {
        updateUnlockButtonVisibility()
        binding.btnPurchasePremium.text = getString(R.string.label_go_premium)
        binding.btnPurchasePremium.backgroundTintList = ContextCompat.getColorStateList(mActivity, R.color.color_light_yellow)
    }

    private fun updateUnlockButtonVisibility() {
        binding.btnUnlock.isVisible = canWatchAd()
    }

    private fun canWatchAd(): Boolean {
        val lastDay = sharedPrefs.getLong(KEY_LAST_AD_DAY, 0L)
        val currentDayStart = System.currentTimeMillis() - (System.currentTimeMillis() % (24 * 60 * 60 * 1000))  // Midnight today
        val adCount = if (lastDay == currentDayStart) {
            sharedPrefs.getInt(KEY_AD_COUNT, 0)
        } else {
            // New day → reset count to 0 and save it
            sharedPrefs.edit()
                .putLong(KEY_LAST_AD_DAY, currentDayStart)
                .putInt(KEY_AD_COUNT, 0)
                .apply()
            0  // New day, reset count
        }
        return adCount < MAX_ADS_PER_DAY
    }

    private fun incrementAdCount() {
        val currentDayStart = System.currentTimeMillis() - (System.currentTimeMillis() % (24 * 60 * 60 * 1000))
        sharedPrefs.edit()
            .putLong(KEY_LAST_AD_DAY, currentDayStart)
            .putInt(KEY_AD_COUNT, sharedPrefs.getInt(KEY_AD_COUNT, 0) + 1)
            .apply()
    }


    private fun grantPremiumAccess() {
        // Extend expiry to 24 hours from NOW (refreshes timer on each new ad)
        val expiryTime = System.currentTimeMillis() + (ACCESS_DURATION_HOURS * 60 * 60 * 1000)

        // Get current unlocked count
        var currentCount = sharedPrefs.getInt(KEY_UNLOCKED_COUNT, 0)

        // Add 10 more
        currentCount += UNLOCK_PER_AD

        // Optional: Cap at maximum (e.g., 50)
        if (currentCount > MAX_UNLOCKABLE) {
            currentCount = MAX_UNLOCKABLE
        }

        // Save both expiry and new count
        sharedPrefs.edit()
            .putLong(KEY_ACCESS_EXPIRY, expiryTime)
            .putInt(KEY_UNLOCKED_COUNT, currentCount)
            .apply()

        // Refresh UI
//        updateUnlockButton()
        loadPremiumWallpapers()  // This should now show up to currentCount wallpapers
    }

    private fun grantPremiumPurchaseAccess() {
        // Paid premium user → full permanent access
        // Set unlocked count to maximum (50)
        // Remove any expiry timer
        sharedPrefs.edit()
            .putInt(KEY_UNLOCKED_COUNT, MAX_UNLOCKABLE)  // 50
            .remove(KEY_ACCESS_EXPIRY)                   // No time limit
            .remove(KEY_AD_COUNT)                        // Optional: reset ad count
            .remove(KEY_LAST_AD_DAY)                     // Optional: clean up
            .apply()

        // Refresh UI
        loadPremiumWallpapers()  // Shows all 50
        binding.btnUnlock.isVisible = false  // Hide rewarded button forever
    }

    fun loadPremiumWallpapers(){
        binding.linPremiumFeature.isVisible = true
    }




}