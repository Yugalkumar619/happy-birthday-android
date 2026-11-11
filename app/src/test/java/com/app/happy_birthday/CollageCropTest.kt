package com.app.happy_birthday

import com.app.happy_birthday.mvvm.collage.model.CollageCrop
import com.app.happy_birthday.mvvm.collage.model.CollageRect
import com.app.happy_birthday.mvvm.collage.model.PhotoAdjustment
import com.app.happy_birthday.mvvm.collage.model.PhotoPlacement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CollageCropTest {

    private val slot = CollageRect(left = 100f, top = 200f, right = 500f, bottom = 600f)
    private val wideW = 2000
    private val wideH = 1000
    private val tolerance = 0.01f

    private fun assertCoversSlot(placement: PhotoPlacement, width: Int, height: Int) {
        assertTrue("left edge shows a gap: $placement", placement.left <= slot.left + tolerance)
        assertTrue("top edge shows a gap: $placement", placement.top <= slot.top + tolerance)
        assertTrue(
            "right edge shows a gap: $placement",
            placement.left + width * placement.scale >= slot.right - tolerance
        )
        assertTrue(
            "bottom edge shows a gap: $placement",
            placement.top + height * placement.scale >= slot.bottom - tolerance
        )
    }

    @Test
    fun `default position center crops the photo`() {
        val placement = CollageCrop.place(wideW, wideH, slot, PhotoAdjustment())

        assertEquals(0.4f, placement.scale, tolerance)
        assertEquals(slot.left - (wideW * 0.4f - slot.width) / 2f, placement.left, tolerance)
        assertEquals(slot.top, placement.top, tolerance)
    }

    @Test
    fun `dragging right moves the photo right`() {
        val before = CollageCrop.place(wideW, wideH, slot, PhotoAdjustment())
        val adjusted = CollageCrop.moved(PhotoAdjustment(), wideW, wideH, slot, 50f, 0f)
        val after = CollageCrop.place(wideW, wideH, slot, adjusted)

        assertEquals(before.left + 50f, after.left, tolerance)
    }

    @Test
    fun `a photo can be moved to show its edge but never leaves a gap`() {
        listOf(-5000f, 5000f).forEach { drag ->
            val adjusted = CollageCrop.moved(PhotoAdjustment(), wideW, wideH, slot, drag, drag)
            assertCoversSlot(CollageCrop.place(wideW, wideH, slot, adjusted), wideW, wideH)
        }

        val leftEdge = CollageCrop.moved(PhotoAdjustment(), wideW, wideH, slot, 5000f, 0f)
        assertEquals(slot.left, CollageCrop.place(wideW, wideH, slot, leftEdge).left, tolerance)
    }

    @Test
    fun `dragging back after reaching an edge responds straight away`() {
        val atEdge = CollageCrop.moved(PhotoAdjustment(), wideW, wideH, slot, 5000f, 0f)
        val back = CollageCrop.moved(atEdge, wideW, wideH, slot, -20f, 0f)

        assertEquals(slot.left - 20f, CollageCrop.place(wideW, wideH, slot, back).left, tolerance)
    }

    @Test
    fun `zoom stays between no zoom and the maximum`() {
        val zoomedIn = CollageCrop.zoomed(PhotoAdjustment(), wideW, wideH, slot, 100f)
        val zoomedOut = CollageCrop.zoomed(zoomedIn, wideW, wideH, slot, 0.0001f)

        assertEquals(CollageCrop.MAX_ZOOM, zoomedIn.zoom, tolerance)
        assertEquals(1f, zoomedOut.zoom, tolerance)
    }

    @Test
    fun `zooming out from a moved photo never leaves a gap`() {
        var adjustment = CollageCrop.zoomed(PhotoAdjustment(), wideW, wideH, slot, 3f)
        adjustment = CollageCrop.moved(adjustment, wideW, wideH, slot, -5000f, -5000f)
        adjustment = CollageCrop.zoomed(adjustment, wideW, wideH, slot, 0.2f)

        assertCoversSlot(CollageCrop.place(wideW, wideH, slot, adjustment), wideW, wideH)
    }

    @Test
    fun `the same adjustment shows the same part of the photo at any canvas size`() {
        val adjustment = CollageCrop.moved(PhotoAdjustment(), wideW, wideH, slot, 80f, 0f)
        val half = CollageRect(slot.left / 2f, slot.top / 2f, slot.right / 2f, slot.bottom / 2f)

        val full = CollageCrop.place(wideW, wideH, slot, adjustment)
        val small = CollageCrop.place(wideW, wideH, half, adjustment)

        assertEquals(full.scale / 2f, small.scale, tolerance)
        assertEquals(full.left / 2f, small.left, tolerance)
        assertEquals(full.top / 2f, small.top, tolerance)
    }
}
