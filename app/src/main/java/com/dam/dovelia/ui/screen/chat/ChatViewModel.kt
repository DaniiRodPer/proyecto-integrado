package com.dam.dovelia.ui.screen.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.dovelia.BuildConfig
import com.dam.dovelia.data.model.Message
import com.dam.dovelia.data.model.MessageCreate
import com.dam.dovelia.data.network.BaseResult
import com.dam.dovelia.data.network.SessionManager
import com.dam.dovelia.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import javax.inject.Inject


/**
 * Clase ChatViewModel:
 * Se encarga de la lógica de la mensajería en tiempo real entre usuarios.
 * Gestiona la carga del historial, el envío de nuevos mensajes y la
 * sincronización mediante WebSockets a través del repositorio.
 *
 * @property repository
 * @property sessionManager
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf<ChatState>(ChatState.Loading)
        private set

    private var targetUserIdLocal: String = ""

    /**
     * Función initChat:
     * Inicializa la sesión de chat con un usuario específico. Recupera el
     * historial de mensajes y configura la escucha de nuevos mensajes entrantes.
     *
     * Marca los mensajes como leídos al entrar y actualiza el estado de exito
     * con la información del perifl del destinatario y los mensajes previos.
     *
     * @param targetUserId - Identificador del usuario con el que se abre chat.
     */
    fun initChat(targetUserId: String) {
        if (targetUserIdLocal.isNotEmpty()) return
        targetUserIdLocal = targetUserId

        repository.currentChatUserId = targetUserId

        viewModelScope.launch {
            repository.markAsRead(targetUserId)
            val myProfileResult = repository.getMyUser()
            val myId = if (myProfileResult is BaseResult.Success) myProfileResult.data.id else ""

            val token =
                withContext(Dispatchers.IO) { sessionManager.getAuthToken() } ?: return@launch
            val targetUserResult = repository.getUserProfile(token, targetUserId)

            if (targetUserResult is BaseResult.Success) {
                val historyResult = repository.getChatMessages(token, targetUserId)
                val history =
                    if (historyResult is BaseResult.Success) historyResult.data else emptyList()

                state = ChatState.Success(
                    myUserId = myId,
                    targetUser = targetUserResult.data,
                    messages = history
                )

                launch {
                    repository.incomingMessages.collect { newMessage ->
                        if (newMessage.sender_id == targetUserIdLocal) {
                            val currentState = state as? ChatState.Success
                            if (currentState != null && !currentState.messages.any { it.id == newMessage.id }) {
                                state =
                                    currentState.copy(messages = currentState.messages + newMessage)
                                repository.markAsRead(targetUserIdLocal)
                            }
                        }
                    }
                }
            } else {
                state = ChatState.NoData
            }
        }
    }

    /**
     * Función leaveChat:
     * Notifica al repositorio que el usuario ha salido de la pantalla de chat.
     * Esto sirve para que el sistema sepa que debe volver a mostrar notificaciones
     * de mensajes entrantes en lugar de marcarlos como leidos directamente.
     */
    fun leaveChat() {
        repository.currentChatUserId = null
    }

    override fun onCleared() {
        super.onCleared()
        leaveChat()
    }

    /**
     * Función onMessageChange:
     * Actualiza el texto del campo de entrada segun el usuario escribe.
     * Aplica una restriccion de 500 caracteres para evitar mensajes demasiado
     * largos que puedan dar problemas en el servidor.
     *
     * @param text
     */
    fun onMessageChange(text: String) {
        (state as? ChatState.Success)?.let { state =
            if (it.inputText.length < 500){
                it.copy(inputText = text)
            } else {
                it
            }
        }
    }

    /**
     * Función sendMessage:
     * Procesa el envío del mensaje actual al servidor. Limpia el campo de
     * entrada inmediatamente para dar sensación de fluidez y añade el nuevo
     * mensaje al listado de la pantalla si la perición tiene éxito.
     *
     * Valida que el texto no esté vacio antes de intentar realizar el envío.
     */
    fun sendMessage() {
        val currentState = state as? ChatState.Success ?: return
        val textToSend = currentState.inputText
        if (textToSend.isBlank()) return

        state = currentState.copy(inputText = "")

        viewModelScope.launch {
            val token =
                withContext(Dispatchers.IO) { sessionManager.getAuthToken() } ?: return@launch
            val msg = MessageCreate(receiver_id = targetUserIdLocal, text = textToSend)
            val result = repository.sendMessage(token, msg)
            if (result is BaseResult.Success) {
                (state as? ChatState.Success)?.let {
                    state = it.copy(messages = it.messages + result.data)
                }
            }
        }
    }
}