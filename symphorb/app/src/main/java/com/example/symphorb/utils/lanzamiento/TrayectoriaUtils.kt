package com.example.symphorb.utils.lanzamiento

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
    var yaRebotoEnTecho = false

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

        val cambioVelocidadY = nuevaVel.y - vel.y
        val reboteEnTecho = vel.y < 0f && cambioVelocidadY > 2f && pos.y < techoY
        if (reboteEnTecho) {
            simulacion.add(nuevaPos)
            pos = nuevaPos
            vel = nuevaVel
            yaRebotoEnTecho = true
            return@repeat
        }

        if (yaRebotoEnTecho) {
            simulacion.add(nuevaPos)
            pos = nuevaPos
            vel = nuevaVel
            return@repeat
        }

        val cambioBrusco = (vel - nuevaVel).getDistance() > 8f

        if (cambioBrusco) {
            if (++rebotes > rebotesMaximos) return simulacion

            val pinCercano = pins.minByOrNull { (pos - it).getDistance() }
            if (pinCercano != null) {
                val dirColision = (pos - pinCercano)
                val distancia = dirColision.getDistance()
                if (distancia > 0.001f) {
                    val normal = dirColision / distancia
                    val puntoDeContacto = pinCercano + normal * (ballRadius + radioPin) * 0.7f
                    if (simulacion.isNotEmpty()) {
                        simulacion[simulacion.size - 1] = puntoDeContacto
                    } else {
                        simulacion.add(puntoDeContacto)
                    }
                } else {
                    simulacion.add(pos)
                }
            } else {
                simulacion.add(pos)
            }
        } else {
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

