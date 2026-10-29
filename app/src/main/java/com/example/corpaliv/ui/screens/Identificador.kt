package com.example.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.corpaliv.ui.screens.contactoscreen.ContactoScreenCompacto
import com.example.corpaliv.ui.screens.contactoscreen.ContactoScreenExpandida
import com.example.corpaliv.ui.screens.contactoscreen.ContactoScreenMediana
import com.example.corpaliv.ui.screens.homescreen.HomeScreenCompacta
import com.example.corpaliv.ui.screens.homescreen.HomeScreenExpandida
import com.example.corpaliv.ui.screens.homescreen.HomeScreenMediana
import com.example.corpaliv.ui.screens.informacion.InfoScreenCompacta
import com.example.corpaliv.ui.screens.informacion.InfoScreenExpandida
import com.example.corpaliv.ui.screens.informacion.InfoScreenMediana
import com.example.corpaliv.ui.screens.registrosscreen.RegistrosScreenCompacta
import com.example.corpaliv.ui.screens.registrosscreen.RegistrosScreenExpandida
import com.example.corpaliv.ui.screens.registrosscreen.RegistrosScreenMediana
import com.example.corpaliv.ui.screens.loginscreen.LoginScreenCompacta
import com.example.corpaliv.ui.screens.loginscreen.LoginScreenMediana
import com.example.corpaliv.ui.screens.loginscreen.LoginScreenExpandida
import com.example.corpaliv.ui.utils.obtenerWindowSizeClass
import com.example.corpaliv.viewmodel.MainViewModel

// Identificador pantalla inicio
@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel) {
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta(
            navController = navController,
            viewModel = viewModel
        )
        WindowWidthSizeClass.Medium -> HomeScreenMediana(
            navController = navController,
            viewModel = viewModel
        )
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida(
            navController = navController,
            viewModel = viewModel
        )
    }
}

// Identificador pantalla información
@Composable
fun InfoScreen(navController: NavController, viewModel: MainViewModel) {
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> InfoScreenCompacta(
            navController = navController,
            viewModel = viewModel
        )
        WindowWidthSizeClass.Medium -> InfoScreenMediana(
            navController = navController,
            viewModel = viewModel
        )
        WindowWidthSizeClass.Expanded -> InfoScreenExpandida(
            navController = navController,
            viewModel = viewModel
        )
    }
}

// Identificador pantalla contacto
@Composable
fun ContactoScreen(navController: NavController, viewModel: MainViewModel) {
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> ContactoScreenCompacto(
            navController = navController,
            viewModel = viewModel
        )
        WindowWidthSizeClass.Medium -> ContactoScreenMediana(
            navController = navController,
            viewModel = viewModel
        )
        WindowWidthSizeClass.Expanded -> ContactoScreenExpandida(
            navController = navController,
            viewModel = viewModel
        )
    }
}

// Identificador pantalla registros
@Composable
fun RegistrosScreen(navController: NavController, viewModel: MainViewModel) {
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> RegistrosScreenCompacta(
            navController = navController,
            viewModel = viewModel
        )
        WindowWidthSizeClass.Medium -> RegistrosScreenMediana(
            navController = navController,
            viewModel = viewModel
        )
        WindowWidthSizeClass.Expanded -> RegistrosScreenExpandida(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun LoginScreen(navController: NavController, viewModel: MainViewModel) {
    val windowSizeClass = obtenerWindowSizeClass()
    when(windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> LoginScreenCompacta(
            navController = navController,
            viewModel = viewModel
        )
        WindowWidthSizeClass.Medium -> LoginScreenMediana(
            navController = navController,
            viewModel = viewModel
        )

        WindowWidthSizeClass.Expanded -> LoginScreenExpandida(
            navController = navController,
            viewModel = viewModel
        )
    }
}