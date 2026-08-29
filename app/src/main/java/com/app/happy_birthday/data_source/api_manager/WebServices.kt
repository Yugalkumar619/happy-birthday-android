package com.app.happy_birthday.data_source.api_manager

import com.app.happy_birthday.helper.Global


object WebServices {
    private val IsUrlType = if (Global.isTestModeEnabled) AppDomain.DEV else AppDomain.LIVE

    fun getDomainUrl(): String =
        when (IsUrlType) {
            AppDomain.LIVE -> ApiLive
            AppDomain.DEV -> ApiDev
        }

    private const val ApiDev = "http://10.0.2.2:8080/api/auth"
    private const val ApiLive = "https://dev-api.openslot.co/api/v1/"

    const val RegisterWs = "user/register?"
    const val LoginWs = "user/login?"

}

enum class AppDomain {
    LIVE, DEV
}