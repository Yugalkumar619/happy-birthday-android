package com.app.happy_birthday.mvvm.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.happy_birthday.databinding.LvItemCaltegoryListBinding
import com.app.happy_birthday.helper.interfaces.CommonInterfaceClickEvent
import com.app.happy_birthday.mvvm.category.model.CategoryDataModel

// here is the CustomAdapter class with photo list, listener and context parameters in constructor
class CategoryAdapter constructor() : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    var onClickEvent: CommonInterfaceClickEvent? = null
    var arrCategoryList: ArrayList<CategoryDataModel?> = kotlin.collections.ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder{
        val binding: LvItemCaltegoryListBinding = LvItemCaltegoryListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val a = arrCategoryList[position]
        val context = holder.itemView.context

//        holder.binding.ivPhoto.setImageResource(a?.image?:0)
//        holder.binding.ivPhoto.loadPhotoUsingCoil(a?.image)
        holder.binding.txtCategoryName.text = a?.day?.displayName
//        holder.binding.txtDate.text = a?.day?.date
        holder.binding.ivCategoryLogo.setImageResource(a?.image?:0)
        holder.binding.root.setOnClickListener{
            onClickEvent?.onItemClick("image",position)
        }

    }


    override fun getItemCount(): Int {
        return arrCategoryList.size
    }

    class MyViewHolder(var binding: LvItemCaltegoryListBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setData(data: ArrayList<CategoryDataModel?>) {
        if (data.isNullOrEmpty()) {
            arrCategoryList = kotlin.collections.ArrayList()
        }
        arrCategoryList = data
        notifyDataSetChanged()
    }

    fun updateData() {
        notifyDataSetChanged()
    }


}