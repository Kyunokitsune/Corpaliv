package com.example.corpaliv.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("HomeScreen")

    data object Info : Screen("InfoScreen")

    data object Contact : Screen("ContactoScreen")
}