package com.example.symphorb.ui.pantallas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.symphorb.physics.PhysicsEngine
import com.example.symphorb.ui.components.lanzamiento.DragAndShootComposable
import kotlinx.coroutines.android.awaitFrame
import com.example.symphorb.R

@Composable
fun PachinkoBoard(navController: NavHostController) {
    val cellSize = 100f
    val radioBola = 12f

    fun x(col: Float) = (col - 1) * cellSize + cellSize / 2
    fun y(row: Int) = (row - 1) * cellSize + cellSize / 2
    fun y(row: Float): Float = (row - 1) * cellSize + cellSize / 2


    val pines = remember {
        buildList {
            for (filaIndex in 0 until 10) {
                val fila = filaIndex + 7.06f //Inicio de la posicion de los pines
                val numPines = filaIndex + 1
                val espacio = 1f
                val centro = 5.6f
                val offsetInicial = -((numPines - 1) / 2f)

                for (i in 0 until numPines) {
                    val col = centro + (offsetInicial + i) * espacio
                    add(Offset(x(col), y(fila)))
                }
            }
        }
    }

    val primerPin = pines.first()
    with(LocalDensity.current) { 45.dp.toPx() }

    var bolaPosicion by remember { mutableStateOf(Offset(primerPin.x, primerPin.y - cellSize)) }
    var bolaVelocidad by remember { mutableStateOf(Offset(0f, 0f)) }
    var simularFisica by remember { mutableStateOf(false) }
    var puedeLanzar by remember { mutableStateOf(true) }
    var mostrarCuadricula by remember { mutableStateOf(false) }
    val physicsEngine = remember { PhysicsEngine() }

    LaunchedEffect(simularFisica) {
        while (simularFisica) {
            awaitFrame()

            val (nuevaPos, nuevaVel) = physicsEngine.update(
                position = bolaPosicion,
                velocity = bolaVelocidad,
                pins = pines,
                ballRadius = radioBola,
                minX = x(0.4f),
                maxX = x(10.7f),
                maxY = y(19)
            )

            bolaPosicion = nuevaPos
            bolaVelocidad = nuevaVel

            if (physicsEngine.shouldStop(bolaPosicion, radioBola, y(19))) {
                simularFisica = false
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {

        // 🎨 Fondo de mosaico árabe - suave y elegante
        Image(
            painter = painterResource(id = R.drawable.alhambra_2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.2f, // suaviza el patrón para no distraer
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((19 * 50).dp)
                    .padding(12.dp)
            ) {
                // NUEVO: Sistema de lanzamiento modular
                DragAndShootComposable(
                    bolaPosicion = bolaPosicion,
                    onDisparo = { direccion ->
                        bolaVelocidad = direccion
                        simularFisica = true
                        puedeLanzar = false
                    },
                    enabled = puedeLanzar,
                    pins = pines,                    // ← lista real de pines del nivel
                    ballRadius = radioBola,          // ← radio real de la bola
                    minX = x(0.5f),                  // ← borde izquierdo real
                    maxX = x(10.5f),                 // ← borde derecho real
                    maxY = y(18)                     // ← borde inferior real
                )

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

                    //Dibujar los límites del techo y las paredes
                    val bordeIzquierdo = x(0.4f)
                    val bordeDerecho = x(10.8f)
                    //val techoVisualY = y(1.85f)


                    //drawLine(
                        //color = Color.DarkGray,
                        //alpha = 0.5f,
                        //start = Offset(bordeIzquierdo, techoVisualY),
                        //end = Offset(bordeDerecho, techoVisualY),
                        //strokeWidth = 18f)

                    drawLine(
                        color = Color.DarkGray,
                        alpha = 0.9f,
                        start = Offset(bordeIzquierdo, 0f),
                        end = Offset(bordeIzquierdo, size.height),
                        strokeWidth = 2f
                    )

                    drawLine(
                        color = Color.DarkGray,
                        alpha = 0.9f,
                        start = Offset(bordeDerecho, 0f),
                        end = Offset(bordeDerecho, size.height),
                        strokeWidth = 2f
                    )

                    pines.forEach { pin ->
                        drawCircle(
                            color = Color.Gray,
                            radius = 15f,
                            center = pin
                        ) //cambiar el radio tambien en Physics Engine para que la fisica afecte al radio.
                    }

                    drawCircle(color = Color.Red, radius = radioBola, center = bolaPosicion)
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current
            val botonAnchoDp = with(density) { cellSize.toDp() }
            val botonAltoDp = with(density) { (cellSize).toDp() }

            Button(
                onClick = { mostrarCuadricula = !mostrarCuadricula },
                modifier = Modifier
                    .offset { IntOffset(x = x(1f).toInt(), y = y(20f).toInt()) }
                    .size(width = botonAnchoDp, height = 36.dp),
                contentPadding = PaddingValues(0.dp) // evita desplazamiento visual
            ) {
                Text(
                    text = if (mostrarCuadricula) "Ocultar" else "Mostrar",
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = { mostrarCuadricula = !mostrarCuadricula },
                modifier = Modifier
                    .offset { IntOffset(x = x(1f).toInt(), y = y(20f).toInt()) }
                    .size(width = botonAnchoDp, height = botonAltoDp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = if (mostrarCuadricula) "🔳" else "⬜️",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = {
                    bolaPosicion = Offset(primerPin.x, primerPin.y - cellSize)
                    bolaVelocidad = Offset(0f, 0f)
                    simularFisica = false
                    puedeLanzar = true
                },
                modifier = Modifier
                    .offset { IntOffset(x = x(3f).toInt(), y = y(20f).toInt()) }
                    .size(width = botonAnchoDp, height = botonAltoDp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "🔘",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = {
                    navController.navigate("pachinko") {
                        popUpTo("pachinko") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .offset { IntOffset(x = x(5f).toInt(), y = y(20f).toInt()) }
                    .size(width = botonAnchoDp, height = botonAltoDp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "🔙",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}




