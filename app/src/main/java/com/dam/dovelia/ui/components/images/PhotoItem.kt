package com.dam.dovelia.ui.components.images

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.dam.dovelia.R
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.theme.ProydrpTheme

@Composable
fun PhotoItem(
    model: Any,
    borderRadius: Dp,
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp? = null,
    showDelete: Boolean = false,
    onDeleteClick: () -> Unit = {}
) {
    val dimensions = LocalDimensions.current
    var imageModifier = modifier

    if (width != null) {
        imageModifier = imageModifier.width(width)
    }
    if (height != null) {
        imageModifier = imageModifier.height(height)
    }

    imageModifier = imageModifier.clip(RoundedCornerShape(borderRadius))

    Box {
        AsyncImage(
            model = model,
            placeholder = painterResource(R.drawable.image_placeholder),
            error = painterResource(R.drawable.image_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )
        if (showDelete) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(dimensions.medium)
                    .size(dimensions.big)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                    .clip(CircleShape)
                    .clickable { onDeleteClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.cancel_icon),
                    contentDescription = stringResource(R.string.delete_image_desc),
                    tint = Color.White,
                    modifier = Modifier.size(dimensions.large)
                )
            }
        }
    }
}

@Preview
@Composable
fun PhotoItemPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            val dimensions = LocalDimensions.current
            PhotoItem("https", dimensions.big, height =  dimensions.giant, width = dimensions.giant)
        }
    }
}