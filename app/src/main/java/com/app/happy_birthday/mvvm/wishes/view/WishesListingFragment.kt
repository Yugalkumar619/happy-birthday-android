package com.app.happy_birthday.mvvm.wishes.view

import AdsManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.FragmentWishesListingBinding
import com.app.happy_birthday.helper.BaseFragment
import com.app.happy_birthday.helper.Constants
import com.app.happy_birthday.helper.Extensions.getSerializableViaArgument
import com.app.happy_birthday.helper.LocaleHelper.getAllBhajan
import com.app.happy_birthday.helper.interfaces.CommonInterfaceClickEvent
import com.app.happy_birthday.mvvm.home.view.HomeActivity
import com.app.happy_birthday.mvvm.wishes.view_model.WishesObj
import com.app.happy_birthday.mvvm.wishes.view_model.WishesViewModel


class WishesListingFragment : BaseFragment() {

    private lateinit var mActivity: HomeActivity
    private lateinit var binding: FragmentWishesListingBinding
    private lateinit var viewModel: WishesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as HomeActivity
        viewModel = ViewModelProvider(this)[WishesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishesListingBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields()
        initOnClickListener()

    }


    private fun initializeFields(){

        binding.root.post {
            AdsManager.loadBanner(requireActivity(), R.id.banner_container)
        }

        if (arguments?.containsKey("WishesObj") == true) {
            viewModel.obj = arguments?.getSerializableViaArgument("WishesObj", WishesObj::class.java)?: WishesObj()
        }


        binding.ilToolbar.root.backgroundTintList = ContextCompat.getColorStateList(mActivity,R.color.color_blue)

        setUpToolbar(binding.ilToolbar,
            title = viewModel.obj.day.displayName +" "+ getString(R.string.label_wishes),
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


        binding.rvBhajan.adapter = viewModel.adapterBhajan
        viewModel.adapterBhajan
        viewModel.arrListBhajanData.clear()
        viewModel.arrListBhajanData.addAll(getAllBhajan(viewModel.obj.day))
        viewModel.adapterBhajan.onClickEvent = onItemClickListener
        viewModel.updateBhajanListAdapter()
    }

    private val onItemClickListener = object :  CommonInterfaceClickEvent{
        override fun onItemClick(type: String, position: Int) {

            var data = viewModel.arrListBhajanData.get(position)
            AdsManager.showInterstitial(mActivity) {}
//            navigateToBhajanDetails(data)
        }
    }
    private fun initOnClickListener(){

    }

}