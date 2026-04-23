package com.dam.dovelia.ui.screen.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dam.dovelia.R
import com.dam.dovelia.data.mock.mockUserProfileList
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.AnimationComponent
import com.dam.dovelia.ui.components.ListItem
import com.dam.dovelia.ui.theme.ProydrpTheme
import com.dam.dovelia.ui.components.SwipeCard
import com.dam.dovelia.ui.components.buttons.ReloadButton
import kotlin.math.abs

data class DiscoverEvents(
    val onSwipe: (Boolean) -> Unit,
    val onButtonPress: (Boolean) -> Unit,
    val onUpdate: () -> Unit,
    val onCardClick: (String) -> Unit
)

@Composable
fun DiscoverScreen(
    scaffoldPadding: PaddingValues,
    onNavigateToProfile: (String) -> Unit,
    filterCity: String?,
    filterRooms: Int?,
    filterBathrooms: Int?
) {
    val viewModel: DiscoverViewModel = hiltViewModel()
    val currentState: DiscoverState = viewModel.state
    val dimensions = LocalDimensions.current

    val events = DiscoverEvents(
        onSwipe = viewModel::onSwipe,
        onButtonPress = viewModel::onButtonPressed,
        onUpdate = {
            val cityToPass = if (filterCity.isNullOrEmpty()) null else filterCity
            viewModel.verifySessionAndLoad(cityToPass, filterRooms, filterBathrooms, true)
        },
        onCardClick = onNavigateToProfile
    )

    LaunchedEffect(filterCity, filterRooms, filterBathrooms) {
        val cityToPass = if (filterCity.isNullOrEmpty()) null else filterCity
        viewModel.verifySessionAndLoad(cityToPass, filterRooms, filterBathrooms)
    }

    when (currentState) {
        DiscoverState.Loading -> {
            Box(
                modifier = Modifier.size(dimensions.extraGiant * 2),
                contentAlignment = Alignment.Center
            ) {
                AnimationComponent(
                    lottie = R.raw.loading_animation,
                    text = stringResource(R.string.loading)
                )
            }
        }

        DiscoverState.NoData -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .padding(horizontal = dimensions.large, vertical = dimensions.standard),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    AnimationComponent(
                        lottie = R.raw.swipes_animation,
                        text = stringResource(R.string.no_swipes)
                    )

                }
                ReloadButton(
                    onClick = { events.onUpdate() },
                )
            }
        }

        is DiscoverState.Success -> {
            DiscoverContent(
                scaffoldPadding,
                state = currentState,
                events = events
            )
        }
    }
}

@Composable
fun DiscoverContent(
    scaffoldPadding: PaddingValues,
    state: DiscoverState.Success,
    events: DiscoverEvents,
) {
    val dimensions = LocalDimensions.current
    var swipeProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(state.currentCard?.id) {
        swipeProgress = 0f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                top = dimensions.extraHuge,
                bottom = scaffoldPadding.calculateBottomPadding()
            ),
        contentAlignment = Alignment.TopCenter,
    ) {
        if (state.cards.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    AnimationComponent(
                        lottie = R.raw.swipes_animation,
                        text = stringResource(R.string.no_swipes),
                    )
                }
                ReloadButton({ events.onUpdate() })
            }
        } else if (state.showMatchAnimation && state.matchUser != null) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.weight(0.3f))
                Box(
                    modifier = Modifier.size(dimensions.extraGiant * 3),
                    contentAlignment = Alignment.Center
                ) {
                    AnimationComponent(
                        lottie = R.raw.match_animation,
                        text = stringResource(R.string.match_desc),
                        animationSize = dimensions.extraGiant * 2,
                        fontSize = 42.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensions.large),
                    contentAlignment = Alignment.Center
                ) {
                    ListItem(
                        userProfile = state.matchUser,
                        notification = false,
                        onChatClick = {},
                        onNavigate = { events.onCardClick(state.matchUser.id) },
                        onDelete = {}
                    )
                }
                Spacer(modifier = Modifier.weight(1.7f))
            }
        } else {
            Column {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    state.cards.take(2).reversed().forEach { user ->
                        val isTopCard = user.id == state.cards.first().id
                        val scale = if (isTopCard) 1f else 0.9f + (0.1f * abs(swipeProgress))
                        val finalAlpha = if (isTopCard) 1f else 0.4f + (0.6f * abs(swipeProgress))

                        key(user.id) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        alpha = finalAlpha
                                    }
                            ) {
                                SwipeCard(
                                    userProfile = user,
                                    onSwipe = events.onSwipe,
                                    onCardClick = { events.onCardClick(user.id) },
                                    onButtonPress = events.onButtonPress,
                                    showButtons = isTopCard,
                                    triggerSwipeLeft = if (isTopCard) state.swipeLeftTrigger else false,
                                    triggerSwipeRight = if (isTopCard) state.swipeRightTrigger else false,
                                    onSwipeProgress = { progress ->
                                        if (isTopCard) swipeProgress = progress
                                    },
                                    isTopCard = isTopCard
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DiscoverScreenPreview() {

    ProydrpTheme {
        val state = DiscoverState.Success(
            cards = mockUserProfileList,
            currentCard = mockUserProfileList.firstOrNull()
        )

        Surface(color = MaterialTheme.colorScheme.background) {
            DiscoverContent(
                scaffoldPadding = PaddingValues(),
                state = state,
                events = DiscoverEvents({}, {}, {}, {})
            )
        }
    }
}

@Preview(showSystemUi = true, name = "Match Screen")
@Composable
fun DiscoverScreenMatchPreview() {
    ProydrpTheme {
        val mockUser = mockUserProfileList.firstOrNull()

        val state = DiscoverState.Success(
            cards = mockUserProfileList,
            currentCard = mockUser,
            showMatchAnimation = true,
            matchUser = mockUser
        )

        Surface(color = MaterialTheme.colorScheme.background) {
            DiscoverContent(
                scaffoldPadding = PaddingValues(),
                state = state,
                events = DiscoverEvents({}, {}, {}, {})
            )
        }
    }
}