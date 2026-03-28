package com.dam.proydrp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dam.proydrp.R
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.components.text.Title
import com.dam.proydrp.ui.theme.ProydrpTheme

@Composable
fun AnimationComponent(
    lottie: LottieCompositionSpec,
    loop: Boolean = true,
    text: String? = null
) {
    val dimensions = LocalDimensions.current
    val composition by rememberLottieComposition(
        lottie
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            iterations = if (loop) LottieConstants.IterateForever else 1,
            modifier = Modifier.size(dimensions.giant * 2)
        )

        if (text != null) {
            Spacer(modifier = Modifier.height(dimensions.extraLarge))
            Title(
                text = text,
                color = MaterialTheme.colorScheme.primary,
                center = true
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoadingAnimationPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            AnimationComponent(LottieCompositionSpec.RawRes(R.raw.loading_animation))
        }
    }
}
