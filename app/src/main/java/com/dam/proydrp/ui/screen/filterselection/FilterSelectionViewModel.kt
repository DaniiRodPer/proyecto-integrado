package com.dam.proydrp.ui.screen.filterselection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dam.proydrp.data.model.AccommodationTag
import com.dam.proydrp.data.model.UserTag
import com.dam.proydrp.ui.screen.editprofile.EditProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterSelectionViewModel @Inject constructor(): ViewModel() {

    var state by mutableStateOf(FilterSelectionState())
        private set

    fun onCityChange(newVal: String) {
        state = state.copy(city = newVal)
    }

    fun onRoomsChange(newVal: Int) {
        state = state.copy(rooms = newVal)
    }

    fun onBathroomsChange(newVal: Int) {
        state = state.copy(bathrooms = newVal)
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