package com.dam.proydrp.ui.screen.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.proydrp.data.mock.mockUserProfileList
import com.dam.proydrp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {
    var state : ProfileState  by mutableStateOf(ProfileState.Loading)
        private set

    init {
        loadUser()
    }

    val user = mockUserProfileList[0]

    private fun loadUser() {
        viewModelScope.launch {
            delay(2000)
            state = if (user == null) {
                ProfileState.NoData
            } else {
                ProfileState.Success(user = user)
            }
        }
    }
}