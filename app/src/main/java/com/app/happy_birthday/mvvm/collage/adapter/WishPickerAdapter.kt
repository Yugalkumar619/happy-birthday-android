package com.app.happy_birthday.mvvm.collage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.happy_birthday.databinding.ItemCollageWishBinding

class WishPickerAdapter(
    private val onSelected: (String) -> Unit
) : ListAdapter<String, WishPickerAdapter.WishViewHolder>(WishDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        val binding = ItemCollageWishBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        val wish = getItem(position)
        holder.binding.txtWish.text = wish
        holder.binding.root.setOnClickListener { onSelected(wish) }
    }

    class WishViewHolder(val binding: ItemCollageWishBinding) :
        RecyclerView.ViewHolder(binding.root)

    private object WishDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }
}
