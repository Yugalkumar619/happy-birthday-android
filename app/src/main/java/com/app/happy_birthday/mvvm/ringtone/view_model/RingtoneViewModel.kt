package com.app.happy_birthday.mvvm.ringtone.view_model

import androidx.lifecycle.ViewModel
import com.app.happy_birthday.mvvm.ringtone.adapter.RingtoneListAdapter
import com.app.happy_birthday.mvvm.ringtone.model.RingtoneDataModel

class RingtoneViewModel:ViewModel() {
    var arrListRingtoneData = kotlin.collections.ArrayList<RingtoneDataModel?>()
    var adapterRingtone = RingtoneListAdapter()

    fun updateRingtoneListAdapter() {
        adapterRingtone.setData(arrListRingtoneData)
    }


}