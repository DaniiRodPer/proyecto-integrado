package com.dam.proydrp.data.converters

import androidx.room.TypeConverter
import com.dam.proydrp.data.model.AccommodationTag
import com.dam.proydrp.data.model.UserTag
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate

class Converters {

    //Photos URLs
    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return list?.let { Json.encodeToString(it) } ?: "[]"
    }
    @TypeConverter
    fun toStringList(data: String?): List<String> {
        return data?.let { Json.decodeFromString(it) } ?: emptyList()
    }

    //User Tags
    @TypeConverter
    fun fromUserTagList(list: List<UserTag>?): String {
        return list?.let { Json.encodeToString(it) } ?: "[]"
    }
    @TypeConverter
    fun toUserTagList(data: String?): List<UserTag> {
        return data?.let { Json.decodeFromString(it) } ?: emptyList()
    }

    //Accommodation Tags
    @TypeConverter
    fun fromAccommodationTagList(list: List<AccommodationTag>?): String {
        return list?.let { Json.encodeToString(it) } ?: "[]"
    }
    @TypeConverter
    fun toAccommodationTagList(data: String?): List<AccommodationTag> {
        return data?.let { Json.decodeFromString(it) } ?: emptyList()
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }
    }
}