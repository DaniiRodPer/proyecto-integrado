package com.dam.proydrp.ui.screen.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.proydrp.data.mock.mockUserProfileList
import com.dam.proydrp.data.network.BaseResult
import com.dam.proydrp.data.network.SessionManager
import com.dam.proydrp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
): ViewModel() {
    var state : ProfileState by mutableStateOf(ProfileState.Loading)
        private set

    fun loadUser(targetUserId: String? = null) {
        viewModelScope.launch {
            state = ProfileState.Loading

            val token = withContext(Dispatchers.IO) { sessionManager.getAuthToken() }
            val userId = withContext(Dispatchers.IO) { sessionManager.fetchUserId() }

            val userIdToFind = targetUserId ?: withContext(Dispatchers.IO) { sessionManager.fetchUserId() }

            if (token.isNullOrEmpty() || userIdToFind.isNullOrEmpty()) {
                state = ProfileState.NoData
                return@launch
            }

            state = when (val result = repository.getUserProfile(token, userIdToFind)) {
                is BaseResult.Success -> {
                    ProfileState.Success(user = result.data)
                }

                is BaseResult.Error -> {
                    ProfileState.NoData
                }
            }
        }
    }
}