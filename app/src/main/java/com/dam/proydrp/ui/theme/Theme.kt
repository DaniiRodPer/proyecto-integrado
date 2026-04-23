package com.dam.proydrp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.dam.proydrp.ui.common.Dimensions

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFDB49F),
    secondary = Color(0xFFD6C38C),
    tertiary = Color(0xFF713523),
    background = Color(0xFF1A110F),
    surface = Color(0xFF322825),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFE99787),
    onBackground = Color(0xFFEBE0D8),
    onSurface = Color(0xFFEBE0D8),
    outline = Color(0xFF5D4037),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFDB49F),
    secondary = Color(0xFFD6C38C),
    tertiary = Color(0xFF713523),
    background = Color(0xFF1A110F),
    surface = Color(0xFF322825),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFE99787),
    onBackground = Color(0xFFEBE0D8),
    onSurface = Color(0xFFEBE0D8),
    outline = Color(0xFF5D4037),
)

@Composable
fun ProydrpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val dimensions = Dimensions()

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}