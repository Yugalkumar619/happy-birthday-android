package com.app.happy_birthday.mvvm.ringtone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.LvItemRingtoneListBinding
import com.app.happy_birthday.helper.Global.loadPhotoUsingCoil
import com.app.happy_birthday.helper.interfaces.CommonInterfaceClickEvent
import com.app.happy_birthday.mvvm.ringtone.model.RingtoneDataModel

// here is the CustomAdapter class with photo list, listener and context parameters in constructor
class RingtoneListAdapter constructor() : RecyclerView.Adapter<RingtoneListAdapter.MyViewHolder>() {

    var onClickEvent: CommonInterfaceClickEvent? = null
    var arrPhotoList: ArrayList<RingtoneDataModel?> = kotlin.collections.ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder{
        val binding: LvItemRingtoneListBinding = LvItemRingtoneListBinding.inflate(
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
        holder.binding.ivBhajan.loadPhotoUsingCoil(a?.image)
        holder.binding.root.setOnClickListener{
            onClickEvent?.onItemClick("image",position)
        }
        holder.binding.ivShare.setOnClickListener {
            onClickEvent?.onItemClick("share",position)
        }
        holder.binding.ivPlayPause.setOnClickListener {
            onClickEvent?.onItemClick("play",position)
        }

        holder.binding.txtBhajanDescription.text = a?.description
        holder.binding.txtBhajanTitle.text = a?.title
        holder.binding.ivPlayPause.setImageResource(if(a?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play)
    }


    override fun getItemCount(): Int {
        return arrPhotoList.size
    }

    class MyViewHolder(var binding: LvItemRingtoneListBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setData(data: ArrayList<RingtoneDataModel?>) {
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

