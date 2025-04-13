
package com.example.symphorb.game

import com.badlogic.gdx.math.Vector2

data class Moneda(val posicion: Vector2, val valor: Int = 10, var recogida: Boolean = false)

object RecolectorMonedas {

    private const val RADIO_BOLA = 0.2f
    private const val RADIO_MONEDA = 0.2f

    fun detectarColisiones(bola: Vector2, monedas: List<Moneda>) {
        monedas.forEach {
            if (!it.recogida && bola.dst(it.posicion) < (RADIO_BOLA + RADIO_MONEDA)) {
                it.recogida = true
            }
        }
    }

    fun calcularPuntuacion(monedas: List<Moneda>): Int {
        return monedas.filter { it.recogida }.sumOf { it.valor }
    }
}
