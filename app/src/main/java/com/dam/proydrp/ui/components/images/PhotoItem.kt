package com.dam.proydrp.ui.components.images

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.dam.proydrp.R
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.theme.ProydrpTheme

@Composable
fun PhotoItem(
    url: String,
    borderRadius: Dp,
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp? = null,
) {
    var imageModifier = modifier

    if (width != null) {
        imageModifier = imageModifier.width(width)
    }
    if (height != null) {
        imageModifier = imageModifier.height(height)
    }

    imageModifier = imageModifier.clip(RoundedCornerShape(borderRadius))

    AsyncImage(
        model = url,
        placeholder = painterResource(R.drawable.image_placeholder),
        error = painterResource(R.drawable.image_placeholder),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = imageModifier
    )
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