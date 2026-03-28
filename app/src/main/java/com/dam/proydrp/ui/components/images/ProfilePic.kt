package com.dam.proydrp.ui.components.images

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.theme.ProydrpTheme

@Composable
fun ProfilePic(url: String, size: Dp) {
    val dimensions = LocalDimensions.current
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .border(
                width = if(size > dimensions.extraBig) dimensions.little else dimensions.tiny,
                color = Color.White,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        PhotoItem(
            url = url,
            borderRadius = dimensions.tiny,
            height = size,
            width = size
        )
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