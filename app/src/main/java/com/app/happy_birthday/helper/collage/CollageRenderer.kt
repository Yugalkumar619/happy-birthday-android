package com.app.happy_birthday.helper.collage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.createBitmap
import com.app.happy_birthday.R
import com.app.happy_birthday.mvvm.collage.model.CollageCrop
import com.app.happy_birthday.mvvm.collage.model.CollageLayouts
import com.app.happy_birthday.mvvm.collage.model.CollageRect
import com.app.happy_birthday.mvvm.collage.model.CollageSlot
import com.app.happy_birthday.mvvm.collage.model.CollageTemplate
import com.app.happy_birthday.mvvm.collage.model.PhotoAdjustment
import com.app.happy_birthday.mvvm.collage.model.SlotShape
import kotlin.math.max

/** A loaded photo and how the user positioned it. */
class PlacedPhoto(val bitmap: Bitmap, val adjustment: PhotoAdjustment)

/** Draws a finished collage into a new bitmap. Safe to call off the main thread. */
object CollageRenderer {

    const val OUTPUT_WIDTH = 1080
    const val OUTPUT_HEIGHT = 1350
    const val PREVIEW_WIDTH = OUTPUT_WIDTH / 2
    const val PREVIEW_HEIGHT = OUTPUT_HEIGHT / 2

    /** Shown as an outline hint before the user has picked any photos. */
    const val PLACEHOLDER_PHOTO_COUNT = 3

    private const val GAP_FRACTION = 0.012f
    private const val FRAME_FRACTION = 0.014f
    private const val SHADOW_FRACTION = 0.012f
    private const val SHADOW_COLOR = 0x66000000
    private const val TITLE_MAX_SIZE_FRACTION = 0.068f
    private const val TITLE_MIN_SIZE_FRACTION = 0.03f
    private const val WISH_MAX_SIZE_FRACTION = 0.05f
    private const val WISH_MIN_SIZE_FRACTION = 0.026f
    private const val WISH_LINE_SPACING = 1.1f
    private const val TITLE_LINE_SPACING = 0.82f
    private const val PLACEHOLDER_ALPHA = 48

    /** The last background drawn, which keeps redrawing smooth while a photo is dragged. */
    @Volatile
    private var cachedBackground: CachedBackground? = null

    private class CachedBackground(val drawableRes: Int, val bitmap: Bitmap)

    /** The slots for [count] photos on a canvas of this size, in pixels. */
    fun photoSlots(
        template: CollageTemplate,
        count: Int,
        width: Int,
        height: Int
    ): List<CollageSlot> = CollageLayouts.slots(
        style = template.layoutStyle,
        count = count,
        area = template.photoArea.toPixels(width, height),
        gap = width * GAP_FRACTION
    )

    fun render(
        context: Context,
        template: CollageTemplate,
        photos: List<PlacedPhoto>,
        title: String,
        wish: String,
        width: Int,
        height: Int
    ): Bitmap {
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)

        canvas.drawBitmap(backgroundBitmap(context, template.backgroundRes, width, height), 0f, 0f, null)
        if (photos.isEmpty()) {
            drawPlaceholders(canvas, template, PLACEHOLDER_PHOTO_COUNT, width, height)
        } else {
            drawPhotos(canvas, template, photos, width, height)
        }
        template.overlayRes?.let { drawDrawable(context, canvas, it, width, height) }

        drawFittedText(
            canvas = canvas,
            text = title,
            color = template.titleTextColor,
            typeface = ResourcesCompat.getFont(context, R.font.font_bold),
            area = template.titleArea.toPixels(width, height),
            maxSize = width * TITLE_MAX_SIZE_FRACTION,
            minSize = width * TITLE_MIN_SIZE_FRACTION,
            keepLines = true,
            lineSpacing = TITLE_LINE_SPACING
        )
        drawFittedText(
            canvas = canvas,
            text = wish,
            color = template.wishTextColor,
            typeface = ResourcesCompat.getFont(context, R.font.font_medium),
            area = template.wishArea.toPixels(width, height),
            maxSize = width * WISH_MAX_SIZE_FRACTION,
            minSize = width * WISH_MIN_SIZE_FRACTION,
            keepLines = false,
            lineSpacing = WISH_LINE_SPACING
        )

