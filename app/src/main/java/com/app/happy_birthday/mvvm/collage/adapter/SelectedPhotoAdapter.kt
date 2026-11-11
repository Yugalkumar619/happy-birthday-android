package com.app.happy_birthday.mvvm.collage.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.happy_birthday.databinding.ItemCollageSelectedPhotoBinding

class SelectedPhotoAdapter(
    private val onRemove: (Uri) -> Unit
) : ListAdapter<Uri, SelectedPhotoAdapter.PhotoViewHolder>(UriDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemCollageSelectedPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val uri = getItem(position)
        holder.binding.ivPhoto.load(uri)
        holder.binding.ivRemove.setOnClickListener { onRemove(uri) }
    }

    class PhotoViewHolder(val binding: ItemCollageSelectedPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    private object UriDiffCallback : DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(oldItem: Uri, newItem: Uri) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Uri, newItem: Uri) = oldItem == newItem
    }
}
