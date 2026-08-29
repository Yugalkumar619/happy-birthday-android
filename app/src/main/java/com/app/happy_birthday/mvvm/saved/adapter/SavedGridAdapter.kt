package com.app.happy_birthday.mvvm.saved.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.happy_birthday.databinding.LvItemSavedListBinding
import com.app.happy_birthday.helper.Global.loadPhotoUsingCoil
import com.app.happy_birthday.helper.interfaces.CommonInterfaceClickEvent
import com.app.happy_birthday.mvvm.wallpaper.model.WallpaperDataModel

// here is the CustomAdapter class with photo list, listener and context parameters in constructor
class SavedGridAdapter constructor() : RecyclerView.Adapter<SavedGridAdapter.MyViewHolder>() {

    var onClickEvent: CommonInterfaceClickEvent? = null
    var arrPhotoList: ArrayList<WallpaperDataModel?> = kotlin.collections.ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder{
        val binding: LvItemSavedListBinding = LvItemSavedListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val a = arrPhotoList[position]
        val context = holder.itemView.context

//        holder.binding.ivPhoto.setImageResource(a?.image?:0)
        holder.binding.ivPhoto.loadPhotoUsingCoil(a?.imageUri)
        holder.binding.ivPhoto.setOnClickListener{
            onClickEvent?.onItemClick("image",position)
        }

        holder.binding.ivDelete.setOnClickListener {
            onClickEvent?.onItemClick("delete",position)
        }

        holder.binding.ivSetWallpaper.setOnClickListener {
            onClickEvent?.onItemClick("set",position)
        }

    }


    override fun getItemCount(): Int {
        return arrPhotoList.size
    }

    class MyViewHolder(var binding: LvItemSavedListBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setData(data: ArrayList<WallpaperDataModel?>) {
        if (data.isNullOrEmpty()) {
            arrPhotoList = kotlin.collections.ArrayList()
        }
        arrPhotoList = data
        notifyDataSetChanged()
    }

    fun updateData() {
        notifyDataSetChanged()
    }


}

