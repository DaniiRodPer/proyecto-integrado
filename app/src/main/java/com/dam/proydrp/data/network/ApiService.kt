package com.dam.proydrp.data.network

import com.dam.proydrp.data.model.LoginRequest
import com.dam.proydrp.data.model.SwipeRequest
import com.dam.proydrp.data.model.SwipeResponse
import com.dam.proydrp.data.model.TokenResponse
import com.dam.proydrp.data.model.UploadResponse
import com.dam.proydrp.data.model.UserProfile
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users/{user_id}")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("user_id") userId: String
    ): Response<UserProfile>

    @GET("users/check-email/{email}")
    suspend fun checkEmail(@Path("email") email: String): Response<Map<String, Boolean>>

    @GET("users/me")
    suspend fun getMyUser(@Header("Authorization") token: String): Response<UserProfile>

    @GET("discover/users")
    suspend fun getDiscoverUsers(
        @Header("Authorization") token: String
    ): List<UserProfile>

    @GET("discover/users")
    suspend fun getDiscoverUsers(
        @Header("Authorization") token: String,
        @Query("city") city: String? = null,
        @Query("rooms") rooms: Int? = null,
        @Query("bathrooms") bathrooms: Int? = null
    ): Response<List<UserProfile>>

    @GET("matches")
    suspend fun getMatches(@Header("Authorization") token: String): Response<List<UserProfile>>


    @POST("login")
    suspend fun loginUser(@Body request: LoginRequest): Response<TokenResponse>

    @POST("users/")
    suspend fun registerUser(@Body user: UserProfile): Response<TokenResponse>

    @POST("discover/swipe")
    suspend fun swipeUser(
        @Header("Authorization") token: String,
        @Body request: SwipeRequest
    ): SwipeResponse

    @Multipart
    @POST("upload/image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<UploadResponse>

    @PUT("users/{user_id}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("user_id") userId: String,
        @Body userUpdate: UserProfile
    ): Response<UserProfile>

    @DELETE("upload/image/{filename}")
    suspend fun deleteImage(
        @Header("Authorization") token: String,
        @Path("filename") filename: String
    ): Response<Unit>
}