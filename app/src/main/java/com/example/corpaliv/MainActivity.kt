package com.example.corpaliv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.corpaliv.navigation.NavigationEvent
import com.example.corpaliv.navigation.Screen
import com.example.corpaliv.ui.screens.HomeScreen
import com.example.corpaliv.ui.screens.InfoScreen
import com.example.corpaliv.ui.theme.CorpalivTheme
import com.example.corpaliv.viewmodel.MainViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorpalivTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = viewModel()

                LaunchedEffect(mainViewModel) {
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
                }

            }
        }
    }
}