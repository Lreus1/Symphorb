package com.example.symphorb.ui.components.lanzamiento

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.symphorb.utils.lanzamiento.calcularDireccionLinealEscalada
import com.example.symphorb.utils.lanzamiento.drawTrayectoriaDegradadaAvanzada
import com.example.symphorb.utils.lanzamiento.simularTrayectoria
import kotlin.math.pow

/**
 * Composable reutilizable para el sistema de lanzamiento tipo drag-and-shoot,
 * evita que se tenga que escribir tod o el sistema en cada uno de los niveles.
 * De esta manera, el sistema puede ser llamado desde cualquier nivel.
 * Dibuja una trayectoria visual y emite una dirección final cuando se suelta el arrastre.
 *
 *
 * @param bolaPosicion Posición actual de la bola (origen del lanzamiento)
 * @param onDisparo Callback que se invoca con la dirección final al soltar
 * @param enabled Si el sistema está activo o bloqueado
 */
@Composable
fun DragAndShootComposable(
    bolaPosicion: Offset,
    onDisparo: (Offset) -> Unit,
    enabled: Boolean,
    pins: List<Offset>,
    ballRadius: Float,
    minX: Float,
    maxX: Float,
    maxY: Float
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenHeightPx = with(LocalDensity.current) { this@BoxWithConstraints.maxHeight.toPx() }
        val maxDragDistancePx = with(LocalDensity.current) { 60.dp.toPx() }

        var arrastrando by remember { mutableStateOf(false) }
        var startDragOffset by remember { mutableStateOf(Offset.Zero) }
        var dragOffset by remember { mutableStateOf(Offset.Zero) }

        val magnitudPx = (dragOffset - startDragOffset).getDistance()

        val direccion = calcularDireccionLinealEscalada(
            inicio = dragOffset,
            fin = startDragOffset,
            maxMagnitudPx = maxDragDistancePx
        )

        val escala = (magnitudPx / maxDragDistancePx).coerceIn(0f, 1f)
        val pasos = (20 + 30 * escala.pow(1.1f)).toInt().coerceAtMost(10)
        val alturaFila = screenHeightPx / 10f
        val techoY = alturaFila * 2f

        val puntos = remember(dragOffset, bolaPosicion) {
            simularTrayectoria(
                origen = bolaPosicion,
                direccion = direccion,
                pasos = pasos,
                pins = pins,
                ballRadius = ballRadius,
                minX = minX,
                maxX = maxX,
                maxY = maxY,
                techoY = techoY // ← ¡nuevo parámetro aquí!
            )
        }

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                detectDragGestures(
                    onDragStart = { offset ->
                        arrastrando = true
                        startDragOffset = offset
                        dragOffset = offset
                    },
                    onDrag = { change, _ ->
                        dragOffset = change.position
                    },
                    onDragEnd = {
                        val direccion = calcularDireccionLinealEscalada(
                            inicio = dragOffset,
                            fin = startDragOffset,
                            maxMagnitudPx = maxDragDistancePx
                        )
                        onDisparo(direccion)
                        arrastrando = false
                    },
                    onDragCancel = {
                        arrastrando = false
                    }
                )
            }
        ) {
            if (arrastrando) {
                drawTrayectoriaDegradadaAvanzada(
                    puntos = puntos,
                    magnitudPx = magnitudPx
                )
            }
        }
    }
}
