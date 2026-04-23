package com.dam.dovelia.ui.screen.login

data class LoginState(
    var email: String = "",
    var password: String = "",
    var credentialsIsError: Boolean = false,
    var credentialsError: Int? = null
)