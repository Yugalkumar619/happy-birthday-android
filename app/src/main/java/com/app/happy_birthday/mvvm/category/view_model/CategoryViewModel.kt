package com.app.happy_birthday.mvvm.category.view_model

import androidx.lifecycle.ViewModel
import com.app.happy_birthday.mvvm.category.adapter.CategoryAdapter
import com.app.happy_birthday.mvvm.category.model.CategoryDataModel
import java.io.Serializable

data class CategoryListingObj(
    val isPremium: Boolean = false,
    val imageCount: Int = 1,
    val isWishes: Boolean = false
): Serializable
class CategoryViewModel:ViewModel() {
//    val mutFavoritesListingResponseData: MutableLiveData<JokerImagesResponseModel?> = MutableLiveData()

    var obj = CategoryListingObj()
    var arrListPhotosData = kotlin.collections.ArrayList<CategoryDataModel?>()
    var adapterPhotos = CategoryAdapter()

    fun updateWallpaperGridAdapter() {
        adapterPhotos.setData(arrListPhotosData)
    }


}