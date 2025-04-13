package com.example.symphorb.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.symphorb.model.Resultado
import com.example.symphorb.viewmodel.RecordViewModel
import com.example.symphorb.utils.SimuladorPachinko
import com.example.symphorb.uis.PuntuacionMaxView
import com.example.symphorb.BolaAnimadaAvanzada
import com.example.symphorb.view.JuegoViewModel
import com.example.symphorb.viewmodel.RecordViewModelFactory

@Composable
fun PachinkoView(navController: NavHostController, viewModel: JuegoViewModel = viewModel()) {
    val resultado by viewModel.resultadoActual.collectAsStateWithLifecycle()
    val puntuacion by viewModel.puntuacionTotal.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val recordViewModel: RecordViewModel = viewModel(factory = RecordViewModelFactory(context))
    val record by recordViewModel.record.collectAsState(initial = 0)

    var mostrarResultado by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TituloView()

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    // Solo navegamos a la pantalla de PachinkoBoard, sin pasar parámetros
                    navController.navigate("pachinkoBoard")
                }
            ) {
                Text("🎯 Nueva bola")
            }

            Button(
                onClick = {
                    navController.navigate("inicio") // Regresa a la pantalla de inicio
                }
            ) {
                Text("🏠 Inicio")
            }
        }

        PuntuacionView(puntuacion = puntuacion)
        PuntuacionMaxView(recordViewModel = recordViewModel)
    }
}

@Composable
fun TituloView() {
    Text(
        text = "\uD83C\uDFB0 Pachinko Game",
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun ResultadoView(resultado: Resultado?, mostrar: Boolean) {
    if (mostrar && resultado != null) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Resultado: ${resultado.nombre}", style = MaterialTheme.typography.bodyLarge)
            Text("Ganaste: ${resultado.puntos} puntos", style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        Text(
            text = "Pulsa el botón para lanzar una bola",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
    }
}

@Composable
fun PuntuacionView(puntuacion: Int) {
    Text("Puntuación total: $puntuacion", style = MaterialTheme.typography.titleMedium)
}

