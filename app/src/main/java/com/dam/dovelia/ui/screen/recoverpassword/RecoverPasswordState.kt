package com.dam.dovelia.ui.screen.recoverpassword

data class RecoverPasswordState(
    var email: String = "",
    var emailIsError: Boolean = false,
    var emailError: Int? = null,
    var resetCode: String = "",
    var resetCodeIsError: Boolean = false,
    var resetCodeError: Int? = null,
    var newPassword: String = "",
    var newPasswordIsError: Boolean = false,
    var newPasswordError: Int? = null,
    var isEmailSent: Boolean = false,
    var isLoading: Boolean = false
)