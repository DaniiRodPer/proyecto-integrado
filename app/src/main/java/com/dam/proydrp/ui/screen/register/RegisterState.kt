package com.dam.proydrp.ui.screen.register

data class RegisterState (
    var name: String = "",
    var nameError: String = "",
    var nameIsError: Boolean = false,

    var surname: String = "",
    var surnameError: String = "",
    var surnameIsError: Boolean = false,

    var email: String = "",
    var emailError: String = "",
    var emailIsError: Boolean = false,

    var emailConfirm: String = "",
    var emailConfirmError: String = "",
    var emailConfirmIsError: Boolean = false,

    var password: String = "",
    var passwordError: String = "",
    var passwordIsError: Boolean = false,

    var passwordConfirm: String = "",
    var passwordConfirmError: String = "",
    var passwordConfirmIsError: Boolean = false
)