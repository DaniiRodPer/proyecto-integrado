package com.dam.dovelia.ui.screen.maincontainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.dovelia.data.network.BaseResult
import com.dam.dovelia.data.network.SessionManager
import com.dam.dovelia.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Clase MainContainerViewModel:
 * Gestiona el estado global de la aplicación mientras esta
 * encendida. Controla la aceptacion de los términos de uso y mantiene viva
 * la conexión de WebSockets para las notificaciones en tiempo real.
 *
 * @property userRepository
 * @property sessionManager
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@HiltViewModel
class MainContainerViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    val unreadUsers = userRepository.unreadUsers

    private val _showTermsDialog = MutableStateFlow(false)
    val showTermsDialog: StateFlow<Boolean> = _showTermsDialog.asStateFlow()

    init {
        checkTermsAcceptance()
    }

    /**
     * Función checkTermsAcceptance:
     * Comprueba mediante el sessionManager si el usaurio ya ha aceptado los términos
     * legales de la aplicación. Si no lo ha hecho, activa el estado para que se muetsra el diálogo de confirmación en la interfaz.
     */
    private fun checkTermsAcceptance() {
        if (!sessionManager.areTermsAccepted()) {
            _showTermsDialog.value = true
        }
    }

    fun acceptTerms() {
        sessionManager.setTermsAccepted(true)
        _showTermsDialog.value = false
    }

    /**
     * Función connectWebSocket:
     * Se encarga de abrir el tunel de comunicación  con el servidor. Primero obtiene el perfil del usuario para conocer su ID y luego solicita
     * al repositorio que inicie la conexión y sincronice los mensajes no leidos.
     */
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

    /**
     * Función connectWebSocketIfNeeded: Verifica si ya existe una conexión abierts con el servidor antes de intentar
     * crear una nueva. Esto evita duplicar sockets y malgastar recursos de red de forma inecesaria.
     */
    fun connectWebSocketIfNeeded() {
        if (userRepository.isWebSocketOpen()) return
        connectWebSocket()
    }

    /**
     * Función disconnectWebSocket:
     * Cierra la conexión activa con el servidor de WebSockets de forma segura.
     * Se utiliza cuando el usario sale de la app o cierra la sesión para liberar los puertos
     */
    fun disconnectWebSocket() {
        userRepository.closeWebSocket()
    }

    /**
     * Función onCleared:
     * Metodo del ciclo de vida del ViewModel que se ejecuta cuando este se destruye.
     * Aprovechamos para rirar la conexión de red y asegurar que no quedan fugas de memoria
     */
    override fun onCleared() {
        super.onCleared()
        userRepository.closeWebSocket()
    }
}