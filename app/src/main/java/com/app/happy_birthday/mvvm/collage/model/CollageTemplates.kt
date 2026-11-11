package com.app.happy_birthday.mvvm.collage.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.app.happy_birthday.R

/** The app's birthday templates. Art is drawn for a 4:5 canvas. */
object CollageTemplates {

    private val TITLE_AREA = CollageRect(left = 0.24f, top = 0.015f, right = 0.76f, bottom = 0.13f)
    private val PHOTO_AREA = CollageRect(left = 0.07f, top = 0.135f, right = 0.93f, bottom = 0.69f)
    private val WISH_AREA = CollageRect(left = 0.09f, top = 0.73f, right = 0.91f, bottom = 0.95f)

    val all: List<CollageTemplate> = listOf(
        template("confetti", R.string.label_collage_template_confetti, R.drawable.bg_collage_confetti, CollageLayoutStyle.GRID, 0xFFC2185B, 0xFF4A2340, 0.025f),
        template("balloons", R.string.label_collage_template_balloons, R.drawable.bg_collage_balloons, CollageLayoutStyle.CIRCLES, 0xFF0D4F8B, 0xFF17375E),
        template("elegant_gold", R.string.label_collage_template_elegant_gold, R.drawable.bg_collage_elegant_gold, CollageLayoutStyle.ARCHES, 0xFFF6D78B, 0xFFFBEFD5),
        template("starry_night", R.string.label_collage_template_starry_night, R.drawable.bg_collage_starry_night, CollageLayoutStyle.HEXAGONS, 0xFFFFE27A, 0xFFF3F1FF),
        template("hearts", R.string.label_collage_template_hearts, R.drawable.bg_collage_hearts, CollageLayoutStyle.HEARTS, 0xFFC2185B, 0xFF5A1F38),
        template("bunting", R.string.label_collage_template_bunting, R.drawable.bg_collage_bunting, CollageLayoutStyle.POLAROIDS, 0xFFC63D2A, 0xFF4A3420),
        template("polka", R.string.label_collage_template_polka, R.drawable.bg_collage_polka, CollageLayoutStyle.STAGGERED_COLUMNS, 0xFF0F7A5A, 0xFF12463A, 0.02f),
        template("sunshine", R.string.label_collage_template_sunshine, R.drawable.bg_collage_sunshine, CollageLayoutStyle.ORBIT, 0xFFB5490A, 0xFF5A3200),
        template("rainbow", R.string.label_collage_template_rainbow, R.drawable.bg_collage_rainbow, CollageLayoutStyle.FAN, 0xFF7B2FBF, 0xFF2C2C54, 0.02f),
        template("candles", R.string.label_collage_template_candles, R.drawable.bg_collage_candles, CollageLayoutStyle.STAIRS, 0xFF6A2FB5, 0xFF3A2466, 0.025f),
        template("gift_boxes", R.string.label_collage_template_gift_boxes, R.drawable.bg_collage_gift_boxes, CollageLayoutStyle.MOSAIC, 0xFF0F6E66, 0xFF11423E, 0.012f),
        template("floral", R.string.label_collage_template_floral, R.drawable.bg_collage_floral, CollageLayoutStyle.PYRAMID, 0xFFB5345C, 0xFF4F2A2A, 0.03f),
        template("candy_stripes", R.string.label_collage_template_candy_stripes, R.drawable.bg_collage_candy_stripes, CollageLayoutStyle.ROWS, 0xFFD81B60, 0xFF4A1F36, 0.015f),
        template("ocean", R.string.label_collage_template_ocean, R.drawable.bg_collage_ocean, CollageLayoutStyle.HERO_BOTTOM, 0xFF0B5C9E, 0xFF0F3A5F, 0.03f),
        template("fireworks", R.string.label_collage_template_fireworks, R.drawable.bg_collage_fireworks, CollageLayoutStyle.DIAMONDS, 0xFFFFD23F, 0xFFF5F5FF),
        template("leaves", R.string.label_collage_template_leaves, R.drawable.bg_collage_leaves, CollageLayoutStyle.HERO_LEFT, 0xFF1F6B3F, 0xFF21432C, 0.025f),
        template("sunset", R.string.label_collage_template_sunset, R.drawable.bg_collage_sunset, CollageLayoutStyle.HERO_TOP, 0xFFFFE9A8, 0xFFFFFFFF, 0.02f),
        template("bubbles", R.string.label_collage_template_bubbles, R.drawable.bg_collage_bubbles, CollageLayoutStyle.CASCADE, 0xFF007C91, 0xFF06404A),
        template("rose_gold", R.string.label_collage_template_rose_gold, R.drawable.bg_collage_rose_gold, CollageLayoutStyle.HERO_RIGHT, 0xFF8E3F4C, 0xFF56282F),
        template("neon", R.string.label_collage_template_neon, R.drawable.bg_collage_neon, CollageLayoutStyle.FILM_STRIP, 0xFF22F5FF, 0xFFFFD6F7)
    )

    private fun template(
        id: String,
        @StringRes nameRes: Int,
        @DrawableRes backgroundRes: Int,
        layoutStyle: CollageLayoutStyle,
        titleColor: Long,
        wishColor: Long,
        cornerRadius: Float = 0f
    ) = CollageTemplate(
        id = id,
        nameRes = nameRes,
        backgroundRes = backgroundRes,
        layoutStyle = layoutStyle,
        titleArea = TITLE_AREA,
        photoArea = PHOTO_AREA,
        wishArea = WISH_AREA,
        titleTextColor = titleColor.toInt(),
        wishTextColor = wishColor.toInt(),
        cornerRadius = cornerRadius
    )
}
