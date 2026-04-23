package com.dam.dovelia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dam.dovelia.R
import com.dam.dovelia.data.mock.mockUserProfileList
import com.dam.dovelia.data.model.UserProfile
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.images.PhotoItem
import com.dam.dovelia.ui.components.text.IconTextItem
import com.dam.dovelia.ui.theme.ProydrpTheme

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    userProfile: UserProfile,
    notification: Boolean,
    onChatClick: (UserProfile) -> Unit,
    onNavigate: (UserProfile) -> Unit,
    onDelete: (UserProfile) -> Unit
) {
    val dimensions = LocalDimensions.current
    if(userProfile.accommodation != null)
    {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensions.big))
                .combinedClickable(
                    onClick = { onNavigate(userProfile) },
                    onLongClick = { onDelete(userProfile) }),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
        ) {
            Row(
                modifier = Modifier.padding(dimensions.large),
                verticalAlignment = Alignment.CenterVertically
            ) {

                PhotoItem(
                    model = userProfile.accommodation.picsUrls[0],
                    borderRadius = dimensions.large,
                    width = dimensions.giant,
                    height = dimensions.giant
                )

                Spacer(Modifier.width(dimensions.large))

                Column(Modifier.weight(1f)) {
                    Text(
                        text = userProfile.accommodation.city,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${userProfile.name}, ${userProfile.surname}",
                        color = MaterialTheme.colorScheme.secondary,
                        lineHeight = 12.sp,
                        modifier = Modifier.padding(bottom = dimensions.small),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(dimensions.large))
                    Row {
                        IconTextItem(R.drawable.house_icon, "${userProfile.accommodation.squareMeters}m²")
                        Spacer(Modifier.width(dimensions.large))
                        IconTextItem(R.drawable.bed_icon, "${userProfile.accommodation.bedrooms}")
                        Spacer(Modifier.width(dimensions.large))
                        IconTextItem(R.drawable.wc_icon, "${userProfile.accommodation.bathrooms}")
                    }
                }
                Spacer(Modifier.width(dimensions.extraLarge))
                Box(
                    modifier = Modifier
                        .wrapContentSize(),
                    contentAlignment = Alignment.TopStart,
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .clip(RoundedCornerShape(dimensions.standard))
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable { onChatClick(userProfile) },
                        contentAlignment = Alignment.TopStart,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.chat_icon),
                            tint = MaterialTheme.colorScheme.background,
                            contentDescription = stringResource(R.string.start_a_chat),
                            modifier = Modifier
                                .size(dimensions.huge)
                                .padding(dimensions.standard)
                        )
                    }

                    if (notification) {
                        Box(
                            Modifier
                                .size(dimensions.extraLarge)
                                .align(Alignment.TopStart)
                                .offset(x = -dimensions.small, y = -dimensions.small)
                                .clip(CircleShape)
                                .background(Color(0xFFA43535))
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ListItemPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val user = mockUserProfileList[0]

            Box(Modifier.fillMaxSize()) {
                ListItem(Modifier, user, true, {}, {}, {})
            }
        }
    }
}