package com.example.corpaliv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.corpaliv.navigation.NavigationEvent
import com.example.corpaliv.navigation.Screen
import com.example.corpaliv.ui.screens.ContactoScreen
import com.example.corpaliv.ui.screens.HomeScreen
import com.example.corpaliv.ui.screens.InfoScreen
import com.example.corpaliv.ui.screens.RegistrosScreen
import com.example.corpaliv.ui.theme.CorpalivTheme
import com.example.corpaliv.ui.utils.rememberGestorPermisosAlmacenamiento
import com.example.corpaliv.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorpalivTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = viewModel()

                var mostrarDialogoPermisoDenegado by remember { mutableStateOf(false) }

                // 1. Inicialización del gestor de permisos
                val solicitarPermisosAlmacenamiento = rememberGestorPermisosAlmacenamiento(
                    onPermisosConcedidos = {
                        // Acción opcional al conceder permisos
                    },
                    onPermisosDenegados = {
                        mostrarDialogoPermisoDenegado = true
                    }
                )

                // 2. Solicitud automática al iniciar la app
                LaunchedEffect(Unit) {
                    solicitarPermisosAlmacenamiento()
                }

                // 3. Colector de eventos de navegación
                LaunchedEffect(Unit) {
                    mainViewModel.navigationEvents.collect { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route.route) {
                                    event.popUpToRoute?.let { popUpScreen ->
                                        popUpTo(popUpScreen.route) {
                                            inclusive = event.inclusive
                                        }
                                    }
                                    launchSingleTop = event.singleTop
                                }
                            }
                            is NavigationEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                            is NavigationEvent.NavigateUp -> {
                                navController.navigateUp()
                            }
                        }
                    }
                }

                // 4. Contenedor de pantallas
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                ) {
                    composable(Screen.Home.route) {
                        HomeScreen(
                            navController = navController,
                            viewModel = mainViewModel
                        )
                    }
                    composable(Screen.Info.route) {
                        InfoScreen(
                            navController = navController,
                            viewModel = mainViewModel
                        )
                    }
                    composable(Screen.Contact.route) {
                        ContactoScreen(
                            navController = navController,
                            viewModel = mainViewModel
                        )
                    }
                    composable(Screen.Registros.route) {
                        RegistrosScreen(
                            navController = navController,
                            viewModel = mainViewModel
                        )
                    }
                }

                // 5. Diálogo si se deniegan los permisos
                if (mostrarDialogoPermisoDenegado) {
                    AlertDialog(
                        onDismissRequest = { mostrarDialogoPermisoDenegado = false },
                        title = { Text("Funciones limitadas") },
                        text = {
                            Text(
                                "Al rechazar los permisos de almacenamiento, no podrás acceder a las siguientes funciones:\n\n" +
                                        "• Creación y guardado de archivos locales.\n" +
                                        "• Lectura de documentos o datos guardados previamente.\n" +
                                        "• Exportación de formularios."
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = { mostrarDialogoPermisoDenegado = false }) {
                                Text("Entendido")
                            }
                        }
                    )
                }
            }
        }
    }
}