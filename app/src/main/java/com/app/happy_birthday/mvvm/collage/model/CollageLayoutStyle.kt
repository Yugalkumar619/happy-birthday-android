package com.app.happy_birthday.mvvm.collage.model

import kotlin.math.min

enum class SlotShape { RECTANGLE, CIRCLE, HEART, HEXAGON, DIAMOND, ARCH }

/**
 * Where one photo goes.
 *
 * @param rotation degrees clockwise around the center of [rect]
 * @param framed draws a white border and shadow around the photo
 */
data class CollageSlot(
    val rect: CollageRect,
    val shape: SlotShape = SlotShape.RECTANGLE,
    val rotation: Float = 0f,
    val framed: Boolean = false
)

/**
 * The photo arrangements, one for each template.
 *
 * @param isTiled true when photos sit side by side and their bounds never overlap
 */
enum class CollageLayoutStyle(val isTiled: Boolean) {
    GRID(true),
    STAGGERED_COLUMNS(true),
    ROWS(true),
    HERO_LEFT(true),
    HERO_RIGHT(true),
    HERO_TOP(true),
    HERO_BOTTOM(true),
    PYRAMID(true),
    MOSAIC(true),
    STAIRS(true),
    FILM_STRIP(true),
    ARCHES(true),
    POLAROIDS(false),
    CIRCLES(false),
    HEARTS(false),
    HEXAGONS(false),
    DIAMONDS(false),
    ORBIT(false),
    FAN(false),
    CASCADE(false)
}

/** Works out the slots for each style. Sizes come from [area], so any unit works. */
object CollageLayouts {

    fun slots(
        style: CollageLayoutStyle,
        count: Int,
        area: CollageRect,
        gap: Float = 0f
    ): List<CollageSlot> {
        require(count in 0..CollageLayout.MAX_PHOTOS) {
            "count must be between 0 and ${CollageLayout.MAX_PHOTOS}, was $count"
        }
        if (count == 0) return emptyList()
        return SlotBuilder(area, gap).build(style, count)
    }
}

/** A photo's center as fractions of the area. */
private data class Spot(val x: Float, val y: Float, val rotation: Float = 0f)

private class SlotBuilder(private val area: CollageRect, private val gap: Float) {

    private val shortSide = min(area.width, area.height)

    fun build(style: CollageLayoutStyle, count: Int): List<CollageSlot> = when (style) {
        CollageLayoutStyle.GRID ->
            CollageLayout.slots(count, area, gap).map { CollageSlot(it) }

        CollageLayoutStyle.STAGGERED_COLUMNS -> staggeredColumns(count)
        CollageLayoutStyle.ROWS -> rows(count)
        CollageLayoutStyle.HERO_LEFT -> heroSide(count, heroOnLeft = true)
        CollageLayoutStyle.HERO_RIGHT -> heroSide(count, heroOnLeft = false)
        CollageLayoutStyle.HERO_TOP -> heroEnd(count, heroOnTop = true)
        CollageLayoutStyle.HERO_BOTTOM -> heroEnd(count, heroOnTop = false)
        CollageLayoutStyle.PYRAMID -> pyramid(count)
        CollageLayoutStyle.MOSAIC -> mosaic(count)
        CollageLayoutStyle.STAIRS -> stairs(count)
        CollageLayoutStyle.FILM_STRIP -> filmStrip(count)
        CollageLayoutStyle.ARCHES -> arches(count)
        CollageLayoutStyle.POLAROIDS -> polaroids(count)
        CollageLayoutStyle.CIRCLES -> circles(count)
        CollageLayoutStyle.HEARTS -> hearts(count)
        CollageLayoutStyle.HEXAGONS -> hexagons(count)
        CollageLayoutStyle.DIAMONDS -> diamonds(count)
        CollageLayoutStyle.ORBIT -> orbit(count)
        CollageLayoutStyle.FAN -> fan(count)
        CollageLayoutStyle.CASCADE -> cascade(count)
    }

    /** A rectangle given as fractions of the area, with half a gap taken off each side. */
    private fun cell(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        shape: SlotShape = SlotShape.RECTANGLE
    ): CollageSlot {
        val inset = gap / 2f
        return CollageSlot(
            rect = CollageRect(
                left = area.left + x * area.width + inset,
                top = area.top + y * area.height + inset,
                right = area.left + (x + width) * area.width - inset,
                bottom = area.top + (y + height) * area.height - inset
            ),
            shape = shape
        )
    }

