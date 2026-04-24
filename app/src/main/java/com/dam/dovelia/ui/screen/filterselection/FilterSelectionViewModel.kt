package com.dam.dovelia.ui.screen.filterselection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.dovelia.data.model.AccommodationTag
import com.dam.dovelia.data.model.CityResult
import com.dam.dovelia.data.network.RetrofitClient.apiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterSelectionViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(FilterSelectionState())
        private set

    fun onCityQueryChange(newQuery: String) {
        state = state.copy(citySearchQuery = newQuery)
    }

    fun searchCities() {
        if (state.citySearchQuery.length < 2) return

        viewModelScope.launch {
            try {
                val response = apiService.searchCity(name = state.citySearchQuery)
                if (response.isSuccessful) {
                    state = state.copy(citySearchResults = response.body()?.results ?: emptyList())
                }
            } catch (e: Exception) {
                state = state.copy(citySearchResults = emptyList())
            }
        }
    }

    fun onCitySelected(city: CityResult) {
        state = if (city.id != -1) {
            val fullLocationName = listOfNotNull(
                city.name,
                city.admin1.takeIf { !it.isNullOrBlank() },
                city.country
            ).joinToString(", ")

            state.copy(
                city = fullLocationName,         // Esto es lo que se guarda/filtra
                citySearchQuery = fullLocationName, // Esto es lo que ve el usuario escrito
                citySearchResults = emptyList()
            )
        } else {
            state.copy(citySearchResults = emptyList())
        }
    }

    fun onCityChange(newVal: String) {
        state = state.copy(city = newVal)
    }

    fun onRoomsChange(newVal: Int) {
        state = state.copy(rooms = newVal)
    }

    fun onBathroomsChange(newVal: Int) {
        state = state.copy(bathrooms = newVal)
    }

    fun onInitialTags(tagNames: List<String>) {
        val tags = tagNames.mapNotNull { name ->
            runCatching { AccommodationTag.valueOf(name) }.getOrNull()
        }
        state = state.copy(accommodationTags = tags)
    }

    fun onClearFilters() {
        state = FilterSelectionState()
    }

    fun onAccommodationTagChange(tag: AccommodationTag) {
        val currentTags = state.accommodationTags

        val newTags = if (currentTags.contains(tag)) {
            currentTags - tag
        } else {
            currentTags + tag
        }

        state = state.copy(accommodationTags = newTags)
    }
}