package com.example.symphorb.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@Composable
fun PantallaInicio(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🎮 Bienvenido al Pachinko de Luigi")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            navController.navigate("pachinko")
        }) {
            Text("🎰 Jugar ahora")
        }
    }
}