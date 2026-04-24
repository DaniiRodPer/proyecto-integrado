package com.dam.dovelia.ui.screen.filterselection

import com.dam.dovelia.data.model.AccommodationTag
import com.dam.dovelia.data.model.CityResult

data class FilterSelectionState(
    var city: String = "",
    var rooms: Int = 1,
    var bathrooms: Int = 1,
    var accommodationTags: List<AccommodationTag> = listOf(),
    var citySearchQuery: String = "",
    var citySearchResults: List<CityResult> = emptyList()
)