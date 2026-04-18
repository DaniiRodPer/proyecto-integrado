package com.dam.proydrp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Accommodation(
    val city: String = "",
    val description: String = "",
    @SerializedName("square_meters")
    val squareMeters: Int,
    val bathrooms: Int,
    val bedrooms: Int,
    @SerializedName("pics_urls")
    val picsUrls: List<String>,
    val tags: List<AccommodationTag>
) : Parcelable