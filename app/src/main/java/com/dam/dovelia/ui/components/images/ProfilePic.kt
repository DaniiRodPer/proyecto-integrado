package com.dam.dovelia.ui.components.images

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.dam.dovelia.R
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.theme.ProydrpTheme

@Composable
fun ProfilePic(
    model: Any?,
    size: Dp,
    clickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    val dimensions = LocalDimensions.current

    var boxModifier = Modifier
        .size(size)
        .clip(CircleShape)
        .border(
            width = if (size > dimensions.extraBig) dimensions.little else dimensions.tiny,
            color = Color.White,
            shape = CircleShape
        )

    if (clickable) {
        boxModifier = boxModifier.clickable { onClick() }
    }

    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.Center
    ) {
        val isEmpty = model == null || (model is String && model.isBlank())

        if (!isEmpty) {
            PhotoItem(
                model = model,
                borderRadius = size / 2,
                height = size,
                width = size
            )

            if (clickable) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.edit_icon),
                        contentDescription = stringResource(R.string.edit_profile_pic),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimensions.big)
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                if (clickable) {
                    Icon(
                        painter = painterResource(R.drawable.add_icon),
                        contentDescription = stringResource(R.string.add_profile_pic_desc),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(dimensions.big)
                    )
                } else {
                    Image(
                        painterResource(R.drawable.image_placeholder),
                        contentDescription = stringResource(R.string.profile_pic_desc)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun ProfilePicPreview() {
    val url = "https://randomuser.me/api/portraits/women/1.jpg"
    val dimensions = LocalDimensions.current

    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ProfilePic(url, dimensions.large)
        }
    }
}