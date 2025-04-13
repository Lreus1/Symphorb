package com.example.symphorb.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.symphorb.ui.pantallas.PantallaInicio
import com.example.symphorb.ui.pantallas.PachinkoView
import com.example.symphorb.ui.pantallas.HistorialView
import com.example.symphorb.ui.pantallas.PachinkoBoard

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") {
            PantallaInicio(navController = navController) // Pasamos navController aquí
        }
        composable("pachinko") {
            PachinkoView(navController = navController) // Pasamos navController aquí
        }
        composable("pachinkoBoard") {
            PachinkoBoard(navController = navController) // Pasamos navController aquí
        }
    }
}