package com.dam.dovelia.ui.screen.editprofile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.dovelia.R
import com.dam.dovelia.data.model.Accommodation
import com.dam.dovelia.data.model.AccommodationTag
import com.dam.dovelia.data.model.CityResult
import com.dam.dovelia.data.model.UserProfile
import com.dam.dovelia.data.model.UserTag
import com.dam.dovelia.data.network.BaseResult
import com.dam.dovelia.data.network.RetrofitClient.apiService
import com.dam.dovelia.data.network.SessionManager
import com.dam.dovelia.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state: EditProfileState by mutableStateOf(EditProfileState.Loading)
        private set

    private var isRegisterMode: Boolean = false
    private var userData: UserProfile? = null

    fun start(userData: UserProfile?, isRegister: Boolean) {
        this.userData = userData
        this.isRegisterMode = isRegister

        if (isRegister && userData != null) {
            state = EditProfileState.Success(
                name = userData.name,
                surname = userData.surname,
                email = userData.email
            )
        } else {
            loadProfile()
        }
    }

    fun onSave(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentState = state as? EditProfileState.Success ?: return@launch

            if (!validateFields()) return@launch

            state = currentState.copy(isSaving = true)

            if (isRegisterMode) {

                var finalProfilePicUrl = currentState.userPicUrl
                currentState.pendingProfilePicUpload?.let { file ->
                    when (val res = userRepository.uploadImage(file)) {
                        is BaseResult.Success -> finalProfilePicUrl = res.data
                        is BaseResult.Error -> {}
                    }
                }
                val newlyUploadedUrls = mutableListOf<String>()
                for (file in currentState.pendingUploads) {
                    when (val res = userRepository.uploadImage(file)) {
                        is BaseResult.Success -> newlyUploadedUrls.add(res.data)
                        is BaseResult.Error -> {}
                    }
                }
                val accommodationData = Accommodation(
                    city = currentState.city,
                    description = currentState.accommodationDescription,
                    squareMeters = currentState.squareMeters.toIntOrNull() ?: 0,
                    bathrooms = currentState.bathrooms,
                    bedrooms = currentState.bedrooms,
                    picsUrls = newlyUploadedUrls,
                    tags = currentState.accommodationTags
                )

                val finalUser = userData?.copy(
                    id = java.util.UUID.randomUUID().toString(),
                    name = currentState.name,
                    profilePicUrl = finalProfilePicUrl,
                    surname = currentState.surname,
                    email = currentState.email,
                    birthDate = currentState.birthDate ?: LocalDate.now(),
                    userDescription = currentState.userDescription,
                    userTags = currentState.userTags,
                    creationDate = System.currentTimeMillis() / 1000,
                    accommodation = accommodationData
                )?.apply {
                    this.password = userData?.password ?: ""
                }

                if (finalUser != null) {
                    when (val result = userRepository.register(finalUser)) {
                        is BaseResult.Success -> {
                            val tokenData = result.data
                            sessionManager.saveAuthToken(tokenData.access_token)
                            sessionManager.saveUserId(tokenData.user_id)
                            state = currentState.copy(
                                isSaving = false,
                                userPicUrl = finalProfilePicUrl,
                                pendingProfilePicUpload = null,
                                pendingProfilePicDeletion = null,
                                accommodationPicsUrls = newlyUploadedUrls,
                                pendingUploads = emptyList(),
                                pendingDeletions = emptyList()
                            )
                            onSuccess()
                        }

                        is BaseResult.Error -> {
                            state = currentState.copy(isSaving = false)
                        }
                    }
                }

            } else {
                val userId = sessionManager.fetchUserId()
                if (userId != null) {
                    var finalProfilePicUrl = currentState.userPicUrl
                    currentState.pendingProfilePicUpload?.let { file ->
                        when (val res = userRepository.uploadImage(file)) {
                            is BaseResult.Success -> {
                                finalProfilePicUrl = res.data
                                currentState.pendingProfilePicDeletion?.let { oldUrl ->
                                    userRepository.deleteImage(oldUrl)
                                }
                            }
                            is BaseResult.Error -> {}
                        }
                    }

                    currentState.pendingDeletions.forEach { url ->
                        userRepository.deleteImage(url)
                    }

                    val newlyUploadedUrls = mutableListOf<String>()
                    for (file in currentState.pendingUploads) {
                        when (val res = userRepository.uploadImage(file)) {
                            is BaseResult.Success -> newlyUploadedUrls.add(res.data)
                            is BaseResult.Error -> {}
                        }
                    }

                    val finalUrls = currentState.accommodationPicsUrls
                        .filter { it !in currentState.pendingDeletions } + newlyUploadedUrls

                    val updatedUser = UserProfile(
                        id = userId,
                        name = currentState.name,
                        surname = currentState.surname,
                        email = currentState.email,
                        birthDate = currentState.birthDate ?: LocalDate.now(),
                        userDescription = currentState.userDescription,
                        userTags = currentState.userTags,
                        profilePicUrl = finalProfilePicUrl,
                        accommodation = Accommodation(
                            city = currentState.city,
                            description = currentState.accommodationDescription,
                            squareMeters = currentState.squareMeters.toIntOrNull() ?: 0,
                            bathrooms = currentState.bathrooms,
                            bedrooms = currentState.bedrooms,
                            tags = currentState.accommodationTags,
                            picsUrls = finalUrls
                        )
                    )

                    when (userRepository.updateUser(userId, updatedUser)) {
                        is BaseResult.Success -> {
                            state = currentState.copy(
                                isSaving = false,
                                userPicUrl = finalProfilePicUrl,
                                pendingProfilePicUpload = null,
                                pendingProfilePicDeletion = null,
                                accommodationPicsUrls = finalUrls,
                                pendingUploads = emptyList(),
                                pendingDeletions = emptyList()
                            )
                            onSuccess()
                        }

                        is BaseResult.Error -> {
                            state = currentState.copy(isSaving = false)
                        }
                    }
                }
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            state = EditProfileState.Loading
            when (val result = userRepository.getMyUser()) {
                is BaseResult.Success -> {
                    val user = result.data
                    state = EditProfileState.Success(
                        id = user.id,
                        userPicUrl = user.profilePicUrl,
                        name = user.name,
                        surname = user.surname,
                        email = user.email,
                        userDescription = user.userDescription,
                        birthDate = user.birthDate,
                        userTags = user.userTags,
                        accommodationDescription = user.accommodation?.description ?: "",
                        bedrooms = user.accommodation?.bedrooms ?: 1,
                        bathrooms = user.accommodation?.bathrooms ?: 1,
                        squareMeters = user.accommodation?.squareMeters?.toString() ?: "",
                        accommodationTags = user.accommodation?.tags ?: emptyList(),
                        accommodationPicsUrls = user.accommodation?.picsUrls ?: emptyList(),
                        city = user.accommodation?.city ?: "",
                        citySearchQuery =  user.accommodation?.city ?: ""
                    )
                }

                is BaseResult.Error -> {
                    state = EditProfileState.Error
                }
            }
        }
    }

    fun onAddAccommodationPhoto(file: File) {
        val currentState = state as? EditProfileState.Success ?: return
        state = currentState.copy(pendingUploads = currentState.pendingUploads + file)
    }

    fun onDeleteAccommodationPhoto(photo: Any) {
        val currentState = state as? EditProfileState.Success ?: return

        if (photo is String) {
            state = currentState.copy(pendingDeletions = currentState.pendingDeletions + photo)
        } else if (photo is File) {
            state = currentState.copy(pendingUploads = currentState.pendingUploads - photo)
        }
    }

    private fun validateFields(): Boolean {
        val currentState = state as? EditProfileState.Success ?: return false

        val isCityEmpty = currentState.city.isBlank()
        val isUserDescriptionEmpty = currentState.userDescription.isBlank()
        val isAccommodationDescriptionEmpty = currentState.accommodationDescription.isBlank()
        val isSquareMetersInvalid =
            currentState.squareMeters.isBlank() || (currentState.squareMeters.toIntOrNull()
                ?: 0) <= 0
        val areUserTagsEmpty = currentState.userTags.isEmpty()
        val areAccommodationTagsEmpty = currentState.accommodationTags.isEmpty()
        val validPhotosCount = currentState.accommodationPicsUrls.count { it !in currentState.pendingDeletions } + currentState.pendingUploads.size
        val arePhotosInsufficient = validPhotosCount < 3

        state = currentState.copy(
            cityIsError = isCityEmpty,
            cityError = if (isCityEmpty) R.string.error_city_required else null,

            userDescriptionIsError = isUserDescriptionEmpty,
            userDescriptionError = if (isUserDescriptionEmpty) R.string.error_user_description_required else null,

            accommodationDescriptionIsError = isAccommodationDescriptionEmpty,
            accommodationDescriptionError = if (isAccommodationDescriptionEmpty) R.string.error_accommodation_description_required else null,

            squareMetersIsError = isSquareMetersInvalid,
            squareMetersError = if (isSquareMetersInvalid) R.string.error_square_meters_invalid else null,

            userTagsIsError = areUserTagsEmpty,
            userTagsError = if (areUserTagsEmpty) R.string.error_user_tags_required else null,

            accommodationTagsIsError = areAccommodationTagsEmpty,
            accommodationTagsError = if (areAccommodationTagsEmpty) R.string.error_accommodation_tags_required else null,

            accommodationPicsIsError = arePhotosInsufficient,
            accommodationPicsError = if (arePhotosInsufficient) R.string.error_insufficient_photos else null
        )

        return !isCityEmpty && !isUserDescriptionEmpty && !isAccommodationDescriptionEmpty &&
                !isSquareMetersInvalid && !areUserTagsEmpty && !areAccommodationTagsEmpty &&
                !arePhotosInsufficient
    }

    fun onBirthDayChange(newVal: LocalDate?) {
        val currentState = state
        if (currentState is EditProfileState.Success) {
            val selectedDate = newVal ?: LocalDate.now()
            state = currentState.copy(birthDate = selectedDate)
        }
    }

    fun onCityQueryChange(newQuery: String) {
        val currentState = state as? EditProfileState.Success ?: return
        state = currentState.copy(citySearchQuery = newQuery)
    }

    fun searchCities() {
        val currentState = state as? EditProfileState.Success ?: return
        if (currentState.citySearchQuery.length < 2) return

        viewModelScope.launch {
            try {
                val response = apiService.searchCity(name = currentState.citySearchQuery)
                if (response.isSuccessful) {
                    val current = state as? EditProfileState.Success ?: return@launch
                    state = current.copy(citySearchResults = response.body()?.results ?: emptyList())
                }
            } catch (e: Exception) {
                val current = state as? EditProfileState.Success ?: return@launch
                state = current.copy(citySearchResults = emptyList())
            }
        }
    }

    fun onCitySelected(city: CityResult) {
        val currentState = state as? EditProfileState.Success ?: return

        state = if (city.id != -1) {
            val fullLocationName = listOfNotNull(
                city.name,
                city.admin1.takeIf { !it.isNullOrBlank() },
                city.country
            ).joinToString(", ")

            currentState.copy(
                city = fullLocationName,
                citySearchQuery = fullLocationName,
                citySearchResults = emptyList(),
                cityIsError = false,
                cityError = null
            )
        } else {
            currentState.copy(citySearchResults = emptyList())
        }
    }

    fun onUserDescriptionChange(newVal: String) {
        val currentState = state
        if (currentState is EditProfileState.Success) {
            if (newVal.length <= 150) {
                state = currentState.copy(userDescription = newVal)
            }
        }
    }
    fun onAccommodationDescriptionChange(newVal: String) {
        val currentState = state
        if (currentState is EditProfileState.Success) {
            if (newVal.length <= 1000) {
                state = currentState.copy(accommodationDescription = newVal)
            }
        }
    }

    fun onRoomsChange(newVal: Int) {
        val currentState = state
        if (currentState is EditProfileState.Success) {
            state = currentState.copy(bedrooms = newVal)
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
            if (newVal.length <= 5) {
                try {
                    newVal.toInt()
                    state = currentState.copy(squareMeters = newVal)
                } catch (_: NumberFormatException) {}
            }
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

    fun onProfilePhotoChanged(file: File) {
        val currentState = state as? EditProfileState.Success ?: return
        state = currentState.copy(
            pendingProfilePicUpload = file,
            pendingProfilePicDeletion = currentState.userPicUrl.ifBlank { currentState.pendingProfilePicDeletion }
        )
    }
}