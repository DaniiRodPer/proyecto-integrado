package com.dam.dovelia.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.dam.dovelia.R
import com.dam.dovelia.data.model.UserProfile
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.FloatingContainer
import com.dam.dovelia.ui.components.buttons.GoogleButton
import com.dam.dovelia.ui.components.buttons.PrimaryButton
import com.dam.dovelia.ui.components.text.Title
import com.dam.dovelia.ui.components.text.PasswordField
import com.dam.dovelia.ui.components.text.TextField
import com.dam.dovelia.ui.theme.ProydrpTheme

data class LoginEvents(
    val onEmailChange: (String) -> Unit,
    val onPasswodChange: (String) -> Unit,
    val onLogin: () -> Unit,
    val onRegister: () -> Unit,
    val onGoogleLogin: (String) -> Unit,
    val onForgotPassword: () -> Unit
)


/**
 * Función LoginScreen:
 * Se encarga de gestionar la pantalla de acceso a la aplicación. Inicializa el
 * viewmodel y define los eventos necesarios para el inicio de sesion normal y
 * mediante la cuenta de Google.
 *
 * Tambien redirige al usuario a la pantalla de registro o de recuperación de
 * contraseña según la opción que elija.
 *
 * @param onRegister - Evento para navegar a la pantalla de registro inicial.
 * @param onLogin - función que se ejecuta cuando el login es correcto.
 * @param onNavigateToRegisterStep2 - Redirige a nuevos usuarios de Google al registro.
 * @param onForgotPassword - Evento para ir a la sección de recuperar contraseña.
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@Composable
fun LoginScreen(
    onRegister: () -> Unit,
    onLogin: () -> Unit,
    onNavigateToRegisterStep2: (UserProfile) -> Unit,
    onForgotPassword: () -> Unit
) {

    val viewModel: LoginViewModel = hiltViewModel()

    val events = LoginEvents(
        onEmailChange = viewModel::onEmailChange,
        onPasswodChange = viewModel::onPasswordChange,
        onLogin = {
            viewModel.onLogin(onSuccess = onLogin)
        },
        onRegister = onRegister,
        onGoogleLogin = { token ->
            viewModel.onGoogleLogin(
                idToken = token,
                onSuccess = onLogin,
                onNewUser = onNavigateToRegisterStep2
            )
        },
        onForgotPassword = onForgotPassword
    )

    LoginContent(
        state = viewModel.state,
        events = events,
    )

}

/**
 * Función LoginContent:
 * Contiene toda la interfaz visual de la pantalla de login.
 * Permite introducir el email y la passwod, gestionando los errores de
 * credenciales si el servidor devuelve un fallo.
 *
 * @param state
 * @param events
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@Composable
fun LoginContent(
    state: LoginState,
    events: LoginEvents,
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
            Modifier.fillMaxSize().statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Title(
                stringResource(R.string.app_name),
                modifier = Modifier.padding(
                    top = dimensions.extraHuge,
                    bottom = dimensions.extraHuge
                ),
                fontSize = 56.sp,
                color = Color.White
            )

            FloatingContainer(
                Modifier
                    .fillMaxHeight(0.9f)
                    .systemBarsPadding(),
                0.9f
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = dimensions.big, end = dimensions.big),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .verticalScroll(
                                rememberScrollState(),
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            stringResource(R.string.login_title),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(
                                top = dimensions.extraHuge,
                                bottom = dimensions.huge
                            ),
                        )
                        TextField(
                            text = state.email,
                            onChange = events.onEmailChange,
                            label = stringResource(R.string.email_address),
                            icon = painterResource(id = R.drawable.email_icon),
                            keyboardType = KeyboardType.Email,
                            isError = state.credentialsIsError
                        )
                        PasswordField(
                            state.password,
                            events.onPasswodChange,
                            isError = state.credentialsIsError,
                            errorText = state.credentialsError?.let { stringResource(it) } ?: ""
                        )
                        Text(
                            stringResource(R.string.forgot_password),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(start = dimensions.big)
                                .align(Alignment.Start)
                                .combinedClickable(onClick = events.onForgotPassword)
                        )
                        PrimaryButton(
                            stringResource(R.string.login_title),
                            onClick = events.onLogin,
                            loadingText = stringResource(R.string.login_loading),
                            isLoading = state.isLoading,
                            modifier = Modifier.padding(top = dimensions.extraBig)
                        )
                        GoogleButton(modifier = Modifier.padding(top = dimensions.standard), events.onGoogleLogin)
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = dimensions.big),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                stringResource(R.string.login_to_register),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .combinedClickable(onClick = events.onRegister)
                            )
                        }
                        Spacer(Modifier.height(dimensions.large))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val state = LoginState(email = "", password = "")
            val events = LoginEvents({}, {}, {}, {}, {}, {})
            LoginContent(state, events)
        }
    }
}