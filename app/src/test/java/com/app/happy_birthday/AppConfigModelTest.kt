package com.app.happy_birthday

import com.app.happy_birthday.helper.helper_model.AppConfigModel
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppConfigModelTest {

    private val gson = Gson()

    @Test
    fun `reads settings saved by a build without R8`() {
        val config = gson.fromJson("""{"lang":"ar","cartBadgeCount":"3"}""", AppConfigModel::class.java)

        assertEquals("ar", config.lang)
        assertEquals("3", config.cartBadgeCount)
    }

    @Test
    fun `reads settings saved by a build where R8 renamed the fields`() {
        val config = gson.fromJson("""{"a":"ar","b":"3"}""", AppConfigModel::class.java)

        assertEquals("ar", config.lang)
        assertEquals("3", config.cartBadgeCount)
    }

    @Test
    fun `saves stable field names`() {
        val json = gson.toJson(AppConfigModel(lang = "en", cartBadgeCount = "0"))

        assertTrue(json, json.contains("\"lang\":\"en\""))
        assertTrue(json, json.contains("\"cartBadgeCount\":\"0\""))
    }
}
