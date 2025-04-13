package com.example.symphorb.utils

import com.example.symphorb.model.Resultado
import kotlin.math.roundToInt
import kotlin.random.Random

object SimuladorPachinko {

    enum class Rebote { IZQUIERDA, DERECHA }

    data class Trayectoria(val rebotes: List<Rebote>) {
        val slotFinal: Int get() = rebotes.count { it == Rebote.DERECHA }
    }

    private val resultadosPorSlot = listOf(
        Resultado("Slot 1", 20),
        Resultado("Slot 2", 50),
        Resultado("Slot 3", 200),
        Resultado("Slot 4", 50),
        Resultado("Slot 5", 20)
    )

    private val gaussianRandom = java.util.Random()

    private fun nextGaussian(): Double {
        return gaussianRandom.nextGaussian()
    }

    /**
     * Simula el lanzamiento de la bola utilizando rebotes entre pines
     * - La trayectoria es una secuencia de rebotes izquierda/derecha
     * - El slot final depende del número de rebotes hacia la derecha
     */
    fun lanzarBolaConRebotes(filas: Int = 4, semilla: Long? = null, probDerecha: Double = 0.5): Pair<Resultado, Int> {
        val trayectoria = generarTrayectoria(filas, semilla, probDerecha)
        val slot = trayectoria.slotFinal.coerceIn(0, resultadosPorSlot.lastIndex)
        val resultado = resultadosPorSlot[slot]
        return resultado to slot
    }

    /**
     * Genera una trayectoria reproducible, con sesgo opcional hacia derecha
     * @param filas Número de filas de pines
     * @param semilla Semilla para reproducibilidad
     * @param probDerecha Probabilidad de que rebote a la derecha (entre 0.0 y 1.0)
     */
    fun generarTrayectoria(filas: Int, semilla: Long? = null, probDerecha: Double = 0.5): Trayectoria {
        val rng = if (semilla != null) Random(semilla) else Random.Default
        val rebotes = List(filas) {
            if (rng.nextDouble() < probDerecha) Rebote.DERECHA else Rebote.IZQUIERDA
        }
        return Trayectoria(rebotes)
    }

    /**
     * Versión alternativa con distribución inversa normal (menos en el centro)
     */
    fun lanzarBola(): Pair<Resultado, Int> {
        val indice = generarIndiceDistribucionInversa(centro = 2, desvio = 1.0, max = 5)
        val resultado = resultadosPorSlot[indice]
        return resultado to indice
    }

    private fun generarIndiceDistribucionInversa(centro: Int, desvio: Double, max: Int): Int {
        repeat(100) {
            val gaussian = nextGaussian() * desvio
            val pos = (centro + gaussian).roundToInt().coerceIn(0, max - 1)
            if (pos != centro) return pos
        }
        return listOf(0, max - 1).random()
    }
}



