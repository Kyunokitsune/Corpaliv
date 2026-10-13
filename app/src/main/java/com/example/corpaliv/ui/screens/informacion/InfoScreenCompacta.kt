package com.example.corpaliv.ui.screens.informacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreenCompacta (
    navController: NavController,
    viewModel: MainViewModel
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

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
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(title = {Text("CORPALIV Compacto APP")})
                }
            ){innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 10.dp)
                ){
                    Text(
                        text = "¿Quienes Somos?",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
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
