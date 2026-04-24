package com.dam.dovelia.data.repository

import androidx.compose.ui.res.stringResource
import com.dam.dovelia.BuildConfig
import com.dam.dovelia.R
import com.dam.dovelia.data.model.GoogleAuthResponse
import com.dam.dovelia.data.model.LoginRequest
import com.dam.dovelia.data.model.Message
import com.dam.dovelia.data.model.MessageCreate
import com.dam.dovelia.data.model.SwipeRequest
import com.dam.dovelia.data.model.SwipeResponse
import com.dam.dovelia.data.model.TokenResponse
import com.dam.dovelia.data.model.UserProfile
import com.dam.dovelia.data.network.ApiService
import com.dam.dovelia.data.network.BaseResult
import com.dam.dovelia.data.network.SessionManager
import com.dam.dovelia.ui.helper.NotificationHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val notificationHandler: NotificationHandler
) {
    private val client = OkHttpClient.Builder()
        .pingInterval(30, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private var webSocket: WebSocket? = null

    private val _incomingMessages = MutableSharedFlow<Message>(extraBufferCapacity = 10)
    val incomingMessages: SharedFlow<Message> = _incomingMessages.asSharedFlow()

    private val _unreadUsers = MutableStateFlow<Set<String>>(emptySet())
    val unreadUsers: StateFlow<Set<String>> = _unreadUsers.asStateFlow()

    var currentChatUserId: String? = null

    fun initGlobalWebSocket(myUserId: String) {
        if (webSocket != null) return

        val request = Request.Builder().url("${BuildConfig.WS_URL}$myUserId").build()
        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val json = JSONObject(text)
                    val type = json.optString("type", "message")
                    val senderId = json.getString("sender_id")

                    if (type == "match") {
                        if (senderId != myUserId) {
                            _unreadUsers.update { currentSet -> currentSet + senderId }
                            notificationHandler.showSimpleNotification(
                                contentTitle = R.string.new_match,
                                contentText = R.string.new_match_desc
                            )
                        }
                    }
                    else {
                        val newMessage = Message(
                            id = json.getInt("id"),
                            sender_id = senderId,
                            receiver_id = json.getString("receiver_id"),
                            text = json.getString("text"),
                            timestamp = json.getString("timestamp")
                        )

                        _incomingMessages.tryEmit(newMessage)

                        if (currentChatUserId != senderId) {
                            _unreadUsers.update { currentSet -> currentSet + senderId }
                            notificationHandler.showSimpleNotification(
                                contentTitle = R.string.new_message,
                                contentText = R.string.new_message_desc
                            )
                        }
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }
        }
        webSocket = client.newWebSocket(request, listener)
    }

    fun closeWebSocket() {
        webSocket?.close(1000, "App closed")
        webSocket = null
    }

    suspend fun markAsRead(senderId: String) {
        val token = sessionManager.getAuthToken() ?: return

        _unreadUsers.update { currentSet -> currentSet - senderId }

        try {
            apiService.markMessagesAsRead("Bearer $token", senderId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun login(request: LoginRequest): BaseResult<TokenResponse> {
        return try {
            val response = apiService.loginUser(request)
            if (response.isSuccessful && response.body() != null) {
                BaseResult.Success(response.body()!!)
            } else {
                BaseResult.Error(Exception("Error en el login: ${response.code()}"))
            }
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun register(user: UserProfile): BaseResult<TokenResponse> {
        return try {
            val response = apiService.registerUser(user)
            if (response.isSuccessful && response.body() != null) {
                BaseResult.Success(response.body()!!)
            } else {
                BaseResult.Error(Exception("Error en el registro: ${response.code()}"))
            }
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun loginWithGoogle(idToken: String): BaseResult<GoogleAuthResponse> {
        return try {
            val response = apiService.googleLogin(mapOf("token" to idToken))
            if (response.isSuccessful && response.body() != null) {
                BaseResult.Success(response.body()!!)
            } else {
                BaseResult.Error(Exception("Error en Google Auth: ${response.code()}"))
            }
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun getUserProfile(token: String, userId: String): BaseResult<UserProfile> {
        return try {
            val response = apiService.getUserProfile("Bearer $token", userId)
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                //userDao.insertUser(user)
                BaseResult.Success(user)
            } else {
                //val cachedUser = userDao.getUserById(userId)
                //if (cachedUser != null) BaseResult.Success(cachedUser)
                //else
                BaseResult.Error(Exception("Usuario no encontrado"))
            }
        } catch (e: Exception) {
            //val cachedUser = userDao.getUserById(userId)
            //if (cachedUser != null) BaseResult.Success(cachedUser)
            //else
            BaseResult.Error(e)
        }
    }

    suspend fun getMyUser(): BaseResult<UserProfile> {
        val token =
            sessionManager.getAuthToken() ?: return BaseResult.Error(Exception("Sesión expirada"))

        return try {
            val response = apiService.getMyUser("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                BaseResult.Success(response.body()!!)
            } else {
                BaseResult.Error(Exception("No se pudo obtener tu perfil: ${response.code()}"))
            }
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun updateUser(userId: String, user: UserProfile): BaseResult<UserProfile> {
        val token =
            sessionManager.getAuthToken() ?: return BaseResult.Error(Exception("Sesión expirada"))
        return try {
            val response = apiService.updateUser("Bearer $token", userId, user)
            if (response.isSuccessful && response.body() != null) {
                BaseResult.Success(response.body()!!)
            } else {
                BaseResult.Error(Exception("Error al actualizar: ${response.code()}"))
            }
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun isEmailAvailable(email: String): BaseResult<Boolean> {
        return try {
            val response = apiService.checkEmail(email)
            if (response.isSuccessful) {
                val exists = response.body()?.get("exists") ?: true
                BaseResult.Success(!exists)
            } else {
                BaseResult.Error(Exception("Error de servidor"))
            }
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun uploadImage(file: File): BaseResult<String> {
        return try {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val response = apiService.uploadImage(body)

            if (response.isSuccessful && response.body() != null) {
                BaseResult.Success(response.body()!!.url)
            } else {
                BaseResult.Error(Exception("Error al subir imagen: ${response.code()}"))
            }
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun deleteImage(url: String): BaseResult<Unit> {
        val token = sessionManager.getAuthToken() ?: return BaseResult.Error(Exception("No token"))
        return try {
            val filename = url.substringAfterLast("/")
            val response = apiService.deleteImage("Bearer $token", filename)

            if (response.isSuccessful) BaseResult.Success(Unit)
            else BaseResult.Error(Exception("Error al borrar en servidor"))
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun getDiscoverUsers(
        token: String,
        city: String? = null,
        rooms: Int? = null,
        bathrooms: Int? = null,
        tags: List<String>? = null
    ): BaseResult<List<UserProfile>> {
        return try {
            val bearerToken = "Bearer $token"
            val response = apiService.getDiscoverUsers(bearerToken, city, rooms, bathrooms, tags)
            BaseResult.Success(response.body() ?: emptyList())
        } catch (e: Exception) {
            BaseResult.Error(Exception(e.message ?: "Error desconocido"))
        }
    }

    suspend fun swipeUser(token: String, swipedId: String, isLike: Boolean): BaseResult<SwipeResponse> {
        return try {
            val bearerToken = "Bearer $token"
            val response = apiService.swipeUser(bearerToken, SwipeRequest(swipedId, isLike))
            BaseResult.Success(response)
        } catch (e: Exception) {
            BaseResult.Error(Exception(e.message ?: "Error al procesar swipe"))
        }
    }

    suspend fun getMatches(token: String): BaseResult<List<UserProfile>> {
        return try {
            val bearerToken = "Bearer $token"
            val response = apiService.getMatches(bearerToken)

            if (response.isSuccessful && response.body() != null) {
                BaseResult.Success(response.body()!!)
            } else {
                BaseResult.Error(Exception("Error al cargar matches: ${response.code()}"))
            }
        } catch (e: Exception) {
            BaseResult.Error(Exception(e.message ?: "Error de conexión al cargar matches"))
        }
    }


    suspend fun getChatMessages(token: String, otherUserId: String): BaseResult<List<Message>> {
        return try {
            val response = apiService.getMessages("Bearer $token", otherUserId)
            if (response.isSuccessful) BaseResult.Success(response.body() ?: emptyList())
            else BaseResult.Error(Exception("Error al cargar chat"))
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun sendMessage(token: String, messageCreate: MessageCreate): BaseResult<Message> {
        return try {
            val response = apiService.sendMessage("Bearer $token", messageCreate)
            if (response.isSuccessful && response.body() != null) BaseResult.Success(response.body()!!)
            else BaseResult.Error(Exception("Error al enviar mensaje"))
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun requestRecoveryPin(email: String): BaseResult<Boolean> {
        return try {
            val response = apiService.requestRecoveryPin(mapOf("email" to email))
            if (response.isSuccessful) BaseResult.Success(true)
            else BaseResult.Error(Exception("Error al solicitar PIN"))
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun resetPassword(email: String, pin: String, newPass: String): BaseResult<Boolean> {
        return try {
            val response = apiService.resetPassword(
                mapOf("email" to email, "pin" to pin, "new_password" to newPass)
            )
            if (response.isSuccessful) BaseResult.Success(true)
            else BaseResult.Error(Exception("PIN incorrecto"))
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun syncUnreadStatus() {
        val token = sessionManager.getAuthToken() ?: return
        try {
            val response = apiService.getUnreadStatus("Bearer $token")
            if (response.isSuccessful) {
                val unreadIds = response.body() ?: emptyList()
                _unreadUsers.value = unreadIds.toSet()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}



