package com.dam.dovelia.ui.screen.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.dovelia.R
import com.dam.dovelia.data.model.UserProfile
import com.dam.dovelia.data.network.BaseResult
import com.dam.dovelia.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Clase RegisterViewModel:
 * Gestiona la lógica del primer paso del registro de usuarios.
 * Se encarga de validar los datos personales y de comprobar con el servidor
 * si el correo electronico ya está en uso antes de permitir avanzar.
 *
 * @property userRepository
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set

    /**
     * Función onRegister:
     * Ejecuta el proceso de validación y registro inicial. Comprueba que el email esté libre y crea un objeto de perfil parcial con la contraseña.
     *
     * Si el servidor confirma que el correo es válido, navega a la siguiente pantalla, de lo contrario, muetsra un error.
     *
     * @param onSuccess - evento para navegar al segundo paso del registro.
     */
    fun onRegister(onSuccess: (UserProfile) -> Unit) {
        viewModelScope.launch {
            val localValid = validateFields()
            if (!localValid) return@launch
            state = state.copy(isLoading = true)

            when (val result = userRepository.isEmailAvailable(state.email)) {
                is BaseResult.Success -> {
                    val isAvailable = result.data
                    if (isAvailable) {
                        val partialUser = UserProfile(
                            name = state.name,
                            surname = state.surname,
                            email = state.email,
                        ).apply {
                            password = state.password
                        }
                        onSuccess(partialUser)
                        state = state.copy(
                            isLoading = false
                        )
                    } else {
                        state = state.copy(
                            emailIsError = true,
                            emailError = R.string.error_email_already_registered,
                            isLoading = false
                        )
                    }
                }

                is BaseResult.Error -> {
                    state = state.copy(
                        emailIsError = true,
                        emailError = R.string.error_server_connection,
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Función validateFields:
     * Realiza una comprobación de todos los campos del formulario.
     *
     * También controla que el nombre y los apellidos no superen el límite de
     * caracteres permitido por la base de datos para evitar erores al guardar.
     *
     * @return True si todos los datos cumplen con las reglas de negocio.
     */
    private fun validateFields(): Boolean {
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$"
        val passwordPattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!._-]).{12,}$"

        val isNameEmpty = state.name.isBlank()
        val isNameTooLong = state.name.length > 50
        val isSurnameEmpty = state.surname.isBlank()
        val isSurnameTooLong = state.surname.length > 50
        val isEmailEmpty = state.email.isBlank()
        val isEmailConfirmEmpty = state.emailConfirm.isBlank()
        val isPasswordEmpty = state.password.isBlank()
        val isPasswordConfirmEmpty = state.passwordConfirm.isBlank()

        val isEmailValid = state.email.matches(emailPattern.toRegex())
        val isEmailMatch = state.email == state.emailConfirm


        val isPasswordValid = state.password.matches(passwordPattern.toRegex())
        val isPasswordMatch = state.password == state.passwordConfirm

        state = state.copy(
            nameIsError = isNameEmpty || isNameTooLong,
            nameError = when {
                isNameEmpty -> R.string.error_name_required
                isNameTooLong -> R.string.error_name_too_long
                else -> null
            },

            surnameIsError = isSurnameEmpty || isSurnameTooLong,
            surnameError = when {
                isSurnameEmpty -> R.string.error_surname_required
                isSurnameTooLong -> R.string.error_surname_too_long
                else -> null
            },

            emailIsError = isEmailEmpty || !isEmailValid,
            emailError = when {
                isEmailEmpty -> R.string.error_email_required
                !isEmailValid -> R.string.error_email_invalid
                else -> null
            },

            emailConfirmIsError = isEmailConfirmEmpty || !isEmailMatch,
            emailConfirmError = when {
                isEmailConfirmEmpty -> R.string.error_email_confirm_required
                !isEmailMatch -> R.string.error_emails_do_not_match
                else -> null
            },

            passwordIsError = isPasswordEmpty || !isPasswordValid,
            passwordError = when {
                isPasswordEmpty -> R.string.error_password_required
                !isPasswordValid -> R.string.error_password_invalid
                else -> null
            },

            passwordConfirmIsError = isPasswordConfirmEmpty || !isPasswordMatch,
            passwordConfirmError = when {
                isPasswordConfirmEmpty -> R.string.error_password_confirm_required
                !isPasswordMatch -> R.string.error_passwords_do_not_match
                else -> null
            }
        )

        return !isNameEmpty && !isSurnameEmpty && !isNameTooLong && !isSurnameTooLong && isEmailValid && isEmailMatch && isPasswordValid && isPasswordMatch
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