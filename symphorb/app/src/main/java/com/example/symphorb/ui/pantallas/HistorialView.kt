package com.example.symphorb.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.symphorb.viewmodel.HistorialViewModel
import com.example.symphorb.model.Historial

@Composable
fun HistorialView(historialViewModel: HistorialViewModel = viewModel()) {
    val historial by historialViewModel.historial.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("📝 Historial de partidas", style = MaterialTheme.typography.headlineMedium)

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(historial) { item ->
                Text("🔹 ${item.fecha} | ${item.resultado} → +${item.puntos} pts")
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = { historialViewModel.reiniciarHistorial() }) {
            Text("🧹 Borrar historial")
        }
    }
}
