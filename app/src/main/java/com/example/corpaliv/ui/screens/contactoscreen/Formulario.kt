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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen() {
    val context = LocalContext.current

    // 1. rememberSaveable evita que la información se borre al girar o cambiar tamaño
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var numeroContacto by rememberSaveable { mutableStateOf("") }
    var comentario by rememberSaveable { mutableStateOf("") }

    // Estado para controlar la visualización del mensaje de éxito
    var mensajeEnviado by rememberSaveable { mutableStateOf(false) }

    val limiteComentario = 1000
    val limitenumtelefonico = 9


    // Listado de dominios permitidos
    val terminacionesPermitidas = listOf("@gmail.com", "@gmail.cl", "@duocuc.cl", "@duoc.cl")
    val esCorreoValido = correo.isEmpty() || terminacionesPermitidas.any { correo.lowercase().endsWith(it) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Formulario de Contacto") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {


            // 1. Campo Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )


            // 2. Campo Correo con validación de terminación
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


            // 3. Campo Número de contacto (Solo permite números)
            OutlinedTextField(
                value = numeroContacto,
                onValueChange = { nuevoTexto ->
                    // Evalúa que solo sean números Y que no supere el límite
                    if (nuevoTexto.all { it.isDigit() } && nuevoTexto.length <= limitenumtelefonico) {
                        numeroContacto = nuevoTexto
                    }
                },
                label = { Text("Número de contacto Ej: 9 1234 5678") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                //  muestra cuántos números ha ingresado
                supportingText = {
                    Text(
                        text = "${numeroContacto.length} / $limitenumtelefonico",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            )


            // 4. Campo Comentarios (Máximo 1000 caracteres)
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


            // Botón de Enviar
            Button(
                onClick = {
                    val correoEsCorrecto = correo.isNotBlank() && terminacionesPermitidas.any { correo.lowercase().endsWith(it) }

                    if (nombre.isNotBlank() && correoEsCorrecto && numeroContacto.isNotBlank()) {
                        mensajeEnviado = true

                        val textoNotificacion = "Formulario enviado, nos pondremos en contacto a la brevedad con usted $nombre"

                        Toast.makeText(
                            context,
                            textoNotificacion,
                            Toast.LENGTH_LONG
                        ).show()
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
                Text("Enviar")
            }


            // Tarjeta de confirmación con el nombre dinámico
            if (mensajeEnviado) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "Formulario enviado nos pondremos en contacto a la brevedad con usted $nombre",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
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