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


@Composable
fun PachinkoView(navController: NavHostController) {

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
                    // Solo navegamos a la pantalla de seleccion de niveles
                    navController.navigate("seleccion_niveles")
                }
            ) {
                Text("Niveles")
            }

            Button(
                onClick = {
                    navController.navigate("inicio") // Regresa a la pantalla de inicio
                }
            ) {
                Text("🏠 Inicio")
            }
        }
    }
}

@Composable
fun TituloView() {
    Text(
        text = "\uD83C\uDFB0 Pachinko Game",
        style = MaterialTheme.typography.headlineMedium
    )
}

