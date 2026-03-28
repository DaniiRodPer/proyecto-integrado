package com.dam.proydrp.ui.screen.profile

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
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.dam.proydrp.R
import com.dam.proydrp.data.mock.mockUserProfileList
import com.dam.proydrp.data.model.UserProfile
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.components.AnimationComponent
import com.dam.proydrp.ui.components.FloatingContainer
import com.dam.proydrp.ui.components.TagList
import com.dam.proydrp.ui.components.images.HorizontalGallery
import com.dam.proydrp.ui.components.images.ProfilePic
import com.dam.proydrp.ui.components.text.Title
import com.dam.proydrp.ui.theme.ProydrpTheme
import com.dam.proydrp.ui.utils.getUserTagLabel

data class ProfileEvents(
    val on: () -> Unit
)

@Composable
fun ProfileScreen(
    scaffoldPadding: PaddingValues,
) {

    val viewModel: ProfileViewModel = hiltViewModel()
    val currentState: ProfileState = viewModel.state


    val events = ProfileEvents(
        {}
    )

    when (currentState) {
        ProfileState.Loading -> {
            AnimationComponent(
                lottie = LottieCompositionSpec.RawRes(R.raw.loading_animation),
                text = stringResource(R.string.loading)
            )
        }

        ProfileState.NoData -> {
            AnimationComponent(
                lottie = LottieCompositionSpec.RawRes(R.raw.like_animation),
                loop = false,
                text = stringResource(R.string.no_match)
            )
        }

        is ProfileState.Success -> {
            ProfileContent(scaffoldPadding, currentState.user, events)
        }
    }
}

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
            .padding(bottom = scaffoldPadding.calculateBottomPadding(), top = dimensions.extraBig)
    ) {
        ProfilePic(url = user.profilePicUrl, dimensions.giant)
        Spacer(Modifier.height(dimensions.medium))
        Title("${user.name} ${user.surname}")
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.location_icon),
                        contentDescription = null,
                        modifier = Modifier.size(dimensions.big),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.width(dimensions.medium))
                    Text(
                        user.city,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    )
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
                    HorizontalGallery(
                        user.accommodationPicsUrls,
                        230.dp,
                        Modifier
                            .padding(dimensions.standard)
                            .clip(RoundedCornerShape(dimensions.big))
                    )
                    Text(
                        user.accommodationDescription,
                        style = TextStyle(
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        ),
                        modifier = Modifier
                            .padding(horizontal = dimensions.standard, vertical = dimensions.medium)
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
                            120, 3, 2,
                            user.accommodationTags
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