package com.app.happy_birthday.mvvm.wishes.view_model

import androidx.lifecycle.ViewModel
import com.app.happy_birthday.helper.helper_model.BirthdayPerson
import com.app.happy_birthday.mvvm.wishes.adapter.WishListAdapter
import com.app.happy_birthday.mvvm.wishes.model.WishesDataModel

data class WishesObj(
    var day: BirthdayPerson = BirthdayPerson.NONE
):java.io.Serializable
class WishesViewModel:ViewModel() {

    var obj = WishesObj()
    var arrListWishData = kotlin.collections.ArrayList<WishesDataModel?>()
    var adapterWish = WishListAdapter()

    fun updateBhajanListAdapter() {
        adapterWish.setData(arrListWishData)
    }


}