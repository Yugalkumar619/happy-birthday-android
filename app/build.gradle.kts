import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-android")
    id ("kotlin-kapt")
    id ("kotlinx-serialization")
    id ("com.google.devtools.ksp")
    id ("com.google.gms.google-services")
}
android {
    namespace = "com.app.happy_birthday"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.app.happy_birthday"
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            ndk.debugSymbolLevel = "FULL"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    ndkVersion = "29.0.13113456 rc1"
    bundle {
        language {
            enableSplit = false
        }
    }
}

dependencies {

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.google.code.gson:gson:2.13.1")
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")

    /** Navigation graph */
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.1")

    /** firebase */
    platform("com.google.firebase:firebase-bom:32.5.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.4")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-messaging:24.1.2")

    /** google */
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    /** facebook */
    implementation("com.facebook.android:facebook-login:18.0.3")

    /** PushWoosh */
    implementation("com.pushwoosh:pushwoosh-firebase:6.7.26")

    /** Network operations
        Ktor Network dependencies */
    val ktorVersion = "3.2.1"
    implementation ("io.ktor:ktor-client-core:$ktorVersion")
    implementation ("io.ktor:ktor-client-android:$ktorVersion")
    implementation ("io.ktor:ktor-client-logging:$ktorVersion")
    implementation ("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation ("io.ktor:ktor-client-content-negotiation:$ktorVersion")

    /** Coil For Image Loader */
    implementation("io.coil-kt:coil:2.7.0")
    implementation("io.coil-kt:coil-gif:2.7.0")
    implementation("io.coil-kt:coil-base:2.7.0")

    /** SDP (dimension) & SSP (TextSizes) */
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")

    /** Room Local Database */
    val roomVersion = "2.7.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    /** ViewPager2 */
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    /** coroutines */
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    /** GITHUB REPOS*/
    implementation("de.hdodenhof:circleimageview:3.1.0")                                        //circle imageview     //snackbar
    implementation("com.github.egek92:ProteinBar:1.4")                                          //snackbar
    implementation("com.github.cachapa:ExpandableLayout:2.9.2")                                 //ExpandableLayout
    implementation("com.github.zcweng:switch-button:0.0.3@aar")                                 //Switch button
    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")                //Calender view

    /** Datastore */
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("androidx.datastore:datastore-core:1.1.7")

    /** Branch IO */
    implementation("io.branch.sdk.android:library:5.19.0")

    /** Floating Action Button */
    implementation("com.getbase:floatingactionbutton:1.10.1")

    /** Latest AdMob SDK */
    implementation("com.google.android.gms:play-services-ads:24.8.0")

    implementation("androidx.activity:activity-ktx:1.8.2")
// or newer
// If you're using Fragment:
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // Google Billing Library
    implementation("com.android.billingclient:billing-ktx:8.3.0")
// or newer

    implementation("androidx.fragment:fragment-ktx:1.6.2")
    // Photo Editor
    implementation("com.burhanrashid52:photoeditor:3.0.2")

    implementation("com.google.android.ump:user-messaging-platform:4.0.0")


}