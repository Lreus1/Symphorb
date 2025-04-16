package com.example.symphorb.ui.pantallas.niveles

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.delay


@Composable
fun Nivel2board(navController: NavHostController) {
    val cellSize = 100f
    val radioBola = 12f


    fun x(col: Float) = (col - 1) * cellSize + cellSize / 2
    fun y(row: Int) = (row - 1) * cellSize + cellSize / 2
    fun y(row: Float): Float = (row - 1) * cellSize + cellSize / 2


    val pines = remember {
        buildList {
            val espacio = 1f
            val columnas = 11 // Ajustable según tamaño del tablero
            val centro = 5.6f
            val offsetInicial = -((columnas - 1) / 2f)

            for (filaIndex in 6..17) {
                val fila = filaIndex + 0.06f // Mismo offset vertical que Nivel 1

                // Desplazamiento lateral si la fila es impar
                val desplazamiento = if (filaIndex % 2 == 1) 0.5f else 0f

                for (colIndex in 0 until columnas) {
                    val col = centro + (offsetInicial + colIndex + desplazamiento) * espacio
                    add(Offset(x(col), y(fila)))
                }
            }
        }
    }
    //Añadimos lineas de puntos en las cajitas de los slots, para que el motor de físicas pueda leerla
    val barrerasSlots = remember {
        buildList {
            for (col in 1..10) {
                val xDiv = x(col + 0.1f)
                val yTop = y(17.05f) + 15f
                val yBottom = y(19f) + 60f
                // Usaremos un punto vertical como "segmento"
                for (i in 0..10) {
                    val yInterpolado = yTop + i * ((yBottom - yTop) / 10f)
                    add(Offset(xDiv, yInterpolado))
                }
            }
        }
    }
    //Clase que define el slot
    data class SlotArea(
        val id: Int,
        val xInicio: Float,
        val xFin: Float,
        val yBase: Float,     // solo para detección física
        val valor: Int,
        val yTexto: Float     // solo para mostrar número visual
    )

    //Val que define el sistema de deteccion de colisiones con los slots
    val slotAreas = remember {
        buildList {
            val yBaseDeteccion = 1680f // físico
            val yTextoVisual = y(18.6f) // para centrar número

            val divisiones = listOf(
                x(0.4f), // pared izquierda
                x(1.1f), x(2.1f), x(3.1f), x(4.1f), x(5.1f),
                x(6.1f), x(7.1f), x(8.1f), x(9.1f), x(10.1f),
                x(10.7f) // pared derecha
            )

            for (i in 0 until divisiones.size - 1) {
                val xInicio = divisiones[i]
                val xFin = divisiones[i + 1]
                val valorAsignado = i + 1 // Slots numerados del 1 al 11
                add(SlotArea(i, xInicio, xFin, yBaseDeteccion, valorAsignado, yTextoVisual))
            }
        }
    }



    val posicionInicialBola = Offset(x(5.6f), y(3f)) //Posicion Inicial Bola

    var bolaPosicion by remember { mutableStateOf(posicionInicialBola) }
    var bolaVelocidad by remember { mutableStateOf(Offset(0f, 0f)) }
    var simularFisica by remember { mutableStateOf(false) }
    var puedeLanzar by remember { mutableStateOf(true) }
    var mostrarCuadricula by remember { mutableStateOf(false) }
    val physicsEngine = remember { PhysicsEngine(techoActivo = false) }

    LaunchedEffect(simularFisica) {
        while (simularFisica) {
            awaitFrame()

            val (nuevaPos, nuevaVel) = physicsEngine.update(
                position = bolaPosicion,
                velocity = bolaVelocidad,
                pins = pines + barrerasSlots,
                ballRadius = radioBola,
                minX = x(0.4f),
                maxX = x(10.7f),
                maxY = y(19)
            )

            bolaPosicion = nuevaPos
            bolaVelocidad = nuevaVel

            if (physicsEngine.shouldStop(bolaPosicion, radioBola, y(18))) {
                //println("🛑 Bola detenida en: X=${bolaPosicion.x}, Y=${bolaPosicion.y}")
                //slotAreas.forEach {
                //println("🟨 Slot ${it.valor}: X=${it.xInicio}..${it.xFin}, Y base ≥ ${it.yBase}")
                //}

                //Deteccion de slot, respuesta al slot y reinicio de la bola.
                val slotDetectado = slotAreas.firstOrNull { slot ->
                    bolaPosicion.x in slot.xInicio..slot.xFin && bolaPosicion.y >= slot.yBase
                }
                //Prints en consola para detectar slots¡
                if (slotDetectado != null) {
                    println("Bola cayó en el slot ${slotDetectado.id}")
                    // Aquí puedes lanzar efectos, sumar puntos, cambiar estado, etc.
                } else {
                    println("Bola no cayó en ningún slot")}
                simularFisica = false
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        // 🎨 Fondo de mosaico árabe - suave y elegante
        Image(
            painter = painterResource(id = R.drawable.alhambra_3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.3f, // suaviza el patrón para no distraer
            modifier = Modifier
                .fillMaxSize()
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
                                        Paint().apply {
                                            color = android.graphics.Color.GRAY
                                            textSize = 12f
                                        }
                                    )
                                }
                            }
                        }
                    }

                    //Dibujar los límites del techo y las paredes
                    val bordeIzquierdo = x(0.3f)
                    val bordeDerecho = x(10.9f)

                    drawLine(
                        color = Color.DarkGray,
                        start = Offset(bordeIzquierdo, 0f),
                        end = Offset(bordeIzquierdo, size.height),
                        strokeWidth = 8f
                    )

                    drawLine(
                        color = Color.DarkGray,
                        start = Offset(bordeDerecho, 0f),
                        end = Offset(bordeDerecho, size.height),
                        strokeWidth = 8f
                    )

                    pines.forEach { pin ->
                        drawCircle(
                            color = Color.Gray,
                            radius = 15f,
                            center = pin
                        ) //cambiar el radio tambien en Physics Engine para que la fisica afecte al radio.
                    }

                    //Dibujamos los slots, lineas verticales debajo de la ultima fila de pines

                    for (col in 1..10) {
                        val xDiv = x(col + 0.1f)
                        val yInicio = y(17.05f) + 15f // justo debajo de los pines
                        val yFin = y(19f) + 60f      // baja hasta el fondo del tablero

                        drawLine(
                            color = Color.DarkGray.copy(alpha=0.5f),
                            start = Offset(xDiv, yInicio),
                            end = Offset(xDiv, yFin),
                            strokeWidth = 2f
                        )
                    }
                    //Añadimos números encima de los slots para identificarlos
                    slotAreas.forEach { slot ->
                        drawContext.canvas.nativeCanvas.drawText(
                            slot.valor.toString(),
                            (slot.xInicio + slot.xFin) / 2f, // centro horizontal del slot
                            slot.yTexto - 40f, // posición vertical ajustable (más arriba = más negativo)
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.DKGRAY
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = 36f
                                isAntiAlias = true
                                textSkewX = -0.25f // NEGATIVO = cursiva hacia la derecha
                                style = android.graphics.Paint.Style.FILL
                            }
                        )
                    }


                    //Dibujar bola
                    drawCircle(color = Color.Red, radius = radioBola, center = bolaPosicion)
                        }
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
                    //bolaPosicion = posicionInicialBola
                    //bolaVelocidad = Offset(0f, 0f)
                    //simularFisica = false
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
            Button(
                onClick = {
                    if (puedeLanzar) {
                        //bolaPosicion = posicionInicialBola
                        //bolaVelocidad = Offset(0f, 0.5f) // velocidad inicial vertical
                        //simularFisica = true
                        puedeLanzar = false
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 24.dp)
                    .size(120.dp), // Tamaño grande
                shape = androidx.compose.foundation.shape.CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "¡LANZA!",
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }





