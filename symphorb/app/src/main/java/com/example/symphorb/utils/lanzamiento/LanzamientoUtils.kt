package com.example.symphorb.utils.lanzamiento

import androidx.compose.ui.geometry.Offset
import kotlin.math.min
import com.example.symphorb.utils.lanzamiento.LanzamientoConfig

fun calcularDireccionLinealEscalada(
    inicio: Offset,
    fin: Offset,
    maxMagnitudPx: Float
): Offset {
    val rawDirection = fin - inicio
    val magnitud = rawDirection.getDistance()

    val fuerza = min(magnitud, maxMagnitudPx)
    val proporcion = fuerza / maxMagnitudPx  // valor entre 0 y 1
    val escalaInterpolada = LanzamientoConfig.escalaMin +
            (LanzamientoConfig.escalaMax - LanzamientoConfig.escalaMin) * proporcion

    val direccionNormalizada = if (magnitud > 0f) rawDirection / magnitud else Offset.Zero
    return direccionNormalizada * fuerza * escalaInterpolada
}