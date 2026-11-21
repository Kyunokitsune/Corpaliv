package com.example.corpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpaliv.data.model.UsuarioApiDto
import com.example.corpaliv.data.remote.RetrofitClient
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

sealed interface ApiUiState {
    object Cargando : ApiUiState
    data class Exito(val usuarios: List<UsuarioApiDto>) : ApiUiState
    data class Error(val mensaje: String) : ApiUiState
}

class MainViewModel : ViewModel() {
    // Usar Channel para eventos de un solo uso (One-time events)
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    // Función para guardar el formulario completo de contacto en la base de datos Firestore
    fun guardarContactoEnBD(registro: RegistroFormulario) {
        val db = Firebase.firestore

        // Datos del formulario completos
        val contacto = hashMapOf(
            "nombre" to registro.nombre,
            "correo" to registro.correo,
            "numeroContacto" to registro.numeroContacto,
            "comentario" to registro.comentario,
            "fecha" to registro.fecha
        )

        // Se guarda en la colección "contactos" de Firestore
        db.collection("contactos")
            .add(contacto)
            .addOnSuccessListener { documentReference ->
                println("Guardado con éxito en Firebase con ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error al guardar en Firebase: $e")
            }
    }

    // Sobrecarga rápida para enviar nombre y correo
    fun guardarContactoEnBD(nombre: String, email: String) {
        guardarContactoEnBD(
            RegistroFormulario(
                nombre = nombre,
                correo = email,
                numeroContacto = "",
                comentario = "",
                fecha = ""
            )
        )
    }

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

    // Función que agrega un registro al enviar el formulario (y lo envía a Firebase)
    fun agregarContacto(contacto: RegistroFormulario) {
        _listaContactos.update { listaActual ->
            listaActual + contacto
        }
        // Enviar a Firebase Firestore automáticamente al agregar contacto
        guardarContactoEnBD(contacto)
    }

    // --- API PÚBLICA ---
    private val _apiUiState = MutableStateFlow<ApiUiState>(ApiUiState.Cargando)
    val apiUiState: StateFlow<ApiUiState> = _apiUiState.asStateFlow()

    init {
        cargarUsuariosDesdeApi()
    }

    fun cargarUsuariosDesdeApi() {
        viewModelScope.launch {
            _apiUiState.value = ApiUiState.Cargando
            try {
                val usuarios = RetrofitClient.apiService.obtenerUsuarios()
                _apiUiState.value = ApiUiState.Exito(usuarios)
            } catch (e: Exception) {
                _apiUiState.value = ApiUiState.Error(e.localizedMessage ?: "Error al conectar con la API")
            }
        }
    }
}
