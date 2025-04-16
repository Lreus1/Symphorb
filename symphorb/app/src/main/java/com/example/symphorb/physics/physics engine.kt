package com.example.symphorb.physics

import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

class PhysicsEngine(
    private val gravity: Float = PhysicsConstants.gravedad,
    private val damping: Float = PhysicsConstants.damping,
    private val substeps: Int = 6 // número de pasos internos por frame
) {

    fun update(
        position: Offset,
        velocity: Offset,
        pins: List<Offset>,
        ballRadius: Float,
        minX: Float,
        maxX: Float,
        maxY: Float
    ): Pair<Offset, Offset> {
        var newVelocity = velocity
        var newPosition = position

        repeat(substeps) {
            newVelocity = newVelocity.copy(y = newVelocity.y + gravity / substeps)
            newPosition += newVelocity * (1f / substeps)

            // Techo físico
            val limiteSuperiorY = 440f
            if (newPosition.y - ballRadius < limiteSuperiorY) {
                newPosition = newPosition.copy(y = limiteSuperiorY + ballRadius)
                newVelocity = newVelocity.copy(y = 0f)
            }

            // Colisión con paredes laterales
            if (newPosition.x - ballRadius < minX) {
                newPosition = newPosition.copy(x = minX + ballRadius)
                newVelocity = newVelocity.copy(x = -newVelocity.x * damping)
            }
            if (newPosition.x + ballRadius > maxX) {
                newPosition = newPosition.copy(x = maxX - ballRadius)
                newVelocity = newVelocity.copy(x = -newVelocity.x * damping)
            }

            // Colisión con suelo
            if (newPosition.y + ballRadius > maxY) {
                newPosition = newPosition.copy(y = maxY - ballRadius)
                newVelocity = Offset.Zero
                return Pair(newPosition, newVelocity) // apaga físicas
            }

            // Colisión con pines precisa
            pins.forEach { pin ->
                val dx = newPosition.x - pin.x
                val dy = newPosition.y - pin.y
                val distance = sqrt(dx * dx + dy * dy)
                val collisionDistance = ballRadius + 15f

                if (distance < collisionDistance && distance > 0.001f) {
                    val normal = Offset(dx / distance, dy / distance)
                    val tangent = Offset(-normal.y, normal.x)

                    val velocityNormal = normal.x * newVelocity.x + normal.y * newVelocity.y
                    val velocityTangent = tangent.x * newVelocity.x + tangent.y * newVelocity.y

                    val restitution = 0.6f
                    val impulseNormal = -restitution * velocityNormal

                    val nuevaVelocidadNormal = Offset(normal.x * impulseNormal, normal.y * impulseNormal)
                    val nuevaVelocidadTangent = Offset(tangent.x * velocityTangent, tangent.y * velocityTangent)

                    newVelocity = nuevaVelocidadNormal + nuevaVelocidadTangent

                    val noise = Offset(
                        (Math.random().toFloat() - 0.5f) * 0.01f,
                        (Math.random().toFloat() - 0.5f) * 0.01f
                    )
                    newVelocity += noise
                    newVelocity *= damping

                    val overlap = collisionDistance - distance
                    newPosition += normal * overlap * 1.0f
                }
            }
        }

        return Pair(newPosition, newVelocity)
    }

    fun shouldStop(position: Offset, ballRadius: Float, maxY: Float): Boolean {
        return position.y + ballRadius >= maxY
    }
}


