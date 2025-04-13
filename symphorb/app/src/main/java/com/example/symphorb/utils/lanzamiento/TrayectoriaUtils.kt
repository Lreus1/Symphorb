package com.example.symphorb.utils.lanzamiento

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.symphorb.physics.PhysicsConstants
import kotlin.math.pow

/**
 * Simula una trayectoria balística discreta desde un punto de origen y una dirección inicial.
 */
fun simularTrayectoria(
    origen: Offset,
    direccion: Offset,
    pasos: Int = 30
): List<Offset> {
    val simulacion = mutableListOf<Offset>()
    var pos = origen
    var vel = direccion
    val gravedad = PhysicsConstants.gravedad
    val damping = PhysicsConstants.damping

    repeat(pasos) {
        vel = Offset(vel.x, vel.y + gravedad)
        pos += vel
        simulacion.add(pos)
        vel *= damping
    }

    return simulacion
}

/**
 * Dibuja la trayectoria con un color verde si la magnitud del arrastre es baja
 * y transiciona a rojo cuando se supera el 80 % del valor máximo configurado.
 *
 * @param puntos Lista de puntos de la trayectoria.
 * @param magnitudPx Magnitud del arrastre en píxeles.
 * @param grosor Ancho de la línea.
 */
fun DrawScope.drawTrayectoriaDegradadaAvanzada(
    puntos: List<Offset>,
    magnitudPx: Float,
    grosor: Float = 6f
) {
    if (puntos.size < 2) return

    val maxPx = LanzamientoConfig.maxMagnitudPx
    val umbral = 0.8f * maxPx

    // t será 0 hasta el 80% de la magnitud, y empieza a crecer de 0 a 1 entre 80% y 100%
    val t = if (magnitudPx <= umbral) 0f
    else ((magnitudPx - umbral) / (maxPx - umbral)).coerceIn(0f, 1f)

    val red = t
    val green = 1f - t
    val color = Color(red, green, 0f, 1f)

    val puntosCurvados = puntos.curvarVisualmente()

    for (i in 0 until puntosCurvados.size - 1) {
        drawLine(
            color = color,
            start = puntosCurvados[i],
            end = puntosCurvados[i + 1],
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )
    }
}
/**
 * Aplica una curvatura visual a una lista de puntos para mostrar una trayectoria más parabólica.
 */
fun List<Offset>.curvarVisualmente(): List<Offset> {
    if (this.size < 2) return this
    val total = this.size - 1
    val intensidad = 100f  // Puedes ajustar este valor para más/menos curva

    return this.mapIndexed { i, punto ->
        val t = i.toFloat() / total
        val deformacion = -intensidad * (4 * t * (1 - t)) // parábola simétrica
        punto.copy(y = punto.y + deformacion)
    }
}

