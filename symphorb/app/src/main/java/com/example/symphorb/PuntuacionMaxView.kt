package com.example.symphorb.uis

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.symphorb.viewmodel.RecordViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.symphorb.viewmodel.RecordViewModelFactory

@Composable
fun PuntuacionMaxView(recordViewModel: RecordViewModel = viewModel()) {
    val context = LocalContext.current
    val recordViewModel: RecordViewModel = viewModel(factory = RecordViewModelFactory(context))
    val record by recordViewModel.record.collectAsState(initial = 0)
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🏅 Record: $record puntos",
            style = MaterialTheme.typography.bodyLarge
        )

        Button(onClick = {
            recordViewModel.resetearRecord()
        }) {
            Text("Resetear récord")
        }
    }
}
