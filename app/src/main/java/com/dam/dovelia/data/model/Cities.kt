package com.dam.dovelia.data.model

data class GeocodingResponse(
    val results: List<CityResult>?
)

data class CityResult(
    val id: Int,
    val name: String,
    val admin1: String?,
    val country: String
) {
    val displayName: String
        get() = listOfNotNull(name, admin1, country).joinToString(", ")
}