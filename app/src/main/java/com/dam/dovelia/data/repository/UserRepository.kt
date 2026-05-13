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

/**
 * Clase UserRepository:
 * Gestiona las peticiones REST y la conexión por WebSockets para la mensajería en tiempo real y las notificaciones.
 *
 * @property apiService - Interfaz de Retrofit para llamadas a la API.
 * @property sessionManager - Gestor de tokens y datos locales de sesión.
 * @property notificationHandler - Utilidad para lanzar notificaciones al sistema.
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
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

    /**
     * Función initGlobalWebSocket:
     * Inicializa el túnel de comunicación en tiempo real. Escucha mensajes y
     * eventos de match entrantes para actualizar la UI y lanzar notificaciones.
     *
     * Si el tipo de evento es un match, incrementa el contador de no leidos
     * y muestra un aviso al usaurio para que sepa que tiene una nueva notificación.
     * *Si es un mensaje y el usuario no está en ese chat, tambien avisa.
     *
     * @param myUserId - ID del usuario actual para identificarse en el socket.
     */
    fun initGlobalWebSocket(myUserId: String) {
        if (webSocket != null) {
            closeWebSocket()
        }

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

    /**
     * Función isWebSocketOpen:
     * Comprueba si existe una conexión activa con el servidor.
     * Sirve para evitar intentar abrir varios sockets a la vez.
     *
     * @return True si el socket está inicializado, False en caso contrario.
     */
    fun isWebSocketOpen(): Boolean {
        return webSocket != null
    }

    /**
     * Función closeWebSocket:
     * Cierra la conexión actual del socket de forma segura enviando un código
     * de cierre estándar. Tambien limpia la lista de usuarios no leidos.
     */
    fun closeWebSocket() {
        webSocket?.close(1000, "User logged out / App closed")
        webSocket = null
        _unreadUsers.value = emptySet()
    }

    /**
     * Función markAsRead:
     * Notifica al servidor que los mensajes de un usuario específico han sido
     * visualizados para limpiar el contador de notificaciones pendientes.
     *
     * Actualiza el estado local de _unreadUsers para quitar el punto rojo de
     * la interfaz inmediatamente sin esperar a la perición de red.
     *
     * @param senderId - ID del usuario cuyos mensajes se maran como leidos.
     */
    suspend fun markAsRead(senderId: String) {
        val token = sessionManager.getAuthToken() ?: return

        _unreadUsers.update { currentSet -> currentSet - senderId }

        try {
            apiService.markMessagesAsRead("Bearer $token", senderId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Función login:
     * Realiza la petición de acceso al servidor enviando las credenciales.
     * Si los datos son correctos, devuelve el token de sesion necesario para  el resto de peticiones de la app.
     *
     * @param request - Objeto con el email y la passwod del usuario.
     */
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

    /**
     * Función register:
     * Registra un nuevo perfil de usuario en la base de datos del servdor enviando toda la información del formulario incluyendo datos de la casa
     *
     * @param user - Objeto UserProfile con toda la info del nuevo usuario.
     */
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

    /**
     * Función loginWithGoogle:
     * Gestiona el inicio de sesión mediante el token de identidad de Google Permite un acceso rapido sin necesidad de contraseña manual.
     *
     * @param idToken - Token devuelto por el proeedor de Google.
     */
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

    /**
     * Función getUserProfile:
     * Obtiene la información detallada de cualquier usuario de la red.
     * Intenta realizar la petición con el token de sesion y devuelve el objeto
     * con los datos del perfil y su alojamiento asociado.
     *
     * @param token - Token de acceso del usuario logueado.
     * @param userId - Identificador del perfil que queremos consultar.
     */
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

    /**
     * Función getMyUser:
     * Recupera el perfil completo del usuario que tiene la sesion iniciada utilizando el token guardado en el gestor de sesiónç
     */
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

    /**
     * Función updateUser:
     * Envía los datos actualizados del perfil al servidor para sobreescribir la información antigua del usuario y su alojamiento.
     *
     * @param userId - Identificador único del usuario a modificar.
     * @param user - Objeto con los nuevos datos a persistir.
     */
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

    /**
     * Función isEmailAvailable:
     * Comprueba si un correo electronico ya está registrado en el sistema.
     * Se usa durante el registro para evitar duplicados.
     *
     * @param email - La dirección de correo que queremos verificar.
     */
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

    /**
     * Función uploadImage:
     * Gestiona la subida de archivos al servidor de imagenes.
     * Convierte el archivo File en un cuerpo Multipart para que la API pueda
     * procesar la imagen y guardarla en el almacenamiento en la nube.
     *
     * @param file - Archivo de imagen seleccionado de la galeria o cámara.
     * @return La URL de la imagen subida si todo sale bien.
     */
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

    /**
     * Función getDiscoverUsers:
     * Pide el listado de perfiles para la pantalla de descubrimiento aplicando los fultros de ciudad, habitaciones o etiquetas si existen.
     */
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

    /**
     * Función swipeUser:
     * Registra la interacción de Like o Dislike entre el usuario actual y otro.
     * Envía la decisión al servidor y devuelve si ha habido un match mutuo.
     *
     * @param token - Token de autenticación.
     * @param swipedId - ID del usuario sobre el que se realiza la acción.
     * @param isLike - Booleano que indica si es un like o un descarte.
     */
    suspend fun swipeUser(token: String, swipedId: String, isLike: Boolean): BaseResult<SwipeResponse> {
        return try {
            val bearerToken = "Bearer $token"
            val response = apiService.swipeUser(bearerToken, SwipeRequest(swipedId, isLike))
            BaseResult.Success(response)
        } catch (e: Exception) {
            BaseResult.Error(Exception(e.message ?: "Error al procesar swipe"))
        }
    }

    /**
     * Función getMatches:
     * Obtiene todos los perfiles de usuarios con los que existe un match mutuo
     */
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

    /**
     * Función getChatMessages:
     * Recupera el historial de mensajes intercambiados con un usuario concreto
     */
    suspend fun getChatMessages(token: String, otherUserId: String): BaseResult<List<Message>> {
        return try {
            val response = apiService.getMessages("Bearer $token", otherUserId)
            if (response.isSuccessful) BaseResult.Success(response.body() ?: emptyList())
            else BaseResult.Error(Exception("Error al cargar chat"))
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    /**
     * Función sendMessage:
     * Realiza el envío de un nuevo mensaje de texto a otro usaurio medianteuna petición POST estándar.
     */
    suspend fun sendMessage(token: String, messageCreate: MessageCreate): BaseResult<Message> {
        return try {
            val response = apiService.sendMessage("Bearer $token", messageCreate)
            if (response.isSuccessful && response.body() != null) BaseResult.Success(response.body()!!)
            else BaseResult.Error(Exception("Error al enviar mensaje"))
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    /**
     * Función requestRecoveryPin:
     * Solicita el envío de un código de seguridad al correo electronico del usuario para iniciar el proceso de cambio de contraseña.
     *
     * @param email - Dirección de correo donde se enviará el PIN.
     */
    suspend fun requestRecoveryPin(email: String): BaseResult<Boolean> {
        return try {
            val response = apiService.requestRecoveryPin(mapOf("email" to email))
            if (response.isSuccessful) BaseResult.Success(true)
            else BaseResult.Error(Exception("Error al solicitar PIN"))
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    /**
     * Función resetPassword:
     * Finaliza el proceso de recuperación de cuenta estableciendo una nueva contraseña tras verificar que el PIN es correcto.
     */
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

    /**
     * Función syncUnreadStatus:
     * Sincroniza el listado de usuarios que nos han enviado mensajes mientras la aplicación estaba carrada
     */
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