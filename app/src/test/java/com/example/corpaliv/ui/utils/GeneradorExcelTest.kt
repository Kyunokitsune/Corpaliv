package com.example.corpaliv.ui.utils

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import java.io.ByteArrayOutputStream
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class GeneradorExcelTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var context: Context
    private lateinit var directorioDescargasSimulado: File

    @Before
    fun setUp() {
        context = spyk(ApplicationProvider.getApplicationContext<Context>())
        directorioDescargasSimulado = tempFolder.newFolder("Downloads")

        mockkStatic(Environment::class)
        every { Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) } returns directorioDescargasSimulado

        mockkStatic(FileProvider::class)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.Q])
    fun guardarFormularioEnExcel_android10OSuperior_generaCsvConBomYEscapaComillas() {
        val mockResolver = mockk<ContentResolver>(relaxed = true)
        every { context.contentResolver } returns mockResolver

        val mockUri = Uri.parse("content://media/external/downloads/123")
        val outputStream = ByteArrayOutputStream()

        every { mockResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, any()) } returns mockUri
        every { mockResolver.openOutputStream(mockUri) } returns outputStream

        val resultado = GeneradorExcel.guardarFormularioEnExcel(
            context = context,
            nombre = "Juan \"El Profe\" Pérez",
            correo = "juan@correo.com",
            numeroContacto = "+56911112222",
            comentario = "Comentario con \"comillas\""
        )

        assertTrue(resultado.exito)
        assertEquals(mockUri, resultado.uri)
        assertTrue(resultado.nombreArchivo.startsWith("Formulario_Contacto_"))
        assertTrue(resultado.nombreArchivo.endsWith(".csv"))

        val contenidoEscrito = outputStream.toString(Charsets.UTF_8.name())
        assertTrue(contenidoEscrito.startsWith("\uFEFF"))
        assertTrue(contenidoEscrito.contains("Nombre completo;Correo electrónico;Número de contacto;Comentarios;Fecha de envío"))
        assertTrue(contenidoEscrito.contains("\"Juan \"\"El Profe\"\" Pérez\";\"juan@correo.com\";\"+56911112222\";\"Comentario con \"\"comillas\"\"\";"))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.Q])
    fun guardarFormularioEnExcel_mediaStoreDevuelveNull_retornaResultadoConError() {
        val mockResolver = mockk<ContentResolver>(relaxed = true)
        every { context.contentResolver } returns mockResolver
        every { mockResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, any()) } returns null

        val resultado = GeneradorExcel.guardarFormularioEnExcel(
            context = context,
            nombre = "Ana",
            correo = "ana@correo.com",
            numeroContacto = "123456",
            comentario = "Hola"
        )

        assertFalse(resultado.exito)
        assertEquals("No se pudo crear el archivo en MediaStore", resultado.mensajeError)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.P])
    fun guardarFormularioEnExcel_android9OInferior_creaArchivoEnDirectorioDescargas() {
        val mockUri = Uri.parse("content://com.example.corpaliv.fileprovider/descargas/test.csv")
        every { FileProvider.getUriForFile(any(), any(), any()) } returns mockUri

        val resultado = GeneradorExcel.guardarFormularioEnExcel(
            context = context,
            nombre = "María López",
            correo = "maria@correo.com",
            numeroContacto = "987654321",
            comentario = "Prueba legacy"
        )

        assertTrue(resultado.exito)
        assertEquals(mockUri, resultado.uri)

        val archivoCreado = File(resultado.rutaArchivo)
        assertTrue(archivoCreado.exists())
        val textoArchivo = archivoCreado.readText(Charsets.UTF_8)
        assertTrue(textoArchivo.contains("\"María López\";\"maria@correo.com\";\"987654321\";\"Prueba legacy\";"))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.Q])
    fun guardarFormularioEnExcel_excepcionInesperada_capturaYRetornaFallo() {
        every { context.contentResolver } throws RuntimeException("Error de almacenamiento")

        val resultado = GeneradorExcel.guardarFormularioEnExcel(
            context = context,
            nombre = "Test",
            correo = "test@correo.com",
            numeroContacto = "000",
            comentario = "Error"
        )

        assertFalse(resultado.exito)
        assertEquals("Error de almacenamiento", resultado.mensajeError)
    }

    @Test
    fun abrirArchivoExcel_conUriValida_iniciaActividadDeVisualizacion() {
        val uriPrueba = Uri.parse("content://media/external/downloads/123")
        val resultado = ResultadoExcel(exito = true, uri = uriPrueba)

        GeneradorExcel.abrirArchivoExcel(context, resultado)

        val shadowApp = shadowOf(ApplicationProvider.getApplicationContext<android.app.Application>())
        val intentLanzado = shadowApp.nextStartedActivity

        assertEquals(Intent.ACTION_VIEW, intentLanzado.action)
        assertEquals(uriPrueba, intentLanzado.data)
        assertEquals("text/csv", intentLanzado.type)
    }

    @Test
    fun abrirArchivoExcel_sinAppCompatible_muestraToastDeError() {
        val uriPrueba = Uri.parse("content://media/external/downloads/123")
        every { context.startActivity(any()) } throws RuntimeException("No Activity found")

        val resultado = ResultadoExcel(exito = true, uri = uriPrueba)
        GeneradorExcel.abrirArchivoExcel(context, resultado)

        assertEquals(
            "No se encontró una aplicación para abrir el archivo CSV/Excel",
            ShadowToast.getTextOfLatestToast()
        )
    }

    @Test
    fun abrirArchivoExcel_conUriNula_noHaceNada() {
        val resultado = ResultadoExcel(exito = false, uri = null)
        GeneradorExcel.abrirArchivoExcel(context, resultado)

        val shadowApp = shadowOf(ApplicationProvider.getApplicationContext<android.app.Application>())
        assertEquals(null, shadowApp.nextStartedActivity)
    }
}