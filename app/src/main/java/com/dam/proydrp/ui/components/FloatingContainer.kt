package com.dam.proydrp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.theme.ProydrpTheme

@Composable
fun FloatingContainer(
    modifier: Modifier = Modifier,
    transparency: Float = 1f,
    content: @Composable () -> Unit
) {

    val dimensions = LocalDimensions.current

    Box(
        modifier = modifier
            .fillMaxWidth(0.90f)
            .clip(RoundedCornerShape(dimensions.extraBig))
            .background(
                color = MaterialTheme.colorScheme.surface.copy(transparency),
                shape = RoundedCornerShape(dimensions.extraBig)
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        content()
    }
}

@Preview
@Composable
fun FloatingContainerPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            FloatingContainer(Modifier.fillMaxSize()) {}
        }
    }
}