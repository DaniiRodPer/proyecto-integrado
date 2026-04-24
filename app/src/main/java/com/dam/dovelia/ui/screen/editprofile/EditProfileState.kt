package com.dam.dovelia.ui.screen.editprofile

import com.dam.dovelia.data.model.AccommodationTag
import com.dam.dovelia.data.model.CityResult
import com.dam.dovelia.data.model.UserTag
import java.io.File
import java.time.LocalDate

sealed class EditProfileState {
    data object Loading : EditProfileState()
    data object Error : EditProfileState()
    data class Success(
        // User data
        val id: String = "",

        val userPicUrl: String = "",
        val pendingProfilePicUpload: File? = null,
        val pendingProfilePicDeletion: String? = null,

        val name: String = "",
        val nameIsError: Boolean = false,
        val nameError: Int? = null,

        val surname: String = "",
        val surnameIsError: Boolean = false,
        val surnameError: Int? = null,

        val email: String = "",
        val emailIsError: Boolean = false,
        val emailError: Int? = null,

        val city: String = "",
        val cityIsError: Boolean = false,
        val cityError: Int? = null,

        val citySearchQuery: String = "",
        val citySearchResults: List<CityResult> = emptyList(),

        val userDescription: String = "",
        val userDescriptionIsError: Boolean = false,
        val userDescriptionError: Int? = null,

        val birthDate: LocalDate? = null,
        val birthDateIsError: Boolean = false,
        val birthDateError: Int? = null,

        val userTags: List<UserTag> = listOf(),
        val userTagsIsError: Boolean = false,
        val userTagsError: Int? = null,



        // Accommodation data
        val accommodationDescription: String = "",
        val accommodationDescriptionIsError: Boolean = false,
        val accommodationDescriptionError: Int? = null,

        val accommodationPicsUrls: List<String> = listOf(),
        val accommodationPicsIsError: Boolean = false,
        val accommodationPicsError: Int? = null,

        val bedrooms: Int = 1,
        val bathrooms: Int = 1,

        val squareMeters: String = "",
        val squareMetersIsError: Boolean = false,
        val squareMetersError: Int? = null,

        val accommodationTags: List<AccommodationTag> = listOf(),
        val accommodationTagsIsError: Boolean = false,
        val accommodationTagsError: Int? = null,

        val pendingUploads: List<File> = emptyList(),
        val pendingDeletions: List<String> = emptyList(),

        val isSaving: Boolean = false,
        val canSave: Boolean = true
    ) : EditProfileState()
}