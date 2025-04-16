package com.example.symphorb.ui.pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SeleccionNiveles(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Selecciona un nivel", style = MaterialTheme.typography.headlineMedium)

        Button(onClick = {
            navController.navigate("pachinkoBoard")
        }) {
            Text("Nivel 1")
        }

        Box(modifier = Modifier.weight(1f)) {} // 🔧 Esto empuja hacia abajo el botón

        Button(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text("Volver")
        }
    }
}

