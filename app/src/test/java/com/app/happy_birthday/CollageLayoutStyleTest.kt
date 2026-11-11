package com.app.happy_birthday

import com.app.happy_birthday.mvvm.collage.model.CollageLayout
import com.app.happy_birthday.mvvm.collage.model.CollageLayoutStyle
import com.app.happy_birthday.mvvm.collage.model.CollageLayouts
import com.app.happy_birthday.mvvm.collage.model.CollageRect
import com.app.happy_birthday.mvvm.collage.model.CollageSlot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.roundToInt

class CollageLayoutStyleTest {

    /** The real photo area on the 1080×1350 canvas. */
    private val area = CollageRect(left = 75.6f, top = 182.25f, right = 1004.4f, bottom = 931.5f)
    private val gap = 13f
    private val canvas = CollageRect(0f, 0f, 1080f, 1350f)
    private val tolerance = 1f

    private fun slots(style: CollageLayoutStyle, count: Int) =
        CollageLayouts.slots(style, count, area, gap)

    private fun forEachLayout(check: (CollageLayoutStyle, Int, List<CollageSlot>) -> Unit) {
        CollageLayoutStyle.entries.forEach { style ->
            for (count in 1..CollageLayout.MAX_PHOTOS) {
                check(style, count, slots(style, count))
            }
        }
    }

    @Test
    fun `there are twenty styles`() {
        assertEquals(20, CollageLayoutStyle.entries.size)
    }

    @Test
    fun `every style returns one slot per photo`() {
        forEachLayout { style, count, slots ->
            assertEquals("$style with $count", count, slots.size)
        }
    }

    @Test
    fun `no two styles share an arrangement for two or more photos`() {
        for (count in 2..CollageLayout.MAX_PHOTOS) {
            val signatures = CollageLayoutStyle.entries.associateWith { signature(slots(it, count)) }
            val duplicates = signatures.entries.groupBy({ it.value }, { it.key })
                .values
                .filter { it.size > 1 }
            assertTrue("$count photos: duplicates $duplicates", duplicates.isEmpty())
        }
    }

    @Test
    fun `each style changes arrangement as photos are added`() {
        CollageLayoutStyle.entries.forEach { style ->
            val signatures = (1..CollageLayout.MAX_PHOTOS).map { signature(slots(style, it)) }
            assertEquals("$style", signatures.size, signatures.distinct().size)
        }
    }

    @Test
    fun `every slot has a usable size`() {
        forEachLayout { style, count, slots ->
            slots.forEach { slot ->
                assertTrue("$style with $count: $slot", slot.rect.width >= 100f)
                assertTrue("$style with $count: $slot", slot.rect.height >= 100f)
            }
        }
    }

    @Test
    fun `slots stay on the canvas and clear of the title and wish`() {
        val top = area.top - 60f
        val bottom = area.bottom + 60f
        forEachLayout { style, count, slots ->
            slots.forEach { slot ->
                val rect = slot.rect
                assertTrue("$style with $count: $rect", rect.left >= canvas.left)
                assertTrue("$style with $count: $rect", rect.right <= canvas.right)
                assertTrue("$style with $count: $rect", rect.top >= top)
                assertTrue("$style with $count: $rect", rect.bottom <= bottom)
            }
        }
    }

    @Test
    fun `tiled styles never overlap`() {
        forEachLayout { style, count, slots ->
            if (style.isTiled) {
                for (first in slots.indices) {
                    for (second in first + 1 until slots.size) {
                        assertFalse(
                            "$style with $count: ${slots[first].rect} and ${slots[second].rect}",
                            overlaps(slots[first].rect, slots[second].rect)
                        )
                    }
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects more photos than the limit`() {
        slots(CollageLayoutStyle.GRID, CollageLayout.MAX_PHOTOS + 1)
    }

    @Test
    fun `no photos means no slots`() {
        CollageLayoutStyle.entries.forEach { assertTrue(slots(it, 0).isEmpty()) }
    }

    private fun signature(slots: List<CollageSlot>): Set<String> = slots.map { slot ->
        val rect = slot.rect
        listOf(
            rect.left.roundToInt() / 4,
            rect.top.roundToInt() / 4,
            rect.right.roundToInt() / 4,
            rect.bottom.roundToInt() / 4,
            slot.rotation.roundToInt()
        ).joinToString() + slot.shape + slot.framed
    }.toSet()

    private fun overlaps(first: CollageRect, second: CollageRect): Boolean =
        first.left < second.right - tolerance &&
            second.left < first.right - tolerance &&
            first.top < second.bottom - tolerance &&
            second.top < first.bottom - tolerance
}
