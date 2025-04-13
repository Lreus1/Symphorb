package com.example.symphorb.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*
import com.example.symphorb.model.Historial

class HistorialViewModel : ViewModel() {

    private val _historial = MutableStateFlow<List<Historial>>(emptyList())
    val historial: StateFlow<List<Historial>> get() = _historial

    fun registrarResultado(resultado: String, puntos: Int) {
        val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fecha = formato.format(Date())
        val nueva = Historial(fecha, resultado, puntos)
        _historial.value = listOf(nueva) + _historial.value
    }

    fun reiniciarHistorial() {
        _historial.value = emptyList()
    }
}

