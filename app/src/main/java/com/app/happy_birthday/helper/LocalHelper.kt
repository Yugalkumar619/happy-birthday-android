package com.app.happy_birthday.helper

import android.content.Context
import android.content.res.Configuration
import com.app.happy_birthday.helper.PrefUtils.getAppConfig
import com.app.happy_birthday.helper.PrefUtils.setAppConfig
import com.app.happy_birthday.helper.helper_model.AppConfigModel
import com.app.happy_birthday.helper.helper_model.ValentineDay
import com.app.happy_birthday.mvvm.wishes.model.WishesDataModel
import com.app.happy_birthday.mvvm.ringtone.model.RingtoneDataModel
import java.util.Locale

object LocaleHelper {

    fun changeLanguage(context: Context) {
        if (context.getAppConfig()?.lang == "en") {
            setLocale(context , "ar")
        } else {
            setLocale(context, "en")
        }
    }


    // the method is used to set the language at runtime
    fun setLocale(context: Context, language: String): Context {
        setInPref(context, language)

        // updating the language for devices above android nougat
        return updateResources(context, language)
        // for devices having lower version of android os
    }

    private fun setInPref(context: Context, language: String) {
        val model = context.getAppConfig()
        context.setAppConfig(AppConfigModel(lang = language, cartBadgeCount = model?.cartBadgeCount ?: ""))
    }