        return bitmap
    }

    /** Draws a small picture of the template, showing where [photoCount] photos would go. */
    fun drawThumbnail(
        context: Context,
        canvas: Canvas,
        template: CollageTemplate,
        photoCount: Int,
        width: Int,
        height: Int
    ) {
        drawDrawable(context, canvas, template.backgroundRes, width, height)
        drawPlaceholders(canvas, template, photoCount, width, height)
    }

    private fun backgroundBitmap(context: Context, drawableRes: Int, width: Int, height: Int): Bitmap {
        cachedBackground?.let { cached ->
            val matches = cached.drawableRes == drawableRes &&
                cached.bitmap.width == width &&
                cached.bitmap.height == height
            if (matches) return cached.bitmap
        }
        val bitmap = createBitmap(width, height)
        drawDrawable(context, Canvas(bitmap), drawableRes, width, height)
        cachedBackground = CachedBackground(drawableRes, bitmap)
        return bitmap
    }

    private fun drawDrawable(
        context: Context,
        canvas: Canvas,
        drawableRes: Int,
        width: Int,
        height: Int
    ) {
        val drawable = AppCompatResources.getDrawable(context, drawableRes)?.mutate() ?: return
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)
    }

    private fun drawPlaceholders(
        canvas: Canvas,
        template: CollageTemplate,
        count: Int,
        width: Int,
        height: Int
    ) {
        val cornerRadius = width * template.cornerRadius
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ColorUtils.setAlphaComponent(template.wishTextColor, PLACEHOLDER_ALPHA)
        }
        photoSlots(template, count, width, height).forEach { slot ->
            canvas.withSlotRotation(slot) {
                drawPath(slotPath(slot, cornerRadius), paint)
            }
        }
    }

    private fun drawPhotos(
        canvas: Canvas,
        template: CollageTemplate,
        photos: List<PlacedPhoto>,
        width: Int,
        height: Int
    ) {
        val cornerRadius = width * template.cornerRadius
        photoSlots(template, photos.size, width, height).zip(photos).forEach { (slot, photo) ->
            canvas.withSlotRotation(slot) {
                if (slot.framed) drawFrame(this, slot, width)
                drawPhoto(this, photo, slot, cornerRadius)
            }
        }
    }

    private inline fun Canvas.withSlotRotation(slot: CollageSlot, draw: Canvas.() -> Unit) {
        val saved = save()
        val rect = slot.rect
        rotate(slot.rotation, (rect.left + rect.right) / 2f, (rect.top + rect.bottom) / 2f)
        draw()
        restoreToCount(saved)
    }

    private fun drawFrame(canvas: Canvas, slot: CollageSlot, width: Int) {
        val border = width * FRAME_FRACTION
        val rect = slot.rect
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            setShadowLayer(width * SHADOW_FRACTION, 0f, width * SHADOW_FRACTION / 3f, SHADOW_COLOR)
        }
        canvas.drawRect(
            rect.left - border,
            rect.top - border,
            rect.right + border,
            rect.bottom + border,
            paint
        )
    }

    /** Draws the photo where the user positioned it, cut to the slot's shape. */
    private fun drawPhoto(canvas: Canvas, photo: PlacedPhoto, slot: CollageSlot, cornerRadius: Float) {
        val bitmap = photo.bitmap
        val placement = CollageCrop.place(bitmap.width, bitmap.height, slot.rect, photo.adjustment)
        val matrix = Matrix().apply {
            setScale(placement.scale, placement.scale)
            postTranslate(placement.left, placement.top)
        }
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP).apply {
            setLocalMatrix(matrix)
        }
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
            this.shader = shader
        }
        canvas.drawPath(slotPath(slot, cornerRadius), paint)
    }

    private fun slotPath(slot: CollageSlot, cornerRadius: Float): Path {
        val rect = slot.rect
        val left = rect.left
        val top = rect.top
        val width = rect.width
        val height = rect.height
        val path = Path()

        // Points are given as fractions of the slot.
        fun x(fraction: Float) = left + width * fraction
        fun y(fraction: Float) = top + height * fraction

        when (slot.shape) {
            SlotShape.RECTANGLE -> {
                val radius = if (slot.framed) 0f else cornerRadius
                path.addRoundRect(rect.toRectF(), radius, radius, Path.Direction.CW)
            }

            SlotShape.CIRCLE -> path.addOval(rect.toRectF(), Path.Direction.CW)

            SlotShape.HEART -> {
                path.moveTo(x(0.5f), y(1f))
                path.cubicTo(x(0.18f), y(0.76f), x(0f), y(0.55f), x(0f), y(0.3f))
                path.cubicTo(x(0f), y(0.12f), x(0.14f), y(0f), x(0.29f), y(0f))
                path.cubicTo(x(0.38f), y(0f), x(0.46f), y(0.05f), x(0.5f), y(0.14f))
                path.cubicTo(x(0.54f), y(0.05f), x(0.62f), y(0f), x(0.71f), y(0f))
                path.cubicTo(x(0.86f), y(0f), x(1f), y(0.12f), x(1f), y(0.3f))
                path.cubicTo(x(1f), y(0.55f), x(0.82f), y(0.76f), x(0.5f), y(1f))
                path.close()
            }

            SlotShape.HEXAGON -> {
                path.moveTo(x(0.5f), y(0f))
                path.lineTo(x(0.95f), y(0.25f))
                path.lineTo(x(0.95f), y(0.75f))
                path.lineTo(x(0.5f), y(1f))
                path.lineTo(x(0.05f), y(0.75f))
                path.lineTo(x(0.05f), y(0.25f))
                path.close()
            }

            SlotShape.DIAMOND -> {
                path.moveTo(x(0.5f), y(0f))
                path.lineTo(x(1f), y(0.5f))
                path.lineTo(x(0.5f), y(1f))
                path.lineTo(x(0f), y(0.5f))
                path.close()
            }

            SlotShape.ARCH -> {
                val curveHeight = minOf(width, height)
                path.moveTo(left, rect.bottom)
                path.lineTo(left, top + curveHeight / 2f)
                path.arcTo(RectF(left, top, rect.right, top + curveHeight), 180f, 180f)
                path.lineTo(rect.right, rect.bottom)
                path.close()
            }
        }
        return path
    }

    /**
     * Shrinks the text until it fits, and cuts it off with an ellipsis as a last resort.
     *
     * @param keepLines true to keep the text's own line breaks, without wrapping any line
     */
    private fun drawFittedText(
        canvas: Canvas,
        text: String,
        @ColorInt color: Int,
        typeface: Typeface?,
        area: CollageRect,
        maxSize: Float,
        minSize: Float,
        keepLines: Boolean,
        lineSpacing: Float
    ) {
        val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = color
            this.typeface = typeface
        }
        val areaWidth = area.width.toInt()
        val ownLines = text.count { it == '\n' } + 1

        var size = maxSize
        var layout: StaticLayout
        while (true) {
            paint.textSize = size
            layout = buildLayout(text, paint, areaWidth, lineSpacing)
            val fits = layout.height <= area.height && (!keepLines || layout.lineCount == ownLines)
            if (fits || size <= minSize) break
            size = max(minSize, size - 2f)
        }

        if (layout.height > area.height || (keepLines && layout.lineCount > ownLines)) {
            val lineHeight = layout.height / layout.lineCount
            val maxLines = if (keepLines) ownLines else max(1, (area.height / lineHeight).toInt())
            layout = buildLayout(text, paint, areaWidth, lineSpacing, maxLines)
        }

        canvas.save()
        canvas.translate(area.left, area.top + (area.height - layout.height) / 2f)
        layout.draw(canvas)
        canvas.restore()
    }

    private fun buildLayout(
        text: String,
        paint: TextPaint,
        width: Int,
        lineSpacing: Float,
        maxLines: Int = Int.MAX_VALUE
    ): StaticLayout = StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
        .setAlignment(Layout.Alignment.ALIGN_CENTER)
        .setLineSpacing(0f, lineSpacing)
        .setIncludePad(false)
        .setEllipsize(TextUtils.TruncateAt.END)
        .setMaxLines(maxLines)
        .build()

    private fun CollageRect.toPixels(width: Int, height: Int) = CollageRect(
        left = left * width,
        top = top * height,
        right = right * width,
        bottom = bottom * height
    )

    private fun CollageRect.toRectF() = RectF(left, top, right, bottom)
}
