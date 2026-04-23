package com.dam.dovelia.ui.common

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.dam.dovelia.R

data class Dimensions(
    val tiny: Dp = 1.dp,
    val little: Dp = 2.dp,
    val small: Dp = 4.dp,
    val medium: Dp = 8.dp,
    val standard: Dp = 12.dp,
    val large: Dp = 16.dp,
    val extraLarge: Dp = 18.dp,
    val big: Dp = 24.dp,
    val bigger: Dp = 28.dp,
    val extraBig: Dp = 32.dp,
    val huge: Dp = 48.dp,
    val extraHuge: Dp = 64.dp,
    val enormous: Dp = 80.dp,
    val giant: Dp = 100.dp,
    val extraGiant: Dp = 150.dp
)

val titleFontFamily = FontFamily(
    Font(R.font.nyghtserif)
)

@OptIn(ExperimentalTextApi::class)
val bodyFontFamily = FontFamily(
    Font(
        resId = R.font.montserrat,
        weight = FontWeight.Thin,
        variationSettings = FontVariation.Settings(FontVariation.weight(100))
    ),
    Font(
        resId = R.font.montserrat,
        weight = FontWeight.ExtraLight,
        variationSettings = FontVariation.Settings(FontVariation.weight(200))
    ),
    Font(
        resId = R.font.montserrat,
        weight = FontWeight.Light,
        variationSettings = FontVariation.Settings(FontVariation.weight(300))
    ),
    Font(
        resId = R.font.montserrat,
        weight = FontWeight.Normal, // o FontWeight.Regular
        variationSettings = FontVariation.Settings(FontVariation.weight(400))
    ),
    Font(
        resId = R.font.montserrat,
        weight = FontWeight.Medium,
        variationSettings = FontVariation.Settings(FontVariation.weight(500))
    ),
    Font(
        resId = R.font.montserrat,
        weight = FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(600))
    ),
    Font(
        resId = R.font.montserrat,
        weight = FontWeight.Bold,
        variationSettings = FontVariation.Settings(FontVariation.weight(700))
    ),
    Font(
        resId = R.font.montserrat,
        weight = FontWeight.ExtraBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(800))
    ),
    Font(
        resId = R.font.montserrat,
        weight = FontWeight.Black,
        variationSettings = FontVariation.Settings(FontVariation.weight(900))
    )
)

val LocalDimensions = staticCompositionLocalOf { Dimensions() }