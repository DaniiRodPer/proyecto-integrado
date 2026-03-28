package com.dam.proydrp.ui.screen.filterselection

import com.dam.proydrp.data.model.AccommodationTag

data class FilterSelectionState (
    var city: String = "",
    var rooms: Int = 1,
    var bathrooms: Int = 1,
    var accommodationTags: List<AccommodationTag> = listOf()
)