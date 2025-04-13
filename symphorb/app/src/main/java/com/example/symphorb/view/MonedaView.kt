package com.example.symphorb.view

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import com.example.symphorb.game.Moneda
import androidx.compose.foundation.background


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MonedaView(monedas: List<Moneda>) {
    val scaleFactor = 60.dp // escala para posicionar

    Box(modifier = Modifier.fillMaxSize()) {
        monedas.forEach { moneda ->
            val offsetX = with(LocalDensity.current) { (moneda.posicion.x * scaleFactor.toPx()).toInt() }
            val offsetY = with(LocalDensity.current) { (moneda.posicion.y * scaleFactor.toPx()).toInt() }

            AnimatedVisibility(
                visible = !moneda.recogida,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .offset { IntOffset(offsetX, offsetY) }
                        .background(color = Color.Yellow, shape = CircleShape)
                )
            }
        }
    }
}
