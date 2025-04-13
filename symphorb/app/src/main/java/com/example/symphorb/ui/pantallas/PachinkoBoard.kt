package com.example.symphorb.ui.pantallas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.symphorb.physics.PhysicsConstants
import com.example.symphorb.physics.PhysicsEngine
import com.example.symphorb.utils.lanzamiento.calcularDireccionLinealEscalada
import com.example.symphorb.utils.lanzamiento.drawTrayectoriaDegradadaAvanzada
import kotlinx.coroutines.android.awaitFrame

@Composable
fun PachinkoBoard(navController: NavHostController) {
    val cellSize = 100f
    val radioBola = 12f

    fun x(col: Float) = (col - 1) * cellSize + cellSize / 2
    fun y(row: Int) = (row - 1) * cellSize + cellSize / 2


    val pines = remember {
        buildList {
            for (filaIndex in 0 until 10) {
                val fila = filaIndex + 4
                val numPines = filaIndex + 1
                val espacio = 1f
                val centro = 5.5f
                val offsetInicial = -((numPines - 1) / 2f)

                for (i in 0 until numPines) {
                    val col = centro + (offsetInicial + i) * espacio
                    add(Offset(x(col), y(fila)))
                }
            }
        }
    }

    val primerPin = pines.first()
    val maxDragDistancePx = with(LocalDensity.current) { 45.dp.toPx() }

    var bolaPosicion by remember { mutableStateOf(Offset(primerPin.x, primerPin.y - cellSize)) }
    var bolaVelocidad by remember { mutableStateOf(Offset(0f, 0f)) }
    var simularFisica by remember { mutableStateOf(false) }
    var puedeLanzar by remember { mutableStateOf(true) }
    var arrastrando by remember { mutableStateOf(false) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var mostrarCuadricula by remember { mutableStateOf(true) }
    var startDragOffset by remember { mutableStateOf(Offset.Zero) }

    val physicsEngine = remember { PhysicsEngine() }

    LaunchedEffect(simularFisica) {
        while (simularFisica) {
            awaitFrame()

            val (nuevaPos, nuevaVel) = physicsEngine.update(
                position = bolaPosicion,
                velocity = bolaVelocidad,
                pins = pines,
                ballRadius = radioBola,
                minX = x(0.3f),
                maxX = x(10.9f),
                maxY = y(18)
            )

            bolaPosicion = nuevaPos
            bolaVelocidad = nuevaVel

            if (physicsEngine.shouldStop(bolaPosicion, radioBola, y(18))) {
                simularFisica = false
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((19 * 35).dp)
                .padding(16.dp)
                .pointerInput(puedeLanzar) {
                    if (!puedeLanzar) return@pointerInput
                    detectDragGestures(
                        onDragStart = { offset ->
                            arrastrando = true
                            startDragOffset = offset
                            dragOffset = offset
                        },
                        onDrag = { change, _ ->
                            dragOffset = change.position
                        },
                        onDragEnd = {
                            val direccion = calcularDireccionLinealEscalada(
                                inicio = startDragOffset,
                                fin = dragOffset,
                                maxMagnitudPx = maxDragDistancePx,
                            )  //Fuerza del lanzamiento
                            bolaVelocidad = direccion
                            simularFisica = true
                            puedeLanzar = false
                            arrastrando = false
                        },
                        onDragCancel = {
                            arrastrando = false
                        }
                    )
                }
        ) {
            if (arrastrando) {
                val direccion = calcularDireccionLinealEscalada(
                    inicio = startDragOffset,
                    fin = dragOffset,
                    maxMagnitudPx = maxDragDistancePx,
                )  //Simulacion de la fuerza
                val puntos = remember(dragOffset, bolaPosicion) {

                    val simulacion = mutableListOf<Offset>()
                    var pos = bolaPosicion
                    var vel = direccion
                    val gravedad = PhysicsConstants.gravedad
                    val damping = PhysicsConstants.damping

                    repeat(10) {
                        vel = Offset(vel.x, vel.y + gravedad)
                        pos += vel
                        simulacion.add(pos)
                        vel *= damping
                    }

                    simulacion
                }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawTrayectoriaDegradadaAvanzada(
                        trayectoria = puntos,
                        fuerza = (dragOffset - startDragOffset).getDistance()
                    )
                }
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                if (mostrarCuadricula) {
                    val columnas = 11
                    val filas = 20
                    val gridColor = Color.LightGray.copy(alpha = 0.5f)

                    for (col in 1..columnas) {
                        val xPos = x(col.toFloat())
                        drawLine(
                            color = gridColor,
                            start = Offset(xPos, 0f),
                            end = Offset(xPos, size.height),
                            strokeWidth = 1f
                        )
                    }

                    for (row in 1..filas) {
                        val yPos = y(row)
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, yPos),
                            end = Offset(size.width, yPos),
                            strokeWidth = 1f
                        )
                    }

                    for (col in 1..columnas) {
                        for (row in 1..filas) {
                            val xPos = x(col.toFloat())
                            val yPos = y(row)
                            drawIntoCanvas { canvas ->
                                canvas.nativeCanvas.drawText(
                                    "($col,$row)",
                                    xPos + 5f,
                                    yPos + 15f,
                                    android.graphics.Paint().apply {
                                        color = android.graphics.Color.GRAY
                                        textSize = 12f
                                    }
                                )
                            }
                        }
                    }
                }

                pines.forEach { pin ->
                    drawCircle(color = Color.Gray, radius = 8f, center = pin)
                }

                drawCircle(color = Color.Red, radius = radioBola, center = bolaPosicion)
            }
        }

        Button(onClick = { mostrarCuadricula = !mostrarCuadricula }) {
            Text(if (mostrarCuadricula) "Ocultar cuadrícula" else "Mostrar cuadrícula")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            bolaPosicion = Offset(primerPin.x, primerPin.y - cellSize)
            bolaVelocidad = Offset(0f, 0f)
            simularFisica = false
            puedeLanzar = true
        }) {
            Text("🔄 Reposicionar bola", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            navController.navigate("pachinko") {
                popUpTo("pachinko") { inclusive = true }
            }
        }) {
            Text("↩ Volver a PachinkoView", style = MaterialTheme.typography.bodyLarge)
        }
    }
}


