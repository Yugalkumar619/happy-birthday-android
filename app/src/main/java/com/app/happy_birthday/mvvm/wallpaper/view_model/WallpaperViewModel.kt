package com.app.happy_birthday.mvvm.wallpaper.view_model

import androidx.lifecycle.ViewModel
import com.app.happy_birthday.helper.helper_model.ValentineDay
import com.app.happy_birthday.mvvm.wallpaper.adapter.WallpaperGridAdapter
import com.app.happy_birthday.mvvm.wallpaper.model.WallpaperDataModel

data class WallpaperListingObj(
    val isPremium: Boolean = false,
    val imageCount: Int = 1,
    val day: ValentineDay = ValentineDay.NONE
):java.io.Serializable
class WallpaperViewModel:ViewModel() {
//    val mutFavoritesListingResponseData: MutableLiveData<JokerImagesResponseModel?> = MutableLiveData()

    var obj = WallpaperListingObj()
    var arrListPhotosData = kotlin.collections.ArrayList<WallpaperDataModel?>()
    var adapterPhotos = WallpaperGridAdapter()

    fun updateWallpaperGridAdapter() {
        adapterPhotos.setData(arrListPhotosData)
    }


}