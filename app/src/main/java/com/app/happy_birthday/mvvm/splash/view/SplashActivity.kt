package com.app.happy_birthday.mvvm.splash.view

import AppNavigation.navigateToHome
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View

import com.app.happy_birthday.databinding.ActivitySplashBinding
import com.app.happy_birthday.helper.BaseActivity
import com.app.happy_birthday.helper.Extensions.handler

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    override fun onStart() {
        super.onStart()
        // Branch init
        /** after adding branch key in strings uncomment this*/
        /*Branch.sessionBuilder(this)
            .withData(this.intent.data)
            .withCallback { referringParams, error ->
                initSaveDeepLink(referringParams, error)
            }.init()*/
    }

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeFields()
    }
    override fun getTopInsetView(): View? {
        return  binding.root
    }

    private fun initializeFields() {
        handler(3000) {
            navigateToHome { finish() }
        }
    }
}
