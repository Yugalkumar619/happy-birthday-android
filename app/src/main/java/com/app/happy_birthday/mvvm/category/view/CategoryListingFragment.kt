package com.app.happy_birthday.mvvm.category.view

import AdsManager
import AppNavigation.navigateToWallpaperListing
import AppNavigation.navigateToWishesListing
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.FragmentCategoryListingBinding
import com.app.happy_birthday.helper.BaseFragment
import com.app.happy_birthday.helper.Constants
import com.app.happy_birthday.helper.Extensions.getCategoryData
import com.app.happy_birthday.helper.Extensions.getSerializableViaArgument
import com.app.happy_birthday.helper.helper_model.ValentineDay
import com.app.happy_birthday.helper.interfaces.CommonInterfaceClickEvent
import com.app.happy_birthday.mvvm.category.view_model.CategoryListingObj
import com.app.happy_birthday.mvvm.category.view_model.CategoryViewModel
import com.app.happy_birthday.mvvm.home.view.HomeActivity
import com.app.happy_birthday.mvvm.wallpaper.view_model.WallpaperListingObj
import com.app.happy_birthday.mvvm.wishes.view_model.WishesObj


class CategoryListingFragment : BaseFragment() {

    private lateinit var mActivity: HomeActivity
    private lateinit var binding: FragmentCategoryListingBinding
    private lateinit var viewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as HomeActivity
        viewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryListingBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields()
        initOnClickListener()

    }


    private fun initializeFields(){

        if (arguments?.containsKey("CategoryListingObj") == true) {
            viewModel.obj =
                arguments?.getSerializableViaArgument("CategoryListingObj", CategoryListingObj::class.java)?: CategoryListingObj()
        }

        binding.root.post {
            AdsManager.loadBanner(requireActivity(), R.id.banner_container)
        }
        setUpToolbar(binding.ilToolbar,
            title = getString(R.string.label_select_a_day),
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



        binding.rvCategory.adapter = viewModel.adapterPhotos
        viewModel.adapterPhotos
        viewModel.arrListPhotosData.clear()
        viewModel.arrListPhotosData.addAll(mActivity.getCategoryData())
        viewModel.adapterPhotos.onClickEvent = onItemClickListener
        viewModel.updateWallpaperGridAdapter()
    }

    private val onItemClickListener = object :  CommonInterfaceClickEvent{
        override fun onItemClick(type: String, position: Int) {
//            navigateToImageDetails(ImageDetailsObj(viewModel.arrListPhotosData,position))
//            val images =  arrayListOf<WallpaperDataModel?>()
//            viewModel.arrListPhotosData.forEach {
//                images.add(
//                    WallpaperDataModel(
//                        image  = it?.image
//                    )
//                )
//            }
//            mActivity.navigateToImageViewer(
//                ImageViewerObj(
//                    images = images,
//                    currentPosition = position
//                )
//            )
            val data = viewModel.arrListPhotosData.get(position)

            if(viewModel.obj.isWishes){
                navigateToWishesListing(WishesObj(data?.day?: ValentineDay.NONE))
            }else{
                navigateToWallpaperListing(
                    WallpaperListingObj(isPremium = false, day = data?.day?: ValentineDay.NONE )
                )
            }

        }
    }
    private fun initOnClickListener(){

    }

}