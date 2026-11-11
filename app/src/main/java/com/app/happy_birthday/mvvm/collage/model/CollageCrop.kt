package com.app.happy_birthday.mvvm.collage.model

import kotlin.math.max

/**
 * How the user positioned a photo inside its slot.
 *
 * @param focusX the point of the photo shown at the slot's center, as a fraction of its width
 * @param focusY the same, as a fraction of its height
 * @param zoom 1 means the photo just covers the slot
 *
 * Fractions keep the position correct at preview size, at full size and across templates.
 */
data class PhotoAdjustment(
    val focusX: Float = 0.5f,
    val focusY: Float = 0.5f,
    val zoom: Float = 1f
)

/** Where to draw a photo: scale it, then move its top-left corner to ([left], [top]). */
data class PhotoPlacement(val scale: Float, val left: Float, val top: Float)

/** The photo always covers its slot, so moving it can never leave a gap. */
object CollageCrop {

    const val MAX_ZOOM = 3f

    fun place(
        photoWidth: Int,
        photoHeight: Int,
        slot: CollageRect,
        adjustment: PhotoAdjustment
    ): PhotoPlacement {
        val fitted = fit(photoWidth, photoHeight, slot, adjustment)
        val scale = scaleFor(photoWidth, photoHeight, slot, fitted.zoom)
        return PhotoPlacement(
            scale = scale,
            left = slot.left + slot.width / 2f - fitted.focusX * photoWidth * scale,
            top = slot.top + slot.height / 2f - fitted.focusY * photoHeight * scale
        )
    }

    /** Moves the photo by a drag, given in the same unit as [slot]. */
    fun moved(
        adjustment: PhotoAdjustment,
        photoWidth: Int,
        photoHeight: Int,
        slot: CollageRect,
        dragX: Float,
        dragY: Float
    ): PhotoAdjustment {
        val scale = scaleFor(photoWidth, photoHeight, slot, adjustment.zoom)
        return fit(
            photoWidth,
            photoHeight,
            slot,
            adjustment.copy(
                focusX = adjustment.focusX - dragX / (photoWidth * scale),
                focusY = adjustment.focusY - dragY / (photoHeight * scale)
            )
        )
    }

    fun zoomed(
        adjustment: PhotoAdjustment,
        photoWidth: Int,
        photoHeight: Int,
        slot: CollageRect,
        factor: Float
    ): PhotoAdjustment = fit(
        photoWidth,
        photoHeight,
        slot,
        adjustment.copy(zoom = adjustment.zoom * factor)
    )

    /** Limits the zoom and focus so that the photo still covers the slot. */
    private fun fit(
        photoWidth: Int,
        photoHeight: Int,
        slot: CollageRect,
        adjustment: PhotoAdjustment
    ): PhotoAdjustment {
        val zoom = adjustment.zoom.coerceIn(1f, MAX_ZOOM)
        val scale = scaleFor(photoWidth, photoHeight, slot, zoom)
        return PhotoAdjustment(
            focusX = limitFocus(adjustment.focusX, slot.width / (photoWidth * scale)),
            focusY = limitFocus(adjustment.focusY, slot.height / (photoHeight * scale)),
            zoom = zoom
        )
    }

    /** [shownShare] is how much of the photo fits in the slot along this direction. */
    private fun limitFocus(focus: Float, shownShare: Float): Float {
        val margin = (shownShare / 2f).coerceAtMost(0.5f)
        return focus.coerceIn(margin, 1f - margin)
    }

    private fun scaleFor(photoWidth: Int, photoHeight: Int, slot: CollageRect, zoom: Float): Float =
        max(slot.width / photoWidth, slot.height / photoHeight) * zoom
}
