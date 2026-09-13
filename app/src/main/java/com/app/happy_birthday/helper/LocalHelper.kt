package com.app.happy_birthday.helper

import android.content.Context
import android.content.res.Configuration
import com.app.happy_birthday.helper.PrefUtils.getAppConfig
import com.app.happy_birthday.helper.PrefUtils.setAppConfig
import com.app.happy_birthday.helper.helper_model.AppConfigModel
import com.app.happy_birthday.helper.helper_model.BirthdayPerson
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

    fun getAllBhajan(day: BirthdayPerson): ArrayList<WishesDataModel> {
        val list = ArrayList<WishesDataModel>()

        when(day){
            BirthdayPerson.BOYFRIEND -> {
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the most amazing boyfriend ever! ❤️ You make my world brighter every day. 🥰"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my love! 💖 May your day be filled with happiness, laughter, and everything you love. 🥳"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to the one who makes my heart smile every day. 😊 I’m so lucky to have you. 💕"))
                list.add(WishesDataModel(description = "🎂 Wishing the happiest birthday to my favorite person! 🥰 May all your dreams come true. ✨"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, sweetheart! 🎉 Thank you for filling my life with so much love, joy, and beautiful memories. ❤️"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to my handsome boyfriend! 😘 I hope your special day is as wonderful as you are. 💕"))
                list.add(WishesDataModel(description = "🎂 Another year of being amazing! ❤️ Happy Birthday, my love. May this year bring you endless happiness. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, babe! 💕 You deserve all the love, success, and happiness in the world. 🥰"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to the man who has my whole heart! 💖 I’m grateful for every moment with you. 🎂"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, love! 😘 Life feels so much better with you by my side. ❤️"))

                list.add(WishesDataModel(description = "🥰 Happy Birthday to my favorite human! 🎉 Thank you for always making me laugh and feel loved. 💖"))
                list.add(WishesDataModel(description = "🎂 Wishing you a birthday full of smiles 😊, unforgettable moments ✨, and lots of love ❤️."))
                list.add(WishesDataModel(description = "💖 Happy Birthday, my handsome! 😘 May your life always be filled with success, peace, and happiness. 🎉"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to the person who makes every ordinary day feel special. ❤️ I love you so much! 🥰"))
                list.add(WishesDataModel(description = "🎂 Cheers to another beautiful year of your life! 🥳 May every moment bring you closer to your dreams. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, baby! 🎉 You are not just my boyfriend, you are my happiness and my safe place. 🥰"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, my love! 💕 May your smile never fade and your heart always stay happy. 😊"))
                list.add(WishesDataModel(description = "🎂 Sending you the biggest birthday hug 🤗 and all my love ❤️. Have an incredible day, sweetheart!"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to the one who makes my heart skip a beat! 😘 I hope your day is absolutely perfect. 🎉"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, darling! ❤️ May this new chapter of your life be filled with beautiful adventures and success. ✨"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday to my favorite person! 💕 Every moment with you is a memory I treasure forever. 🥰"))
                list.add(WishesDataModel(description = "❤️ You deserve the world and so much more! 🌎 Happy Birthday, my love. 🎉"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, handsome! 😘 Thank you for being the wonderful person you are. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, babe! 💖 May today bring you countless reasons to smile and a lifetime of reasons to be happy. 😊"))
                list.add(WishesDataModel(description = "🎉 Wishing my amazing boyfriend the happiest birthday! ❤️ I hope this year brings you everything you’ve wished for. ✨"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday, my love! 🎂 I’m thankful for you today, tomorrow, and every day. ❤️"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to the man who knows how to make me smile even on my worst days. 😊 I love you! 🎉"))
                list.add(WishesDataModel(description = "🎂 May your birthday be as handsome, charming, and wonderful as you are! 😘 Happy Birthday, baby! ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, sweetheart! 💕 May every dream you chase become a beautiful reality. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to my one and only! 🥰 You make my life more beautiful simply by being in it. 🎂"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, my love! 💖 Here’s to more laughter, late-night talks, adventures, and memories together. 🥰"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to my favorite person in the entire world! 🌎❤️ I’m so grateful to call you mine."))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, baby! 😘 May your day be filled with love, laughter, delicious cake 🍰, and wonderful surprises."))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, handsome! 🎂 You make my heart happier than words can ever explain. 💕"))
                list.add(WishesDataModel(description = "💖 Another year older, wiser, and even more handsome! 😍 Happy Birthday, my love! 🎉"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my biggest reason to smile! 😊 May happiness follow you wherever you go. ❤️"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday, sweetheart! 🎉 Thank you for loving me, supporting me, and always being there. 💖"))
                list.add(WishesDataModel(description = "❤️ Today is all about celebrating you! 🎂 Happy Birthday to the most special man in my life. 🥳"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my darling! 💕 May your heart be full of happiness and your life full of beautiful moments. ✨"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, love! 🥰 I hope you know how incredibly special and loved you are. ❤️"))

                list.add(WishesDataModel(description = "💖 Happy Birthday to my favorite smile, favorite voice, and favorite person! 😘 I love you endlessly. 🎉"))
                list.add(WishesDataModel(description = "🥳 Wishing my wonderful boyfriend a birthday full of love ❤️, laughter 😂, and unforgettable memories. 🎂"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, babe! 💕 May this year be your best one yet, filled with success and happiness. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to the man who makes my life complete! 🥰 I’m grateful for every second we share. 🎉"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my handsome! 💖 Keep chasing your dreams and never stop being the amazing person you are. 🌟"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday, my love! 🎂 May our journey together be filled with countless happy moments and endless love. ❤️"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to the one I can laugh with, dream with, and love with. 🥰 You mean everything to me. 🎉"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sweetheart! ❤️ I hope this special day reminds you how deeply you are loved and appreciated. 💕"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my love! 😘 May every new year of your life bring you closer to everything your heart desires. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to my forever favorite! 🎂 Thank you for being such a beautiful part of my life. 🥰"))
            }

            BirthdayPerson.GIRLFRIEND -> {

                list.add(WishesDataModel(description = "🎂 Happy Birthday, my love! ❤️ You make every day brighter just by being in my life. 🥰"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to the most beautiful girl in my world! 💖 May your day be filled with endless smiles. 😊"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, sweetheart! 🎂 You are my happiness, my favorite person, and my biggest blessing. ❤️"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday to the girl who owns my heart! ❤️ I hope all your dreams come true. ✨"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, beautiful! 💕 Life feels so much more special with you beside me. 🥰"))
                list.add(WishesDataModel(description = "🎉 Wishing my amazing girlfriend the happiest birthday! ❤️ You deserve all the love and happiness in the world. 🌎"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, baby! 😘 Your smile is my favorite thing in the world. Keep smiling always. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my favorite girl! 🥰 Thank you for making my life so beautiful and full of love. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, princess! 👑💖 May your special day be as wonderful and beautiful as you are. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to the girl who makes my heart smile every single day! 🥰 I love you endlessly. 🎂"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, my sweetheart! 💕 May this new year of your life bring you success, happiness, and countless beautiful memories. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, gorgeous! 😍 You deserve every beautiful thing life has to offer. ❤️"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, my love! 🎉 Thank you for being the reason behind so many of my happiest moments. 🥰"))
                list.add(WishesDataModel(description = "🎂 Wishing the happiest birthday to my favorite person! ❤️ I’m so lucky to have you in my life. 💕"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, darling! 💖 May your smile shine brighter than all the stars tonight. ✨"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday to the woman who makes my world complete! ❤️ You mean more to me than words can say. 🎂"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, beautiful! 😘 May every moment today give you another reason to smile. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, baby girl! ❤️ I hope this year brings you closer to every dream in your heart. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my one and only! 💕 Every moment with you is a memory I want to keep forever. 🥰"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, my princess! 👑 You make my life happier simply by being you. 🎂"))

                list.add(WishesDataModel(description = "🥰 Happy Birthday, love! 🎉 Here’s to more laughter, adventures, late-night talks, and beautiful memories together. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the girl who has my whole heart! 💖 I’m grateful for every moment we share. 🥰"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, sweetheart! 🎉 May your heart always be happy and your beautiful smile never fade. 😊"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, gorgeous! ❤️ You are not just my girlfriend, you are my favorite part of every day. 🥰"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my love! 💕 I hope your special day is filled with love, laughter, cake, and wonderful surprises. 🎁"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, beautiful! 💖 Keep chasing your dreams and never stop being the amazing person you are. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to my favorite smile! 😊 You make even the simplest moments feel magical. 🎂"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, darling! 🥰 May this year be filled with new adventures, beautiful moments, and endless happiness. 💖"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, baby! 🎂 I hope you know how deeply loved and special you are to me. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my beautiful girl! 🥰 I’ll always be here to celebrate your happiness and support your dreams. 💕"))

                list.add(WishesDataModel(description = "🎉 Happy Birthday, my love! ❤️ You are the reason so many of my ordinary days feel extraordinary. ✨"))
                list.add(WishesDataModel(description = "🥰 Wishing my beautiful girlfriend the happiest birthday ever! 🎂 May happiness follow you wherever you go. 💖"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, sweetheart! 😘 You make my heart happier than words could ever explain. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, princess! 👑 May your life always be filled with love, peace, success, and beautiful moments. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to the most special girl in my life! 💕 I’m thankful for you today and every day. 🥰"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, baby! 🎂 May every wish you make today find its way into reality. ✨"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday, my beautiful! 💖 Thank you for bringing so much love and happiness into my life. 🎉"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, darling! ❤️ Your happiness means everything to me, and I hope you smile all day long. 😊"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to my favorite girl! 🎉 May this be the beginning of your most beautiful year yet. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, love! 🥰 I hope today reminds you just how incredibly special you are to everyone who loves you. ❤️"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, gorgeous! 💕 You make my world more colorful, happier, and so much more beautiful. 🌸"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to the girl I love! 🥰 May your birthday be filled with all the happiness your heart can hold. 🎉"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, my sweetheart! 🎂 I’m grateful for your love, your smile, and every beautiful moment we share. ❤️"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, beautiful! 💕 May your dreams grow bigger, your smile grow brighter, and your happiness grow stronger. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, baby! ❤️ No gift could ever compare to how precious you are to me. 🥰"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my love! 💖 May every chapter ahead be filled with love, success, laughter, and unforgettable memories. ✨"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday to my forever favorite girl! ❤️ Thank you for making my life a little sweeter every day. 🎉"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, princess! 🎂 I hope today is full of everything that makes your heart happy. 😊"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my beautiful love! ❤️ I choose you today, tomorrow, and every day. ♾️"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to my forever person! 🥰 May we create countless more beautiful memories together. 🎂"))

            }

            BirthdayPerson.FRIENDS -> {

                list.add(WishesDataModel(description = "🎂 Happy Birthday, buddy! 🎉 May your day be full of fun, laughter, and unforgettable moments. 😄"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, my friend! 🎂 Wishing you happiness, success, and lots of good times ahead. ✨"))
                list.add(WishesDataModel(description = "🎉 Another year older, another year crazier! 😂 Happy Birthday, buddy! 🎂"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday! 🥳 May all your wishes come true and your snacks never run out. 🍕"))
                list.add(WishesDataModel(description = "🎉 Wishing you an amazing birthday! 😄 Keep smiling, keep enjoying, and keep being awesome. 🤘"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, friend! 🎂 May this year bring you plenty of happiness, success, and adventures. 🌟"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to one of the coolest people I know! 😎 Have an absolutely fantastic day! 🎉"))
                list.add(WishesDataModel(description = "🎉 Cheers to another year of friendship, fun, and crazy memories! 🥳 Happy Birthday!"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, buddy! 😄 May your life always be filled with laughter, good friends, and great moments. ❤️"))
                list.add(WishesDataModel(description = "🥳 Have an incredible birthday! 🎉 Forget your worries, enjoy your day, and eat lots of cake. 🍰"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, champ! 🏆 May you achieve everything you’re working for this year. 💪"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, mate! 😎 Wishing you success, happiness, and countless reasons to celebrate. 🥳"))
                list.add(WishesDataModel(description = "🥳 Birthday vibes are here! 🎂 Have fun, make memories, and enjoy every second. 😄"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, bro! 🤝 May your year be filled with good luck, great opportunities, and amazing memories. ✨"))
                list.add(WishesDataModel(description = "🎂 Wishing you a birthday as awesome as your friendship! 😄 Have a fantastic day, buddy! 🎉"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday! 🎂 May your problems be few, your happiness be plenty, and your weekends be longer. 😎"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my friend! ❤️ Thanks for all the laughs and crazy memories. Here’s to many more! 🥳"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday! 😄 May this new year of your life be full of exciting adventures and unforgettable experiences. 🌍"))
                list.add(WishesDataModel(description = "🥳 Wishing you a super fun birthday! 🎉 May today give you plenty of reasons to smile. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, legend! 😎 Keep being awesome and make this year your best one yet. 🔥"))

                list.add(WishesDataModel(description = "🎉 Happy Birthday, buddy! 🥳 May your day be filled with good food 🍕, great company, and endless laughter. 😂"))
                list.add(WishesDataModel(description = "🎂 Another year, another collection of memories! 😄 Happy Birthday, my friend! 🎉"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday! 🎂 Wishing you peace, happiness, success, and plenty of reasons to celebrate. ✨"))
                list.add(WishesDataModel(description = "🎉 Hope your birthday is full of surprises, laughter, and everything you enjoy! 😎 Happy Birthday!"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, buddy! 🤝 Here’s to more jokes, more adventures, and more unforgettable memories together. 🥳"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, friend! 🎉 May every day ahead bring you closer to your dreams. 🌟"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday! 😄 Stay happy, stay healthy, and keep spreading your positive vibes everywhere. ✨"))
                list.add(WishesDataModel(description = "🎉 Wishing you the happiest birthday! 🥳 May this year be packed with fun, success, and amazing opportunities. 🚀"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, buddy! 😎 Age is just a number, so keep acting young and having fun! 😂"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to an awesome friend! 🎉 May your day be as fun and memorable as you are. 😄"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday! 🎉 May your life be filled with good people, good vibes, and great memories. ❤️"))
                list.add(WishesDataModel(description = "🥳 Birthday wishes coming your way! 🎂 May happiness and success follow you wherever you go. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, buddy! 😄 Today is your day, so enjoy every moment and make it count! 🥳"))
                list.add(WishesDataModel(description = "🎂 Wishing you another year of laughter, friendship, adventures, and unforgettable stories. 🥳 Happy Birthday!"))
                list.add(WishesDataModel(description = "😎 Happy Birthday, bro! 🎉 Keep chasing your dreams and never forget to enjoy the journey. 💪"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, friend! 🎂 May your life always have more reasons to laugh than to worry. 😄"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday! 🍰 May your cake be big, your gifts be awesome, and your happiness be endless. 😎"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, buddy! 🥳 Here’s to another year of becoming wiser, stronger, and hopefully not more boring! 😂"))
                list.add(WishesDataModel(description = "🎉 Wishing you an unforgettable birthday! 😄 May this year bring new opportunities and amazing experiences. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, champ! 🎂 Keep smiling, keep growing, and keep making great memories. 💪"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, friend! 🎉 May your special day be filled with laughter, happiness, and your favorite people. ❤️"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, buddy! 😎 May every challenge make you stronger and every success make you happier. 🌟"))
                list.add(WishesDataModel(description = "🎉 Another year of friendship and crazy moments! 😂 Happy Birthday, my friend! 🎂"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday! 🥳 Wishing you a year full of exciting plans, successful days, and unforgettable nights. 🌙"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, mate! 😄 May your life be filled with happiness, adventure, and plenty of reasons to celebrate. 🥳"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to an amazing friend! 🤝 Grateful for all the memories and looking forward to many more. ❤️"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday! 🎉 May this year bring you everything you’ve been working hard for. 💪✨"))
                list.add(WishesDataModel(description = "🎂 Have a fantastic birthday, buddy! 😄 Eat well, laugh harder, and enjoy your special day. 🍕🎉"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, legend! 🥳 May your life always be full of great friends, great moments, and great stories. 😎"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my friend! ❤️ Keep smiling, keep enjoying life, and make every year better than the last. 🥳"))

            }

            BirthdayPerson.BROTHER -> {

                list.add(WishesDataModel(description = "🎂 Happy Birthday, bro! 🎉 May your day be filled with happiness, laughter, and lots of cake. 🍰"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to the best brother! ❤️ Wishing you success, happiness, and an amazing year ahead. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, brother! 😎 Keep smiling, keep growing, and keep being awesome. 💪"))
                list.add(WishesDataModel(description = "🎂 Another year older, bro! 😂 Don’t worry, you’re still young enough to blame everything on me. 😄"))
                list.add(WishesDataModel(description = "🥳 Wishing you the happiest birthday, brother! 🎉 May all your dreams turn into reality. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, bro! 🎂 Life is more fun and memorable because I have a brother like you. 🤝"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my amazing brother! 💪 May this year bring you success, peace, and endless happiness. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, bro! 🥳 May your life be full of great adventures, good friends, and unforgettable memories. 🌟"))
                list.add(WishesDataModel(description = "😎 Happy Birthday, brother! 🎉 Keep chasing your dreams and never stop believing in yourself. 💪"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, bro! 🎂 Enjoy your special day, eat plenty of cake, and have an incredible time. 🍰"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday to my partner in crime! 😂 May we make many more crazy memories together. 🥳"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, brother! ❤️ I’m lucky to have someone like you by my side through everything. 🤝"))
                list.add(WishesDataModel(description = "🥳 Wishing my awesome brother a fantastic birthday! 🎂 May happiness always find you. ✨"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, bro! 😄 May your problems be small, your happiness be huge, and your weekends be long. 😂"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, brother! 💪 May you achieve everything you work hard for and more. 🌟"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, bro! 🥳 Thanks for all the laughs, fights, advice, and unforgettable memories. 😄"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the coolest brother ever! 😎 Keep rocking and make this year your best one yet. 🔥"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, bro! 🎉 May every new day bring you closer to your goals and dreams. ✨"))
                list.add(WishesDataModel(description = "🎂 Wishing you a birthday full of fun, laughter, delicious food, and amazing surprises! 🎁 Happy Birthday, brother!"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, bro! ❤️ No matter how much we argue, you’ll always be one of my favorite people. 😄"))

                list.add(WishesDataModel(description = "🥳 Happy Birthday, brother! 🎂 May this year bring you new opportunities, great experiences, and plenty of success. 🚀"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, bro! 😎 Stay strong, stay happy, and keep making everyone proud. 💪"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my amazing brother! ❤️ Wishing you a life full of happiness and unforgettable moments. ✨"))
                list.add(WishesDataModel(description = "🥳 Another year, another level unlocked! 🎮 Happy Birthday, bro! Keep winning in life. 🏆"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, brother! 🎂 May your smile never fade and your confidence never run out. 😎"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, bro! 🥳 I hope this year gives you countless reasons to smile and celebrate. 🎉"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday! 😄 May your life be filled with good health, success, happiness, and amazing adventures. 🌍"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, brother! 🎉 Keep being the wonderful person you are and make every year count. 🌟"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, bro! 😂 You’re getting older, but luckily you’re still not as old as you act sometimes. 😎"))
                list.add(WishesDataModel(description = "🎉 Wishing my brother an unforgettable birthday! ❤️ May the coming year be full of happiness and success. ✨"))

                list.add(WishesDataModel(description = "🥳 Happy Birthday, bro! 🎂 Here’s to more laughs, more adventures, and more crazy memories together. 😂"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my forever teammate! 🤝 May you always have the courage to chase your biggest dreams. 💪"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, brother! ❤️ I may not say it often, but I’m always proud to have you as my brother. 🥰"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, bro! 🎉 May this year be filled with exciting opportunities and wonderful surprises. 🎁"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my favorite troublemaker! 😂 Keep making life interesting, bro! 🥳"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, brother! 😎 May you always have the strength to overcome challenges and the courage to follow your dreams. 💪"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, bro! ❤️ Wishing you endless happiness, great success, and countless memorable moments. ✨"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, brother! 🎉 May your day be as awesome as you are and your year even better. 😄"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, bro! 🥳 Thanks for always being there when I need you. Here’s to many more years of brotherhood! 🤝"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to the one and only brother! 🎂 May your life always be full of happiness, success, and good vibes. 😎"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, bro! 🥳 May every challenge make you stronger and every achievement make you prouder. 💪"))
                list.add(WishesDataModel(description = "🎉 Wishing you an amazing birthday, brother! ❤️ Keep dreaming big and working hard. Your best days are ahead. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, bro! 🎂 Forget the age and enjoy the cake! 😂 Have an awesome day!"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, brother! 😄 May your year be filled with laughter, adventures, success, and unforgettable memories. 🌟"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, bro! ❤️ I hope life gives you every reason to stay happy and every opportunity to succeed. 💪"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to an incredible brother! 🎉 Keep shining, keep growing, and keep making us proud. ✨"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, bro! 😎 May your birthday be full of good food, good people, and great memories. 🍕"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, brother! ❤️ No matter where life takes us, you’ll always have a special place in my heart. 🤝"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, bro! 🎂 Wishing you another amazing year filled with happiness, success, and plenty of fun. 😄"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, brother! ❤️ Stay happy, stay strong, and keep being the amazing brother you are. 🥳"))

            }

            BirthdayPerson.SISTER -> {

                list.add(WishesDataModel(description = "🎂 Happy Birthday, sis! 🎉 May your day be filled with happiness, laughter, and everything you love. 💖"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to the best sister ever! ❤️ May all your dreams come true and your smile never fade. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, sister! 🎂 Wishing you endless happiness, success, and beautiful memories. 🌸"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, sis! 🥰 I’m so lucky to have a wonderful sister like you in my life. ❤️"))
                list.add(WishesDataModel(description = "🎂 Wishing my amazing sister the happiest birthday! 🎉 May this year bring you lots of joy and success. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, sis! 💕 Keep smiling, keep shining, and keep being the amazing person you are. 🌟"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my beautiful sister! 🌸 May your special day be as wonderful as you are. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sis! 😊 Thank you for all the laughs, memories, and moments we’ve shared. 💖"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, sister! 🎉 May life always give you plenty of reasons to smile. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my favorite sister! 😄 Wishing you a fantastic day and an even better year ahead. ✨"))

                list.add(WishesDataModel(description = "💖 Happy Birthday, sis! 🎉 May every dream you have turn into something beautiful. 🌸"))
                list.add(WishesDataModel(description = "🎂 Wishing you a birthday full of laughter 😂, happiness 😊, delicious cake 🍰, and unforgettable moments. 🥳"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday, sister! ❤️ No matter how much we argue, you’ll always be one of my favorite people. 🤗"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, sis! 🌟 May this new chapter bring you success, peace, and countless happy moments. 💖"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to an incredible sister! 🥳 Keep chasing your dreams and never stop believing in yourself. 💪"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, sis! 😊 Your happiness means so much to me. Have the most amazing day! 🎉"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, beautiful sister! 🌸 May your life always be surrounded by love, laughter, and positivity. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, sis! 🎉 Here’s to more fun, more adventures, and more unforgettable memories together. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sister! 💕 May every year make you stronger, wiser, happier, and more successful. 🌟"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, sis! 😄 Enjoy your special day, eat lots of cake, and have an amazing time! 🍰"))

                list.add(WishesDataModel(description = "🥳 Happy Birthday to my wonderful sister! ❤️ I hope your day is filled with everything that makes you happy. 🎂"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sis! 🌸 May your life be filled with beautiful opportunities and wonderful people. 💖"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, sister! 😊 Keep shining bright and making everyone around you proud. ✨"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, sis! 🥰 I hope this year brings you closer to all your goals and dreams. 🌟"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my forever partner in family chaos! 😂 Here’s to many more crazy memories together. 🥳"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, sis! 🎉 May your happiness be endless and your worries be few. ❤️"))
                list.add(WishesDataModel(description = "🎂 Wishing you a beautiful birthday, sister! 💖 You deserve all the happiness and success in the world. 🌎"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, sis! 😄 May every day ahead give you another reason to smile. 😊"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, sister! ❤️ Thank you for always being someone I can count on. 🤝"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sis! 🌸 May your journey ahead be filled with confidence, courage, and countless achievements. 💪"))

                list.add(WishesDataModel(description = "🎉 Happy Birthday to my amazing sister! 💖 Keep being kind, strong, confident, and wonderfully you. 🥰"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sis! 🥳 May your birthday be full of good food, great company, and lots of laughter. 🍰"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, sister! 🎉 May you always have the courage to follow your heart and chase your dreams. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, sis! ❤️ You make our family brighter just by being yourself. 🌸"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my lovely sister! 😊 May this year bring you new adventures and beautiful experiences. 🌍"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, sis! 💕 Wishing you happiness today, tomorrow, and throughout the entire year. ✨"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday, sister! 🎂 I’m grateful for every memory and every laugh we’ve shared. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sis! 🌟 May you continue to grow, shine, and achieve everything you set your heart on. 💪"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, sister! 💖 May your special day be filled with smiles, surprises, and wonderful memories. 🎁"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, sis! ❤️ Stay happy, stay strong, and never forget how special you are. 🌸"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, sister! 🎉 Another year of being awesome! Keep shining and making life brighter. ✨"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, sis! 🥰 May every challenge make you stronger and every achievement make you prouder. 💪"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my incredible sister! 🎂 May your future be filled with success, peace, and happiness. ❤️"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, sis! 😊 Forget your worries today and enjoy every beautiful moment of your special day. 🎂"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sister! 🌸 May you always be surrounded by people who appreciate, support, and love you. 💖"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, sis! ❤️ No matter where life takes us, you’ll always have a special place in my heart. 🤗"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to the coolest sister! 😎 May this year be your most exciting and successful one yet. 🌟"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sis! 💕 May your life always be filled with laughter, love, adventure, and beautiful memories. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, sister! 🥰 Keep smiling, keep dreaming, and keep making everyone proud. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my wonderful sister! 💖 Wishing you a lifetime of happiness, success, and countless reasons to celebrate. 🥳"))

            }

            BirthdayPerson.WIFE -> {

                list.add(WishesDataModel(description = "🎂 Happy Birthday to my beautiful wife! ❤️ You make my life happier, warmer, and more complete every day. 🥰"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, my love! 🎉 I’m grateful every day that I get to call you my wife. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the most amazing woman in my life! 🥰 May your day be as beautiful as your heart. 🌸"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, sweetheart! 🎉 Thank you for filling our life together with love, laughter, and beautiful memories. 💕"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, my beautiful wife! 🎂 You are my happiness, my strength, and my favorite person. ❤️"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to the woman who makes every house feel like home. 🏡❤️ I love you more every day."))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, darling! 🥰 Life is so much more beautiful because I get to share it with you. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my forever love! 💕 May your day be filled with all the happiness you bring into my life. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, my wife! 🎂 You are not just my partner, but my best friend and my greatest blessing. 🥰"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, beautiful! 💖 I hope today reminds you just how loved and special you truly are. 🌸"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, my love! ❤️ Every year with you is another year I’m thankful for the beautiful life we share. 🥰"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to my amazing wife! 🎉 May this new year bring you endless smiles, peace, and success. ✨"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday, sweetheart! 🎂 You make even the simplest moments feel special. I love you endlessly. ❤️"))
                list.add(WishesDataModel(description = "🎉 Wishing my beautiful wife the happiest birthday! 💕 May every dream in your heart come true. ✨"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, darling! ❤️ Thank you for standing beside me through every moment of life. 🤗"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to my one and only! 🥰 I would choose you again in every lifetime. ♾️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, wife! ❤️ You make my world brighter with your smile and warmer with your love. 🌸"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, my sweetheart! 🎂 May your life always be filled with love, happiness, and beautiful moments. 💕"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to my soulmate! 🥰 I’m incredibly lucky to walk through life with you by my side. 🎉"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, beautiful wife! 💖 You are the reason behind so many of my happiest memories. ❤️"))

                list.add(WishesDataModel(description = "🥰 Happy Birthday, my love! 🎉 Here’s to more adventures, laughter, late-night conversations, and beautiful years together. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the queen of my heart! 👑❤️ May your special day be filled with love and joy."))
                list.add(WishesDataModel(description = "💖 Happy Birthday, darling! 🥳 You deserve every beautiful thing this world has to offer. 🌎"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my wonderful wife! ❤️ Thank you for making every day of our journey together so special. 🥰"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sweetheart! 💕 May this year bring you closer to your dreams and fill your heart with happiness. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, my beautiful wife! ❤️ Your love is the greatest gift life has ever given me. 🎁"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to the woman I love more with every passing day! 🎂 You will always have my heart. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my love! 🥰 May your smile stay bright and your heart stay happy forever. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, wife! ❤️ I’m thankful for every laugh, every hug, and every beautiful memory we’ve created together. 🤗"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, my forever person! 🥰 Growing old with you is the adventure I’ll always choose. ♾️"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday to my gorgeous wife! ❤️ You make my life more meaningful simply by being in it. 🥰"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, sweetheart! 💕 May your birthday be filled with love, laughter, cake, and everything that makes you smile. 🎂"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday, my queen! 👑❤️ You deserve to be celebrated today and every single day. 🎉"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, wife! 🎂 No words can truly describe how much you mean to me. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my beautiful partner in life! 🥰 Thank you for always being there through every high and low. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my love! 💕 May our journey together continue to be filled with laughter, love, and countless memories. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to the woman who knows me best and loves me anyway! 😂🥰 I’m forever grateful for you."))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, darling! 🎂 You make every chapter of my life more beautiful. I love you endlessly. ❤️"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to my amazing wife! 🎉 May happiness follow you everywhere you go and love surround you always. 🌸"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sweetheart! 🥰 You are my home, my happiness, and the love of my life. ❤️"))

                list.add(WishesDataModel(description = "🎉 Happy Birthday, my love! 💖 Thank you for making our journey together more beautiful than I ever imagined. 🥰"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, beautiful wife! ❤️ May this year bring you countless reasons to smile and celebrate. ✨"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday to my forever love! 💕 Every moment spent with you is a moment I treasure. 🎉"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, darling! 🎂 I promise to keep loving, supporting, and annoying you for many more years. 😂❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my queen! 👑 May your day be as wonderful, kind, and beautiful as you are. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my soulmate and life partner! 🥰 I’m grateful for every day we get to spend together. 💖"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, wife! 🎉 You turned my life into a story worth celebrating every single day. 🥰"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, my love! 🎂 May our love grow stronger, our laughter grow louder, and our memories grow sweeter. ♾️"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to the most precious woman in my life! ❤️ I’ll always be grateful that you are mine. 💕"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my beautiful wife! ❤️ Today, tomorrow, and always, you will be my favorite person. 🥰"))

            }

            BirthdayPerson.SON -> {

                list.add(WishesDataModel(description = "🎂 Happy Birthday, my dear son! ❤️ You are one of the greatest blessings in my life. 🥰"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my wonderful son! 💙 May your life always be filled with happiness and success. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, son! 🎂 Watching you grow into the person you are today makes me incredibly proud. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my boy! 💙 May all your dreams come true and every step take you closer to success. 🌟"))
                list.add(WishesDataModel(description = "🎉 Wishing my amazing son the happiest birthday! ❤️ May your future be bright and full of wonderful opportunities. ✨"))
                list.add(WishesDataModel(description = "💙 Happy Birthday, son! 🥰 Always believe in yourself, follow your dreams, and never give up. 💪"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the best son anyone could ask for! ❤️ You make our family proud every single day. 🥳"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, dear son! 💙 May this year bring you new adventures, great achievements, and endless happiness. 🌟"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, my son! 🎂 No matter how old you become, you will always be my little boy. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, champ! 🏆 May you always have the courage to chase your dreams and the strength to achieve them. 💪"))

                list.add(WishesDataModel(description = "🎉 Happy Birthday, son! ❤️ You bring so much joy and pride into our lives. Have an amazing day! 🥳"))
                list.add(WishesDataModel(description = "💙 Wishing my beloved son a fantastic birthday! 🎂 May every day ahead bring you closer to your goals. ✨"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday, son! 🎉 You have grown into an incredible person, and I couldn’t be prouder of you. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my boy! 💙 May you always stay strong, kind, confident, and true to yourself. 🌟"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my precious son! ❤️ May your journey through life be filled with beautiful memories and great achievements. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, son! 🎂 Keep learning, keep growing, and keep making us proud. 💪"))
                list.add(WishesDataModel(description = "💙 Happy Birthday to my amazing boy! 🎉 May your smile always stay bright and your heart always stay happy. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, son! ❤️ You are stronger and more capable than you realize. Believe in yourself always. 💪"))
                list.add(WishesDataModel(description = "🎉 Wishing you a wonderful birthday, son! 💙 May this year be filled with success, happiness, and unforgettable moments. 🥳"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my dear son! 🥰 You will always have my love, support, and blessings wherever life takes you. ❤️"))

                list.add(WishesDataModel(description = "🥳 Happy Birthday, son! 🎉 May you continue to grow into the strong, kind, and successful person you are meant to be. 🌟"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, champ! 💙 May every challenge make you stronger and every achievement make you prouder. 💪"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, son! ❤️ I hope your special day is filled with laughter, happiness, and your favorite things. 😊"))
                list.add(WishesDataModel(description = "💙 Happy Birthday to my wonderful son! 🎂 May you always find happiness in the journey and courage in every challenge. ✨"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday, son! 🎉 You are a beautiful part of our family and a constant source of pride. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my boy! 💙 May your future be brighter than you ever imagined. Keep chasing those dreams! 🌟"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, son! 🥳 May you be blessed with good health, happiness, success, and wonderful people around you. ❤️"))
                list.add(WishesDataModel(description = "💙 Happy Birthday, dear son! 🎂 Remember that no matter how far you go, you will always have a home in our hearts. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, son! 😊 May your life be full of exciting adventures, meaningful friendships, and beautiful memories. 🌍"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to our amazing son! 🎉 Your happiness and success mean the world to us. 💙"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, son! ❤️ We are proud of the person you have become and excited for everything ahead of you. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my boy! 💙 May you always have the confidence to dream big and the determination to make it happen. 💪"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, son! 🎂 Enjoy your special day and remember how deeply you are loved. ❤️"))
                list.add(WishesDataModel(description = "💙 Happy Birthday to my wonderful son! 🌟 May every new year of your life bring new opportunities and achievements. 🎉"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, champ! 🏆 Keep working hard, stay humble, and never stop believing in your potential. 💪"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, son! ❤️ You have brought countless happy moments into our lives, and we are forever grateful for you. 🥰"))
                list.add(WishesDataModel(description = "💙 Happy Birthday, my dear son! 🎂 May your path always lead you toward happiness, peace, and success. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, son! 🎉 May every goal you set become another achievement you can proudly celebrate. 🌟"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my boy! ❤️ No matter how much you grow, you will always be incredibly special to me. 🥰"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, son! 💙 May your life be filled with courage, kindness, success, and countless reasons to smile. 😊"))

                list.add(WishesDataModel(description = "🥳 Happy Birthday to my incredible son! 🎂 Keep shining, keep growing, and always make yourself proud. 🌟"))
                list.add(WishesDataModel(description = "💙 Happy Birthday, son! ❤️ May you have the strength to overcome every obstacle and the wisdom to enjoy every success. 💪"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, dear son! 🎉 I hope this year brings you closer to every dream you have been working toward. ✨"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday, son! 💙 You are loved more than words can express and supported every step of the way. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my boy! 🎂 May your birthday be filled with laughter, delicious food, great memories, and lots of happiness. 🥳"))
                list.add(WishesDataModel(description = "💙 Happy Birthday, son! 🌟 Always stay curious, keep learning, and never be afraid to dream bigger. 🚀"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to our beloved son! ❤️ May life reward your hard work and fill your future with success. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, son! 🎉 Wherever life takes you, remember that we will always be cheering for you. 💙"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my dear boy! ❤️ May you grow wiser, stronger, happier, and more successful with every passing year. 🌟"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, son! 💙 You are our pride, our joy, and one of the most precious gifts life has given us. 🥰"))

            }

            BirthdayPerson.DAD -> {

                list.add(WishesDataModel(description = "🎂 Happy Birthday, Dad! ❤️ Thank you for always being my strength, my guide, and my biggest support. 🙏"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to the best dad ever! 💙 Wishing you good health, happiness, and many wonderful years ahead. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Dad! ❤️ Your love and guidance have made me the person I am today. Thank you for everything. 🙏"))
                list.add(WishesDataModel(description = "🥳 Wishing my amazing dad the happiest birthday! 🎉 May your life always be filled with peace, joy, and good health. 💙"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, Dad! 🎂 You are not just my father, but also my greatest inspiration and role model. 🌟"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Dad! 💙 May every day ahead bring you more reasons to smile and celebrate. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the strongest man I know! 💪❤️ Thank you for always standing by our family."))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Dad! 🎉 I’m grateful for every lesson, every sacrifice, and every moment you’ve given us. ❤️"))
                list.add(WishesDataModel(description = "💙 Happy Birthday to my wonderful father! 🎂 May you always stay healthy, happy, and surrounded by the people you love. 🙏"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Dad! ❤️ Your presence makes our family stronger and our home happier. 🏡"))

                list.add(WishesDataModel(description = "🎂 Wishing you a very Happy Birthday, Dad! 💙 May this year bring you peace, good health, and countless happy moments. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Dad! ❤️ Thank you for being my guide whenever I need direction and my support whenever I need strength. 🙏"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to an incredible father! 💙 May all your days be filled with happiness, laughter, and good memories. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Dad! ❤️ Your wisdom and kindness are gifts I will always cherish. 🌟"))
                list.add(WishesDataModel(description = "💙 Happy Birthday to my hero! 🦸‍♂️ Thank you for always protecting, supporting, and believing in me. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Dad! 🥳 May your special day be filled with love, laughter, delicious food, and beautiful memories. 🍰"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Dad! ❤️ I hope life gives you back all the happiness and love you have given to our family. 🙏"))
                list.add(WishesDataModel(description = "🥳 Wishing my beloved dad a wonderful birthday! 💙 May you always have good health, peace, and happiness. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Dad! ❤️ No words can truly express how grateful I am to have you in my life. 🙏"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the man who taught me to believe in myself! 💪💙 May you have an amazing year ahead. 🌟"))

                list.add(WishesDataModel(description = "❤️ Happy Birthday, Dad! 🎉 Your advice has guided me through so many moments, and I’ll always be thankful for it. 🙏"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Papa! 💙 May your smile always remain bright and your heart always remain happy. 😊"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Dad! 🎉 You deserve all the happiness, respect, and love in the world. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my amazing father! 💙 May every year bring you more peace, health, and wonderful memories. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Dad! ❤️ Thank you for being the person I can always count on. 🤝"))
                list.add(WishesDataModel(description = "💙 Happy Birthday, Dad! 🎂 May your days be long, your heart be peaceful, and your life be full of happiness. 🙏"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Dad! ❤️ I’m proud to be your child and grateful for everything you’ve done for me. 🌟"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Papa! 🎉 May this special day bring you countless smiles and beautiful moments with the family. 👨‍👩‍👧‍👦"))
                list.add(WishesDataModel(description = "💙 Happy Birthday to my greatest supporter! ❤️ Your belief in me has always given me the courage to move forward. 💪"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Dad! 🎂 Wishing you a year filled with good health, peaceful days, and lots of happiness. 😊"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, Dad! ❤️ You have always been my teacher, protector, and biggest source of inspiration. 🙏"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Papa! 💙 May every moment of your life be filled with happiness and surrounded by love. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Dad! 😊 Thank you for all the little things you do that mean so much to our family. 💙"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the pillar of our family! ❤️ May you always stay strong, healthy, and happy. 💪"))
                list.add(WishesDataModel(description = "💙 Happy Birthday, Dad! 🎉 Your love has been one of the greatest blessings of my life. 🙏"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Dad! 🎂 May your special day be as wonderful as the memories you have created for our family. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Papa! 💙 Wishing you endless happiness, good health, peace, and many more beautiful years. ✨"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Dad! ❤️ I hope you know how deeply loved, respected, and appreciated you are. 🙏"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to my wonderful father! 💙 May every dream and wish you have bring happiness into your life. 🌟"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Dad! ❤️ Thank you for giving me strength when I needed it and wisdom when I was lost. 🙏"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, Dad! 💙 May your life always be surrounded by family, laughter, peace, and love. ❤️"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Papa! 🎉 I wish you a long, healthy, peaceful, and incredibly happy life. 🙏"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the man who has always been there for me! ❤️ I’m forever grateful for your love and support. 💙"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Dad! 😊 May every new year of your life bring more happiness and fewer worries. ❤️"))
                list.add(WishesDataModel(description = "💙 Happy Birthday, Dad! 🎂 You have given our family so much love and strength. Today, we celebrate you! 🥳"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, Papa! 🎉 May you always have a reason to smile and a family around you to share it with. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Dad! 💙 Your lessons stay with me wherever I go. Thank you for shaping my life. 🙏"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to my incredible father! ❤️ May you be blessed with health, happiness, peace, and many more birthdays. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Dad! 💙 I hope today reminds you how much you mean to all of us. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Dad! ❤️ You will always be one of the most important people in my life. Wishing you endless happiness and good health. 🙏"))

            }

            BirthdayPerson.MOTHER -> {

                list.add(WishesDataModel(description = "🎂 Happy Birthday, Mom! ❤️ You are the heart of our family and the greatest blessing in my life. 🥰"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to the most wonderful mother! 💖 May your day be filled with happiness, love, and beautiful moments. 🌸"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Mom! ❤️ Thank you for filling my life with endless love, care, and beautiful memories. 🙏"))
                list.add(WishesDataModel(description = "🥳 Wishing my amazing mother the happiest birthday! 💕 May you always stay healthy, happy, and smiling. 😊"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, Mom! 🎂 Your love has been my greatest strength and your smile my biggest happiness. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to the best mom ever! 🥰 Thank you for always believing in me and standing by my side. 💕"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Mom! 🌸 You deserve all the happiness and love that you have given to everyone around you. ❤️"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Mom! 💖 May your life always be filled with peace, good health, laughter, and countless happy moments. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to my beautiful mother! 🎂 Your love makes every difficult moment easier and every happy moment brighter. 🥰"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Mom! 💕 I’m forever grateful for every sacrifice, every lesson, and every hug you’ve given me. 🤗"))

                list.add(WishesDataModel(description = "🎂 Wishing my beloved Mom the happiest birthday! ❤️ May every wish in your heart come true. ✨"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday, Mom! 🎉 You are my first teacher, my biggest supporter, and my forever source of comfort. ❤️"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to the woman with the kindest heart! 🌸 May your special day be as beautiful as you are. 🎂"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Mom! ❤️ Your love is one of the greatest gifts life has ever given me. 🙏"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Maa! 💕 May you always have a reason to smile and never have a reason to worry. 😊"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Mom! ❤️ Thank you for being my comfort on difficult days and my biggest cheerleader on good ones. 🌟"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my wonderful mother! 💖 May this year bring you endless peace, happiness, and beautiful memories. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Mom! 🥰 I hope life gives you back all the love and happiness you have given to our family. ❤️"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, Mom! 🎂 No words can ever fully express how much you mean to me. ❤️"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Maa! 🌸 Your smile makes our home brighter and your love makes our family stronger. 🏡❤️"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, Mom! ❤️ I’m proud to be your child and grateful for every moment we share. 🥰"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my greatest blessing! 💖 May you always be surrounded by love, family, and happiness. 👨‍👩‍👧‍👦"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Mom! 🎂 May your days be peaceful, your heart be happy, and your smile never fade. 😊"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, Maa! 💕 Thank you for teaching me the meaning of kindness, strength, and unconditional love. 🙏"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the queen of our home! 👑❤️ May you have a beautiful and joyful year ahead. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Mom! 🥰 Every good thing in me carries a little piece of what you taught me. ❤️"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, Mom! 🎂 May every day ahead bring you more happiness than the day before. 🌸"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Maa! ❤️ You have always been my safe place, my guide, and my biggest source of love. 🤗"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Mom! 💕 I hope today is filled with laughter, delicious food, beautiful memories, and lots of love. 🎉"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to my incredible mother! 🥰 You make life better simply by being in it. 🎂"))

                list.add(WishesDataModel(description = "🎉 Happy Birthday, Mom! 💖 May you always have good health, peace of mind, and countless reasons to celebrate. ✨"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Maa! ❤️ Your hugs have always been my favorite kind of comfort. 🤗"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Mom! 🌸 May your beautiful heart always be filled with happiness and peace. 💖"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my amazing mother! ❤️ Thank you for loving me through every stage of my life. 🥰"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Mom! 💕 You deserve every beautiful thing this world has to offer. 🌎✨"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, Maa! 🎉 May your life be filled with laughter, love, good health, and wonderful memories. 😊"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday, Mom! 🎂 I can never repay all that you have done for me, but I hope you always know how much I love you. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Mom! 🌸 May every new year of your life bring more happiness, peace, and beautiful moments. ✨"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my loving mother! ❤️ You are truly one of the most precious gifts in my life. 🙏"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Maa! 💖 Keep smiling, keep shining, and always know how deeply you are loved. 🌟"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, Mom! ❤️ Your love has shaped my life in more ways than I could ever explain. 🙏"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my forever favorite woman! 🥰 Thank you for being there through every joy and every challenge. ❤️"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, Mom! 🌸 May your heart always be peaceful and your life always be surrounded by people who love you. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Maa! 🥳 Today we celebrate you and all the love you bring into our lives every day. 💕"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Mom! ❤️ Wishing you many more years of good health, happiness, laughter, and family memories. 🙏"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday to the most caring mother! 🎂 Your love is something I will cherish for the rest of my life. 💖"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, Mom! 🎉 May every dream you have come true and every day bring you a reason to smile. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Maa! ❤️ No matter how old I get, I will always need your love, advice, and blessings. 🙏"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Mom! 💕 You are the strength behind our family and the warmth that makes our home special. 🏡"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my wonderful Mom! ❤️ May you always be happy, healthy, loved, and surrounded by your family. 🥰"))

            }

            BirthdayPerson.KIDS -> {

                list.add(WishesDataModel(description = "🎂 Happy Birthday, little superstar! 🌟 May your day be filled with fun, laughter, and lots of cake! 🎉"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, little one! 🎈 Keep smiling, keep playing, and keep making everyone happy. 💖"))
                list.add(WishesDataModel(description = "🎉 Wishing you the happiest birthday ever! 🎂 May your special day be full of toys, games, and yummy treats. 🍭"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, champ! 🏆 May you have an amazing day filled with fun, laughter, and wonderful surprises. 🎁"))
                list.add(WishesDataModel(description = "🎈 Happy Birthday, cutie! 🥰 May your day sparkle with happiness, balloons, cake, and lots of smiles. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, little hero! 🦸 May every day bring you new adventures and exciting discoveries. 🌈"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sunshine! ☀️ Keep shining brightly and spreading happiness wherever you go. 💛"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, little superstar! 🌟 May all your tiny dreams grow into big beautiful ones. 💖"))
                list.add(WishesDataModel(description = "🎈 Wishing you a magical birthday! 🪄 May your day be filled with laughter, fun, and lots of sweet surprises. 🍬"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, kiddo! 🥳 Eat lots of cake, open lots of presents, and have the best day ever! 🎁"))

                list.add(WishesDataModel(description = "🌟 Happy Birthday, little champion! 🎉 Keep learning, keep smiling, and always believe in yourself. 💪"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, cutie pie! 🥰 May your birthday be filled with colorful balloons, delicious cake, and happy moments. 🎈"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, little explorer! 🌍 May every day bring you a brand-new adventure. 🚀"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, superstar! ⭐ May your smile shine brighter than all the birthday candles. 🎂"))
                list.add(WishesDataModel(description = "🎈 Happy Birthday, little buddy! 😄 Have lots of fun, play your favorite games, and enjoy every moment! 🧸"))
                list.add(WishesDataModel(description = "🎂 Wishing you a super-duper birthday! 🥳 May your day be packed with fun, laughter, and awesome surprises. 🎁"))
                list.add(WishesDataModel(description = "🌈 Happy Birthday, little sunshine! ☀️ May your world always be colorful, happy, and full of wonderful adventures. 💖"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, champ! 🏆 May you grow smarter, stronger, kinder, and happier every day. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, little one! 🎈 May your childhood be filled with magical memories that make you smile forever. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, kiddo! 🍰 Today is your special day, so have fun, laugh loudly, and enjoy every yummy bite! 😋"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, little star! ⭐ May you always shine bright and make your dreams come true. 🌟"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday! 🥳 May your day be filled with your favorite toys, favorite people, and favorite treats. 🧸🍭"))
                list.add(WishesDataModel(description = "🎈 Happy Birthday, little champion! 💪 May every day bring you something new and exciting to learn. 📚✨"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, cutie! 🥰 Keep laughing, keep playing, and keep filling the world with your beautiful smile. 😊"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, little adventurer! 🚀 May your imagination take you to the most amazing places. 🌍"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, superstar! 🌟 Wishing you a day full of games, giggles, gifts, and lots of cake! 🎂"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, little hero! 🦸 May you always be brave, kind, curious, and full of joy. ❤️"))
                list.add(WishesDataModel(description = "🎈 Happy Birthday, kiddo! 🎉 May your special day be brighter than a rainbow and sweeter than candy. 🌈🍬"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, little buddy! 🧸 May you have a day full of hugs, laughter, fun, and wonderful surprises. 🤗"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sunshine! ☀️ May every year bring you more happiness, adventures, and beautiful memories. 💖"))

                list.add(WishesDataModel(description = "🎉 Happy Birthday, little genius! 🧠✨ Keep asking questions, learning new things, and having lots of fun! 📚"))
                list.add(WishesDataModel(description = "🎈 Happy Birthday, little one! 🥳 May your birthday be filled with balloons, presents, cake, and endless smiles. 🎁"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, champ! 🏆 May you always have the courage to dream big and the joy to enjoy every little moment. 💖"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, little star! 🌟 May your imagination always stay big and your heart always stay happy. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, kiddo! 🎂 May today be full of laughter, games, tasty treats, and unforgettable fun. 😄"))
                list.add(WishesDataModel(description = "🌈 Happy Birthday, little sunshine! ☀️ May your life always be as colorful, cheerful, and wonderful as a rainbow. 🌈"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, little hero! 🦸 Keep being brave, kind, curious, and wonderfully yourself. 💙"))
                list.add(WishesDataModel(description = "🎈 Wishing you the most magical birthday! ✨ May all your wishes fly high like balloons in the sky. 🎈"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, little superstar! 🌟 Today is your day to play, laugh, eat cake, and have tons of fun! 🎂"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, cutie! 🥰 May every day of your childhood be filled with happiness, love, and magical memories. 💖"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, little champ! 🏆 May you grow up healthy, happy, confident, and full of wonderful dreams. 🌟"))
                list.add(WishesDataModel(description = "🎈 Happy Birthday, kiddo! 🎉 May your day be filled with big smiles, tiny adventures, and giant amounts of fun! 😄"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, little explorer! 🚀 Keep discovering, learning, imagining, and enjoying the wonderful world around you. 🌍"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, superstar! ⭐ May every candle you blow out bring you one step closer to your wishes. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, little buddy! 🧸 May you always be surrounded by love, laughter, friendship, and lots of fun. ❤️"))
                list.add(WishesDataModel(description = "🌈 Happy Birthday, little one! 🎂 May your life be filled with colorful dreams and beautiful adventures. 💖"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, champ! 🎈 Have an amazing day full of cake, presents, games, and lots of laughter! 🎁"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, little star! 🌟 Keep smiling, keep dreaming, and keep making the world brighter with your happiness. 😊"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, kiddo! ❤️ May you always stay curious, cheerful, kind, and full of imagination. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, little superstar! 🎂 May your childhood be full of laughter, love, adventures, and memories you’ll treasure forever. 💖"))

            }

            BirthdayPerson.HUSBAND -> {

                list.add(WishesDataModel(description = "🎂 Happy Birthday, my love! ❤️ You are not just my husband, but my best friend and my forever person. 🥰"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to the most amazing husband! 💖 Thank you for filling my life with love, laughter, and happiness. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sweetheart! 🥰 Life is so much more beautiful because I get to share it with you. ❤️"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to my wonderful husband! 🎉 May your day be filled with happiness, love, and everything you deserve. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, my love! 🎂 I’m grateful for every moment, every memory, and every adventure we share together. 🥰"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to the man who has my whole heart! ❤️ I’m so lucky to call you my husband. 💕"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, darling! 💖 You make my life happier, my heart warmer, and my days brighter. ☀️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my handsome husband! 😘 May this year bring you success, peace, happiness, and countless beautiful moments. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to my soulmate! 🥰 Growing old with you is one of the greatest joys of my life. ♾️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, husband! 💕 Thank you for always being my strength, my support, and my safe place. 🤗"))

                list.add(WishesDataModel(description = "🥳 Happy Birthday, my love! 🎉 Here’s to another year of laughter, adventures, and making beautiful memories together. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the king of my heart! 👑❤️ May your special day be filled with love and happiness."))
                list.add(WishesDataModel(description = "💖 Happy Birthday, sweetheart! 🥰 You are the greatest partner I could ever ask for. ❤️"))
                list.add(WishesDataModel(description = "🎉 Wishing my amazing husband the happiest birthday! 🎂 May all your dreams and wishes come true. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, darling! 🥰 Every day with you is a beautiful chapter in the story of our life together. 📖"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my forever love! 💖 I would choose you again and again in every lifetime. ♾️"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, my husband! ❤️ Your smile still has the power to make my entire day better. 😊"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to the man who makes our house feel like home. 🏡❤️ I love you more than words can say."))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my love! 💕 May your life always be filled with success, good health, happiness, and love. ✨"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to my amazing husband! 🥰 Thank you for being there through every high and every low. ❤️"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, sweetheart! ❤️ You are my favorite person to laugh with, dream with, and grow old with. 🥰"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my handsome! 😘 May this new year of your life be your happiest and most successful yet. 🌟"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to my life partner! ❤️ I’m thankful for every beautiful moment we have shared and every moment still to come. ✨"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, husband! 💖 You are my greatest blessing and one of the best things that ever happened to me. 🙏"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, my love! 🎉 May our journey together continue to be filled with laughter, love, and unforgettable memories. 🥰"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the man I love! 💕 I hope today reminds you just how special and deeply loved you are. ❤️"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, darling! 🎉 Thank you for making ordinary days feel extraordinary just by being beside me. ✨"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to my forever partner! 🎂 With you, every challenge feels easier and every happiness feels bigger. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, husband! 🥰 May your smile never fade and may every dream you chase become reality. 🌟"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, my love! ❤️ You are my home, my happiness, and the person I want beside me forever. ♾️"))

                list.add(WishesDataModel(description = "💖 Happy Birthday to my incredible husband! 🎉 I’m grateful for your love, patience, support, and endless care. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, sweetheart! 🥰 May your special day be filled with laughter, delicious food, and all your favorite things. 🍰"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, my king! 👑❤️ You deserve every beautiful thing life has to offer. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, husband! 💖 Thank you for making our life together so special and meaningful. 🥰"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, darling! ❤️ I promise to keep loving you, supporting you, and annoying you for many more years. 😂💕"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to my soulmate and best friend! 🥰 I’m so thankful that life brought us together. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my love! 🎂 May this year bring you new achievements, wonderful memories, and endless happiness. ✨"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, husband! 🥰 Your love is one of the greatest gifts I have ever received. 🎁"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my forever favorite! 💕 Every year with you makes me love you even more. ❤️"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, sweetheart! 🎉 May our love continue to grow stronger with every passing year. ♾️"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, my handsome husband! ❤️ You make my heart happy in ways I could never fully explain. 🥰"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my love! 💖 May your birthday be as wonderful as the happiness you bring into my life. ✨"))
                list.add(WishesDataModel(description = "🥰 Happy Birthday to my one and only! 🎂 Thank you for being my partner, my support, and my favorite person. ❤️"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, husband! 🎉 May every new year of your life bring more happiness, success, and beautiful memories. 🌟"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, darling! ❤️ I’m thankful for every hug, every laugh, and every little moment we share. 🤗"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday to the love of my life! 💕 Wherever life takes us, I’ll always want to walk beside you. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my husband! 🎂 You make my world complete and my heart full. I love you endlessly. ❤️"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, sweetheart! 🥰 May we continue creating beautiful memories and growing old together. ♾️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my forever person! ❤️ You are my today, my tomorrow, and all the beautiful days ahead. ✨"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, my love! 🥰 Thank you for being the wonderful husband, partner, and best friend I could ever wish for. ❤️"))

            }

            BirthdayPerson.GRANDPA -> {

                list.add(WishesDataModel(description = "🎂 Happy Birthday, Grandpa! ❤️ Wishing you good health, happiness, and many more wonderful years ahead. 🙏"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to the best Grandpa ever! 💙 May your day be filled with love, laughter, and beautiful memories. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Grandpa! 🥰 Your wisdom, kindness, and love make our family truly special. ❤️"))
                list.add(WishesDataModel(description = "🥳 Wishing my wonderful Grandpa the happiest birthday! 🎉 May you always stay healthy, happy, and smiling. 💙"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, Grandpa! 🎂 Thank you for filling our lives with stories, wisdom, and unforgettable memories. 📖"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my amazing Grandpa! 💖 May every day ahead bring you peace, joy, and lots of reasons to smile. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Grandpa! ❤️ Your love and blessings mean more to me than words can express. 🙏"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Grandpa! 💙 May your special day be surrounded by family, laughter, and all the people you love. 👨‍👩‍👧‍👦"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to the heart of our family! ❤️ Wishing you a long, healthy, and happy life, Grandpa. 🙏"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Grandpa! 🥰 Your stories and life lessons are treasures I will always carry with me. 💖"))

                list.add(WishesDataModel(description = "💙 Happy Birthday, Grandpa! 🎉 May your smile always stay bright and your heart always stay peaceful. 😊"))
                list.add(WishesDataModel(description = "🎂 Wishing you a very Happy Birthday, Grandpa! ❤️ May you be blessed with good health and countless happy moments. 🙏"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Grandpa! 🎉 Thank you for being such a wonderful source of love, wisdom, and inspiration. 💖"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my beloved Grandpa! 💙 May your birthday be as wonderful as the memories you have given our family. ❤️"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday, Grandpa! 🥰 Your presence brings warmth and happiness to everyone around you. 🌸"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Grandpa! 🎂 May every new year of your life bring you more peace, health, and happiness. ✨"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to the wisest man I know! 👴 May your days always be filled with love and laughter. 😊"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Grandpa! 💙 I’m grateful for every story, every smile, and every lesson you have shared with me. ❤️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Grandpa! 🙏 May you always be surrounded by family and blessed with many more beautiful years. 💖"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to an incredible Grandpa! ❤️ Your kindness and wisdom inspire us every day. 🌟"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, Grandpa! 💙 May your life continue to be filled with love, peace, good health, and wonderful memories. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Grandpa! ❤️ Today we celebrate you and all the love you have given to our family. 🎉"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Grandpa! 😊 May your day be filled with delicious food, laughter, family, and lots of happiness. 🍰"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to my wonderful Grandpa! 💖 You are a precious part of our family and someone I will always cherish. ❤️"))
                list.add(WishesDataModel(description = "💙 Happy Birthday, Grandpa! 🥰 May you always have plenty of reasons to smile and many beautiful moments to enjoy. 🌟"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Grandpa! ❤️ Your blessings and guidance have always been a priceless gift to our family. 🙏"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Grandpa! 💖 Wishing you many more years of laughter, family gatherings, and happy memories. 👨‍👩‍👧‍👦"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Grandpa! 🎉 May every day ahead bring you comfort, happiness, and peace of mind. 💙"))
                list.add(WishesDataModel(description = "❤️ Happy Birthday to my amazing Grandpa! 🎂 Thank you for making our family stronger with your love and wisdom. 🙏"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Grandpa! 💙 May your heart always be young, your smile always bright, and your days always joyful. 😊"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, Grandpa! 🥰 I hope your special day is filled with love from everyone whose life you have touched. ❤️"))
                list.add(WishesDataModel(description = "💖 Happy Birthday to the pillar of our family! 🎉 May you always stay healthy, happy, and surrounded by loved ones. 🙏"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Grandpa! 💙 Your life is full of stories worth telling and memories worth celebrating. 📖✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Grandpa! ❤️ May this year bring you peaceful days, happy moments, and plenty of family time. 👨‍👩‍👧‍👦"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my dear Grandpa! 🎂 Your love has made so many beautiful memories in our family. 💖"))
                list.add(WishesDataModel(description = "💙 Happy Birthday, Grandpa! 😊 May every sunrise bring you another reason to be grateful and happy. ☀️"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Grandpa! ❤️ You are a wonderful blessing to our family, and we are grateful for you every day. 🙏"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Grandpa! 🎉 May your special day be full of smiles, warm hugs, and unforgettable moments. 🤗"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, Grandpa! 🎂 Wishing you a life filled with good health, peaceful moments, and endless family love. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Grandpa! 💙 Thank you for being a wonderful storyteller, guide, and source of wisdom. 📖"))

                list.add(WishesDataModel(description = "🎂 Happy Birthday, Grandpa! ❤️ May your years ahead be filled with good health, happiness, and countless blessings. 🙏"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Grandpa! 💖 May your home always be filled with laughter, love, and the voices of family. 🏡"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday to my beloved Grandpa! 💙 I hope today brings you all the happiness you deserve. 😊"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday, Grandpa! ❤️ Your wisdom is precious, your stories are unforgettable, and your love is priceless. 🥰"))
                list.add(WishesDataModel(description = "💖 Happy Birthday, Grandpa! 🎉 May every year bring you more wonderful memories and more reasons to celebrate. ✨"))
                list.add(WishesDataModel(description = "🥳 Happy Birthday, Grandpa! 💙 Stay happy, stay healthy, and keep blessing us with your wonderful presence. 🙏"))
                list.add(WishesDataModel(description = "🎂 Happy Birthday to the most special Grandpa! ❤️ Thank you for being such an important part of our lives. 🌟"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, Grandpa! 🥰 May your day be filled with family love, joyful conversations, and beautiful memories. 💖"))
                list.add(WishesDataModel(description = "💙 Happy Birthday, Grandpa! 🎂 Wishing you many more happy birthdays surrounded by everyone who loves you. ❤️"))
                list.add(WishesDataModel(description = "🎉 Happy Birthday, dear Grandpa! ❤️ May you always be blessed with good health, happiness, peace, and a loving family around you. 🙏"))

            }

            BirthdayPerson.ME -> {

                list.add(WishesDataModel(description = "Happy Birthday to me! 🎂✨ Here’s to another beautiful year of growth, happiness, and unforgettable memories. 💫"))
                list.add(WishesDataModel(description = "Cheers to another year of being me! 🥳🎉 May this birthday bring new adventures, bigger dreams, and endless happiness. ❤️"))
                list.add(WishesDataModel(description = "Happy Birthday to the person who has been with me through every high and low — me! 😄🎂 Here’s to becoming an even better version of myself. 🌟"))
                list.add(WishesDataModel(description = "Another year older, wiser, and hopefully better at pretending I have everything figured out! 😂🎂 Happy Birthday to me!"))
                list.add(WishesDataModel(description = "Today is all about celebrating me! 🥳 May this new chapter be filled with peace, success, laughter, and countless reasons to smile. ✨"))
                list.add(WishesDataModel(description = "Happy Birthday to me! 🎂 May I always have the courage to chase my dreams and the strength to overcome every challenge. 💪🌟"))
                list.add(WishesDataModel(description = "Here’s to another trip around the sun! ☀️🎉 May this year be kinder, brighter, happier, and more exciting than the last."))
                list.add(WishesDataModel(description = "Happy Birthday to me! 🥳 Today I celebrate how far I’ve come and look forward to everything that is still waiting for me. 💫"))
                list.add(WishesDataModel(description = "A new age, a new chapter, and countless new possibilities! 🎂✨ Happy Birthday to me!"))
                list.add(WishesDataModel(description = "Happy Birthday to me! ❤️ May I continue to grow, learn, laugh, love, and create a life I’m proud of. 🌱✨"))
                list.add(WishesDataModel(description = "Today I’m celebrating myself, my journey, and every little victory along the way. 🥳🎂 Happy Birthday to me!"))
                list.add(WishesDataModel(description = "Another year, another collection of memories! 📸🎉 May the coming year give me even more beautiful moments to remember."))
                list.add(WishesDataModel(description = "Happy Birthday to me! 🎂 May my dreams get bigger, my worries get smaller, and my smile get brighter every day. 😊✨"))
                list.add(WishesDataModel(description = "Growing older is mandatory, but growing wiser is a choice! 😄🎂 Here’s to making this year count. Happy Birthday to me!"))
                list.add(WishesDataModel(description = "Cheers to myself! 🥂🎉 May this birthday mark the beginning of one of the happiest and most successful chapters of my life."))
                list.add(WishesDataModel(description = "Happy Birthday to me! 🌟 May I always believe in myself, trust my journey, and never stop chasing what makes me happy. ❤️"))
                list.add(WishesDataModel(description = "Today I’m not counting the years, I’m counting the experiences, lessons, and memories that made me who I am. 🎂💫 Happy Birthday to me!"))
                list.add(WishesDataModel(description = "Another year older, but still young enough to make questionable decisions! 😂🎉 Happy Birthday to me!"))
                list.add(WishesDataModel(description = "Happy Birthday to me! 🎂 May this year bring unexpected blessings, exciting opportunities, and plenty of reasons to celebrate. ✨"))
                list.add(WishesDataModel(description = "This birthday is a reminder that every year is a gift. 🎁❤️ I’m grateful for life, lessons, loved ones, and everything ahead. Happy Birthday to me!"))
                list.add(WishesDataModel(description = "New age unlocked! 🔓🎂 May this level of life bring better adventures, stronger confidence, and bigger achievements. 🎮✨"))
                list.add(WishesDataModel(description = "Happy Birthday to me! 🥳 May I become stronger through challenges, wiser through experiences, and happier through every little moment."))
                list.add(WishesDataModel(description = "Today I celebrate the person I am and the person I’m becoming. 🎂🌟 Happy Birthday to me!"))
                list.add(WishesDataModel(description = "May this birthday be the start of a year filled with good health, great opportunities, peaceful days, and unforgettable memories. 🎉❤️"))
                list.add(WishesDataModel(description = "Happy Birthday to me! 🎂 Here’s to leaving behind what no longer serves me and welcoming everything that helps me grow. 🌱✨"))
                list.add(WishesDataModel(description = "Another year of life, another reason to be grateful. 🙏🎉 Happy Birthday to me, and may the best chapters still be ahead!"))
                list.add(WishesDataModel(description = "Birthday mood: grateful, happy, and ready for whatever comes next! 😎🎂✨ Happy Birthday to me!"))
                list.add(WishesDataModel(description = "Happy Birthday to me! 🎉 May I always find reasons to laugh, people worth keeping, and dreams worth chasing. ❤️"))
                list.add(WishesDataModel(description = "Here’s to another year of learning, growing, traveling, laughing, and making memories that last forever. 🌍🎂 Happy Birthday to me!"))
                list.add(WishesDataModel(description = "Today I choose to celebrate myself without comparing my journey to anyone else’s. 🎂❤️ Happy Birthday to me!"))
                list.add(WishesDataModel(description = "Happy Birthday to me! 🥳 May this year bring me closer to my goals and further away from unnecessary stress. 😂✨"))
                list.add(WishesDataModel(description = "Another birthday, another chance to dream bigger and live better! 🎂🚀 May this year be absolutely amazing."))
                list.add(WishesDataModel(description = "Happy Birthday to me! 💫 I’m proud of everything I’ve survived, everything I’ve learned, and everything I’m becoming."))
                list.add(WishesDataModel(description = "May this new year of my life be filled with confidence, courage, love, laughter, and countless beautiful surprises. 🎁🎂 Happy Birthday to me!"))
                list.add(WishesDataModel(description = "Birthday reminder: I’ve made it through every difficult day so far. 💪❤️ Here’s to another year of strength and success. Happy Birthday to me!"))
                list.add(WishesDataModel(description = "Happy Birthday to me! 🎂 May I never forget to appreciate the little things while working toward the big things. ✨"))
                list.add(WishesDataModel(description = "A toast to myself and the journey ahead! 🥂🎉 May every month of this new year bring something worth smiling about."))
                list.add(WishesDataModel(description = "Happy Birthday to me! ❤️ May I have the courage to start over, the wisdom to choose wisely, and the heart to enjoy the journey."))
                list.add(WishesDataModel(description = "Another year has been added to my story. 📖🎂 May the next chapter be filled with happiness, adventure, success, and beautiful surprises."))
                list.add(WishesDataModel(description = "Happy Birthday to me! 🎉 May I keep becoming someone my younger self would be proud of. 🌟"))
                list.add(WishesDataModel(description = "Today is my day! 🎂🥳 No stress, no regrets, just gratitude for another beautiful year of life."))
                list.add(WishesDataModel(description = "Happy Birthday to me! ✨ May this year bring clarity to my dreams, peace to my mind, and happiness to my heart. ❤️"))
                list.add(WishesDataModel(description = "Another year older means another year of stories, lessons, laughs, and unforgettable moments. 📸🎂 Happy Birthday to me!"))
                list.add(WishesDataModel(description = "Happy Birthday to me! 🥳 May I always have the confidence to be myself and the courage to build the life I truly want."))
                list.add(WishesDataModel(description = "Here’s to celebrating every scar, every success, every lesson, and every beautiful moment that brought me here. 🎂❤️ Happy Birthday to me!"))
                list.add(WishesDataModel(description = "May this birthday open the door to new opportunities, meaningful connections, exciting adventures, and endless happiness. 🚪✨ Happy Birthday to me!"))
                list.add(WishesDataModel(description = "Happy Birthday to me! 🎉 May I spend this year worrying less, smiling more, and living every moment to the fullest. 😄"))
                list.add(WishesDataModel(description = "A fresh year of life begins today! 🎂🌟 May I make it one of my most memorable, meaningful, and successful years yet."))
                list.add(WishesDataModel(description = "Happy Birthday to me! ❤️ I’m grateful for who I am, proud of how far I’ve come, and excited for where I’m going."))
                list.add(WishesDataModel(description = "Cheers to another year of life, dreams, challenges, victories, and everything in between! 🥂🎂 Happy Birthday to me!"))
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