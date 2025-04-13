package com.example.symphorb.ui.components.controles

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DireccionSelectorView(modifier: Modifier = Modifier, onAngleSelected: (Float) -> Unit) {
    var angulo by remember { mutableStateOf(90f) }

    Canvas(modifier = modifier) {
        drawCircle(Color.Gray, radius = 100f, center = center)
        val x = center.x + 80 * cos(Math.toRadians(angulo.toDouble())).toFloat()
        val y = center.y - 80 * sin(Math.toRadians(angulo.toDouble())).toFloat()
        drawLine(Color.Red, start = center, end = Offset(x, y), strokeWidth = 4.dp.toPx())
    }

    LaunchedEffect(angulo) {
        onAngleSelected(angulo)
    }
}