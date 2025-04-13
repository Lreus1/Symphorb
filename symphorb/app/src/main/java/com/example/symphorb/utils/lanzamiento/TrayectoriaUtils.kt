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

    val maxPx = com.example.symphorb.utils.lanzamiento.LanzamientoConfig.maxMagnitudPx
    val umbral = 0.8f * maxPx

    // t será 0 hasta el 80% de la magnitud, y empieza a crecer de 0 a 1 entre 80% y 100%
    val t = if (magnitudPx <= umbral) 0f
    else ((magnitudPx - umbral) / (maxPx - umbral)).coerceIn(0f, 1f)

    val red = t
    val green = 1f - t
    val color = Color(red, green, 0f, 1f)

    for (i in 0 until puntos.size - 1) {
        drawLine(
            color = color,
            start = puntos[i],
            end = puntos[i + 1],
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Genera una trayectoria parabólica visual usando una curva de Bézier cuadrática.
 * Ideal para visualización artística o efectos alternativos al modelo físico real.
 *
 * @param inicio Punto inicial del lanzamiento (P0).
 * @param fin Punto final estimado del lanzamiento (P2).
 * @param alturaControl Altura del vértice de la parábola (más negativo = parábola más marcada).
 * @param pasos Número de puntos a generar (resolución de la curva).
 */
fun generarTrayectoriaBezierParabolica(
    inicio: Offset,
    fin: Offset,
    alturaControl: Float = -550f,
    pasos: Int = 30
): List<Offset> {
    val puntos = mutableListOf<Offset>()

    // Punto de control: vértice de la parábola
    val control = Offset(
        x = (inicio.x + fin.x) / 2f,
        y = (inicio.y + fin.y) / 2f + alturaControl
    )

    for (i in 0..pasos) {
        val t = i / pasos.toFloat()

        val x = (1 - t).pow(2) * inicio.x +
                2 * (1 - t) * t * control.x +
                t.pow(2) * fin.x

        val y = (1 - t).pow(2) * inicio.y +
                2 * (1 - t) * t * control.y +
                t.pow(2) * fin.y

        puntos.add(Offset(x, y))
    }

    return puntos
}
