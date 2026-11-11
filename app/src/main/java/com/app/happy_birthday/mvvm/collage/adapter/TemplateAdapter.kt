package com.app.happy_birthday.mvvm.collage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.happy_birthday.databinding.ItemCollageTemplateBinding
import com.app.happy_birthday.helper.collage.CollageRenderer
import com.app.happy_birthday.mvvm.collage.model.CollageTemplate

class TemplateAdapter(
    private val templates: List<CollageTemplate>,
    private val onSelected: (CollageTemplate) -> Unit
) : RecyclerView.Adapter<TemplateAdapter.TemplateViewHolder>() {

    private var selectedId: String? = null
    private var photoCount = CollageRenderer.PLACEHOLDER_PHOTO_COUNT

    fun setSelected(template: CollageTemplate) {
        val previous = templates.indexOfFirst { it.id == selectedId }
        selectedId = template.id
        if (previous >= 0) notifyItemChanged(previous)
        notifyItemChanged(templates.indexOfFirst { it.id == template.id })
    }

    /** Thumbnails show the arrangement for the photos picked so far. */
    fun setPhotoCount(count: Int) {
        val shown = if (count > 0) count else CollageRenderer.PLACEHOLDER_PHOTO_COUNT
        if (shown == photoCount) return
        photoCount = shown
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        val binding = ItemCollageTemplateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TemplateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        val template = templates[position]
        holder.binding.ivTemplate.bind(template, photoCount)
        holder.binding.txtTemplateName.setText(template.nameRes)
        holder.binding.root.isSelected = template.id == selectedId
        holder.binding.root.setOnClickListener { onSelected(template) }
    }

    override fun getItemCount(): Int = templates.size

    class TemplateViewHolder(val binding: ItemCollageTemplateBinding) :
        RecyclerView.ViewHolder(binding.root)
}