    /** A slot centered on [spot], sized as fractions of the area's short side. */
    private fun spot(
        spot: Spot,
        widthFraction: Float,
        heightFraction: Float = widthFraction,
        shape: SlotShape = SlotShape.RECTANGLE,
        framed: Boolean = false
    ): CollageSlot {
        val centerX = area.left + spot.x * area.width
        val centerY = area.top + spot.y * area.height
        val halfWidth = widthFraction * shortSide / 2f
        val halfHeight = heightFraction * shortSide / 2f
        return CollageSlot(
            rect = CollageRect(
                left = centerX - halfWidth,
                top = centerY - halfHeight,
                right = centerX + halfWidth,
                bottom = centerY + halfHeight
            ),
            shape = shape,
            rotation = spot.rotation,
            framed = framed
        )
    }

    /** Rows of equal photos, with [perRow] photos in each row. */
    private fun rowsOf(
        perRow: List<Int>,
        x: Float = 0f,
        y: Float = 0f,
        width: Float = 1f,
        height: Float = 1f,
        shape: SlotShape = SlotShape.RECTANGLE
    ): List<CollageSlot> {
        val rowHeight = height / perRow.size
        return perRow.flatMapIndexed { row, columns ->
            val columnWidth = width / columns
            List(columns) { column ->
                cell(x + column * columnWidth, y + row * rowHeight, columnWidth, rowHeight, shape)
            }
        }
    }

    private fun staggeredColumns(count: Int): List<CollageSlot> {
        if (count == 1) return listOf(cell(0.15f, 0f, 0.7f, 1f))
        val columnWidth = 1f / count
        return List(count) { column ->
            val y = if (column % 2 == 0) 0f else 0.14f
            cell(column * columnWidth, y, columnWidth, 0.86f)
        }
    }

    private fun rows(count: Int): List<CollageSlot> {
        return when (count) {
            1 -> listOf(cell(0f, 0.12f, 1f, 0.76f))
            // More than four single rows would be too thin to show a face.
            5 -> rowsOf(listOf(1, 2, 2))
            6 -> rowsOf(listOf(2, 2, 2))
            else -> rowsOf(List(count) { 1 })
        }
    }

    private fun heroSide(count: Int, heroOnLeft: Boolean): List<CollageSlot> {
        val heroWidth = if (count == 1) 0.84f else HERO_SHARE
        val heroX = if (heroOnLeft) 0f else 1f - heroWidth
        val hero = cell(heroX, 0f, heroWidth, 1f)
        if (count == 1) return listOf(hero)

        val sideX = if (heroOnLeft) heroWidth else 0f
        return listOf(hero) + rowsOf(
            perRow = List(count - 1) { 1 },
            x = sideX,
            width = 1f - heroWidth
        )
    }

    private fun heroEnd(count: Int, heroOnTop: Boolean): List<CollageSlot> {
        val heroHeight = if (count == 1) 0.84f else HERO_SHARE
        val heroY = if (heroOnTop) 0f else 1f - heroHeight
        val hero = cell(0f, heroY, 1f, heroHeight)
        if (count == 1) return listOf(hero)

        val stripY = if (heroOnTop) heroHeight else 0f
        return listOf(hero) + rowsOf(
            perRow = listOf(count - 1),
            y = stripY,
            height = 1f - heroHeight
        )
    }

    private fun pyramid(count: Int): List<CollageSlot> = when (count) {
        1 -> listOf(cell(0.1f, 0.05f, 0.8f, 0.9f))
        2 -> listOf(cell(0f, 0f, 0.72f, 0.5f), cell(0.28f, 0.5f, 0.72f, 0.5f))
        3 -> listOf(cell(0.2f, 0f, 0.6f, 0.5f), cell(0f, 0.5f, 0.5f, 0.5f), cell(0.5f, 0.5f, 0.5f, 0.5f))
        4 -> listOf(
            cell(0.2f, 0f, 0.6f, 0.34f),
            cell(0f, 0.34f, 0.5f, 0.32f),
            cell(0.5f, 0.34f, 0.5f, 0.32f),
            cell(0.2f, 0.66f, 0.6f, 0.34f)
        )
        5 -> listOf(
            cell(0f, 0f, 0.5f, 0.34f),
            cell(0.5f, 0f, 0.5f, 0.34f),
            cell(0.2f, 0.34f, 0.6f, 0.32f),
            cell(0f, 0.66f, 0.5f, 0.34f),
            cell(0.5f, 0.66f, 0.5f, 0.34f)
        )
        else -> listOf(
            cell(0.25f, 0f, 0.5f, 0.34f),
            cell(0.08f, 0.34f, 0.42f, 0.32f),
            cell(0.5f, 0.34f, 0.42f, 0.32f),
            cell(0f, 0.66f, 1f / 3f, 0.34f),
            cell(1f / 3f, 0.66f, 1f / 3f, 0.34f),
            cell(2f / 3f, 0.66f, 1f / 3f, 0.34f)
        )
    }

