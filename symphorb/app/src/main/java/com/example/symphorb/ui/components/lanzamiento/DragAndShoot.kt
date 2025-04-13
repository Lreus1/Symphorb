package com.example.symphorb.ui.components.lanzamiento

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.symphorb.utils.lanzamiento.calcularDireccionLinealEscalada
import com.example.symphorb.utils.lanzamiento.drawTrayectoriaDegradadaAvanzada
import com.example.symphorb.utils.lanzamiento.simularTrayectoria
import kotlin.math.sqrt

/**
 * Composable que muestra la trayectoria anticipada de un disparo tipo "drag and shoot".
 * Utiliza una curva de Bézier cuadrática para representar una parábola visualmente atractiva.
 *
 * @param bolaPosicion Posición actual de la bola (punto de partida).
 * @param onDisparo Callback que se invoca cuando el usuario suelta el arrastre con la dirección calculada.
 * @param enabled Si está activado o no el sistema de trayectoria.
 */
@Composable
fun TrayectoriaLanzamiento(
    bolaPosicion: Offset,
    onDisparo: (Offset) -> Unit,
    enabled: Boolean
) {
    val density = LocalDensity.current
    val maxDragDistancePx = with(density) { 45.dp.toPx() }

    var arrastrando by remember { mutableStateOf(false) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    // Simula la trayectoria física (gravedad + rebote ligero)
    val puntosTrayectoria = remember(dragOffset, arrastrando) {
        if (!arrastrando) return@remember emptyList()

        val direccion = calcularDireccionLinealEscalada(
            inicio = dragOffset,
            fin = bolaPosicion,
            maxMagnitudPx = maxDragDistancePx
        )
        simularTrayectoria(
            origen = bolaPosicion,
            direccion = direccion,
            pasos = 30
        )
    }
    val direccion = calcularDireccionLinealEscalada(
        inicio = dragOffset,
        fin = bolaPosicion,
        maxMagnitudPx = maxDragDistancePx
    )

    // Canvas que captura gestos y dibuja puntos de trayectoria
    Canvas(modifier = Modifier
        .pointerInput(enabled) {
            if (!enabled) return@pointerInput
            detectDragGestures(
                onDragStart = { offset ->
                    val distancia = sqrt((offset - bolaPosicion).getDistanceSquared())
                    if (distancia < 60f) {
                        arrastrando = true
                        dragOffset = offset
                    }
                },
                onDrag = { change, _ ->
                    if (arrastrando) dragOffset = change.position
                },
                onDragEnd = {
                    if (arrastrando) {
                        onDisparo(direccion)
                        arrastrando = false
                    }
                },
                onDragCancel = { arrastrando = false }
            )
        }
    ) {
        if (arrastrando) {
            drawTrayectoriaDegradadaAvanzada(
                puntos = puntosTrayectoria,
                magnitudPx = (bolaPosicion - dragOffset).getDistance()
            )
        }
    }
}

// Función auxiliar eficiente para obtener distancia al cuadrado
private fun Offset.getDistanceSquared(): Float {
    return this.x * this.x + this.y * this.y
}



