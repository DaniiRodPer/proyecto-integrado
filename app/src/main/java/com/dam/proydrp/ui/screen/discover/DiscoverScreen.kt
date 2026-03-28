package com.dam.proydrp.ui.screen.discover

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.dam.proydrp.R
import com.dam.proydrp.data.mock.mockUserProfileList
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.components.AnimationComponent
import com.dam.proydrp.ui.theme.ProydrpTheme
import com.dam.proydrp.ui.components.SwipeCard
import kotlin.math.abs

data class DiscoverEvents(
    val onSwipe: (Boolean) -> Unit,
    val onButtonPress: (Boolean) -> Unit
)

@Composable
fun DiscoverScreen(
    scaffoldPadding: PaddingValues,
) {
    val viewModel: DiscoverViewModel = hiltViewModel()
    val currentState: DiscoverState = viewModel.state

    val events = DiscoverEvents(
        onSwipe = viewModel::onSwipe,
        onButtonPress = viewModel::onButtonPressed
    )

    when (currentState) {
        DiscoverState.Loading -> {
            AnimationComponent(
                lottie = LottieCompositionSpec.RawRes(R.raw.loading_animation),
                text = stringResource(R.string.loading)
            )
        }

        DiscoverState.NoData -> {
            AnimationComponent(
                lottie = LottieCompositionSpec.RawRes(R.raw.swipes_animation),
                text = stringResource(R.string.no_swipes)
            )
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
            .padding(
                top = dimensions.enormous,
                bottom = scaffoldPadding.calculateBottomPadding()
            ),
        contentAlignment = Alignment.TopCenter,
    ) {
        if (state.cards.isEmpty()) {
            AnimationComponent(
                lottie = LottieCompositionSpec.RawRes(R.raw.swipes_animation),
                text = stringResource(R.string.no_swipes)
            )
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
                events = DiscoverEvents({}, {})
            )
        }
    }
}