    // the method is used update the language of application by creating
    // object of inbuilt Locale class and passing language argument to it
    private fun updateResources(context: Context, language: String): Context {
        /*val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)*/

        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        return context
    }

    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    fun getAllBhajan(day: ValentineDay): ArrayList<WishesDataModel> {
        val list = ArrayList<WishesDataModel>()

        when(day){
            ValentineDay.BOYFRIEND -> {
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 💖 May your life bloom with love ❤️ and happiness 😊."))
                list.add(WishesDataModel(description = "🌹 A rose for you on Rose Day 🌸 to express my feelings 💕."))
                list.add(WishesDataModel(description = "💐 Happy Rose Day! 🌹 May love always surround you ❤️."))
                list.add(WishesDataModel(description = "🌹 Sending you a rose full of love 💖 and care 🤗. Happy Rose Day!"))
                list.add(WishesDataModel(description = "🌸 Happy Rose Day! 😊 Let love blossom in your heart ❤️."))
                list.add(WishesDataModel(description = "🌹 One rose 🌹 can say more than a thousand words 💌. Happy Rose Day!"))
                list.add(WishesDataModel(description = "💖 Happy Rose Day! 🌸 May your life be as beautiful as roses 🌹."))
                list.add(WishesDataModel(description = "🌹 This Rose Day, I send you love ❤️ wrapped in petals 🌸."))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 😊 You make life colorful like a rose 🌈."))
                list.add(WishesDataModel(description = "💐 A rose 🌹 for the one who makes my heart smile 😍. Happy Rose Day!"))

                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 💕 May every petal bring joy 😄."))
                list.add(WishesDataModel(description = "🌸 Sending roses 🌹 and warm wishes 🤍 this Rose Day!"))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 💖 Let love grow stronger every day 🌱."))
                list.add(WishesDataModel(description = "💐 Roses are red 🌹 and so is my love ❤️. Happy Rose Day!"))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 😊 May your heart bloom with happiness 🌸."))
                list.add(WishesDataModel(description = "🌸 A rose 🌹 to remind you how special you are 💖."))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 💕 Let this rose carry my feelings 💌."))
                list.add(WishesDataModel(description = "💐 May your Rose Day 🌹 be filled with smiles 😄 and love ❤️."))
                list.add(WishesDataModel(description = "🌹 Sending you a virtual rose 🌸 with lots of love 💖."))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 😊 You are my favorite flower 🌸."))

                list.add(WishesDataModel(description = "🌸 Happy Rose Day! 💖 Love is in the air 🌹."))
                list.add(WishesDataModel(description = "🌹 A rose 🌹 for the one who makes my days brighter ☀️."))
                list.add(WishesDataModel(description = "💐 Happy Rose Day! 😊 Let romance bloom 💕."))
                list.add(WishesDataModel(description = "🌹 May this Rose Day 🌸 bring love ❤️ and warmth 🤗."))
                list.add(WishesDataModel(description = "🌸 Roses speak the language of love 💌. Happy Rose Day!"))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 💖 Sending sweet floral vibes 🌸."))
                list.add(WishesDataModel(description = "💐 A rose 🌹 just for you, with love ❤️."))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 😊 May your life stay colorful 🌈."))
                list.add(WishesDataModel(description = "🌸 Let love blossom 🌹 in your heart today 💖."))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 💕 You make my world bloom 🌸."))

                list.add(WishesDataModel(description = "💐 Roses are symbols of love 🌹. Happy Rose Day!"))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 😊 May your smile shine brighter 🌟."))
                list.add(WishesDataModel(description = "🌸 A single rose 🌹 can express a thousand emotions 💖."))
                list.add(WishesDataModel(description = "🌹 Sending roses 🌸 and positive vibes ✨. Happy Rose Day!"))
                list.add(WishesDataModel(description = "💐 Happy Rose Day! 💕 Keep spreading love ❤️."))
                list.add(WishesDataModel(description = "🌹 May this Rose Day 🌸 fill your heart with joy 😄."))
                list.add(WishesDataModel(description = "🌸 A rose 🌹 to celebrate love and friendship 🤝."))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 💖 Love always wins ❤️."))
                list.add(WishesDataModel(description = "💐 Roses and smiles 🌹 make the best combo 😊."))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 🌸 Stay blessed and loved 💕."))

                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 💖 May love bloom endlessly in your heart 🌸."))
                list.add(WishesDataModel(description = "🌸 Sending you roses 🌹 filled with warmth 🤍 and care 😊."))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 😍 A rose to brighten your beautiful smile 🌸."))
                list.add(WishesDataModel(description = "💐 May this Rose Day 🌹 add colors 🌈 of love to your life ❤️."))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 💕 Let every rose remind you of love 💌."))
                list.add(WishesDataModel(description = "🌸 A rose 🌹 to say you mean everything to me 💖."))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! 😊 May your days be as lovely as roses 🌸."))
                list.add(WishesDataModel(description = "💐 Roses bloom 🌹 just like my feelings for you ❤️. Happy Rose Day!"))
                list.add(WishesDataModel(description = "🌹 Sending a bundle of roses 🌸 and lots of love 💕 your way."))
                list.add(WishesDataModel(description = "🌹 Happy Rose Day! ✨ May your life always smell like roses 🌸."))

            }

            ValentineDay.GIRLFRIEND -> {

                list.add(WishesDataModel(description = "💍 Happy Propose Day! ❤️ Will you be mine forever? 😊"))
                list.add(WishesDataModel(description = "💖 On this Propose Day 💍, I choose you today and always ♾️."))
                list.add(WishesDataModel(description = "💍 Happy Propose Day! 😍 My heart says only your name ❤️."))
                list.add(WishesDataModel(description = "🌹 This Propose Day 💍, let me be the reason for your smile 😊."))
                list.add(WishesDataModel(description = "💖 Happy Propose Day! 💍 One question, one lifetime ❤️."))

                list.add(WishesDataModel(description = "💍 I may not be perfect 😌, but my love for you is ❤️. Happy Propose Day!"))
                list.add(WishesDataModel(description = "💕 Happy Propose Day! 💍 Can I hold your hand forever? 🤝"))
                list.add(WishesDataModel(description = "💍 On Propose Day 💖, I promise to love you endlessly ♾️."))
                list.add(WishesDataModel(description = "😍 Happy Propose Day! 💍 My heart already belongs to you ❤️."))
                list.add(WishesDataModel(description = "💖 Let this Propose Day 💍 be the start of our forever 🥰."))

                list.add(WishesDataModel(description = "💍 Happy Propose Day! 😊 Say yes to a lifetime of love ❤️."))
                list.add(WishesDataModel(description = "💕 I propose my heart 💍 to you today and always 😍."))
                list.add(WishesDataModel(description = "💍 Happy Propose Day! ❤️ You are my today and all tomorrows ♾️."))
                list.add(WishesDataModel(description = "🌹 On this Propose Day 💖, my heart beats only for you 💓."))
                list.add(WishesDataModel(description = "💍 Happy Propose Day! 😄 Will you walk this journey with me? 🚶‍♂️🚶‍♀️"))

                list.add(WishesDataModel(description = "💖 My love grows stronger every day 💍. Happy Propose Day!"))
                list.add(WishesDataModel(description = "💍 Happy Propose Day! ❤️ Let’s make forever beautiful together ✨."))
                list.add(WishesDataModel(description = "😍 I found my happiness in you 💖. Happy Propose Day!"))
                list.add(WishesDataModel(description = "💍 On Propose Day 💕, I offer you my heart 💓 and soul."))
                list.add(WishesDataModel(description = "💖 Happy Propose Day! 😊 You complete my world 🌍."))

                list.add(WishesDataModel(description = "💍 Will you be my always? ❤️ Happy Propose Day!"))
                list.add(WishesDataModel(description = "💕 Happy Propose Day! 💍 Loving you is my best decision 😍."))
                list.add(WishesDataModel(description = "💍 On this special day 💖, I choose love, I choose you ❤️."))
                list.add(WishesDataModel(description = "😊 Happy Propose Day! 💍 Together is my favorite place 💕."))
                list.add(WishesDataModel(description = "💖 Let me be yours 💍 today, tomorrow, forever ♾️."))

                list.add(WishesDataModel(description = "💍 Happy Propose Day! 😍 My heart smiles when I see you 😊."))
                list.add(WishesDataModel(description = "💕 On Propose Day 💍, I promise to cherish you always ❤️."))
                list.add(WishesDataModel(description = "💍 Happy Propose Day! 💖 You are my dream come true ✨."))
                list.add(WishesDataModel(description = "😍 I don’t need a reason 💕, I just need you 💍. Happy Propose Day!"))
                list.add(WishesDataModel(description = "💖 My forever starts with you 💍. Happy Propose Day!"))

                list.add(WishesDataModel(description = "💍 Happy Propose Day! ❤️ Let love write our story 📖."))
                list.add(WishesDataModel(description = "😊 On this Propose Day 💖, I give you my heart 💓."))
                list.add(WishesDataModel(description = "💍 Happy Propose Day! 😍 Say yes to love ❤️."))
                list.add(WishesDataModel(description = "💕 I propose a lifetime of smiles 😊 and love 💍."))
                list.add(WishesDataModel(description = "💖 Happy Propose Day! 💍 You are the one I’ve been waiting for ⏳."))

                list.add(WishesDataModel(description = "💍 From this moment on 💕, my heart is yours ❤️. Happy Propose Day!"))
                list.add(WishesDataModel(description = "😍 Happy Propose Day! 💍 Together we are unstoppable 💪."))
                list.add(WishesDataModel(description = "💖 Let’s turn today into forever 💍. Happy Propose Day!"))
                list.add(WishesDataModel(description = "💍 Happy Propose Day! 😊 I choose you again and again ❤️."))
                list.add(WishesDataModel(description = "💕 With all my heart 💓, I propose my love to you 💍. Happy Propose Day!"))

            }

            ValentineDay.FRIENDS -> {
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 😍 May your day be filled with sweetness ❤️."))
                list.add(WishesDataModel(description = "🍫 Sending you chocolates and love 💖 this Chocolate Day!"))
                list.add(WishesDataModel(description = "🍬 Happy Chocolate Day! 😊 Because love tastes better with chocolate ❤️."))
                list.add(WishesDataModel(description = "🍫 A little chocolate, a lot of love 💕. Happy Chocolate Day!"))
                list.add(WishesDataModel(description = "🍩 Happy Chocolate Day! 😄 Let sweetness melt your heart ❤️."))

                list.add(WishesDataModel(description = "🍫 On Chocolate Day 🍬, I’m sending you sweet smiles 😊 and love 💖."))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 😍 Life is sweeter with you and chocolate ❤️."))
                list.add(WishesDataModel(description = "🍬 Chocolate + Love = Happiness 😄. Happy Chocolate Day!"))
                list.add(WishesDataModel(description = "🍫 May your Chocolate Day 🍬 be full of joy 😊 and sweetness 💕."))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! ❤️ Sharing sweetness, sharing love 😍."))

                list.add(WishesDataModel(description = "🍬 Chocolate makes everything better 😄. Happy Chocolate Day!"))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 💖 Sweet treats for a sweet soul 😊."))
                list.add(WishesDataModel(description = "🍩 Sending you chocolates 🍫 wrapped in love 💕."))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 😍 May love melt like chocolate ❤️."))
                list.add(WishesDataModel(description = "🍬 Life is dull without chocolate 🍫. Happy Chocolate Day!"))

                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 😊 A bite of sweetness and a heart full of love 💖."))
                list.add(WishesDataModel(description = "🍫 Let this Chocolate Day 🍬 melt all worries away 😄."))
                list.add(WishesDataModel(description = "🍩 Chocolates speak the language of love ❤️. Happy Chocolate Day!"))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 💕 Sweet moments, sweet memories 😍."))
                list.add(WishesDataModel(description = "🍬 Sending chocolaty hugs 🍫 and smiles 😊."))

                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 😄 Because you deserve extra sweetness ❤️."))
                list.add(WishesDataModel(description = "🍬 Chocolate is my gift 🍫, love is my promise 💖."))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 😍 Love wrapped in cocoa ❤️."))
                list.add(WishesDataModel(description = "🍩 May this Chocolate Day 🍫 fill your heart with joy 😊."))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 💖 Sweetness that never fades 😄."))

                list.add(WishesDataModel(description = "🍬 A chocolate a day 🍫 keeps sadness away 😊. Happy Chocolate Day!"))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 😍 Sending sweet vibes only ❤️."))
                list.add(WishesDataModel(description = "🍩 Chocolate makes love stronger 🍫. Happy Chocolate Day!"))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 💕 Smile, it’s chocolate time 😄."))
                list.add(WishesDataModel(description = "🍬 Love feels sweeter with chocolate 🍫. Happy Chocolate Day!"))

                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 😍 One bite, endless happiness ❤️."))
                list.add(WishesDataModel(description = "🍩 Chocolates and love 🍫 make the perfect combo 💖."))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 😊 Let sweetness rule today ❤️."))
                list.add(WishesDataModel(description = "🍬 Sending you chocolate kisses 🍫 and love 😍."))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 💕 Melt hearts, not chocolates 😄."))

                list.add(WishesDataModel(description = "🍩 A chocolate gift 🍫 to say I care 💖. Happy Chocolate Day!"))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 😍 Sweet moments are better shared ❤️."))
                list.add(WishesDataModel(description = "🍬 Chocolate is happiness 🍫 in edible form 😄."))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 💖 Because love should be sweet 😊."))
                list.add(WishesDataModel(description = "🍩 Sending chocolaty smiles 🍫 your way 😍. Happy Chocolate Day!"))

                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 😄 Life is sweeter with you ❤️."))
                list.add(WishesDataModel(description = "🍬 Chocolate today 🍫, love forever 💕. Happy Chocolate Day!"))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 😍 Sweeten your day with love ❤️."))
                list.add(WishesDataModel(description = "🍩 Chocolates 🍫 and hearts 💖 go perfectly together."))
                list.add(WishesDataModel(description = "🍫 Happy Chocolate Day! 😊 Keep smiling and stay sweet ❤️."))

            }

            ValentineDay.BROTHER -> {
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 💖 May your life be filled with warm hugs 🤗."))
                list.add(WishesDataModel(description = "🧸 Sending you a teddy full of love ❤️. Happy Teddy Day!"))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 😊 A soft hug to brighten your day 💕."))
                list.add(WishesDataModel(description = "🧸 On Teddy Day 🧸, I’m sending you cuddles and smiles 😄."))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 💖 A teddy to remind you of my love ❤️."))

                list.add(WishesDataModel(description = "🧸 Life feels cozier with you 😊. Happy Teddy Day!"))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 🤗 May your heart feel warm and loved 💕."))
                list.add(WishesDataModel(description = "🧸 Sending a fluffy teddy 🧸 and lots of love ❤️."))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 😍 Because you deserve endless hugs 🤗."))
                list.add(WishesDataModel(description = "🧸 A teddy hug 🧸 just for you 💖. Happy Teddy Day!"))

                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 😊 Soft toys and softer feelings 💕."))
                list.add(WishesDataModel(description = "🧸 May this Teddy Day 🧸 fill your heart with joy 😄."))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! ❤️ Sending you comfort and care 🤍."))
                list.add(WishesDataModel(description = "🧸 A teddy 🧸 to say I’m always with you 💖."))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 😄 Keep smiling and stay cozy 🤗."))

                list.add(WishesDataModel(description = "🧸 Teddy bears 🧸 are sweet, just like you ❤️. Happy Teddy Day!"))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 😊 A cuddle that lasts forever 💕."))
                list.add(WishesDataModel(description = "🧸 Sending you teddy hugs 🧸 and happy vibes 😍."))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 💖 You make my world warmer 🤗."))
                list.add(WishesDataModel(description = "🧸 A teddy 🧸 full of love and happiness ❤️."))

                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 😄 May your day be soft and sweet 💕."))
                list.add(WishesDataModel(description = "🧸 Teddy hugs 🧸 are the best kind of hugs 🤗. Happy Teddy Day!"))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! ❤️ Sending you a bundle of cuddles 😊."))
                list.add(WishesDataModel(description = "🧸 You’re never alone 🧸. Happy Teddy Day!"))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 💖 A teddy to keep you smiling 😄."))

                list.add(WishesDataModel(description = "🧸 On Teddy Day 🧸, I wish you comfort and happiness ❤️."))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 😊 May love hug you tight 🤗."))
                list.add(WishesDataModel(description = "🧸 A teddy 🧸 and a heart full of love 💖."))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 😍 Soft hugs, sweet moments ❤️."))
                list.add(WishesDataModel(description = "🧸 Sending you warmth and teddy love 🧸."))

                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 💕 May cuddles chase worries away 😄."))
                list.add(WishesDataModel(description = "🧸 Teddy bears 🧸 make everything better 😊. Happy Teddy Day!"))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! ❤️ Wrap yourself in love 🤗."))
                list.add(WishesDataModel(description = "🧸 A teddy hug 🧸 to make your heart smile 💖."))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 😄 Stay cute and cozy ❤️."))

                list.add(WishesDataModel(description = "🧸 Sending teddy vibes 🧸 and endless love 💕."))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 😊 Love feels warmer today 🤗."))
                list.add(WishesDataModel(description = "🧸 A teddy 🧸 just to say I care ❤️."))
                list.add(WishesDataModel(description = "🧸 Happy Teddy Day! 💖 Cuddles make everything brighter 😍."))
                list.add(WishesDataModel(description = "🧸 Keep calm and hug a teddy 🧸. Happy Teddy Day!"))

            }

            ValentineDay.SISTER -> {
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 💖 I promise to stand by you always ❤️."))
                list.add(WishesDataModel(description = "🤝 On Promise Day 💕, I promise my heart to you forever ♾️."))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 😊 A promise made with love ❤️."))
                list.add(WishesDataModel(description = "🤝 I promise to choose you every day 💖. Happy Promise Day!"))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 💕 Together is my favorite promise 😊."))

                list.add(WishesDataModel(description = "🤝 On this Promise Day 💖, I promise to never give up on us ❤️."))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 😍 My promises are sealed with love 💕."))
                list.add(WishesDataModel(description = "🤝 I promise to make you smile 😊 always. Happy Promise Day!"))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! ❤️ A bond built on trust and love 🤍."))
                list.add(WishesDataModel(description = "🤝 Promise Day reminds me how lucky I am to have you 💖."))

                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 😊 I promise to hold your hand forever 🤝."))
                list.add(WishesDataModel(description = "🤝 On Promise Day 💕, my heart makes a vow to yours ❤️."))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 😄 Love grows stronger with promises 💖."))
                list.add(WishesDataModel(description = "🤝 I promise to be your safe place 🤍. Happy Promise Day!"))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! ❤️ Together through every storm 🌧️."))

                list.add(WishesDataModel(description = "🤝 A promise today 💖, a lifetime of love tomorrow ♾️."))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 😊 I promise to care and cherish you ❤️."))
                list.add(WishesDataModel(description = "🤝 Promises made with love 💕 never fade. Happy Promise Day!"))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 😍 My promise is simple—it's you ❤️."))
                list.add(WishesDataModel(description = "🤝 I promise to be honest, loyal, and loving 💖."))

                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 😊 Let’s build forever together ❤️."))
                list.add(WishesDataModel(description = "🤝 On Promise Day 💕, I vow to protect your smile 😊."))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! ❤️ Trust, love, and togetherness 🤝."))
                list.add(WishesDataModel(description = "🤝 I promise to walk beside you always 🚶‍♂️🚶‍♀️."))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 💖 My love is my promise to you ♾️."))

                list.add(WishesDataModel(description = "🤝 A promise sealed with my heart ❤️. Happy Promise Day!"))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 😊 I promise to make you feel loved 💕."))
                list.add(WishesDataModel(description = "🤝 On this Promise Day 💖, my forever begins with you ❤️."))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 😍 Promises make love stronger 💪."))
                list.add(WishesDataModel(description = "🤝 I promise to be your strength and support 🤍."))

                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 😊 A vow of love, care, and trust ❤️."))
                list.add(WishesDataModel(description = "🤝 Promises are small words with big meanings 💖."))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! ❤️ My heart is committed to you ♾️."))
                list.add(WishesDataModel(description = "🤝 I promise to love you more each day 😊. Happy Promise Day!"))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 💕 Our bond grows stronger today 🤝."))

                list.add(WishesDataModel(description = "🤝 A promise made in love ❤️ lasts forever ♾️."))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 😊 I vow to keep you happy 💖."))
                list.add(WishesDataModel(description = "🤝 On Promise Day 💕, I promise my forever to you ❤️."))
                list.add(WishesDataModel(description = "🤝 Happy Promise Day! 😄 Love begins with trust 🤍."))
                list.add(WishesDataModel(description = "🤝 My promise is simple—always you ❤️. Happy Promise Day!"))

            }

            ValentineDay.WIFE -> {

                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 💖 May your day be filled with warmth and love ❤️."))
                list.add(WishesDataModel(description = "🤗 Sending you a big warm hug 🤍. Happy Hug Day!"))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😊 A hug to make everything feel better 💕."))
                list.add(WishesDataModel(description = "🤗 On Hug Day 💖, here’s a hug full of care and comfort 🤍."))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! ❤️ Because hugs say what words cannot 😊."))

                list.add(WishesDataModel(description = "🤗 A tight hug 🤍 to remind you how loved you are ❤️. Happy Hug Day!"))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😊 Let my hug take away all your worries 💕."))
                list.add(WishesDataModel(description = "🤗 Sending you a hug filled with love ❤️ and peace 🤍."))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😍 Wrapped in love and warmth 💖."))
                list.add(WishesDataModel(description = "🤗 One hug 🤍 can change the whole day ❤️. Happy Hug Day!"))

                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😊 May love hold you close today 💕."))
                list.add(WishesDataModel(description = "🤗 A hug 🤍 to say I care ❤️. Happy Hug Day!"))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😄 Sending cozy vibes and love 💖."))
                list.add(WishesDataModel(description = "🤗 On this Hug Day 💕, feel the comfort of my arms 🤍."))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! ❤️ Because you deserve endless hugs 😊."))

                list.add(WishesDataModel(description = "🤗 Hugs are my favorite way to say I love you ❤️. Happy Hug Day!"))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😊 May your heart feel lighter 💕."))
                list.add(WishesDataModel(description = "🤗 Sending a soft hug 🤍 and sweet smiles 😊."))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😍 Love feels better in a hug 💖."))
                list.add(WishesDataModel(description = "🤗 A warm hug 🤍 just for you ❤️. Happy Hug Day!"))

                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😊 Let love wrap you tight 💕."))
                list.add(WishesDataModel(description = "🤗 A hug 🤍 to chase all sadness away ❤️."))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😄 Comfort begins with a hug 💖."))
                list.add(WishesDataModel(description = "🤗 On Hug Day 💕, I’m holding you close in my thoughts 🤍."))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! ❤️ Hugs make everything brighter 😊."))

                list.add(WishesDataModel(description = "🤗 A hug 🤍 is the safest place to be ❤️. Happy Hug Day!"))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😊 Sending love through this hug 💕."))
                list.add(WishesDataModel(description = "🤗 Wrapped in care 🤍 and love ❤️. Happy Hug Day!"))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😍 One hug, endless comfort 💖."))
                list.add(WishesDataModel(description = "🤗 A gentle hug 🤍 to remind you you’re not alone ❤️."))

                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😊 Hugs heal hearts 💕."))
                list.add(WishesDataModel(description = "🤗 A hug 🤍 to make your heart smile ❤️. Happy Hug Day!"))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😄 May love surround you today 💖."))
                list.add(WishesDataModel(description = "🤗 Sending you hugs 🤍 full of happiness 😊."))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! ❤️ Because everyone needs a hug today 🤗."))

                list.add(WishesDataModel(description = "🤗 A warm hug 🤍 and lots of love ❤️. Happy Hug Day!"))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😊 Let my hug be your comfort 💕."))
                list.add(WishesDataModel(description = "🤗 Hugs speak louder than words ❤️. Happy Hug Day!"))
                list.add(WishesDataModel(description = "🤗 Happy Hug Day! 😍 Sending cuddles and care 🤍."))
                list.add(WishesDataModel(description = "🤗 One hug 🤍 can say everything ❤️. Happy Hug Day!"))

            }

            ValentineDay.SON -> {

                list.add(WishesDataModel(description = "💋 Happy Kiss Day! ❤️ One kiss, endless love 😘."))
                list.add(WishesDataModel(description = "💋 Sending you a sweet kiss 😘 filled with love ❤️. Happy Kiss Day!"))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😊 Let love be sealed with a kiss ❤️."))
                list.add(WishesDataModel(description = "💋 On Kiss Day 😘, here’s a kiss straight from my heart ❤️."))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😍 A kiss to make your day brighter ✨."))

                list.add(WishesDataModel(description = "💋 One kiss 😘 can say what words can’t ❤️. Happy Kiss Day!"))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😊 Sending soft kisses and warm love 💕."))
                list.add(WishesDataModel(description = "💋 A kiss 😘 to remind you how special you are ❤️."))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😍 Let love begin with a kiss 💖."))
                list.add(WishesDataModel(description = "💋 On this Kiss Day 😘, my love reaches you ❤️."))

                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😊 A kiss full of care and affection 💕."))
                list.add(WishesDataModel(description = "💋 Sending you gentle kisses 😘 and sweet thoughts ❤️."))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😄 One kiss, a thousand feelings 💖."))
                list.add(WishesDataModel(description = "💋 A loving kiss 😘 just for you ❤️. Happy Kiss Day!"))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😍 Kisses make love stronger 💪."))

                list.add(WishesDataModel(description = "💋 A kiss 😘 to melt all worries away ❤️."))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😊 Let hearts meet with a kiss 💕."))
                list.add(WishesDataModel(description = "💋 Sending kisses 😘 wrapped in love ❤️."))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😍 Love tastes sweeter with a kiss 💖."))
                list.add(WishesDataModel(description = "💋 On Kiss Day 😘, feel my love in every kiss ❤️."))

                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😊 A kiss that speaks love 💕."))
                list.add(WishesDataModel(description = "💋 One soft kiss 😘 can light up the heart ❤️."))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😄 Kisses create beautiful memories 💖."))
                list.add(WishesDataModel(description = "💋 Sending warm kisses 😘 and loving vibes ❤️."))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😍 A kiss to seal our love 💞."))

                list.add(WishesDataModel(description = "💋 A kiss 😘 to say I care ❤️. Happy Kiss Day!"))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😊 Love starts with a simple kiss 💕."))
                list.add(WishesDataModel(description = "💋 Sending you sweet kisses 😘 all day long ❤️."))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😍 One kiss, pure magic ✨."))
                list.add(WishesDataModel(description = "💋 On this Kiss Day 😘, let love touch your heart ❤️."))

                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😊 A kiss that lingers in the heart 💖."))
                list.add(WishesDataModel(description = "💋 Gentle kisses 😘 and endless love ❤️."))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😄 Kisses make moments unforgettable 💕."))
                list.add(WishesDataModel(description = "💋 A loving kiss 😘 just to make you smile 😊."))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😍 Every kiss tells a love story 📖."))

                list.add(WishesDataModel(description = "💋 A kiss 😘 to remind you you’re loved ❤️."))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😊 Let love flow with every kiss 💕."))
                list.add(WishesDataModel(description = "💋 Sending soft kisses 😘 across the distance ❤️."))
                list.add(WishesDataModel(description = "💋 Happy Kiss Day! 😍 One kiss, forever feelings 💖."))
                list.add(WishesDataModel(description = "💋 Kisses speak the language of love ❤️. Happy Kiss Day!"))

            }

            ValentineDay.DAD -> {

                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 💖 You make my world brighter 😊."))
                list.add(WishesDataModel(description = "❤️ Wishing you a Valentine’s Day filled with love 💕 and smiles 😄."))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😍 You are my favorite feeling 💖."))
                list.add(WishesDataModel(description = "❤️ On Valentine’s Day 💕, my heart chooses you ❤️."))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😊 Love feels perfect with you 💞."))

                list.add(WishesDataModel(description = "❤️ You are my today and all my tomorrows 💖. Happy Valentine’s Day!"))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😄 My heart beats for you 💓."))
                list.add(WishesDataModel(description = "❤️ Sending you love 💕, hugs 🤗, and smiles 😊 today."))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😍 You are my forever ♾️."))
                list.add(WishesDataModel(description = "❤️ On this special day 💖, I’m grateful for you ❤️."))

                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😊 Love looks beautiful on you 💕."))
                list.add(WishesDataModel(description = "❤️ My heart feels at home with you 💖. Happy Valentine’s Day!"))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😄 You make life sweeter 💞."))
                list.add(WishesDataModel(description = "❤️ Today is all about love 💕, and that means you ❤️."))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😍 You complete my world 🌍."))

                list.add(WishesDataModel(description = "❤️ With you, every day feels like Valentine’s Day 💖."))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😊 My love for you grows daily 🌱."))
                list.add(WishesDataModel(description = "❤️ Sending heartfelt love 💕 straight to you ❤️."))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😄 You are my happy place 💞."))
                list.add(WishesDataModel(description = "❤️ On Valentine’s Day 💖, I celebrate us ❤️."))

                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😊 Love feels easy with you 💕."))
                list.add(WishesDataModel(description = "❤️ My favorite love story is ours 💖. Happy Valentine’s Day!"))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😍 You are my heart’s wish 💞."))
                list.add(WishesDataModel(description = "❤️ Love is sweeter when shared with you 💕."))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😄 Thank you for being you ❤️."))

                list.add(WishesDataModel(description = "❤️ With you, love feels magical ✨. Happy Valentine’s Day!"))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😊 My smile begins with you 💖."))
                list.add(WishesDataModel(description = "❤️ Sending endless love 💕 and warm thoughts ❤️."))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😍 You make my heart smile 😊."))
                list.add(WishesDataModel(description = "❤️ On this Valentine’s Day 💖, my love is yours ❤️."))

                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😊 Every moment with you is special 💞."))
                list.add(WishesDataModel(description = "❤️ My heart chose you 💖. Happy Valentine’s Day!"))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😄 Love feels right with you ❤️."))
                list.add(WishesDataModel(description = "❤️ You are my favorite reason to smile 😊. Happy Valentine’s Day!"))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😍 You mean everything to me 💖."))

                list.add(WishesDataModel(description = "❤️ Love grows stronger with you 💕. Happy Valentine’s Day!"))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😊 You are my sunshine ☀️."))
                list.add(WishesDataModel(description = "❤️ Sending love 💖 that lasts beyond today ❤️."))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😄 My heart feels lucky 💞."))
                list.add(WishesDataModel(description = "❤️ Today I celebrate love 💕, today I celebrate you ❤️."))

                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😊 You are my greatest gift 🎁."))
                list.add(WishesDataModel(description = "❤️ With you, love feels complete 💖. Happy Valentine’s Day!"))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😍 My forever starts with you ❤️."))
                list.add(WishesDataModel(description = "❤️ Love, laughter, and us 💕. Happy Valentine’s Day!"))
                list.add(WishesDataModel(description = "❤️ Happy Valentine’s Day! 😊 Always and forever 💖."))

            }
            else -> {

            }
        }

        return list
    }

    fun getAllRingtone(): ArrayList<RingtoneDataModel>{
        val bhajanList = arrayListOf<RingtoneDataModel>()


        return bhajanList
    }
}