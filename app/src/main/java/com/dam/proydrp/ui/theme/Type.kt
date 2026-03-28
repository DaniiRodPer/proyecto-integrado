package com.dam.proydrp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dam.proydrp.ui.common.titleFontFamily
import com.dam.proydrp.ui.common.bodyFontFamily

val Typography = Typography(
    headlineLarge = TextStyle(fontFamily = titleFontFamily),
    headlineMedium = TextStyle(fontFamily = titleFontFamily),
    headlineSmall = TextStyle(fontFamily = titleFontFamily),
    titleLarge = TextStyle(fontFamily = titleFontFamily),
    titleMedium = TextStyle(fontFamily = titleFontFamily),
    titleSmall = TextStyle(fontFamily = titleFontFamily),

    bodyLarge = TextStyle(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(fontFamily = bodyFontFamily),
    bodySmall = TextStyle(fontFamily = bodyFontFamily),
    labelLarge = TextStyle(fontFamily = bodyFontFamily),
    labelMedium = TextStyle(fontFamily = bodyFontFamily),
    labelSmall = TextStyle(fontFamily = bodyFontFamily)
)
