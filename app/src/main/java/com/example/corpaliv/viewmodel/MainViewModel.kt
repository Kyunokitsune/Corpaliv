package com.example.corpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpaliv.navigation.NavigationEvent
import com.example.corpaliv.navigation.Screen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.receiveAsFlow

class MainViewModel : ViewModel() {
    // Usar Channel para eventos de un solo uso (One-time events)
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()


    //private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    //val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()


    //se cambia el emit por send para poder manejar el 1 solo uso y que la pantalla siempre vuelva al home
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

}