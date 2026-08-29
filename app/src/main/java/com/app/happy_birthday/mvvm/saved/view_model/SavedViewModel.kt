package com.app.happy_birthday.mvvm.saved.view_model

import androidx.lifecycle.ViewModel
import com.app.happy_birthday.mvvm.saved.adapter.SavedGridAdapter
import com.app.happy_birthday.mvvm.wallpaper.model.WallpaperDataModel

class SavedViewModel:ViewModel() {
//    val mutFavoritesListingResponseData: MutableLiveData<JokerImagesResponseModel?> = MutableLiveData()
    var arrListPhotosData = kotlin.collections.ArrayList<WallpaperDataModel?>()
    var adapterPhotos = SavedGridAdapter()

    fun updateSaveGridAdapter() {
        adapterPhotos.setData(arrListPhotosData)
    }


}