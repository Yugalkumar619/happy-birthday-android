package com.app.happy_birthday

import com.app.happy_birthday.mvvm.collage.model.CollageRect
import com.app.happy_birthday.mvvm.collage.model.CollageTemplates
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CollageTemplatesTest {

    private val templates = CollageTemplates.all

    @Test
    fun `there are twenty templates with unique ids`() {
        assertEquals(20, templates.size)
        assertEquals(templates.size, templates.map { it.id }.distinct().size)
    }

    @Test
    fun `each template has its own art name and layout`() {
        assertEquals(templates.size, templates.map { it.backgroundRes }.distinct().size)
        assertEquals(templates.size, templates.map { it.nameRes }.distinct().size)
        assertEquals(templates.size, templates.map { it.layoutStyle }.distinct().size)
    }

    @Test
    fun `all areas are valid fractions of the canvas`() {
        templates.forEach { template ->
            listOf(template.titleArea, template.photoArea, template.wishArea).forEach { area ->
                assertTrue("${template.id}: $area", area.left >= 0f && area.top >= 0f)
                assertTrue("${template.id}: $area", area.right <= 1f && area.bottom <= 1f)
                assertTrue("${template.id}: $area", area.width > 0f && area.height > 0f)
            }
        }
    }

    @Test
    fun `title photos and wish do not overlap`() {
        templates.forEach { template ->
            assertFalse(template.id, overlaps(template.titleArea, template.photoArea))
            assertFalse(template.id, overlaps(template.photoArea, template.wishArea))
            assertFalse(template.id, overlaps(template.titleArea, template.wishArea))
        }
    }

    private fun overlaps(first: CollageRect, second: CollageRect): Boolean =
        first.left < second.right && second.left < first.right &&
            first.top < second.bottom && second.top < first.bottom
}
