package com.app.happy_birthday.mvvm.collage.model

/** A rectangle in any single unit, such as canvas fractions or pixels. */
data class CollageRect(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {
    val width: Float get() = right - left
    val height: Float get() = bottom - top
}

/** Result of adding photos to a selection. */
data class PhotoMergeResult<T>(
    val photos: List<T>,
    val wasLimited: Boolean
)

/** Photo selection rules, and splitting a template's photo area into one slot per photo. */
object CollageLayout {

    const val MAX_PHOTOS = 6

    /**
     * Adds photos up to [MAX_PHOTOS], ignoring any already selected. The app enforces the limit
     * because the fallback picker on older devices ignores it.
     */
    fun <T> mergePhotos(current: List<T>, added: List<T>): PhotoMergeResult<T> {
        val combined = (current + added).distinct()
        return PhotoMergeResult(
            photos = combined.take(MAX_PHOTOS),
            wasLimited = combined.size > MAX_PHOTOS
        )
    }

    /**
     * @param count number of photos, from 0 to [MAX_PHOTOS]
     * @param area the area to fill
     * @param gap space between slots, in the same unit as [area]
     * @return slots in reading order, or an empty list when [count] is 0
     */
    fun slots(count: Int, area: CollageRect, gap: Float = 0f): List<CollageRect> {
        require(count in 0..MAX_PHOTOS) { "count must be between 0 and $MAX_PHOTOS, was $count" }
        if (count == 0) return emptyList()

        val rows = rowsFor(count)
        val rowHeight = (area.height - gap * (rows.size - 1)) / rows.size

        return rows.flatMapIndexed { rowIndex, columns ->
            val top = area.top + rowIndex * (rowHeight + gap)
            val columnWidth = (area.width - gap * (columns - 1)) / columns

            List(columns) { columnIndex ->
                val left = area.left + columnIndex * (columnWidth + gap)
                CollageRect(left, top, left + columnWidth, top + rowHeight)
            }
        }
    }

    /** Number of photos in each row, from top to bottom. */
    private fun rowsFor(count: Int): List<Int> = when (count) {
        1 -> listOf(1)
        2 -> listOf(2)
        3 -> listOf(1, 2)
        4 -> listOf(2, 2)
        5 -> listOf(2, 3)
        else -> listOf(3, 3)
    }
}
