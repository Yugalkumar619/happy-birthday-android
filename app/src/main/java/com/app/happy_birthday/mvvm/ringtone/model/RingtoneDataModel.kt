package com.app.happy_birthday.mvvm.ringtone.model

import java.io.Serializable

data class RingtoneDataModel(
    var title: String?= null,
    var description: String?= null,
    var bhajan: String?= null,
    var image: Int?= null,
    var ringtone: Int?= null,
    var isPlaying: Boolean ?= false
): Serializable