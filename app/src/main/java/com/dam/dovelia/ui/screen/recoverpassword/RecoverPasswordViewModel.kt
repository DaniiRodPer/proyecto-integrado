package com.dam.dovelia.ui.screen.recoverpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.dovelia.R
import com.dam.dovelia.data.network.BaseResult
import com.dam.dovelia.data.network.SessionManager
import com.dam.dovelia.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Clase RecoverPasswordViewModel:
 * Se encarga de gestionar la lógica para recuperar la cuenta del usaurio.
 * Controla el envío del código de recuperación al correo y la validación
 * de la nueva contraseña introducida.
 *
 * @property userRepository
 * @property sessionManager
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(RecoverPasswordState())
        private set

    fun onEmailChange(newVal: String) {
        state = state.copy(email = newVal, emailIsError = false, emailError = null)
    }

    /**
     * Solo permite introducir digitos y tiene un límite de 4 caracteres para coincidir con el PIN.
     */
    fun onResetCodeChange(newVal: String) {
        if (newVal.length <= 4 && newVal.all { it.isDigit() }) {
            state = state.copy(resetCode = newVal, resetCodeIsError = false, resetCodeError = null)
        }
    }

    fun onNewPasswordChange(newVal: String) {
        state =
            state.copy(newPassword = newVal, newPasswordIsError = false, newPasswordError = null)
    }

    /**
     * Función validateEmail:
     * Utiliza una expresión regular para comprobar que el correo tiene un formato
     * correcto antes de intentar realizar la peticion de recuperación.
     *
     * @return True si el email es valido, False si esta vacío o mal escrito.
     */
    private fun validateEmail(): Boolean {
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$"
        val isEmailEmpty = state.email.isBlank()
        val isEmailValid = state.email.matches(emailPattern.toRegex())

        state = state.copy(
            emailIsError = isEmailEmpty || !isEmailValid,
            emailError = when {
                isEmailEmpty -> R.string.error_email_required
                !isEmailValid -> R.string.error_email_invalid
                else -> null
            }
        )
        return !isEmailEmpty && isEmailValid
    }

    /**
     * Comprueba que el código de reset sea correcto y que la nueva contraseña cumpla con los requisitos minimos de seguridad.
     */
    private fun validateResetFields(): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!._-]).{12,}$"

        val isPinEmpty = state.resetCode.isBlank()
        val isPinValid = state.resetCode.length == 4

        val isPasswordEmpty = state.newPassword.isBlank()
        val isPasswordValid = state.newPassword.matches(passwordPattern.toRegex())

        state = state.copy(
            resetCodeIsError = isPinEmpty || !isPinValid,
            resetCodeError = when {
                isPinEmpty || !isPinValid -> R.string.error_incorrect_code
                else -> null
            },
            newPasswordIsError = isPasswordEmpty || !isPasswordValid,
            newPasswordError = when {
                isPasswordEmpty -> R.string.error_password_required
                !isPasswordValid -> R.string.error_password_invalid
                else -> null
            }
        )
        return !isPinEmpty && isPinValid && !isPasswordEmpty && isPasswordValid
    }


    /**
     * Función onSend:
     * Manda la orden al servidor para enviar el correo de recuperación.
     * Si la perición tiene éxito, cambia el contenido de la pantalla para mostrar los campos del código y la nueva contraseña.
     */
    fun onSend() {
        if (!validateEmail()) return

        state = state.copy(isLoading = true)

        viewModelScope.launch {
            state = when (userRepository.requestRecoveryPin(state.email)) {
                is BaseResult.Success -> {
                    state.copy(isEmailSent = true)
                }

                is BaseResult.Error -> {
                    state.copy(
                        emailIsError = true,
                        emailError = R.string.error_email_invalid,
                        isLoading = false
                    )
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    /**
     * Función onConfirmChanges:
     * Finaliza el proceso de recuperación enviando los nuevos datos al servidor. Si todo sale bien, navega al login
     *
     * @param onSuccess - evento para navegar al login tras confirmar el cambio.
     */
    fun onConfirmChanges(onSuccess: () -> Unit) {
        if (!validateResetFields()) return

        state = state.copy(isLoading = true)

        viewModelScope.launch {
            when (userRepository.resetPassword(state.email, state.resetCode, state.newPassword)) {
                is BaseResult.Success -> {
                    onSuccess()
                }

                is BaseResult.Error -> {
                    state = state.copy(
                        resetCodeIsError = true,
                        resetCodeError = R.string.error_incorrect_code,
                        isLoading = false
                    )
                }
            }
        }
    }
}