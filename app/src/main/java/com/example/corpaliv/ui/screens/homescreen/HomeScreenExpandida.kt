package com.example.corpaliv.ui.screens.homescreen

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.corpaliv.navigation.Screen
import com.example.corpaliv.viewmodel.MainViewModel
import kotlinx.coroutines.launch

// Pantalla inicio versión expandida
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpandida(
    navController: NavController,
    viewModel: MainViewModel
) {
    // Estado del menú lateral
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Menú de navegación lateral
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    label = { Text("Home Screen") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("¿Quienes somos?") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Info)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Contacto") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Contact)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Ver Registros") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Registros)
                    }
                )
            }
        }
    ) {
        // Estructura de la pantalla
        Scaffold(
            // Barra superior con menú
            topBar = {
                TopAppBar(
                    title = { Text("CORPALIV Expandido APP") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            // Contenedor principal de inicio
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp)
            ) {
                // Mensaje de bienvenida inicial
                Text(
                    text = "¡Bienvenido!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )

                // Botón interactivo principal
                Button(onClick = { /* accion futura */ }) {
                    Text(text = "Presióname")
                }

                // Botón interactivo secundario
                Button(onClick = { /* accion extra */ }) {
                    Text(text = "Más información")
                }

                // Imagen de inicio corporativa
                Image(
                    painter = painterResource(id = R.drawable.ic_menu_gallery),
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}