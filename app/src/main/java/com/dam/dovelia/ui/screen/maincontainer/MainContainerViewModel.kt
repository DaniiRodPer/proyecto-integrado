package com.dam.dovelia.ui.screen.maincontainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.dovelia.data.network.BaseResult
import com.dam.dovelia.data.network.SessionManager
import com.dam.dovelia.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainContainerViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
): ViewModel() {

    val unreadUsers = userRepository.unreadUsers

    fun connectWebSocket() {
        viewModelScope.launch {
            val token = withContext(Dispatchers.IO) { sessionManager.getAuthToken() }
            if (!token.isNullOrEmpty()) {
                val myProfile = userRepository.getMyUser()
                if (myProfile is BaseResult.Success) {
                    userRepository.initGlobalWebSocket(myProfile.data.id)
                    userRepository.syncUnreadStatus()
                }
            }
        }
    }

    fun disconnectWebSocket() {
        userRepository.closeWebSocket()
    }

    override fun onCleared() {
        super.onCleared()
        userRepository.closeWebSocket()
    }
}