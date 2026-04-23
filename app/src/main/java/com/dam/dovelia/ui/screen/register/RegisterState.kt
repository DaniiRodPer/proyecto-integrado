package com.dam.dovelia.ui.screen.register

data class RegisterState (
    var name: String = "",
    var nameError: Int? = null,
    var nameIsError: Boolean = false,

    var surname: String = "",
    var surnameError: Int? = null,
    var surnameIsError: Boolean = false,

    var email: String = "",
    var emailError: Int? = null,
    var emailIsError: Boolean = false,

    var emailConfirm: String = "",
    var emailConfirmError: Int? = null,
    var emailConfirmIsError: Boolean = false,

    var password: String = "",
    var passwordError: Int? = null,
    var passwordIsError: Boolean = false,

    var passwordConfirm: String = "",
    var passwordConfirmError: Int? = null,
    var passwordConfirmIsError: Boolean = false,

    var isLoading: Boolean = false
)