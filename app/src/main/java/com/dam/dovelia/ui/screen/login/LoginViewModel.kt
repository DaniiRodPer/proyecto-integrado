package com.dam.dovelia.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.dovelia.R
import com.dam.dovelia.data.model.LoginRequest
import com.dam.dovelia.data.model.UserProfile
import com.dam.dovelia.data.network.BaseResult
import com.dam.dovelia.data.network.SessionManager
import com.dam.dovelia.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Clase LoginViewModel:
 * Se encarga de gestionar la lógica de autenticación de la aplicación.
 * Controla tanto el inicio de sesión tradicional con email y contraseña
 * como el acceso rápido mediante la cuenta de Google.
 *
 * @property userRepository
 * @property sessionManager
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set


    /**
     * Función onLogin:
     * Ejecuta el proceso de inicio de sesión estándar. Primero valida que los
     * campos no esten vacíos y luego realiza la petición al servidor.
     *
     * Si el acceso es correcto, guarda los datos de sesión y ejecuta el evento
     * para entrar a la app, de lo contrario muetsra un error de credenciales.
     *
     * @param onSuccess - evento para navegar al home tras un login exitoso.
     */
    fun onLogin(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val localValid = validateFields()
            if (!localValid) return@launch

            state = state.copy(isLoading = true, credentialsIsError = false)
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
                        isLoading = false,
                        credentialsIsError = true,
                        credentialsError = R.string.login_error
                    )
                }
            }
        }
    }

    /**
     * Función onGoogleLogin:
     * Gestiona el acceso mediante el token proporcionado por Google.
     * Comprueba si el usaurio ya existe o si es nuevo en la plataforma.
     *
     * Si es nuevo, redirige al proceso de registro con sus datos básicos, si ya
     * tiene cuenta, guarda la sesión y permite el acceso directo.
     *
     * @param idToken - Token de identidad devuelto por el SDK de Google.
     * @param onSuccess - Callback para acceder si el usuario ya existe.
     * @param onNewUser - Callback para completar el registro si es usuario nuevo.
     */
    fun onGoogleLogin(
        idToken: String,
        onSuccess: () -> Unit,
        onNewUser: (UserProfile) -> Unit
    ) {
        viewModelScope.launch {
            when (val result = userRepository.loginWithGoogle(idToken)) {
                is BaseResult.Success -> {
                    val body = result.data
                    if (body.is_new_user) {
                        val partialUser = UserProfile(
                            name = body.name ?: "",
                            surname = body.surname ?: "",
                            email = body.email ?: "",
                        )
                        onNewUser(partialUser)
                    } else {
                        sessionManager.saveAuthToken(body.access_token!!)
                        sessionManager.saveUserId(body.user_id!!)
                        onSuccess()
                    }
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

    /**
     * Función validateFields:
     * Realiza una comprobacion básica para asegurar que el usuario ha rellenado
     * tanto el email como la contraseña antes de intentar conectar con la API.
     *
     * @return True si los campos contienen texto, False si alguno está vacio.
     */
    private fun validateFields(): Boolean {
        val isEmailEmpty = state.email.isBlank()
        val isPasswordEmpty = state.password.isBlank()

        state = state.copy(
            credentialsIsError = isEmailEmpty || isPasswordEmpty,
            credentialsError = if (isEmailEmpty || isPasswordEmpty) R.string.login_error else null
        )

        return !isEmailEmpty && !isPasswordEmpty
    }
}