package com.example.corpaliv.ui.screens

import androidx.compose.runtime.Composable
import com.example.corpaliv.ui.screens.registrosscreen.TablaRegistrosContent
import com.example.corpaliv.viewmodel.MainViewModel

// Redirección para compatibilidad
@Deprecated("Usar RegistrosScreen en ui.screens.registrosscreen")
@Composable
fun TablaRegistrosScreen(viewModel: MainViewModel) {
    TablaRegistrosContent(viewModel = viewModel)
}