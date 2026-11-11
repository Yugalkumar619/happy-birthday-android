package com.app.happy_birthday

import com.app.happy_birthday.mvvm.collage.model.CollageLayout
import com.app.happy_birthday.mvvm.collage.model.CollageRect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CollageLayoutTest {

    private val area = CollageRect(left = 100f, top = 50f, right = 1000f, bottom = 950f)
    private val gap = 12f
    private val tolerance = 0.01f

    @Test
    fun `returns one slot per photo`() {
        for (count in 0..CollageLayout.MAX_PHOTOS) {
            assertEquals(count, CollageLayout.slots(count, area, gap).size)
        }
    }

    @Test
    fun `every slot stays inside the area`() {
        forEachPhotoCount { count, slots ->
            slots.forEach { slot ->
                assertTrue("count $count: $slot", slot.left >= area.left - tolerance)
                assertTrue("count $count: $slot", slot.top >= area.top - tolerance)
                assertTrue("count $count: $slot", slot.right <= area.right + tolerance)
                assertTrue("count $count: $slot", slot.bottom <= area.bottom + tolerance)
            }
        }
    }

    @Test
    fun `slots do not overlap`() {
        forEachPhotoCount { count, slots ->
            for (first in slots.indices) {
                for (second in first + 1 until slots.size) {
                    assertFalse(
                        "count $count: ${slots[first]} overlaps ${slots[second]}",
                        overlaps(slots[first], slots[second])
                    )
                }
            }
        }
    }

    @Test
    fun `slots fill the area to its edges`() {
        forEachPhotoCount { count, slots ->
            assertEquals("count $count", area.left, slots.minOf { it.left }, tolerance)
            assertEquals("count $count", area.top, slots.minOf { it.top }, tolerance)
            assertEquals("count $count", area.right, slots.maxOf { it.right }, tolerance)
            assertEquals("count $count", area.bottom, slots.maxOf { it.bottom }, tolerance)
        }
    }

    @Test
    fun `one photo fills the whole area`() {
        assertEquals(listOf(area), CollageLayout.slots(1, area, gap))
    }

    @Test
    fun `two photos sit side by side with the gap between them`() {
        val (left, right) = CollageLayout.slots(2, area, gap)

        assertEquals(left.top, right.top, tolerance)
        assertEquals(left.width, right.width, tolerance)
        assertEquals(gap, right.left - left.right, tolerance)
    }

    @Test
    fun `rows match the planned layout for each count`() {
        assertEquals(listOf(1), photosPerRow(1))
        assertEquals(listOf(2), photosPerRow(2))
        assertEquals(listOf(1, 2), photosPerRow(3))
        assertEquals(listOf(2, 2), photosPerRow(4))
        assertEquals(listOf(2, 3), photosPerRow(5))
        assertEquals(listOf(3, 3), photosPerRow(6))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects more photos than the limit`() {
        CollageLayout.slots(CollageLayout.MAX_PHOTOS + 1, area, gap)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects a negative count`() {
        CollageLayout.slots(-1, area, gap)
    }

    @Test
    fun `merging adds new photos after the current ones`() {
        val result = CollageLayout.mergePhotos(listOf("a"), listOf("b", "c"))

        assertEquals(listOf("a", "b", "c"), result.photos)
        assertFalse(result.wasLimited)
    }

    @Test
    fun `merging ignores photos already selected`() {
        val result = CollageLayout.mergePhotos(listOf("a", "b"), listOf("b", "a", "c"))

        assertEquals(listOf("a", "b", "c"), result.photos)
        assertFalse(result.wasLimited)
    }

    @Test
    fun `merging exactly up to the limit is not reported as limited`() {
        val result = CollageLayout.mergePhotos(listOf("a", "b"), listOf("c", "d", "e", "f"))

        assertEquals(CollageLayout.MAX_PHOTOS, result.photos.size)
        assertFalse(result.wasLimited)
    }

    @Test
    fun `merging past the limit keeps the earliest photos and reports it`() {
        val added = listOf("c", "d", "e", "f", "g", "h")
        val result = CollageLayout.mergePhotos(listOf("a", "b"), added)

        assertEquals(listOf("a", "b", "c", "d", "e", "f"), result.photos)
        assertTrue(result.wasLimited)
    }

    private fun forEachPhotoCount(check: (Int, List<CollageRect>) -> Unit) {
        for (count in 1..CollageLayout.MAX_PHOTOS) {
            check(count, CollageLayout.slots(count, area, gap))
        }
    }

    private fun photosPerRow(count: Int): List<Int> =
        CollageLayout.slots(count, area, gap)
            .groupBy { it.top }
            .toSortedMap()
            .values
            .map { it.size }

    private fun overlaps(first: CollageRect, second: CollageRect): Boolean =
        first.left < second.right - tolerance &&
            second.left < first.right - tolerance &&
            first.top < second.bottom - tolerance &&
            second.top < first.bottom - tolerance
}
