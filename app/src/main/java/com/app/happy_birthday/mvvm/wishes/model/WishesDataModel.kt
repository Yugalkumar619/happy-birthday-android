package com.app.happy_birthday.mvvm.wishes.model

import java.io.Serializable

data class WishesDataModel(
    var title: String?= null,
    var description: String?= null,
    var bhajan: String?= null,
    var image: Int?= null,
): Serializable