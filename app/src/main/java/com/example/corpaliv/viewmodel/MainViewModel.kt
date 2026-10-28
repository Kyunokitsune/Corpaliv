package com.example.corpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpaliv.navigation.NavigationEvent
import com.example.corpaliv.navigation.Screen
import com.example.corpaliv.ui.utils.RegistroFormulario
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // Usar Channel para eventos de un solo uso (One-time events)
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun navigateTo(screen: Screen) {
        viewModelScope.launch {
            _navigationEvents.send(NavigationEvent.NavigateTo(route = screen))
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvents.send(NavigationEvent.PopBackStack)
        }
    }

    fun navigateUp() {
        viewModelScope.launch {
            _navigationEvents.send(NavigationEvent.NavigateUp)
        }
    }

    // Contactos Guardados
    private val _listaContactos = MutableStateFlow<List<RegistroFormulario>>(emptyList())
    val listaContactos: StateFlow<List<RegistroFormulario>> = _listaContactos.asStateFlow()

    // Función que agrega un registro al enviar el formulario
    fun agregarContacto(contacto: RegistroFormulario) {
        _listaContactos.update { listaActual ->
            listaActual + contacto
        }
    }
}