package com.example.symphorb.model


data class Nivel(
    val id: Int,
    val nombre: String,
    val layoutPines: List<List<Boolean>>,
    val layoutMonedas: List<Pair<Int, Int>>
)