package com.example.symphorb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.symphorb.navigation.NavGraph
import com.example.symphorb.ui.theme.SymphorbTheme  // Asegúrate de que este sea el nombre correcto

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SymphorbTheme {  // Usa el nombre correcto del tema aquí
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}