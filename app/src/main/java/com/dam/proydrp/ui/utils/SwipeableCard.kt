package com.dam.proydrp.ui.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun Modifier.swipeableCard(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onSwipeProgress: (Float) -> Unit = {},
    swipeThreshold: Float = 400f,
    triggerSwipeLeft: Boolean = false,
    triggerSwipeRight: Boolean = false,
    enabled: Boolean = true // NUEVO: Para desactivar el toque en la tarjeta de abajo
): Modifier {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // 1. LA MAGIA: Asegurarnos de usar siempre la función más actual
    val currentOnSwipeProgress by rememberUpdatedState(onSwipeProgress)
    val currentOnSwipeLeft by rememberUpdatedState(onSwipeLeft)
    val currentOnSwipeRight by rememberUpdatedState(onSwipeRight)

    LaunchedEffect(offsetX) {
        snapshotFlow { offsetX.value }.collect { value ->
            val progress = (value / swipeThreshold).coerceIn(-1f, 1f)
            currentOnSwipeProgress(progress)
        }
    }

    LaunchedEffect(triggerSwipeLeft, triggerSwipeRight) {
        if (triggerSwipeLeft) {
            // Objetivo más lejano (-1500f) para que no frene en pantalla
            offsetX.animateTo(-1500f, tween(300))
            currentOnSwipeLeft()
        } else if (triggerSwipeRight) {
            offsetX.animateTo(1500f, tween(300))
            currentOnSwipeRight()
        } else {
            offsetX.snapTo(0f)
        }
    }

    return this
        .pointerInput(enabled) { // Pasamos enabled para que se actualice si cambia
            if (!enabled) return@pointerInput // Si es la de abajo, ignoramos los toques

            detectDragGestures(
                onDragEnd = {
                    if (offsetX.value > swipeThreshold) {
                        scope.launch {
                            offsetX.animateTo(1500f, tween(200, easing = LinearEasing))
                            currentOnSwipeRight()
                        }
                    } else if (offsetX.value < -swipeThreshold) {
                        scope.launch {
                            offsetX.animateTo(-1500f, tween(200, easing = LinearEasing))
                            currentOnSwipeLeft()
                        }
                    } else {
                        scope.launch { offsetX.animateTo(0f, spring()) }
                    }
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    scope.launch { offsetX.snapTo(offsetX.value + dragAmount.x) }
                }
            )
        }
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
        .graphicsLayer {
            rotationZ = offsetX.value / 20
            // 2. NUEVO EFECTO: La tarjeta se vuelve transparente conforme se aleja
            alpha = (1f - (abs(offsetX.value) / 1000f)).coerceIn(0f, 1f)
        }
}