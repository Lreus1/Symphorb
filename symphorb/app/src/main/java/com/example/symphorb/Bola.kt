package com.example.symphorb

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BolaAnimadaAvanzada(
    lanzar: Boolean,
    slotIndex: Int?,
    onFinAnimacion: () -> Unit
) {
    val visible = remember { mutableStateOf(false) }
    val posicionX = remember { Animatable(0f) }
    val posicionY = remember { Animatable(-100f) }

    LaunchedEffect(lanzar) {
        if (lanzar && slotIndex != null) {
            visible.value = true

            val anchoSlot = 60f + 8f
            val inicioX = (anchoSlot * slotIndex) + anchoSlot / 2

            posicionX.snapTo(inicioX)
            posicionY.snapTo(-100f)

            posicionY.animateTo(
                targetValue = 800f,
                animationSpec = tween(durationMillis = 1200, easing = LinearEasing)
            )

            visible.value = false
            onFinAnimacion()
        }
    }

    if (visible.value) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(900.dp)
        ) {
            drawCircle(
                color = Color.Red,
                radius = 20f,
                center = Offset(posicionX.value, posicionY.value)
            )
        }
    }
}