package com.dam.dovelia.ui.screen.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dam.dovelia.R
import com.dam.dovelia.data.mock.mockUserProfileList
import com.dam.dovelia.data.model.UserProfile
import com.dam.dovelia.permissions.AppPermissions
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.AnimationComponent
import com.dam.dovelia.ui.components.FloatingContainer
import com.dam.dovelia.ui.components.TagList
import com.dam.dovelia.ui.components.images.HorizontalGallery
import com.dam.dovelia.ui.components.images.ProfilePic
import com.dam.dovelia.ui.components.text.Title
import com.dam.dovelia.ui.helper.rememberPermissionsLauncher
import com.dam.dovelia.ui.theme.ProydrpTheme
import com.dam.dovelia.ui.utils.getUserTagLabel

data class ProfileEvents(
    val on: () -> Unit
)

/**
 * Función composable ProfileScreen:
 * Se encarga de cargar la información del perfil, ya sea el del propio usuario
 * logueado o de otro usuario de la plataforma.
 * Gestiona los permisos de notificaciones al entrar y controla los estados
 *
 * de la pantalla segun la respuesta del servidor:
 * * - Cargando -> Animación de carga con lottie.
 * * - Sin datos -> Pantalla de error si no se puede obtener el perifl.
 * * - Éxito -> Muestra el detalle completo en ProfileContent.
 *
 * @param scaffoldPadding - Padding para no solapar con la navegación.
 * @param targetUserId - ID del usuario a consultar (null para el perfil propio).
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@Composable
fun ProfileScreen(
    scaffoldPadding: PaddingValues,
    targetUserId: String? = null
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val currentState: ProfileState = viewModel.state

    val requestNotificationPermission = rememberPermissionsLauncher(
        permissions = listOf(AppPermissions.Notifications),
        onAllGranted = {},
        onDenied = {}
    )

    LaunchedEffect(Unit) {
        viewModel.loadUser(targetUserId)
        if (targetUserId == null) {
            requestNotificationPermission()
        }
    }

    val events = ProfileEvents(
        {}
    )

    when (currentState) {
        ProfileState.Loading -> {
            AnimationComponent(
                lottie = R.raw.loading_animation,
                text = stringResource(R.string.loading)
            )
        }

        ProfileState.NoData -> {
            AnimationComponent(
                lottie = R.raw.error_animation,
                loop = false,
                text = stringResource(R.string.user_load_error)
            )
        }

        is ProfileState.Success -> {
            ProfileContent(scaffoldPadding, currentState.user, events)
        }
    }
}

/**
 * Función ProfileContent:
 * Muestra toda la información visual del perfil, incluyendo la foto,
 * descripción personal, etiquetas de usuario y datos del alojamiento.
 *
 * Utiliza un verticalScroll para que los detalles de la casa y la galería
 * de fotos se puedan consultar comodamente en pantallas pequeñas.
 *
 * Tambien formatea las etiquetas de usuario en un solo String separado por
 * comas para que la visualización sea mas límpia.
 *
 * @param scaffoldPadding
 * @param user
 * @param events
 *
 * @author Daniel Rodríguez Pérez
 * @version 1.0
 */
@Composable
fun ProfileContent(
    scaffoldPadding: PaddingValues,
    user: UserProfile,
    events: ProfileEvents,
) {
    val dimensions = LocalDimensions.current
    val scrollState = rememberScrollState()
    val userTags = buildString {
        user.userTags.forEachIndexed { index, tag ->
            append(getUserTagLabel(tag))
            if (index < user.userTags.lastIndex) append(", ")
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(bottom = scaffoldPadding.calculateBottomPadding(), top = dimensions.large)
    ) {
        ProfilePic(model = user.profilePicUrl, dimensions.giant)
        Spacer(Modifier.height(dimensions.large))
        Title(
            text = "${user.name} ${user.surname}",
            center = true,
            modifier = Modifier
                .fillMaxWidth(0.9f)
        )
        Spacer(Modifier.height(dimensions.standard))
        FloatingContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = dimensions.medium,
                        horizontal = dimensions.large,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(user.accommodation != null){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.location_icon),
                            contentDescription = null,
                            modifier = Modifier.size(dimensions.big),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.width(dimensions.medium))
                        Text(
                            user.accommodation.city,
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
                Spacer(Modifier.height(dimensions.medium))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.label_icon),
                        contentDescription = null,
                        modifier = Modifier.size(dimensions.big),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.width(dimensions.medium))
                    Text(
                        userTags,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                Spacer(Modifier.height(dimensions.standard))
                Text(
                    "\"" + user.userDescription + "\"",
                    style = TextStyle(
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.height(dimensions.small))
            }
        }
        Spacer(Modifier.height(dimensions.large))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            FloatingContainer {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(dimensions.extraBig))
                        .verticalScroll(scrollState)
                ) {
                    if (user.accommodation != null) {

                        HorizontalGallery(
                            user.accommodation.picsUrls,
                            230.dp,
                            Modifier
                                .padding(dimensions.standard)
                                .clip(RoundedCornerShape(dimensions.big))
                        )
                        Text(
                            user.accommodation.description,
                            style = TextStyle(
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                            ),
                            modifier = Modifier
                                .padding(
                                    horizontal = dimensions.standard,
                                    vertical = dimensions.medium
                                )
                        )
                        HorizontalDivider(
                            thickness = dimensions.tiny,
                            modifier = Modifier.padding(
                                dimensions.standard
                            )
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = dimensions.extraLarge,
                                    top = dimensions.extraLarge,
                                    end = dimensions.extraLarge,
                                    bottom = dimensions.bigger,
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            TagList(
                                user.accommodation.squareMeters,
                                user.accommodation.bedrooms,
                                user.accommodation.bathrooms,
                                user.accommodation.tags
                            )
                        }
                    } else {
                        AnimationComponent(
                            lottie = R.raw.error_animation,
                            text = stringResource(R.string.user_load_error)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A110F, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val user = mockUserProfileList[0]
            val events = ProfileEvents({})
            ProfileContent(PaddingValues(), user, events)
        }
    }
}