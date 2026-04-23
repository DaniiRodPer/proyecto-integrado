package com.dam.dovelia.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.dovelia.R
import com.dam.dovelia.data.model.LoginRequest
import com.dam.dovelia.data.network.BaseResult
import com.dam.dovelia.data.network.SessionManager
import com.dam.dovelia.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onLogin(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val request = LoginRequest(state.email, state.password)

            when (val result = userRepository.login(request)) {
                is BaseResult.Success -> {
                    val body = result.data
                    sessionManager.saveAuthToken(body.access_token)
                    sessionManager.saveUserId(body.user_id)
                    onSuccess()
                }
                is BaseResult.Error -> {
                    state = state.copy(
                        credentialsIsError = true,
                        credentialsError = R.string.login_error
                    )
                }
            }
        }
    }

    fun onEmailChange(newVal: String){
        state = state.copy(email = newVal)
    }

    fun onPasswordChange(newVal: String){
        state = state.copy(password = newVal)
    }
}