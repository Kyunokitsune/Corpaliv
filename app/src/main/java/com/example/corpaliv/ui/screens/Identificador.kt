package com.example.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.corpaliv.ui.screens.homescreen.HomeScreenCompacta
import com.example.corpaliv.ui.screens.homescreen.HomeScreenExpandida
import com.example.corpaliv.ui.screens.homescreen.HomeScreenMediana
import com.example.corpaliv.ui.screens.informacion.InfoScreenCompacta
import com.example.corpaliv.ui.screens.informacion.InfoScreenMediana
import com.example.corpaliv.ui.screens.informacion.InfoScreenExpandida
import com.example.corpaliv.ui.utils.obtenerWindowSizeClass
import com.example.corpaliv.viewmodel.MainViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel){
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact -> HomeScreenCompacta(
            navController = navController,
            viewModel = viewModel)
        WindowWidthSizeClass.Medium -> HomeScreenMediana(
            navController = navController,
            viewModel = viewModel)
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida(
            navController = navController,
            viewModel = viewModel)


    }
}

@Composable
fun InfoScreen(navController: NavController, viewModel: MainViewModel){
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact -> InfoScreenCompacta(
            navController = navController,
            viewModel = viewModel)
        WindowWidthSizeClass.Medium -> InfoScreenMediana(
            navController = navController,
            viewModel = viewModel)
        WindowWidthSizeClass.Expanded -> InfoScreenExpandida(
            navController = navController,
            viewModel = viewModel)
    }
}