    private fun mosaic(count: Int): List<CollageSlot> = when (count) {
        1 -> listOf(cell(0f, 0.05f, 0.9f, 0.95f))
        2 -> listOf(cell(0f, 0f, 0.66f, 0.68f), cell(0.66f, 0.68f, 0.34f, 0.32f))
        3 -> listOf(cell(0f, 0f, 0.6f, 0.62f), cell(0.6f, 0f, 0.4f, 0.62f), cell(0f, 0.62f, 1f, 0.38f))
        4 -> listOf(
            cell(0f, 0f, 0.62f, 0.38f),
            cell(0.62f, 0f, 0.38f, 0.62f),
            cell(0.38f, 0.62f, 0.62f, 0.38f),
            cell(0f, 0.38f, 0.38f, 0.62f)
        )
        5 -> listOf(
            cell(0f, 0f, 0.6f, 0.36f),
            cell(0.6f, 0f, 0.4f, 0.64f),
            cell(0.4f, 0.64f, 0.6f, 0.36f),
            cell(0f, 0.36f, 0.4f, 0.64f),
            cell(0.4f, 0.36f, 0.2f, 0.28f)
        )
        else -> listOf(
            cell(0f, 0f, 0.5f, 0.5f),
            cell(0.5f, 0f, 0.25f, 0.25f),
            cell(0.75f, 0f, 0.25f, 0.25f),
            cell(0.5f, 0.25f, 0.5f, 0.25f),
            cell(0f, 0.5f, 0.375f, 0.5f),
            cell(0.375f, 0.5f, 0.625f, 0.5f)
        )
    }

    private fun stairs(count: Int): List<CollageSlot> {
        if (count == 1) return listOf(cell(0.05f, 0f, 0.7f, 0.88f))
        val width = 1f / count
        val height = 0.62f
        return List(count) { step ->
            cell(step * width, step * (1f - height) / (count - 1), width, height)
        }
    }

    private fun filmStrip(count: Int): List<CollageSlot> {
        if (count == 1) return listOf(cell(0.08f, 0.14f, 0.84f, 0.72f))
        return rowsOf(perRow = listOf(count), y = 0.14f, height = 0.72f)
    }

    private fun arches(count: Int): List<CollageSlot> = when (count) {
        1 -> listOf(cell(0.2f, 0f, 0.6f, 1f, SlotShape.ARCH))
        5 -> rowsOf(listOf(3, 2), shape = SlotShape.ARCH)
        6 -> rowsOf(listOf(3, 3), shape = SlotShape.ARCH)
        else -> rowsOf(listOf(count), shape = SlotShape.ARCH)
    }

    private fun polaroids(count: Int): List<CollageSlot> {
        val (spots, size) = when (count) {
            1 -> listOf(Spot(0.5f, 0.5f, -4f)) to 0.8f
            2 -> listOf(Spot(0.3f, 0.47f, -6f), Spot(0.7f, 0.53f, 5f)) to 0.66f
            3 -> listOf(Spot(0.2f, 0.38f, -7f), Spot(0.5f, 0.62f, 3f), Spot(0.8f, 0.38f, 7f)) to 0.54f
            4 -> listOf(
                Spot(0.26f, 0.27f, -6f),
                Spot(0.74f, 0.27f, 5f),
                Spot(0.26f, 0.73f, 4f),
                Spot(0.74f, 0.73f, -5f)
            ) to 0.44f
            5 -> listOf(
                Spot(0.2f, 0.26f, -7f),
                Spot(0.8f, 0.26f, 6f),
                Spot(0.2f, 0.74f, 5f),
                Spot(0.8f, 0.74f, -6f),
                Spot(0.5f, 0.5f, 2f)
            ) to 0.42f
            else -> listOf(
                Spot(0.18f, 0.27f, -6f),
                Spot(0.5f, 0.25f, 4f),
                Spot(0.82f, 0.27f, -3f),
                Spot(0.18f, 0.73f, 5f),
                Spot(0.5f, 0.75f, -4f),
                Spot(0.82f, 0.73f, 6f)
            ) to 0.4f
        }
        return spots.map { spot(it, size, size * 1.08f, framed = true) }
    }

