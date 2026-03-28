package com.dam.proydrp.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dam.proydrp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onLogin(){
    }

    fun onEmailChange(newVal: String){
        state = state.copy(email = newVal)
    }

    fun onPasswordChange(newVal: String){
        state = state.copy(password = newVal)
    }
}