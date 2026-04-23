package com.dam.dovelia.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dam.dovelia.R
import com.dam.dovelia.data.mock.mockUserProfileList
import com.dam.dovelia.data.model.UserProfile
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.images.ProfilePic
import com.dam.dovelia.ui.components.images.TapGallery
import com.dam.dovelia.ui.theme.ProydrpTheme
import com.dam.dovelia.ui.utils.getUserTagLabel
import com.dam.dovelia.ui.utils.swipeableCard
import kotlin.math.abs

@Composable
fun SwipeCard(
    userProfile: UserProfile,
    onSwipe: (Boolean) -> Unit,
    onCardClick: () -> Unit,
    onButtonPress: (Boolean) -> Unit,
    showButtons: Boolean = true,
    triggerSwipeLeft: Boolean = false,
    triggerSwipeRight: Boolean = false,
    onSwipeProgress: (Float) -> Unit = {},
    isTopCard: Boolean = true
) {
    val dimensions = LocalDimensions.current
    val buttonsSize = dimensions.extraHuge
    var localSwipeProgress by remember { mutableFloatStateOf(0f) }

    val userTags = buildString {
        userProfile.userTags.forEachIndexed { index, tag ->
            append(getUserTagLabel(tag))
            if (index < userProfile.userTags.lastIndex) append(", ")
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

        val dynamicMaxLines = when {
            maxHeight < 700.dp -> 2
            maxHeight < 800.dp -> 3
            else -> 4
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight()
                    .swipeableCard(
                        onSwipeLeft = { onSwipe(false) },
                        onSwipeRight = { onSwipe(true) },
                        triggerSwipeLeft = triggerSwipeLeft,
                        triggerSwipeRight = triggerSwipeRight,
                        onSwipeProgress = { progress ->
                            localSwipeProgress = progress
                            onSwipeProgress(progress)
                        },
                        enabled = isTopCard
                    )
            ) {
                FloatingContainer(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = buttonsSize / 2)
                ) {
                    if (userProfile.accommodation != null) {
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = buttonsSize / 2 + dimensions.standard)
                                .verticalScroll(scrollState)
                                .padding(
                                    start = dimensions.standard,
                                    end = dimensions.standard,
                                    top = dimensions.standard
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                TapGallery(
                                    urls = userProfile.accommodation.picsUrls,
                                    city = userProfile.accommodation.city,
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(dimensions.standard))
                                    .clickable { onCardClick() }
                            ) {
                                Text(
                                    userProfile.accommodation.description,
                                    maxLines = dynamicMaxLines,
                                    lineHeight = 16.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = dimensions.standard),
                                )

                                TagList(
                                    userProfile.accommodation.squareMeters,
                                    userProfile.accommodation.bedrooms,
                                    userProfile.accommodation.bathrooms,
                                    modifier = Modifier.padding(vertical = dimensions.standard)
                                )

                                HorizontalDivider(thickness = dimensions.tiny)

                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = dimensions.standard)
                                ) {
                                    ProfilePic(userProfile.profilePicUrl, dimensions.extraBig)
                                    Text(
                                        "${userProfile.name} ${userProfile.surname[0]}, ${userProfile.age}  |  $userTags",
                                        fontSize = 14.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(start = dimensions.standard)
                                    )
                                }
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.error_icon),
                                contentDescription = stringResource(R.string.user_load_error),
                                modifier = Modifier.size(dimensions.huge)
                            )
                            Text(
                                text = stringResource(R.string.user_load_error),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = dimensions.standard)
                            )
                        }
                    }
                    val startThreshold = 0.1f
                    val rawSwipeIntensity = abs(localSwipeProgress)

                    val smoothedAlpha = if (rawSwipeIntensity < startThreshold) {
                        0f
                    } else {
                        (rawSwipeIntensity - startThreshold) / (1f - startThreshold)
                    }

                    val isLike = localSwipeProgress > 0
                    val feedbackColor = if (isLike) Color(0xFF76946A) else Color(0xFF9E6565)
                    val iconId = if (isLike) R.drawable.check_icon else R.drawable.cancel_icon

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(dimensions.big))
                            .alpha(smoothedAlpha)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.9f * smoothedAlpha),
                                        feedbackColor.copy(alpha = 0.9f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = iconId),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(dimensions.extraGiant)
                                .graphicsLayer {
                                    scaleX = 0.5f + (0.4f * smoothedAlpha)
                                    scaleY = 0.5f + (0.4f * smoothedAlpha)
                                }
                        )
                    }
                }
                AnimatedVisibility(
                    visible = showButtons,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    enter = fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.7f),
                    exit = fadeOut(tween(300)) + scaleOut(tween(300), targetScale = 0.7f)
                ) {
                    Row(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.spacedBy(dimensions.extraHuge),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(buttonsSize)
                                .clip(CircleShape)
                                .background(Color(0xFF9E6565))
                                .clickable { onButtonPress(false) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painterResource(R.drawable.cancel_icon),
                                null,
                                tint = Color.White,
                                modifier = Modifier.size(dimensions.huge)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(buttonsSize)
                                .clip(CircleShape)
                                .background(Color(0xFF76946A))
                                .clickable { onButtonPress(true) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painterResource(R.drawable.check_icon),
                                null,
                                tint = Color.White,
                                modifier = Modifier.size(dimensions.huge)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun SwipeCardPreview() {
    val user = mockUserProfileList[15]

    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val dimensions = LocalDimensions.current
            Column {
                SwipeCard(user, {}, {}, {})
            }

        }
    }
}