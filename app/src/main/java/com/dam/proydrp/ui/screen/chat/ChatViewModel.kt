package com.dam.proydrp.ui.screen.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.proydrp.BuildConfig
import com.dam.proydrp.data.model.Message
import com.dam.proydrp.data.model.MessageCreate
import com.dam.proydrp.data.network.BaseResult
import com.dam.proydrp.data.network.SessionManager
import com.dam.proydrp.data.repository.UserRepository
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

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf<ChatState>(ChatState.Loading)
        private set

    private var targetUserIdLocal: String = ""
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    fun initChat(targetUserId: String) {
        if (targetUserIdLocal.isNotEmpty()) return
        targetUserIdLocal = targetUserId

        viewModelScope.launch {
            val myProfileResult = repository.getMyUser()
            val myId = if (myProfileResult is BaseResult.Success) myProfileResult.data.id else ""

            val token = withContext(Dispatchers.IO) { sessionManager.getAuthToken() } ?: return@launch
            val targetUserResult = repository.getUserProfile(token, targetUserId)

            if (targetUserResult is BaseResult.Success) {
                // Pedimos TODOS los mensajes de golpe
                val historyResult = repository.getChatMessages(token, targetUserId)
                val history = if (historyResult is BaseResult.Success) historyResult.data else emptyList()

                state = ChatState.Success(
                    myUserId = myId,
                    targetUser = targetUserResult.data,
                    messages = history
                )
                connectWebSocket(myId)
            } else {
                state = ChatState.NoData
            }
        }
    }

    private fun connectWebSocket(myUserId: String) {
        val request = Request.Builder().url("${BuildConfig.WS_URL}$myUserId").build()
        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val json = JSONObject(text)
                    if (json.getString("sender_id") == targetUserIdLocal) {
                        val newMessage = Message(
                            id = json.getInt("id"),
                            sender_id = json.getString("sender_id"),
                            receiver_id = json.getString("receiver_id"),
                            text = json.getString("text"),
                            timestamp = json.getString("timestamp")
                        )
                        val currentState = state as? ChatState.Success
                        if (currentState != null) {
                            state = currentState.copy(messages = currentState.messages + newMessage)
                        }
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }
        }
        webSocket = client.newWebSocket(request, listener)
    }

    override fun onCleared() {
        super.onCleared()
        webSocket?.close(1000, "Cerrando chat")
    }

    fun onMessageChange(text: String) {
        (state as? ChatState.Success)?.let { state = it.copy(inputText = text) }
    }

    fun sendMessage() {
        val currentState = state as? ChatState.Success ?: return
        val textToSend = currentState.inputText
        if (textToSend.isBlank()) return

        state = currentState.copy(inputText = "")

        viewModelScope.launch {
            val token = withContext(Dispatchers.IO) { sessionManager.getAuthToken() } ?: return@launch
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