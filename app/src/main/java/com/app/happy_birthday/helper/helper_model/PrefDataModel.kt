package com.app.happy_birthday.helper.helper_model

import com.google.gson.annotations.SerializedName

data class StoreDataModel(
    val currency_code_ar: String? = null,
    val currency_code_en: String? = null,
    val dial_code: String? = null,
    val flag: String? = null,
    val is_default: String? = null,
    val iso2_code: String? = null,
    val name: String? = null,
    val name_ar: String? = null,
    val store_code: String? = null,
    var isSelected: Boolean? = false
)

data class DeepLinkModel(
    val PB_target : String= "" ,
    val PB_target_id : String = "",
    val PB_secondary_id : String = "",
    val PB_title_en : String = "",
    val PB_title_ar : String = "",
)

data class Device(
    val device_token: String? = null,
    val device_type: String? = null,
    val device_model: String? = null,
    val os_version: String? = null,
    val app_version: String? = null
)

/**
 * Saved as JSON, so the names must not change when R8 renames these fields. The alternate names
 * read settings saved by a build where R8 had already renamed them.
 */
data class AppConfigModel(
    @SerializedName("lang", alternate = ["a"])
    var lang :String = "en",
    @SerializedName("cartBadgeCount", alternate = ["b"])
    var cartBadgeCount :String = "0"
)
