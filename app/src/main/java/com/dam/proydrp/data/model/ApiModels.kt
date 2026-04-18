package com.dam.proydrp.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class TokenResponse(
    val access_token: String,
    val token_type: String,
    val user_id: String
)