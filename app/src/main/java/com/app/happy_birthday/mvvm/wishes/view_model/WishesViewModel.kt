package com.app.happy_birthday.mvvm.wishes.view_model

import androidx.lifecycle.ViewModel
import com.app.happy_birthday.helper.helper_model.ValentineDay
import com.app.happy_birthday.mvvm.wishes.adapter.BhajanListAdapter
import com.app.happy_birthday.mvvm.wishes.model.WishesDataModel

data class WishesObj(
    var day: ValentineDay = ValentineDay.NONE
):java.io.Serializable
class WishesViewModel:ViewModel() {

    var obj = WishesObj()
    var arrListBhajanData = kotlin.collections.ArrayList<WishesDataModel?>()
    var adapterBhajan = BhajanListAdapter()

    fun updateBhajanListAdapter() {
        adapterBhajan.setData(arrListBhajanData)
    }


}