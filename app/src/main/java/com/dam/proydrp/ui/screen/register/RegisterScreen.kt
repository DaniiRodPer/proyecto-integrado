package com.dam.proydrp.ui.screen.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dam.proydrp.R
import com.dam.proydrp.data.model.UserProfile
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.common.TopBarConfig
import com.dam.proydrp.ui.components.FloatingContainer
import com.dam.proydrp.ui.components.buttons.PrimaryButton
import com.dam.proydrp.ui.components.text.Title
import com.dam.proydrp.ui.components.text.PasswordField
import com.dam.proydrp.ui.components.text.TextField
import com.dam.proydrp.ui.theme.ProydrpTheme

data class RegisterEvents(
    val onNameChange: (String) -> Unit,
    val onSurnameChange: (String) -> Unit,
    val onEmailChange: (String) -> Unit,
    val onEmailConfirmChange: (String) -> Unit,
    val onPasswordChange: (String) -> Unit,
    val onPasswordConfirmChange: (String) -> Unit,
    val onRegister: () -> Unit,
    val onLogin: () -> Unit,
)

@Composable
fun RegisterScreen(
    onLogin: () -> Unit,
    onNextStep: (UserProfile) -> Unit
) {

    val viewModel: RegisterViewModel = hiltViewModel()

    val events = RegisterEvents(
        onNameChange = viewModel::onNameChange,
        onSurnameChange = viewModel::onSurnameChange,
        onEmailChange = viewModel::onEmailChange,
        onEmailConfirmChange = viewModel::onEmailConfirmChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordConfirmChange = viewModel::onPasswordConfirmChange,
        onRegister = {viewModel.onRegister(onNextStep)},
        onLogin = onLogin
    )

    RegisterContent(
        state = viewModel.state,
        events = events
    )

}

@Composable
fun RegisterContent(
    state: RegisterState,
    events: RegisterEvents,
) {
    val dimensions = LocalDimensions.current

    Box {
        Image(
            painter = painterResource(R.drawable.logreg_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Title(
                stringResource(R.string.app_name),
                modifier = Modifier.padding(
                    top = dimensions.huge,
                    bottom = dimensions.huge
                ),
                fontSize = 56.sp,
                color = Color.White
            )

            FloatingContainer(
                Modifier
                    .fillMaxHeight(0.95f)
                    .systemBarsPadding(),
                0.9f
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = dimensions.big, end = dimensions.big),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        stringResource(R.string.register_title),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 28.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(
                            top = dimensions.extraBig,
                            bottom = dimensions.extraLarge
                        ),
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextField(
                            text = state.name,
                            onChange = events.onNameChange,
                            label = stringResource(R.string.name),
                            icon = painterResource(id = R.drawable.profile_icon),
                            isError = state.nameIsError,
                            errorText = state.nameError?.let { stringResource(it) } ?: ""
                        )
                        TextField(
                            text = state.surname,
                            onChange = events.onSurnameChange,
                            label = stringResource(R.string.surname),
                            icon = painterResource(id = R.drawable.surname_icon),
                            isError = state.surnameIsError,
                            errorText = state.surnameError?.let { stringResource(it) } ?: ""
                        )
                        TextField(
                            text = state.email,
                            onChange = events.onEmailChange,
                            label = stringResource(R.string.email_address),
                            icon = painterResource(id = R.drawable.email_icon),
                            isError = state.emailIsError,
                            errorText = state.emailError?.let { stringResource(it) } ?: "",
                            keyboardType = KeyboardType.Email
                        )
                        TextField(
                            text = state.emailConfirm,
                            onChange = events.onEmailConfirmChange,
                            label = stringResource(R.string.confirm_email_address),
                            icon = painterResource(id = R.drawable.email_icon),
                            isError = state.emailConfirmIsError,
                            errorText = state.emailConfirmError?.let { stringResource(it) } ?: "",
                            keyboardType = KeyboardType.Email
                        )
                        PasswordField(
                            state.password,
                            events.onPasswordChange,
                            isError = state.passwordIsError,
                            errorText = state.passwordError?.let { stringResource(it) } ?: ""
                        )
                        PasswordField(
                            state.passwordConfirm,
                            events.onPasswordConfirmChange,
                            isError = state.passwordConfirmIsError,
                            errorText = state.passwordConfirmError?.let { stringResource(it) } ?: ""
                        )
                    }

                    PrimaryButton(
                        stringResource(R.string.register_title),
                        events.onRegister,
                        modifier = Modifier.padding(top = dimensions.extraBig)
                    )
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(dimensions.big),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.register_to_login),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .combinedClickable(onClick = events.onLogin)
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val state = RegisterState()
    val events = RegisterEvents(
        onNameChange = {},
        onSurnameChange = {},
        onEmailChange = {},
        onEmailConfirmChange = {},
        onPasswordChange = {},
        onPasswordConfirmChange = {},
        onRegister = {},
        onLogin = {}
    )
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            RegisterContent(state = state, events = events)
        }
    }
}