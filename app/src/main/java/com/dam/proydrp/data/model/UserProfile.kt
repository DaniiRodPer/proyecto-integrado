package com.dam.proydrp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.Period

@Parcelize
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: String = "",
    val name: String,
    val surname: String,
    val email: String,
    @SerializedName("birth_date")
    val birthDate: LocalDate = LocalDate.now(),
    @SerializedName("profile_pic_url")
    val profilePicUrl: String = "",
    @SerializedName("user_tags")
    val userTags: List<UserTag> = emptyList(),
    @SerializedName("user_description")
    val userDescription: String = "",
    @SerializedName("creation_date")
    val creationDate: Long = 0,
    val accommodation: Accommodation? = null
): Parcelable {
    @Ignore
    @SerializedName("password")
    var password: String = ""
    val age: Int get() = Period.between(birthDate, LocalDate.now()).years
}