package com.app.happy_birthday.helper.location

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}