package com.app.happy_birthday.mvvm.collage.model

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * A birthday template. Areas are fractions (0 to 1) of the canvas, so one template works at
 * preview size and at full size. The title is drawn as text so that it can be translated.
 *
 * @param layoutStyle how photos are arranged inside [photoArea]; each template has its own
 * @param cornerRadius photo corner radius as a fraction of the canvas width
 */
data class CollageTemplate(
    val id: String,
    @param:StringRes val nameRes: Int,
    @param:DrawableRes val backgroundRes: Int,
    @param:DrawableRes val overlayRes: Int? = null,
    val layoutStyle: CollageLayoutStyle,
    val titleArea: CollageRect,
    val photoArea: CollageRect,
    val wishArea: CollageRect,
    @param:ColorInt val titleTextColor: Int,
    @param:ColorInt val wishTextColor: Int,
    val cornerRadius: Float = 0f
)
