
package com.example.symphorb.game

object CondicionesJuego {

    // Condición de victoria: recoger al menos N monedas
    fun esVictoria(monedas: List<Moneda>, minMonedas: Int = 3): Boolean {
        return monedas.count { it.recogida } >= minMonedas
    }

    // Condición de derrota: se lanzó la bola y no se recolectaron suficientes monedas
    fun esDerrota(monedas: List<Moneda>, minMonedas: Int = 3): Boolean {
        return !esVictoria(monedas, minMonedas)
    }
}
