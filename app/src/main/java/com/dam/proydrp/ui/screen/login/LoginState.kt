package com.dam.proydrp.ui.screen.login

data class LoginState(
    var email: String = "",
    var password: String = "",
    var credentialsError: Boolean = false,
    var credentialsIsError: String = ""
)