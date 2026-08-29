package com.app.happy_birthday.mvvm.intro.view

import AppNavigation.navigateToLogin
import android.os.Bundle
import android.view.View
import com.app.happy_birthday.databinding.ActivityIntroBinding
import com.app.happy_birthday.helper.AppController
import com.app.happy_birthday.helper.BaseActivity

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClickListeners()
    }

    override fun getTopInsetView(): View? {
        return  binding.root
    }

    private fun onClickListeners(){
        binding.btnEnglish.setOnClickListener {
            AppController.instance.englishLanguage()
            navigateToLogin()
        }
        binding.btnArabic.setOnClickListener {
            AppController.instance.arabicLanguage()
            navigateToLogin()
        }
    }
}