package com.app.happy_birthday.mvvm.wishes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.happy_birthday.databinding.LvItemWishListBinding
import com.app.happy_birthday.helper.Extensions.makeCopyable
import com.app.happy_birthday.helper.Extensions.shareText
import com.app.happy_birthday.helper.interfaces.CommonInterfaceClickEvent
import com.app.happy_birthday.mvvm.wishes.model.WishesDataModel

// here is the CustomAdapter class with photo list, listener and context parameters in constructor
class WishListAdapter constructor() : RecyclerView.Adapter<WishListAdapter.MyViewHolder>() {

    var onClickEvent: CommonInterfaceClickEvent? = null
    var arrPhotoList: ArrayList<WishesDataModel?> = kotlin.collections.ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder{
        val binding: LvItemWishListBinding = LvItemWishListBinding.inflate(
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
//        holder.binding.ivBhajan.loadPhotoUsingCoil(a?.image)
        holder.binding.root.setOnClickListener{
            onClickEvent?.onItemClick("image",position)
        }

        holder.binding.txtBhajanDescription.text = a?.description
        holder.binding.txtBhajanDescription.makeCopyable(click = holder.binding.ivCopy){
            onClickEvent?.onItemClick("",position)
        }

        holder.binding.ivShare.setOnClickListener {
            context.shareText(holder.binding.txtBhajanDescription.text.toString())
        }
//        holder.binding.txtBhajanTitle.text = a?.title
    }


    override fun getItemCount(): Int {
        return arrPhotoList.size
    }

    class MyViewHolder(var binding: LvItemWishListBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setData(data: ArrayList<WishesDataModel?>) {
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

