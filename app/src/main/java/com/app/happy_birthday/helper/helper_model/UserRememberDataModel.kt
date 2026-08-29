package com.app.happy_birthday.helper.helper_model


data class UserRememberModel(
    val `data`: UserRememberDataModel? = null,
)

data class UserRememberDataModel(
    val emailOrPhone: String? = null,
    val password: String? = null,
    val isRemember: Boolean? = false
)
