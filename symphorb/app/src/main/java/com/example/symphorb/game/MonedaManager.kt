
package com.example.symphorb.game

import com.badlogic.gdx.math.Vector2
import kotlin.random.Random

object MonedaManager {

    private const val ANCHO_TABLERO = 6
    private const val ALTO_TABLERO = 10
    private const val NUM_MONEDAS = 6

    var monedas: MutableList<Moneda> = mutableListOf()

    fun generarMonedasAleatorias() {
        monedas.clear()
        repeat(NUM_MONEDAS) {
            val x = Random.nextFloat() * ANCHO_TABLERO
            val y = Random.nextFloat() * ALTO_TABLERO
            monedas.add(Moneda(posicion = Vector2(x, y)))
        }
    }

    fun actualizarColisiones(posicionBola: Vector2) {
        RecolectorMonedas.detectarColisiones(posicionBola, monedas)
    }

    fun obtenerPuntosTotales(): Int {
        return RecolectorMonedas.calcularPuntuacion(monedas)
    }

    fun reset() {
        monedas.clear()
    }
}
