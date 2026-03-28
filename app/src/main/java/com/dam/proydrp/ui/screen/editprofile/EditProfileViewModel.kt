package com.dam.proydrp.ui.screen.editprofile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.proydrp.data.mock.mockUserProfileList
import com.dam.proydrp.data.model.AccommodationTag
import com.dam.proydrp.data.model.UserTag
import com.dam.proydrp.ui.screen.discover.DiscoverState
import com.dam.proydrp.ui.screen.register.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor() : ViewModel() {

    var state : EditProfileState by mutableStateOf(EditProfileState.Loading)
        private set

    val userId = 0

    init {
        loadProfile(userId)
    }

    fun loadProfile(id: Int){
        viewModelScope.launch {
            delay(2000)
            val user = mockUserProfileList[id]
            state = if (user == null) {
                EditProfileState.Error
            } else {
                EditProfileState.Success(
                    id = user.id,
                    userPicUrl = user.profilePicUrl,
                    name = user.name,
                    surname = user.surname,
                    city = user.city,
                    userDescription = user.userDescription,
                    birthDate = user.birthDate,
                    userTags = user.userTags,
                    accommodationDescription = user.accommodationDescription,
                    accommodationPicsUrls = user.accommodationPicsUrls,
                    rooms = user.bedrooms,
                    bathrooms = user.bathrooms,
                    squareMeters = user.squareMeters.toString(),
                    accommodationTags = user.accommodationTags
                )
            }
        }
    }

    fun onBirthDayChange(newVal: LocalDate?) {
        val currentState = state
        if (currentState is EditProfileState.Success) {
            val selectedDate = newVal ?: LocalDate.now()
            state = currentState.copy(birthDate = selectedDate)
        }
    }

    fun onCityChange(newVal: String) {
        val currentState = state
        if (currentState is EditProfileState.Success) {
            state = currentState.copy(city = newVal)
        }
    }

    fun onUserDescriptionChange(newVal: String) {
        val currentState = state
        if (currentState is EditProfileState.Success) {
            state = currentState.copy(userDescription = newVal)
        }
    }

    fun onAccommodationDescriptionChange(newVal: String) {
        val currentState = state
        if (currentState is EditProfileState.Success) {
            state = currentState.copy(accommodationDescription = newVal)
        }
    }

    fun onRoomsChange(newVal: Int) {
        val currentState = state
        if (currentState is EditProfileState.Success) {
            state = currentState.copy(rooms = newVal)
        }
    }

    fun onBathroomsChange(newVal: Int) {
        val currentState = state
        if (currentState is EditProfileState.Success) {
            state = currentState.copy(bathrooms = newVal)
        }
    }

    fun onSquareMetersChange(newVal: String) {
        val currentState = state
        if (currentState is EditProfileState.Success) {
            if (newVal.isEmpty()) {
                state = currentState.copy(squareMeters = newVal)
                return
            }
            try {
                newVal.toInt()
                state = currentState.copy(squareMeters = newVal)
            } catch (e: NumberFormatException) {}
        }
    }

    fun onAccommodationTagChange(tag: AccommodationTag) {
        val currentState = state
        if (currentState is EditProfileState.Success) {
            val currentTags = currentState.accommodationTags
            val newTags = if (currentTags.contains(tag)) {
                currentTags - tag
            } else {
                currentTags + tag
            }
            state = currentState.copy(accommodationTags = newTags)
        }
    }

    fun onUserTagChange(tag: UserTag) {
        val currentState = state
        if (currentState is EditProfileState.Success) {
            val currentTags = currentState.userTags
            val newTags = if (currentTags.contains(tag)) {
                currentTags - tag
            } else {
                if (currentTags.size < 3) {
                    currentTags + tag
                } else {
                    currentTags
                }
            }
            state = currentState.copy(userTags = newTags)
        }
    }

    fun onSave() {

    }
}