package com.example.symphorb.utils

import com.example.symphorb.model.Orbe
import com.example.symphorb.model.TipoOrbe

object OrbeFactory {
    fun generarOrbeAleatorio(): Orbe {
        return when ((1..100).random()) {
            in 1..60 -> Orbe()
            in 61..80 -> Orbe(tipo = TipoOrbe.MULTI_REBOTE, reboteExtra = true)
            in 81..95 -> Orbe(tipo = TipoOrbe.EXPLOSIVO, dañoEnArea = true)
            else -> Orbe(tipo = TipoOrbe.CURATIVO)
        }
    }
}