    private fun circles(count: Int): List<CollageSlot> {
        val (spots, size) = when (count) {
            1 -> listOf(Spot(0.5f, 0.5f)) to 0.98f
            2 -> listOf(Spot(0.27f, 0.5f), Spot(0.73f, 0.5f)) to 0.56f
            3 -> listOf(Spot(0.5f, 0.27f), Spot(0.27f, 0.73f), Spot(0.73f, 0.73f)) to 0.52f
            4 -> listOf(Spot(0.5f, 0.21f), Spot(0.2f, 0.5f), Spot(0.8f, 0.5f), Spot(0.5f, 0.79f)) to 0.42f
            5 -> listOf(
                Spot(0.2f, 0.25f),
                Spot(0.8f, 0.25f),
                Spot(0.5f, 0.5f),
                Spot(0.2f, 0.75f),
                Spot(0.8f, 0.75f)
            ) to 0.44f
            else -> threeByTwo to 0.39f
        }
        return spots.map { spot(it, size, shape = SlotShape.CIRCLE) }
    }

    private fun hearts(count: Int): List<CollageSlot> {
        val (spots, size) = when (count) {
            1 -> listOf(Spot(0.5f, 0.5f)) to 0.98f
            2 -> listOf(Spot(0.29f, 0.45f, -6f), Spot(0.71f, 0.55f, 6f)) to 0.62f
            3 -> listOf(Spot(0.5f, 0.3f), Spot(0.25f, 0.72f, -6f), Spot(0.75f, 0.72f, 6f)) to 0.52f
            4 -> listOf(
                Spot(0.28f, 0.27f, -6f),
                Spot(0.72f, 0.27f, 6f),
                Spot(0.28f, 0.73f, 6f),
                Spot(0.72f, 0.73f, -6f)
            ) to 0.46f
            5 -> listOf(
                Spot(0.18f, 0.28f),
                Spot(0.5f, 0.28f),
                Spot(0.82f, 0.28f),
                Spot(0.33f, 0.73f),
                Spot(0.67f, 0.73f)
            ) to 0.42f
            else -> threeByTwo to 0.4f
        }
        return spots.map { spot(it, size, shape = SlotShape.HEART) }
    }

    private fun hexagons(count: Int): List<CollageSlot> {
        val (spots, size) = when (count) {
            1 -> listOf(Spot(0.5f, 0.5f)) to 1f
            2 -> listOf(Spot(0.3f, 0.38f), Spot(0.7f, 0.62f)) to 0.68f
            3 -> listOf(Spot(0.27f, 0.28f), Spot(0.73f, 0.28f), Spot(0.5f, 0.72f)) to 0.54f
            4 -> listOf(Spot(0.32f, 0.26f), Spot(0.68f, 0.26f), Spot(0.14f, 0.74f), Spot(0.86f, 0.74f)) to 0.46f
            5 -> listOf(
                Spot(0.32f, 0.25f),
                Spot(0.68f, 0.25f),
                Spot(0.14f, 0.75f),
                Spot(0.5f, 0.75f),
                Spot(0.86f, 0.75f)
            ) to 0.43f
            else -> listOf(
                Spot(0.32f, 0.19f),
                Spot(0.68f, 0.19f),
                Spot(0.14f, 0.5f),
                Spot(0.5f, 0.5f),
                Spot(0.86f, 0.5f),
                Spot(0.5f, 0.81f)
            ) to 0.36f
        }
        return spots.map { spot(it, size, shape = SlotShape.HEXAGON) }
    }

