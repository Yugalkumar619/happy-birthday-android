package com.app.happy_birthday.mvvm.collage.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.app.happy_birthday.helper.collage.CollageRenderer
import com.app.happy_birthday.mvvm.collage.model.CollageTemplate
import kotlin.math.min

/** A small picture of a template, with its photo arrangement drawn on top. */
class CollageThumbnailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var template: CollageTemplate? = null
    private var photoCount = CollageRenderer.PLACEHOLDER_PHOTO_COUNT

    fun bind(template: CollageTemplate, photoCount: Int) {
        this.template = template
        this.photoCount = photoCount
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val template = template ?: return

        val scale = min(
            width.toFloat() / CollageRenderer.OUTPUT_WIDTH,
            height.toFloat() / CollageRenderer.OUTPUT_HEIGHT
        )
        val thumbnailWidth = (CollageRenderer.OUTPUT_WIDTH * scale).toInt()
        val thumbnailHeight = (CollageRenderer.OUTPUT_HEIGHT * scale).toInt()
        if (thumbnailWidth <= 0 || thumbnailHeight <= 0) return

        val saved = canvas.save()
        canvas.translate((width - thumbnailWidth) / 2f, (height - thumbnailHeight) / 2f)
        CollageRenderer.drawThumbnail(context, canvas, template, photoCount, thumbnailWidth, thumbnailHeight)
        canvas.restoreToCount(saved)
    }
}
