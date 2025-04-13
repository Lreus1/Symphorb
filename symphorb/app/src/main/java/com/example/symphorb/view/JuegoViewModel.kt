package com.example.symphorb.view

import androidx.lifecycle.ViewModel
import com.example.symphorb.model.Resultado
import com.example.symphorb.utils.SimuladorPachinko
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class JuegoViewModel : ViewModel() {

    private val _resultadoActual = MutableStateFlow<Resultado?>(null)
    val resultadoActual: StateFlow<Resultado?> = _resultadoActual.asStateFlow()

    private val _puntuacionTotal = MutableStateFlow(0)
    val puntuacionTotal: StateFlow<Int> = _puntuacionTotal.asStateFlow()

    private val _slotSeleccionado = MutableStateFlow<Int?>(null)
    val slotSeleccionado: StateFlow<Int?> = _slotSeleccionado.asStateFlow()

    fun lanzarBola() {
        val (resultado, slot) = SimuladorPachinko.lanzarBolaConRebotes(filas = 6)
        _resultadoActual.value = resultado
        _slotSeleccionado.value = slot
        _puntuacionTotal.value = _puntuacionTotal.value + resultado.puntos
    }

    fun reiniciarJuego() {
        _resultadoActual.value = null
        _slotSeleccionado.value = null
        _puntuacionTotal.value = 0
    }
}