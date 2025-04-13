package com.example.symphorb.physics

import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

class PhysicsEngine(
    private val gravity: Float = PhysicsConstants.gravedad,
    private val damping: Float = PhysicsConstants.damping
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
        var newVelocity = velocity.copy(y = velocity.y + gravity)
        var newPosition = position + newVelocity

        // Rebotar contra las paredes laterales
        if (newPosition.x - ballRadius < minX) {
            newPosition = newPosition.copy(x = minX + ballRadius)
            newVelocity = newVelocity.copy(x = -newVelocity.x * damping)
        }
        if (newPosition.x + ballRadius > maxX) {
            newPosition = newPosition.copy(x = maxX - ballRadius)
            newVelocity = newVelocity.copy(x = -newVelocity.x * damping)
        }

        // Rebotar contra la base inferior
        if (newPosition.y + ballRadius > maxY) {
            newPosition = newPosition.copy(y = maxY - ballRadius)
            newVelocity = newVelocity.copy(y = -newVelocity.y * damping)
        }

        // Colisión con los pines
        pins.forEach { pin ->
            val dx = newPosition.x - pin.x
            val dy = newPosition.y - pin.y
            val distance = sqrt(dx * dx + dy * dy)
            val collisionDistance = ballRadius + 8f

            if (distance < collisionDistance && distance > 0.01f) {
                val normal = Offset(dx / distance, dy / distance)
                val relativeVelocity = newVelocity
                val speedAlongNormal = relativeVelocity.x * normal.x + relativeVelocity.y * normal.y

                if (speedAlongNormal < 0) {
                    // Rebote con restitución
                    val restitution = 0.9f
                    val impulse = - (1 + restitution) * speedAlongNormal

                    val impulseVector = Offset(impulse * normal.x, impulse * normal.y)
                    newVelocity += impulseVector

                    // Aleatoriedad ligera para realismo
                    val noise = Offset(
                        (Math.random().toFloat() - 0.5f) * 0.4f,
                        (Math.random().toFloat() - 0.5f) * 0.4f
                    )
                    newVelocity += noise

                    // Corrección de posición para evitar superposición
                    val overlap = collisionDistance - distance
                    newPosition += normal * overlap
                }
            }
        }
        return Pair(newPosition, newVelocity)
    }
    fun shouldStop(position: Offset, radius: Float, maxY: Float): Boolean {
        return position.y > maxY + radius * 2
    }
}


