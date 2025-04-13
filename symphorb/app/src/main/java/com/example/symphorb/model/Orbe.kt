package com.example.symphorb.model

data class Orbe(
    val tipo: TipoOrbe = TipoOrbe.NORMAL,
    val potencia: Int = 1,
    val reboteExtra: Boolean = false,
    val dañoEnArea: Boolean = false
)

enum class TipoOrbe {
    NORMAL,
    MULTI_REBOTE,
    EXPLOSIVO,
    CURATIVO
}