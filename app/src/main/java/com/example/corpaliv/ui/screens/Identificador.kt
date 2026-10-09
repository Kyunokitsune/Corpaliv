package com.example.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.example.corpaliv.ui.screens.homescreen.HomeScreenCompacta
import com.example.corpaliv.ui.screens.homescreen.HomeScreenExpandida
import com.example.corpaliv.ui.screens.homescreen.HomeScreenMediana
import com.example.corpaliv.ui.screens.informacion.InfoScreenCompacta
import com.example.corpaliv.ui.screens.informacion.InfoScreenMediana
import com.example.corpaliv.ui.screens.informacion.InfoScreenExpandida
import com.example.corpaliv.ui.utils.obtenerWindowSizeClass

@Composable
fun HomeScreen(){
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact -> HomeScreenCompacta()
        WindowWidthSizeClass.Medium -> HomeScreenMediana()
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida()


    }
}

@Composable
fun InfoScreen(){
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact -> InfoScreenCompacta()
        WindowWidthSizeClass.Medium -> InfoScreenMediana()
        WindowWidthSizeClass.Expanded -> InfoScreenExpandida()
    }
}