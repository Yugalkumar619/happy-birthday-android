package com.app.happy_birthday.mvvm.home.view

import AdsManager
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.ActivityHomeBinding
import com.app.happy_birthday.databinding.NavHeaderLayoutBinding
import com.app.happy_birthday.helper.BaseActivity
import com.app.happy_birthday.helper.Extensions.getAppVersionName
import com.app.happy_birthday.helper.Extensions.hideKeyboard
import com.app.happy_birthday.helper.Extensions.openMyAppOnPlayStore
import com.app.happy_birthday.helper.Extensions.openPrivacyPolicy
import com.app.happy_birthday.helper.Extensions.openPublisherOnPlayStore
import com.app.happy_birthday.helper.Extensions.printLog
import com.app.happy_birthday.helper.Extensions.toastRamWallpaperFolder
import com.app.happy_birthday.helper.Global.showSnackBar
import com.app.happy_birthday.helper.ShareDetails.initSaveDeepLink
import com.google.android.gms.ads.MobileAds
import com.pushwoosh.Pushwoosh
import org.json.JSONObject

class HomeActivity : BaseActivity() {

    lateinit var binding: ActivityHomeBinding
    private lateinit var headerLayout: NavHeaderLayoutBinding
    private var isBackPressed: Long = 0
    private var currentMenuItemId: Int? = 0

    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onStart() {
        super.onStart()
        /** after adding branch key in strings uncomment this*/
        /*Branch.sessionBuilder(this)
            .withData(this.intent.data)
            .withCallback { referringParams, error ->
                initSaveDeepLink(referringParams, error)
            }.init()*/
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        /** after adding branch key in strings uncomment this*/
        /*Branch.sessionBuilder(this)
            .withData(intent?.data)
            .withCallback { referringParams, error ->
                initSaveDeepLink(referringParams, error)
            }.reInit()*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this) {  // Callback for init complete
            Log.d("AdMob", "Initialized")
        }

        binding.root.post {
            // Optional: preload interstitial/
            AdsManager.loadInterstitial(this)
            AdsManager.loadRewarded(this)
        }


//        val requestConfiguration = RequestConfiguration.Builder()
//            .setTestDeviceIds(listOf("YOUR_DEVICE_ID_HERE"))  // Get from Logcat when running app
//            .build()
//        MobileAds.setRequestConfiguration(requestConfiguration)

        initBottomTabs()
        initLeftNavMenuDrawer()
        initializeFields()
        initializeToolbar()
        onClickListeners()
    }

    override fun getTopInsetView(): View? {
        return  binding.conMain
    }

    private fun initLeftNavMenuDrawer() {
        binding.navigationView.setupWithNavController(navController)
        headerLayout = binding.headerLayout

        binding.headerLayout.ilInviteFriend.txtTitle.text = getString(R.string.label_invite_friend)
        binding.headerLayout.ilInviteFriend.ivSideMenu.setImageResource(R.drawable.ic_invite_friend)

        binding.headerLayout.ilRateApp.txtTitle.text = getString(R.string.label_rate_app)
        binding.headerLayout.ilRateApp.ivSideMenu.setImageResource(R.drawable.ic_rate_app)

        binding.headerLayout.ilMoreApps.txtTitle.text = getString(R.string.label_more_app)
        binding.headerLayout.ilMoreApps.ivSideMenu.setImageResource(R.drawable.ic_more_app_black)

        binding.headerLayout.ilPrivacyPolicy.txtTitle.text = getString(R.string.label_privacy_policy)
        binding.headerLayout.ilPrivacyPolicy.ivSideMenu.setImageResource(R.drawable.ic_privacy_policy)

        binding.headerLayout.ilLocation.txtTitle.text = getString(R.string.label_location)
        binding.headerLayout.ilLocation.ivSideMenu.setImageResource(R.drawable.ic_file_location)

        binding.headerLayout.txtVersion.text =
            getString(R.string.label_version, getAppVersionName())

//        binding.headerLayout.ilClearCache.txtTitle.text = getString(R.string.label_clear_cache)


        binding.rootLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
               binding.rootLayout.hideKeyboard()
            }
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}
        })

        onLeftNavMenuDrawerClickListener()
    }

    private fun onLeftNavMenuDrawerClickListener() {
    }

    private fun initBottomTabs() {
        binding.bottomNavigationView.setupWithNavController(navController)
        onBottomNavigationItemClickListener()
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp(
            AppBarConfiguration(
                topLevelDestinationIds = setOf(
                    R.id.nav_graph_home,
                    R.id.nav_graph_2,
                    R.id.nav_graph_3,
                    R.id.nav_graph_4
                ), fallbackOnNavigateUpListener = ::onSupportNavigateUp
            )
        )
    }

    private fun onClickListeners() {

        binding.headerLayout.ilMoreApps.root.setOnClickListener {
            openPublisherOnPlayStore(this)
        }

        binding.headerLayout.ilLocation.root.setOnClickListener {
            toastRamWallpaperFolder()
        }

        binding.headerLayout.ilInviteFriend.root.setOnClickListener {
            share(this.contentResolver)
        }

        binding.headerLayout.ilPrivacyPolicy.root.setOnClickListener {
            openPrivacyPolicy(this)
        }

        binding.headerLayout.ilRateApp.root.setOnClickListener {
            openMyAppOnPlayStore(this)
        }
    }

    private fun initializeToolbar() {
        toolbarInit(getString(R.string.app_name))
    }

    private fun initializeFields() {
        getIntentData()
        onBackPressedDispatcher.addCallback(this@HomeActivity, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                onBackPressedCallback()
            }
        })
    }

    private fun getIntentData() {
        if (intent.hasExtra(Pushwoosh.PUSH_RECEIVE_EVENT)) {
            val referringParams = JSONObject(intent.getStringExtra(Pushwoosh.PUSH_RECEIVE_EVENT).toString())
            referringParams.toString().printLog("PUSH DETAILS")
            initSaveDeepLink(referringParams , null)
        }
    }



    fun onBackPressedCallback() {
        if (navController.currentDestination?.id == R.id.navigation_home) {
            if (isBackPressed + 2000 > System.currentTimeMillis()) {
                finish()
            } else {
                binding.root.showSnackBar(getString(R.string.press_back_message))
                isBackPressed = System.currentTimeMillis()
            }
        } else {
            navController.navigateUp()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun toolbarInit(title: String){
//        setUpToolbar(binding.layoutToolbar,
//            title = title,
//            isBackArrow = false,
//            toolbarClickListener = object : CommonInterfaceClickEvent {
//                override fun onToolBarListener(type: String) {
//                    if (type == Constants.TOOLBAR_ICON_ONE){
//
//                    }
//                    if (type == Constants.TOOLBAR_ICON_TWO){
//
//                    }
//                }
//            }
//        )
    }

    fun share(contentResolver: ContentResolver) {
        val options = BitmapFactory.Options().apply {
            inScaled = false
        }
        val bitPhoto = BitmapFactory.decodeResource(resources, R.drawable.img_boyfriend_7, options)
        val icon: Bitmap = bitPhoto
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"

        share.putExtra(
            Intent.EXTRA_TEXT,
            getString(
                R.string.label_check_out_app,
                getString(R.string.app_name),
                packageName
            )

        )
        startActivity(Intent.createChooser(share, getString(R.string.label_share_image)))
    }


    private fun onBottomNavigationItemClickListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentMenuItemId = destination.id
            when (destination.id) {
                R.id.navigation_home -> {
                    toolbarInit(getString(R.string.app_name))
                }

                else -> {
//                    setUpToolbar(binding.layoutToolbar, title = getString(R.string.app_name), isBackArrow = false)
                }
            }
        }
    }
}