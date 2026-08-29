package com.app.happy_birthday.mvvm.wishes.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.FragmentWishesDetailsBinding
import com.app.happy_birthday.helper.BaseFragment
import com.app.happy_birthday.helper.Constants
import com.app.happy_birthday.helper.Extensions.getSerializableViaArgument
import com.app.happy_birthday.helper.Global.loadPhotoUsingCoil
import com.app.happy_birthday.helper.interfaces.CommonInterfaceClickEvent
import com.app.happy_birthday.mvvm.wishes.model.WishesDataModel
import com.app.happy_birthday.mvvm.wishes.view_model.WishesDetailsViewModel
import com.app.happy_birthday.mvvm.home.view.HomeActivity


class WishesDetailsFragment : BaseFragment() {

    private lateinit var mActivity: HomeActivity
    private lateinit var binding: FragmentWishesDetailsBinding
    private lateinit var viewModel: WishesDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as HomeActivity
        viewModel = ViewModelProvider(this)[WishesDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields()
        initOnClickListener()

    }


    private fun initializeFields(){
        binding.ilToolbar.root.backgroundTintList = ContextCompat.getColorStateList(mActivity,R.color.color_blue)

        if (arguments?.containsKey("BhajanDataModel") == true) {
            viewModel.obj =
                arguments?.getSerializableViaArgument("BhajanDataModel", WishesDataModel::class.java)?: WishesDataModel()
        }

        setUpToolbar(binding.ilToolbar,
            title = viewModel.obj.title?:"",
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

        binding.txtBhajan.text = viewModel.obj.bhajan
        binding.ivBhajanImage.loadPhotoUsingCoil(viewModel.obj.image)

    }

    private fun initOnClickListener(){

    }

}