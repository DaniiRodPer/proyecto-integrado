package com.dam.dovelia.ui.screen.recoverpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dam.dovelia.R
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.FloatingContainer
import com.dam.dovelia.ui.components.buttons.PrimaryButton
import com.dam.dovelia.ui.components.buttons.SecondaryButton
import com.dam.dovelia.ui.components.text.Title
import com.dam.dovelia.ui.components.text.PasswordField
import com.dam.dovelia.ui.components.text.TextField
import com.dam.dovelia.ui.theme.ProydrpTheme

data class RecoverPasswordEvents(
    val onEmailChange: (String) -> Unit,
    val onResetCodeChange: (String) -> Unit,
    val onNewPasswordChange: (String) -> Unit,
    val onSend: () -> Unit,
    val onConfirmChanges: () -> Unit,
    val onCancel: () -> Unit
)

@Composable
fun RecoverPasswordScreen(
    onNavigateToLogin: () -> Unit,
    onCancel: () -> Unit
) {

    val viewModel: RecoverPasswordViewModel = hiltViewModel()

    val events = RecoverPasswordEvents(
        onEmailChange = viewModel::onEmailChange,
        onResetCodeChange = viewModel::onResetCodeChange,
        onNewPasswordChange = viewModel::onNewPasswordChange,
        onSend = viewModel::onSend,
        onConfirmChanges = { viewModel.onConfirmChanges(onSuccess = onNavigateToLogin) },
        onCancel = onCancel
    )

    RecoverPasswordContent(
        state = viewModel.state,
        events = events,
    )

}

@Composable
fun RecoverPasswordContent(
    state: RecoverPasswordState,
    events: RecoverPasswordEvents
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
            Modifier
                .fillMaxSize()
                .statusBarsPadding(),
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
                // BoxWithConstraints nos permite saber la altura máxima disponible
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val minContainerHeight = maxHeight
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = minContainerHeight)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = dimensions.big),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.reset_password_icon),
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(top = dimensions.extraBig)
                                    .size(dimensions.giant)
                            )
                            Text(
                                stringResource(R.string.recover_password),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 28.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = dimensions.large),
                            )
                            if (!state.isEmailSent) {
                                Text(
                                    stringResource(R.string.recover_password_desc),
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(
                                        top = dimensions.standard,
                                        bottom = dimensions.standard
                                    )
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = dimensions.large), // Un margen para que no se pegue
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (!state.isEmailSent) {
                                TextField(
                                    text = state.email,
                                    onChange = events.onEmailChange,
                                    label = stringResource(R.string.email_address),
                                    icon = painterResource(id = R.drawable.email_icon),
                                    keyboardType = KeyboardType.Email,
                                    isError = state.emailIsError,
                                    errorText = state.emailError?.let { stringResource(it) } ?: ""
                                )
                            } else {
                                TextField(
                                    text = state.resetCode,
                                    onChange = events.onResetCodeChange,
                                    label = stringResource(R.string.reset_code),
                                    icon = painterResource(id = R.drawable.reset_code_icon),
                                    isError = state.resetCodeIsError,
                                    errorText = state.resetCodeError?.let { stringResource(it) }
                                        ?: ""
                                )
                                PasswordField(
                                    state.newPassword,
                                    events.onNewPasswordChange,
                                    isError = state.newPasswordIsError,
                                    errorText = state.newPasswordError?.let { stringResource(it) }
                                        ?: ""
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = dimensions.large),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            var buttonText: Int
                            var buttonAction: () -> Unit

                            if (!state.isEmailSent) {
                                buttonText = R.string.reset_password
                                buttonAction = events.onSend
                            } else {
                                buttonText = R.string.confirm_changes
                                buttonAction = events.onConfirmChanges
                            }
                            PrimaryButton(
                                stringResource(buttonText),
                                onClick = buttonAction,
                                modifier = Modifier.padding(
                                    top = dimensions.extraBig,
                                    bottom = dimensions.standard
                                ),
                                enabled = !state.isLoading,
                                isLoading = state.isLoading,
                                loadingText = stringResource(R.string.loading),
                            )
                            SecondaryButton(stringResource(R.string.cancel), events.onCancel)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecoverPasswordPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val state = RecoverPasswordState()
            val events = RecoverPasswordEvents({}, {}, {}, {}, {}, {})
            RecoverPasswordContent(state, events)
        }
    }
}