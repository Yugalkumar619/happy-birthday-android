package com.app.happy_birthday.data_source.local_db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Collections
class Converters {
    @TypeConverter
    fun fromConfigList(list: List<DBConfigurableOptionModel>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toConfigList(json: String?): List<DBConfigurableOptionModel> {
        return if (json.isNullOrEmpty()) Collections.emptyList()
        else Gson().fromJson(json, object : TypeToken<List<DBConfigurableOptionModel>>() {}.type)
    }

    @TypeConverter
    fun fromAttributeList(list: List<DBAttributeOptionModel>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toAttributeList(json: String?): List<DBAttributeOptionModel> {
        return if (json.isNullOrEmpty()) Collections.emptyList()
        else Gson().fromJson(json, object : TypeToken<List<DBAttributeOptionModel>>() {}.type)
    }

    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toStringList(json: String?): List<String> {
        return if (json.isNullOrEmpty()) Collections.emptyList()
        else Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
    }
}