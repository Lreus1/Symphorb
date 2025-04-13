package com.example.symphorb.utils

object MelodiaManager {
    private val notasNivel1 = listOf("C4", "D4", "E4", "F4", "G4")
    private var indiceActual = 0

    fun notaSiguiente(): String {
        val nota = notasNivel1[indiceActual % notasNivel1.size]
        indiceActual++
        return nota
    }

    fun reiniciar() {
        indiceActual = 0
    }
}