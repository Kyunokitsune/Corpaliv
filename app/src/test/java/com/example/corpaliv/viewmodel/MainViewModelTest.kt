package com.example.corpaliv.viewmodel

import app.cash.turbine.test
import com.example.corpaliv.MainDispatcherRule
import com.example.corpaliv.data.model.UsuarioApiDto
import com.example.corpaliv.data.remote.ApiService
import com.example.corpaliv.data.remote.RetrofitClient
import com.example.corpaliv.navigation.NavigationEvent
import com.example.corpaliv.navigation.Screen
import com.example.corpaliv.ui.utils.RegistroFormulario
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockApiService: ApiService

    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var mockCollection: CollectionReference
    private lateinit var mockTask: Task<DocumentReference>

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        mockkObject(RetrofitClient)
        mockApiService = mockk()
        every { RetrofitClient.apiService } returns mockApiService

        coEvery { mockApiService.obtenerUsuarios() } returns emptyList()

        mockkStatic("com.google.firebase.firestore.ktx.FirestoreKt")
        mockFirestore = mockk()
        mockCollection = mockk()
        mockTask = mockk(relaxed = true)

        every { Firebase.firestore } returns mockFirestore
        every { mockFirestore.collection("contactos") } returns mockCollection
        every { mockCollection.add(any()) } returns mockTask

        viewModel = MainViewModel()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // NAVEGACION

    @Test
    fun navigateTo_emite_evento_correcto() = runTest {
        viewModel.navigationEvents.test {
            viewModel.navigateTo(Screen.Info)

            val evento = awaitItem()
            assertTrue(evento is NavigationEvent.NavigateTo)
            assertEquals(Screen.Info, (evento as NavigationEvent.NavigateTo).route)
        }
    }

    @Test
    fun navigateBack_emite_evento_correcto() = runTest {
        viewModel.navigationEvents.test {
            viewModel.navigateBack()

            val evento = awaitItem()
            assertEquals(NavigationEvent.PopBackStack, evento)
        }
    }

    @Test
    fun navigateUp_emite_evento_correcto() = runTest {
        viewModel.navigationEvents.test {
            viewModel.navigateUp()

            val evento = awaitItem()
            assertEquals(NavigationEvent.NavigateUp, evento)
        }
    }

    // CONTACTOS

    @Test
    fun agregarContacto_Firestore() {
        val nuevoRegistro = RegistroFormulario(
            nombre = "Ana Pérez",
            correo = "ana@correo.com",
            numeroContacto = "+56912345678",
            comentario = "Consulta general",
            fecha = "2026-11-22"
        )

        viewModel.agregarContacto(nuevoRegistro)

        assertEquals(1, viewModel.listaContactos.value.size)
        assertEquals(nuevoRegistro, viewModel.listaContactos.value.first())

        val captura = slot<Any>()
        verify(exactly = 1) {
            mockCollection.add(capture(captura))
        }

        val mapa = captura.captured as Map<*, *>
        assertEquals("Ana Pérez", mapa["nombre"])
        assertEquals("ana@correo.com", mapa["correo"])
        assertEquals("+56912345678", mapa["numeroContacto"])
        assertEquals("Consulta general", mapa["comentario"])
        assertEquals("2026-11-22", mapa["fecha"])
    }

    @Test
    fun guardarContacto_Firestore() {
        viewModel.guardarContactoEnBD(nombre = "Carlos", email = "carlos@correo.com")

        val captura = slot<Any>()
        verify(exactly = 1) {
            mockCollection.add(capture(captura))
        }

        val mapa = captura.captured as Map<*, *>
        assertEquals("Carlos", mapa["nombre"])
        assertEquals("carlos@correo.com", mapa["correo"])
        assertEquals("", mapa["numeroContacto"])
        assertEquals("", mapa["comentario"])
        assertEquals("", mapa["fecha"])
    }

    // API

    @Test
    fun cargarUsuariosDesdeApi_exito() = runTest {
        val usuariosSimulados = listOf(
            mockk<UsuarioApiDto>(),
            mockk<UsuarioApiDto>()
        )
        coEvery { mockApiService.obtenerUsuarios() } returns usuariosSimulados

        viewModel.cargarUsuariosDesdeApi()

        val estadoActual = viewModel.apiUiState.value
        assertTrue(estadoActual is ApiUiState.Exito)
        assertEquals(usuariosSimulados, (estadoActual as ApiUiState.Exito).usuarios)
    }

    @Test
    fun cargarUsuariosDesdeApi_error() = runTest {
        coEvery { mockApiService.obtenerUsuarios() } throws RuntimeException("Sin conexión a internet")

        viewModel.cargarUsuariosDesdeApi()

        val estadoActual = viewModel.apiUiState.value
        assertTrue(estadoActual is ApiUiState.Error)
        assertEquals("Sin conexión a internet", (estadoActual as ApiUiState.Error).mensaje)
    }

}