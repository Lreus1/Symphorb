
package com.example.symphorb.game

import com.badlogic.gdx.math.Vector2

data class Nivel(
    val id: Int,
    val nombre: String,
    val pines: List<Vector2>,
    val monedas: List<Moneda>
)

object NivelManager {

    private val niveles: List<Nivel> = List(10) { index ->
        val pines = List((index + 3) * 2) {
            val x = (0.5f + it % 6)
            val y = (it / 6) + 1f
            Vector2(x, y)
        }
        val monedas = List(4 + (index % 4)) {
            val x = (0.3f + it % 5)
            val y = (it / 5) + 1f
            Moneda(posicion = Vector2(x, y), valor = 10 + (index % 3) * 10)
        }
        Nivel(
            id = index + 1,
            nombre = "Nivel ${index + 1}",
            pines = pines,
            monedas = monedas
        )
    }

    var nivelActualIndex: Int = 0
        private set

    val nivelActual: Nivel
        get() = niveles[nivelActualIndex]

    fun siguienteNivel(): Boolean {
        return if (nivelActualIndex < niveles.lastIndex) {
            nivelActualIndex++
            true
        } else false
    }

    fun reiniciar() {
        nivelActualIndex = 0
    }
}
