package com.example.corpaliv.ui.screens.informacion

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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

// Pantalla información versión expandida
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreenExpandida(
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
                        viewModel.navigateTo(Screen.Home)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("¿Quienes somos?") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
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
                NavigationDrawerItem(
                    label = { Text("Login Administracion")},
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Login)
                    }
                )
            }
        }
    ) {
        // Estructura de la pantalla
        Scaffold(
            // Barra de título superior
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
            // Contenido vertical desplazable
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp)
            ) {
                // Título de la sección
                Text(
                    text = "¿Quienes Somos?",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )

                // Imagen corporativa
                Image(
                    painter = painterResource(id = R.drawable.ic_menu_gallery),
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )

                // Texto informativo principal
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque pharetra nulla at ante egestas, sed posuere dui dapibus. Curabitur auctor felis vel odio sollicitudin tincidunt. Vivamus enim orci, pretium in bibendum sit amet, cursus consectetur dui. In rhoncus id sapien ac consectetur. Donec euismod, lacus quis rhoncus sodales, dui justo fringilla mauris, eget molestie velit velit vitae elit. Morbi ut quam maximus, ultricies tellus vitae, mollis ante. Pellentesque ut dolor tempus, egestas tortor quis, dapibus purus. Cras ipsum urna, varius sed auctor et, aliquam a risus. Quisque urna mauris, vestibulum quis mattis a, finibus in sem. Proin diam turpis, gravida in ullamcorper sed, lobortis eget orci. Integer ornare elit ac mi tincidunt, sed ullamcorper urna dapibus.\n" +
                            "\n" +
                            "Aenean ullamcorper lorem nisl, quis efficitur ex tristique ut. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut sit amet tincidunt velit. Cras facilisis, lectus ac hendrerit vulputate, dui justo aliquet neque, at euismod nulla lectus ac turpis. Donec ut efficitur eros. Duis interdum nec augue laoreet euismod. Pellentesque ullamcorper sagittis tristique. Proin vitae ante consectetur, cursus dui ac, sodales nulla. Curabitur tincidunt vitae metus vehicula cursus. Donec consectetur volutpat mauris, eget eleifend risus interdum et. Donec aliquet purus non ligula euismod egestas. Integer quis sem non ex malesuada vestibulum eu eget quam. Sed sed posuere ex.\n" +
                            "\n" +
                            "Duis commodo quam eu congue dignissim. Nullam ultrices neque augue, eu pretium nibh hendrerit pellentesque. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nam congue malesuada urna, tincidunt semper enim. Nam id fermentum sem. Suspendisse laoreet sagittis risus, id scelerisque tortor mattis ac. Mauris malesuada mi ut libero convallis ullamcorper. Praesent ut velit pretium, scelerisque odio id, condimentum leo. Cras tellus enim, ornare posuere placerat non, cursus eget leo. In imperdiet eu lacus et vehicula.\n" +
                            "\n" +
                            "Sed nunc magna, finibus sit amet sagittis sed, sodales bibendum arcu. Praesent tincidunt tempus leo, eget dignissim elit venenatis sit amet. Aenean tincidunt magna accumsan mauris scelerisque imperdiet. Aliquam ut sollicitudin urna. Sed ut placerat ligula. Mauris vitae justo consectetur, porttitor ex eu, gravida dolor. Nulla facilisi. Sed facilisis turpis vel quam congue auctor.\n" +
                            "\n" +
                            "Maecenas et tortor mauris. In posuere ligula urna, sed condimentum leo pretium sit amet. Phasellus sit amet orci eget lacus ornare hendrerit sed eu justo. Donec ultrices eu ipsum id lacinia. Aliquam feugiat dictum nibh eget elementum. Nam consectetur eu lorem eget sollicitudin. Nulla varius orci massa, ut dapibus nibh condimentum vel. Interdum et malesuada fames ac ante ipsum primis in faucibus. Proin vel nulla purus. Nullam tincidunt leo id nulla pharetra, eu convallis neque tempus. Nullam euismod suscipit turpis, eget laoreet orci. Fusce porttitor, arcu sit amet posuere malesuada, mi massa consequat quam, efficitur euismod ante sem eu leo. Fusce gravida nulla at quam semper maximus..",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}