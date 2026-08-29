package com.app.happy_birthday.mvvm.wallpaper.view

import AppNavigation.navigateToImageViewer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.FragmentWallpaperListingBinding
import com.app.happy_birthday.helper.BaseFragment
import com.app.happy_birthday.helper.Constants
import com.app.happy_birthday.helper.Extensions.getSerializableViaArgument
import com.app.happy_birthday.helper.Extensions.getWallpaperImages
import com.app.happy_birthday.helper.interfaces.CommonInterfaceClickEvent
import com.app.happy_birthday.mvvm.home.view.HomeActivity
import com.app.happy_birthday.mvvm.wallpaper.model.WallpaperDataModel
import com.app.happy_birthday.mvvm.wallpaper.view_model.WallpaperListingObj
import com.app.happy_birthday.mvvm.wallpaper.view_model.WallpaperViewModel


class WallpaperListingFragment : BaseFragment() {

    private lateinit var mActivity: HomeActivity
    private lateinit var binding: FragmentWallpaperListingBinding
    private lateinit var viewModel: WallpaperViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as HomeActivity
        viewModel = ViewModelProvider(this)[WallpaperViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWallpaperListingBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields()
        initOnClickListener()

    }


    private fun initializeFields(){

        if (arguments?.containsKey("WallpaperListingObj") == true) {
            viewModel.obj =
                arguments?.getSerializableViaArgument("WallpaperListingObj", WallpaperListingObj::class.java)?: WallpaperListingObj()
        }

        binding.root.post {
            AdsManager.loadBanner(requireActivity(), R.id.banner_container)
        }
        setUpToolbar(binding.ilToolbar,
            title = viewModel.obj.day.displayName,
            isBackArrow = true,
            toolbarClickListener = object : CommonInterfaceClickEvent {
                override fun onToolBarListener(type: String) {
                    if (type == Constants.TOOLBAR_ICON_ONE){

                    }
                    if (type == Constants.TOOLBAR_ICON_TWO){

                    }
                }
            }
        )
        binding.ilToolbar.root.backgroundTintList = ContextCompat.getColorStateList(mActivity,if(viewModel.obj.isPremium) R.color.color_yellow else R.color.color_red)



        binding.rvWallpaper.adapter = viewModel.adapterPhotos
        viewModel.adapterPhotos
        viewModel.arrListPhotosData.clear()
        viewModel.arrListPhotosData.addAll(mActivity.getWallpaperImages(isPremium = viewModel.obj.isPremium, premiumCount = viewModel.obj.imageCount, day = viewModel.obj.day))
        viewModel.adapterPhotos.onClickEvent = onItemClickListener
        viewModel.updateWallpaperGridAdapter()
    }

    private val onItemClickListener = object :  CommonInterfaceClickEvent{
        override fun onItemClick(type: String, position: Int) {
//            navigateToImageDetails(ImageDetailsObj(viewModel.arrListPhotosData,position))
            val images =  arrayListOf<WallpaperDataModel?>()
            viewModel.arrListPhotosData.forEach {
                images.add(
                    WallpaperDataModel(
                        image  = it?.image
                    )
                )
            }
            mActivity.navigateToImageViewer(ImageViewerObj(
                images = images,
                currentPosition = position
            ))
        }
    }
    private fun initOnClickListener(){

    }

}