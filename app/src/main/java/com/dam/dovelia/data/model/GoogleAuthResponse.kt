package com.dam.dovelia.data.model

data class GoogleAuthResponse(
    val is_new_user: Boolean,
    val access_token: String? = null,
    val user_id: String? = null,
    val email: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val picture: String? = null
)