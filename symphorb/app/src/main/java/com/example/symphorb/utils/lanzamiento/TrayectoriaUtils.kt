package com.example.symphorb.utils.lanzamiento

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.symphorb.physics.PhysicsConstants
import com.example.symphorb.physics.PhysicsEngine
import kotlin.math.pow

/**
 * Simula una trayectoria balística discreta desde un punto de origen y una dirección inicial.
 */
@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
// Verifica si hay colisión real con algún pin
fun detectarColisionConPin(pos: Offset, pins: List<Offset>, ballRadius: Float, radioPin: Float): Offset? {
    return pins.firstOrNull {
        val distancia = (pos - it).getDistance()
        distancia < (ballRadius + radioPin)
    }
}


fun simularTrayectoria(
    origen: Offset,
    direccion: Offset,
    pasos: Int,
    pins: List<Offset>,
    ballRadius: Float,
    minX: Float,
    maxX: Float,
    maxY: Float,
    techoY: Float
): List<Offset> {
    val simulacion = mutableListOf<Offset>()
    simulacion.add(origen)

    var pos = origen
    var vel = direccion
    val physicsEngine = PhysicsEngine()
    val radioPin = 15f

    var rebotes = 0
    val rebotesMaximos = 1

    repeat(pasos) {
        val (nuevaPos, nuevaVel) = physicsEngine.update(
            position = pos,
            velocity = vel,
            pins = pins,
            ballRadius = ballRadius,
            minX = minX,
            maxX = maxX,
            maxY = maxY
        )
        val cambioBrusco = (vel - nuevaVel).getDistance() > 8f

        if (cambioBrusco) {
            if (++rebotes > rebotesMaximos) return simulacion

            val pinColisionado = detectarColisionConPin(nuevaPos, pins, ballRadius, radioPin)

            if (pinColisionado != null) {
                val vectorColision = nuevaPos - pinColisionado
                val distanciaActual = vectorColision.getDistance()
                val radioTotal = ballRadius + radioPin

                if (distanciaActual > 0.001f) {
                    val normal = vectorColision / distanciaActual
                    val puntoRebote = pinColisionado + (normal * radioTotal) * 0.65f

                    if (simulacion.isNotEmpty()) {
                        simulacion.removeLast()
                    }

                    simulacion.add(puntoRebote)

                    val puntoIntermedio = Offset(
                        (puntoRebote.x + nuevaPos.x) / 2f,
                        (puntoRebote.y + nuevaPos.y) / 2f
                    )
                    simulacion.add(puntoIntermedio)
                }
            }

            simulacion.add(nuevaPos)
        } else {
            // 🔹 Si no hubo cambio brusco, seguimos la trayectoria normal
            simulacion.add(nuevaPos)
        }

        pos = nuevaPos
        vel = nuevaVel

        if (physicsEngine.shouldStop(pos, ballRadius, maxY)) {
            return simulacion
        }
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

