package com.dam.proydrp.ui.screen.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dam.proydrp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set

    fun onRegister() {
        if (validateFields()) {

        }
    }

    private fun validateFields(): Boolean {
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$"
        val passwordPattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!._-]).{12,}$"

        val isNameEmpty = state.name.isBlank()
        val isSurnameEmpty = state.surname.isBlank()
        val isEmailEmpty = state.email.isBlank()
        val isEmailConfirmEmpty = state.emailConfirm.isBlank()
        val isPasswordEmpty = state.password.isBlank()
        val isPasswordConfirmEmpty = state.passwordConfirm.isBlank()

        val isEmailValid = state.email.matches(emailPattern.toRegex())
        val isEmailMatch = state.email == state.emailConfirm

        val isPasswordValid = state.password.matches(passwordPattern.toRegex())
        val isPasswordMatch = state.password == state.passwordConfirm

        state = state.copy(
            nameIsError = isNameEmpty,
            nameError = if (isNameEmpty) "El nombre es obligatorio" else "",

            surnameIsError = isSurnameEmpty,
            surnameError = if (isSurnameEmpty) "El apellido es obligatorio" else "",

            emailIsError = isEmailEmpty || !isEmailValid,
            emailError = when {
                isEmailEmpty -> "El correo es obligatorio"
                !isEmailValid -> "Formato de email inválido"
                else -> ""
            },

            emailConfirmIsError = isEmailConfirmEmpty || !isEmailMatch,
            emailConfirmError = when {
                isEmailConfirmEmpty -> "Debes confirmar el correo"
                !isEmailMatch -> "Los correos no coinciden"
                else -> ""
            },

            passwordIsError = isPasswordEmpty || !isPasswordValid,
            passwordError = when {
                isPasswordEmpty -> "La contraseña es obligatoria"
                !isPasswordValid -> "Mínimo 12 caracteres, mayúscula, número y símbolo"
                else -> ""
            },

            passwordConfirmIsError = isPasswordConfirmEmpty || !isPasswordMatch,
            passwordConfirmError = when {
                isPasswordConfirmEmpty -> "Debes confirmar la contraseña"
                !isPasswordMatch -> "Las contraseñas no coinciden"
                else -> ""
            }
        )

        return !isNameEmpty && !isSurnameEmpty && isEmailValid && isEmailMatch && isPasswordValid && isPasswordMatch
    }

    fun onNameChange(newVal: String) {
        state = state.copy(name = newVal)
    }

    fun onSurnameChange(newVal: String) {
        state = state.copy(surname = newVal)
    }

    fun onEmailChange(newVal: String) {
        state = state.copy(email = newVal)
    }

    fun onEmailConfirmChange(newVal: String) {
        state = state.copy(emailConfirm = newVal)
    }

    fun onPasswordChange(newVal: String) {
        state = state.copy(password = newVal)
    }

    fun onPasswordConfirmChange(newVal: String) {
        state = state.copy(passwordConfirm = newVal)
    }
}