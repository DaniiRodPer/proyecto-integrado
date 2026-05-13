package com.dam.dovelia.ui.screen.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dam.dovelia.R
import com.dam.dovelia.data.mock.mockUserProfileList
import com.dam.dovelia.data.model.Message
import com.dam.dovelia.ui.chat.ChatGlobe
import com.dam.dovelia.ui.chat.ChatInput
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.AnimationComponent
import com.dam.dovelia.ui.theme.ProydrpTheme
import com.dam.dovelia.ui.utils.dateToHour

data class ChatEvents(
    val onMessageChange: (String) -> Unit,
    val onSend: () -> Unit
)

/**
 * Función ChatScreen:
 * Se encarga de inicializar el chat con el usuario deestino y gestionar los
 * estados de la pantalla segun la carga de mensajes y la conexión:
 *
 * * - Cargando -> Animación lottie de carga.
 * * - Error o Sin datos -> Muestra aviso de error mediante una animación.
 * * - Éxito -> Carga el contenido del chat llamando a ChatContent.
 *
 * Al salir de la pantalla se asegura de limpiar la sesión del chat para no
 * dejar conexiones abiertas innecesariamente.
 *
 * @param scaffoldPadding - Padding del scaffold de la app.
 * @param targetUserId - ID del usuario con el que estamos chateando.
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@Composable
fun ChatScreen(
    scaffoldPadding: PaddingValues,
    targetUserId: String
) {
    val viewModel: ChatViewModel = hiltViewModel()
    val currentState = viewModel.state

    LaunchedEffect(targetUserId) {
        viewModel.initChat(targetUserId)
    }

    DisposableEffect(targetUserId) {
        onDispose {
            viewModel.leaveChat()
        }
    }

    val events = ChatEvents(
        onMessageChange = viewModel::onMessageChange,
        onSend = viewModel::sendMessage
    )

    when (currentState) {
        ChatState.Loading -> {
            AnimationComponent(
                lottie = R.raw.loading_animation,
                text = stringResource(R.string.loading)
            )
        }

        ChatState.NoData -> {
            AnimationComponent(
                lottie = R.raw.error_animation,
                loop = false,
                text = stringResource(R.string.no_data_error)
            )
        }

        is ChatState.Success -> {
            ChatContent(
                scaffoldPadding = scaffoldPadding,
                state = currentState,
                events = events
            )
        }
    }
}


/**
 * Función ChatContent:
 * Representa la interfaz del chat, mostrando el lsitado de mensajes y el
 * campo de entrada de texto.
 *
 * Utiliza un LazyColumn con reverseLayout para que los mensajes nuevos
 * aparezcan por la parte inferior y la lista se desplace sola.
 *
 * @param scaffoldPadding - Padding para no solapar con barras del sistema.
 * @param state - Estado de éxito que contiene los mensajes y el texto actual.
 * @param events - Eventos para cambiar texto y enviar mensajes.
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@Composable
fun ChatContent(
    scaffoldPadding: PaddingValues,
    state: ChatState.Success,
    events: ChatEvents
) {
    val listState = rememberLazyListState()
    val isEmpty = remember(state.messages) { state.messages.isEmpty() }
    val dimensions = LocalDimensions.current

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding)
            .imePadding()
            .padding(horizontal = dimensions.standard)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (isEmpty) {
                AnimationComponent(
                    lottie = R.raw.chat_animation,
                    text = stringResource(R.string.greeting),
                    animationSize = dimensions.giant * 2
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    reverseLayout = true,
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(state.messages.reversed()) { message ->
                        val isSender = message.sender_id == state.myUserId
                        ChatGlobe(
                            text = message.text,
                            time = dateToHour(message.timestamp),
                            sender = isSender
                        )
                    }
                }
            }
        }

        ChatInput(
            text = state.inputText,
            onChange = events.onMessageChange,
            onSend = { events.onSend() }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun ChatScreenPreview() {
    ProydrpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val mockEvents = ChatEvents(onMessageChange = {}, onSend = {})

            val mockState = ChatState.Success(
                messages = listOf(
                    Message(
                        1,
                        "yo",
                        "otro",
                        "Hola, Sigue disponible la habitación?",
                        "2026-04-20T10:00:00Z"
                    ),
                    Message(
                        2,
                        "otro",
                        "yo",
                        "Sí, Te gustaría venir a verla?",
                        "2026-04-20T10:05:00Z"
                    )
                ),
                inputText = "",
                targetUser = mockUserProfileList[0],
                myUserId = "yo"
            )

            ChatContent(
                scaffoldPadding = PaddingValues(),
                state = mockState,
                events = mockEvents
            )
        }
    }
}