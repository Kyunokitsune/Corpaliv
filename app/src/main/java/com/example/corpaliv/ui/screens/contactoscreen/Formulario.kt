package com.example.corpaliv.ui.screens.contactoscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.corpaliv.ui.utils.GeneradorExcel
import com.example.corpaliv.ui.utils.RegistroFormulario
import com.example.corpaliv.ui.utils.ResultadoExcel
import com.example.corpaliv.ui.utils.rememberGestorPermisosAlmacenamiento
import com.example.corpaliv.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Formulario de contacto principal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(viewModel: MainViewModel? = null) {
    val context = LocalContext.current

    // Estado del nombre ingresado
    var nombre by rememberSaveable { mutableStateOf("") }
    // Estado del correo ingresado
    var correo by rememberSaveable { mutableStateOf("") }
    // Estado del teléfono ingresado
    var numeroContacto by rememberSaveable { mutableStateOf("") }
    // Estado del comentario ingresado
    var comentario by rememberSaveable { mutableStateOf("") }

    // Estado para resultado guardado
    var mensajeEnviado by rememberSaveable { mutableStateOf(false) }
    var rutaArchivoExcel by rememberSaveable { mutableStateOf("") }
    var resultadoUltimoExcel by remember { mutableStateOf<ResultadoExcel?>(null) }

    // Límites de caracteres ingresados
    val limiteComentario = 1000
    val limitenumtelefonico = 9

    // Validación de correos permitidos
    val terminacionesPermitidas = listOf("@gmail.com", "@gmail.cl", "@duocuc.cl", "@duoc.cl")
    val esCorreoValido = correo.isEmpty() || terminacionesPermitidas.any { correo.lowercase().endsWith(it) }

    // Procesa y guarda Excel
    val ejecutarGuardadoExcel = {
        val resultado = GeneradorExcel.guardarFormularioEnExcel(
            context = context,
            nombre = nombre,
            correo = correo,
            numeroContacto = numeroContacto,
            comentario = comentario
        )

        val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        // Agrega registro al ViewModel
        viewModel?.agregarContacto(
            RegistroFormulario(
                nombre = nombre,
                correo = correo,
                numeroContacto = numeroContacto,
                comentario = comentario,
                fecha = fechaActual
            )
        )

        if (resultado.exito) {
            mensajeEnviado = true
            rutaArchivoExcel = resultado.rutaArchivo
            resultadoUltimoExcel = resultado

            Toast.makeText(
                context,
                "Excel descargado en: ${resultado.rutaArchivo}",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                context,
                "Error al guardar Excel: ${resultado.mensajeError}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Solicita permisos de almacenamiento
    val solicitarPermisosYGuardar = rememberGestorPermisosAlmacenamiento(
        onPermisosConcedidos = {
            ejecutarGuardadoExcel()
        },
        onPermisosDenegados = {
            Toast.makeText(
                context,
                "No se otorgaron permisos para guardar el archivo Excel",
                Toast.LENGTH_SHORT
            ).show()
        }
    )

    // Estructura contenedora del formulario
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Formulario de Contacto") })
        }
    ) { innerPadding ->
        // Columna vertical con campos
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Campo de texto nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo de texto correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it.trim() },
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = !esCorreoValido,
                supportingText = {
                    if (!esCorreoValido) {
                        Text(
                            text = "Debe terminar en @gmail.com, @gmail.cl, @duocuc.cl o @duoc.cl",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            // Campo de texto teléfono
            OutlinedTextField(
                value = numeroContacto,
                onValueChange = { nuevoTexto ->
                    if (nuevoTexto.all { it.isDigit() } && nuevoTexto.length <= limitenumtelefonico) {
                        numeroContacto = nuevoTexto
                    }
                },
                label = { Text("Número de contacto Ej: 9 1234 5678") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = {
                    Text(
                        text = "${numeroContacto.length} / $limitenumtelefonico",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            )

            // Campo de texto comentarios
            OutlinedTextField(
                value = comentario,
                onValueChange = { nuevoTexto ->
                    if (nuevoTexto.length <= limiteComentario) {
                        comentario = nuevoTexto
                    }
                },
                label = { Text("Comentarios") },
                placeholder = { Text("Escribe tus comentarios u observaciones aquí...") },
                minLines = 4,
                maxLines = 7,
                supportingText = {
                    Text(
                        text = "${comentario.length} / $limiteComentario",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón enviar y descargar
            Button(
                onClick = {
                    val correoEsCorrecto = correo.isNotBlank() && terminacionesPermitidas.any { correo.lowercase().endsWith(it) }

                    if (nombre.isNotBlank() && correoEsCorrecto && numeroContacto.isNotBlank()) {
                        solicitarPermisosYGuardar()
                    } else {
                        val textoError = if (!correoEsCorrecto && correo.isNotBlank()) {
                            "El correo debe terminar en @gmail.com, @gmail.cl, @duocuc.cl o @duoc.cl"
                        } else {
                            "Por favor completa los campos principales correctamente"
                        }

                        Toast.makeText(
                            context,
                            textoError,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar y Descargar Excel")
            }

            // Tarjeta de confirmación enviada
            if (mensajeEnviado) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "¡Formulario enviado con éxito!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Text(
                            text = "Gracias $nombre, nos pondremos en contacto a la brevedad.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "📄 Archivo Excel generado y descargado en:",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Text(
                            text = rutaArchivoExcel,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        resultadoUltimoExcel?.let { res ->
                            if (res.uri != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                // Botón para abrir archivo
                                OutlinedButton(
                                    onClick = {
                                        GeneradorExcel.abrirArchivoExcel(context, res)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Abrir archivo Excel")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFormularioScreen() {
    FormularioScreen()
}