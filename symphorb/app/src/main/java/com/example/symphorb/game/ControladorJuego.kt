
package com.example.symphorb.game

object ControladorJuego {

    enum class Estado {
        JUGANDO,
        VICTORIA,
        DERROTA
    }

    var estadoActual: Estado = Estado.JUGANDO
        private set

    fun evaluarEstadoFinal(): Estado {
        val monedas = MonedaManager.monedas
        estadoActual = if (CondicionesJuego.esVictoria(monedas)) {
            Estado.VICTORIA
        } else {
            Estado.DERROTA
        }
        return estadoActual
    }

    fun reiniciarNivel() {
        estadoActual = Estado.JUGANDO
        MonedaManager.reset()
        MonedaManager.generarMonedasAleatorias()
    }

    fun avanzarNivel(): Boolean {
        val pudoAvanzar = NivelManager.siguienteNivel()
        estadoActual = Estado.JUGANDO
        return pudoAvanzar
    }

    fun reiniciarJuego() {
        NivelManager.reiniciar()
        estadoActual = Estado.JUGANDO
    }
}
