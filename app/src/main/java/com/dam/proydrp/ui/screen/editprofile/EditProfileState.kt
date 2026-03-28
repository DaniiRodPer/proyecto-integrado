package com.dam.proydrp.ui.screen.editprofile

import com.dam.proydrp.data.model.AccommodationTag
import com.dam.proydrp.data.model.UserTag
import java.time.LocalDate

sealed class EditProfileState {
    data object Loading : EditProfileState()
    data object Error : EditProfileState()
    data class Success(
        val id: String = "",
        val userPicUrl: String = "",
        val name: String = "",
        val surname: String = "",
        val city: String = "",
        val userDescription: String = "",
        val birthDate: LocalDate = LocalDate.now(),
        val userTags: List<UserTag> = listOf(),
        val accommodationDescription: String = "",
        val accommodationPicsUrls: List<String> = listOf(),
        val rooms: Int = 1,
        val bathrooms: Int = 1,
        val squareMeters: String = "",
        val accommodationTags: List<AccommodationTag> = listOf(),
        val isSaving: Boolean = false,
        val canSave: Boolean = true
    ) : EditProfileState()
}