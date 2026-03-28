package com.dam.proydrp.ui.screen.profile

import com.dam.proydrp.data.model.UserProfile

sealed class ProfileState {
    data object NoData : ProfileState()
    data object Loading : ProfileState()
    data class Success(
        val user: UserProfile,
    ) : ProfileState()
}