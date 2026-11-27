package com.example.corpaliv.ui.screens.contactoscreen

import android.os.Build
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.example.corpaliv.ui.utils.GeneradorExcel
import com.example.corpaliv.ui.utils.ResultadoExcel
import com.example.corpaliv.viewmodel.MainViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import android.Manifest
import android.app.Application
import androidx.test.core.app.ApplicationProvider
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU]) // Android 13 (API 33): no requiere pedir permisos legacy de almacenamiento
class FormularioScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: MainViewModel

    @Before
    fun setUp() {
        mockViewModel = mockk(relaxed = true)
        mockkObject(GeneradorExcel)

        // Otorga los permisos requeridos en Android 13 (TIRAMISU) en el entorno de Robolectric
        val app = ApplicationProvider.getApplicationContext<Application>()
        shadowOf(app).grantPermissions(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun correoConDominioNoPermitido_muestraMensajeDeErrorEnCampo() {
        composeTestRule.setContent {
            FormularioScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Correo electrónico")
            .performTextInput("usuario@hotmail.com")

        composeTestRule.onNodeWithText(
            "Debe terminar en @gmail.com, @gmail.cl, @duocuc.cl o @duoc.cl"
        ).assertIsDisplayed()
    }

    @Test
    fun telefono_filtraLetrasYRespetaLimiteDe9Digitos() {
        composeTestRule.setContent {
            FormularioScreen(viewModel = mockViewModel)
        }

        // Intentamos ingresar letras y más de 9 dígitos
        composeTestRule.onNodeWithText("Número de contacto Ej: 9 1234 5678")
            .performTextInput("abc91234567899")

        // El contador debe marcar 0 / 9 porque la validación rechaza la entrada si contiene letras o excede 9
        composeTestRule.onNodeWithText("0 / 9").assertIsDisplayed()

        // Ingresamos exactamente 9 dígitos válidos
        composeTestRule.onNodeWithText("Número de contacto Ej: 9 1234 5678")
            .performTextInput("912345678")

        composeTestRule.onNodeWithText("9 / 9").assertIsDisplayed()
    }

    @Test
    fun enviarConCamposVacios_muestraToastDeErrorYNoGuarda() {
        composeTestRule.setContent {
            FormularioScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Enviar y Descargar Excel")
            .performScrollTo()
            .performClick()

        assertEquals(
            "Por favor completa los campos principales correctamente",
            ShadowToast.getTextOfLatestToast()
        )
        verify(exactly = 0) { mockViewModel.agregarContacto(any()) }
    }

    @Test
    fun enviarConDominioInvalido_muestraToastEspecificoDeDominio() {
        composeTestRule.setContent {
            FormularioScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Nombre completo")
            .performTextInput("Carlos Soto")
        composeTestRule.onNodeWithText("Correo electrónico")
            .performTextInput("carlos@yahoo.com")
        composeTestRule.onNodeWithText("Número de contacto Ej: 9 1234 5678")
            .performTextInput("987654321")

        composeTestRule.onNodeWithText("Enviar y Descargar Excel")
            .performScrollTo()
            .performClick()

        assertEquals(
            "El correo debe terminar en @gmail.com, @gmail.cl, @duocuc.cl o @duoc.cl",
            ShadowToast.getTextOfLatestToast()
        )
        verify(exactly = 0) { mockViewModel.agregarContacto(any()) }
    }

    @Test
    fun enviarFormularioValido_generaExcelAgregaContactoYMuestraDialogoExito() {
        every {
            GeneradorExcel.guardarFormularioEnExcel(any(), any(), any(), any(), any())
        } returns ResultadoExcel(
            exito = true,
            rutaArchivo = "/storage/emulated/0/Download/Formulario_Contacto.csv",
            nombreArchivo = "Formulario_Contacto.csv"
        )

        composeTestRule.setContent {
            FormularioScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Nombre completo")
            .performTextInput("Ana Pérez")
        composeTestRule.onNodeWithText("Correo electrónico")
            .performTextInput("ana@duocuc.cl")
        composeTestRule.onNodeWithText("Número de contacto Ej: 9 1234 5678")
            .performTextInput("912345678")
        composeTestRule.onNodeWithText("Comentarios")
            .performTextInput("Consulta de prueba")

        composeTestRule.onNodeWithText("Enviar y Descargar Excel")
            .performScrollTo()
            .performClick()

        // Verifica que se llamó al ViewModel con los datos del formulario
        verify(exactly = 1) {
            mockViewModel.agregarContacto(
                match {
                    it.nombre == "Ana Pérez" &&
                            it.correo == "ana@duocuc.cl" &&
                            it.numeroContacto == "912345678" &&
                            it.comentario == "Consulta de prueba"
                }
            )
        }

        // Verifica que aparece el diálogo de confirmación en pantalla
        composeTestRule.onNodeWithText("¡Formulario Enviado!").assertIsDisplayed()
    }
}