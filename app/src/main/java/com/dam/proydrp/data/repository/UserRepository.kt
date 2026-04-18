package com.dam.proydrp.data.repository

import com.dam.proydrp.data.model.LoginRequest
import com.dam.proydrp.data.model.SwipeRequest
import com.dam.proydrp.data.model.SwipeResponse
import com.dam.proydrp.data.model.TokenResponse
import com.dam.proydrp.data.model.UserProfile
import com.dam.proydrp.data.network.ApiService
import com.dam.proydrp.data.network.BaseResult
import com.dam.proydrp.data.network.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    //private val userDao: UserDao,
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

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
        bathrooms: Int? = null
    ): BaseResult<List<UserProfile>> {
        return try {
            val bearerToken = "Bearer $token"
            val response = apiService.getDiscoverUsers(bearerToken, city, rooms, bathrooms)
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
}

