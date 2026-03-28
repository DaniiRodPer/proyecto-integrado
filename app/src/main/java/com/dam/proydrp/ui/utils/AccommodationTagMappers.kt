package com.dam.proydrp.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dam.proydrp.R
import com.dam.proydrp.data.model.AccommodationTag

@Composable
fun getAccommodationTagIcon(accommodationTag: AccommodationTag): Int {
    return when (accommodationTag) {
        AccommodationTag.WIFI -> R.drawable.wifi_icon
        AccommodationTag.PETS -> R.drawable.pets_icon
        AccommodationTag.CENTER -> R.drawable.timelapse_icon
        AccommodationTag.AIR_CONDITIONING -> R.drawable.aircon_icon
        AccommodationTag.HEATING -> R.drawable.heating_icon
        AccommodationTag.PARKING -> R.drawable.parking_icon
        AccommodationTag.POOL -> R.drawable.pool_icon
        AccommodationTag.BALCONY -> R.drawable.balcony_icon
        AccommodationTag.FIREPLACE -> R.drawable.fireplace_icon
        AccommodationTag.GYM -> R.drawable.gym_icon
        AccommodationTag.SMOKE_FREE -> R.drawable.smoke_free_icon
    }
}

@Composable
fun getAccommodationTagLabel(accommodationTag: AccommodationTag): String {
    return when (accommodationTag) {
        AccommodationTag.WIFI -> stringResource(R.string.wifi)
        AccommodationTag.PETS -> stringResource(R.string.pets_allowed)
        AccommodationTag.CENTER -> stringResource(R.string.near_center)
        AccommodationTag.AIR_CONDITIONING -> stringResource(R.string.air_conditioning)
        AccommodationTag.HEATING -> stringResource(R.string.heating)
        AccommodationTag.PARKING -> stringResource(R.string.parking)
        AccommodationTag.POOL -> stringResource(R.string.pool)
        AccommodationTag.BALCONY -> stringResource(R.string.balcony)
        AccommodationTag.FIREPLACE -> stringResource(R.string.fireplace)
        AccommodationTag.GYM -> stringResource(R.string.gym)
        AccommodationTag.SMOKE_FREE -> stringResource(R.string.smoke_free)
    }
}