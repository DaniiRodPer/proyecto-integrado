package com.dam.proydrp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.dam.proydrp.ui.common.Dimensions
import com.dam.proydrp.ui.common.LocalDimensions
import com.dam.proydrp.ui.home.NavHostScreen
import com.dam.proydrp.ui.screen.maincontainer.MainContainerScreen
import com.dam.proydrp.ui.theme.ProydrpTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ProydrpTheme {
                val dimensions = Dimensions()

                CompositionLocalProvider(
                    LocalDimensions provides dimensions
                ){
                    MainContainerScreen(navController, Modifier)
                }
            }
        }
    }
}