    private fun diamonds(count: Int): List<CollageSlot> {
        val (spots, size) = when (count) {
            1 -> listOf(Spot(0.5f, 0.5f)) to 1f
            2 -> listOf(Spot(0.3f, 0.5f), Spot(0.7f, 0.5f)) to 0.86f
            3 -> listOf(Spot(0.2f, 0.5f), Spot(0.5f, 0.5f), Spot(0.8f, 0.5f)) to 0.64f
            4 -> listOf(Spot(0.5f, 0.25f), Spot(0.2f, 0.5f), Spot(0.8f, 0.5f), Spot(0.5f, 0.75f)) to 0.5f
            5 -> listOf(
                Spot(0.2f, 0.25f),
                Spot(0.8f, 0.25f),
                Spot(0.5f, 0.5f),
                Spot(0.2f, 0.75f),
                Spot(0.8f, 0.75f)
            ) to 0.5f
            else -> threeByTwo to 0.46f
        }
        return spots.map { spot(it, size, shape = SlotShape.DIAMOND) }
    }

    private fun orbit(count: Int): List<CollageSlot> {
        val (center, centerSize, satellites, satelliteSize) = when (count) {
            1 -> OrbitPlan(Spot(0.42f, 0.5f), 0.96f, emptyList(), 0f)
            2 -> OrbitPlan(Spot(0.4f, 0.5f), 0.9f, listOf(Spot(0.85f, 0.75f)), 0.4f)
            3 -> OrbitPlan(Spot(0.5f, 0.5f), 0.8f, listOf(Spot(0.12f, 0.24f), Spot(0.88f, 0.76f)), 0.38f)
            4 -> OrbitPlan(
                Spot(0.5f, 0.5f),
                0.72f,
                listOf(Spot(0.13f, 0.22f), Spot(0.87f, 0.22f), Spot(0.5f, 0.0f + 0.87f)),
                0.3f
            )
            5 -> OrbitPlan(Spot(0.5f, 0.5f), 0.66f, corners, 0.32f)
            else -> OrbitPlan(Spot(0.5f, 0.45f), 0.6f, corners + Spot(0.5f, 0.9f), 0.26f)
        }
        return listOf(spot(center, centerSize, shape = SlotShape.CIRCLE)) +
            satellites.map { spot(it, satelliteSize, shape = SlotShape.CIRCLE) }
    }

    private fun fan(count: Int): List<CollageSlot> {
        if (count == 1) return listOf(spot(Spot(0.5f, 0.5f, -5f), 0.72f, 0.9f))
        val first = if (count <= 3) 0.25f else 0.14f
        val last = 1f - first
        val spread = 9f + count * 2f
        val width = when {
            count == 2 -> 0.66f
            count == 3 -> 0.58f
            count == 4 -> 0.46f
            else -> 0.4f
        }
        return List(count) { index ->
            val progress = index.toFloat() / (count - 1)
            val x = first + (last - first) * progress
            val rotation = -spread + 2f * spread * progress
            val y = 0.5f + kotlin.math.abs(progress - 0.5f) * 0.12f
            spot(Spot(x, y, rotation), width, 0.86f)
        }
    }

    private fun cascade(count: Int): List<CollageSlot> {
        if (count == 1) return listOf(spot(Spot(0.5f, 0.5f), 1.05f, 0.9f, framed = true))
        // Smaller cards leave more of the photos behind them visible.
        val size = when (count) {
            2 -> 0.72f
            3 -> 0.64f
            4 -> 0.56f
            else -> 0.48f
        }
        val widthFraction = size * area.width / shortSide
        return List(count) { index ->
            val progress = index.toFloat() / (count - 1)
            val x = size / 2f + (1f - size) * progress
            val y = size / 2f + (1f - size) * progress
            spot(Spot(x, y), widthFraction * 0.92f, size * 0.94f, framed = true)
        }
    }

    private companion object {
        const val HERO_SHARE = 0.62f

        val threeByTwo = listOf(
            Spot(0.18f, 0.27f),
            Spot(0.5f, 0.27f),
            Spot(0.82f, 0.27f),
            Spot(0.18f, 0.73f),
            Spot(0.5f, 0.73f),
            Spot(0.82f, 0.73f)
        )

        val corners = listOf(
            Spot(0.12f, 0.2f),
            Spot(0.88f, 0.2f),
            Spot(0.12f, 0.8f),
            Spot(0.88f, 0.8f)
        )
    }
}

private data class OrbitPlan(
    val center: Spot,
    val centerSize: Float,
    val satellites: List<Spot>,
    val satelliteSize: Float
)
