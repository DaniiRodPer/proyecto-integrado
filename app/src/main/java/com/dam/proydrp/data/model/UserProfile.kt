package com.dam.proydrp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.Period

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: String,
    val name: String,
    val surname: String,
    val email: String,
    val birthDate: LocalDate,
    val profilePicUrl: String,
    val userTags: List<UserTag>,
    val city: String,
    val accommodationDescription: String,
    val userDescription: String,
    val squareMeters: Int,
    val bathrooms: Int,
    val bedrooms: Int,
    val accommodationPicsUrls: List<String>,
    val accommodationTags: List<AccommodationTag>,
    val creationDate: Long,
    ) {
    val age: Int get() = Period.between(birthDate, LocalDate.now()).years
}