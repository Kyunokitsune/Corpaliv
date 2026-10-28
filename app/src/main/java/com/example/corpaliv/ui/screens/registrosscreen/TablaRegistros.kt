package com.example.corpaliv.ui.screens.registrosscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.corpaliv.viewmodel.MainViewModel

// Contenido principal de registros
@Composable
fun TablaRegistrosContent(viewModel: MainViewModel) {
    // Obtiene lista del ViewModel
    val listaContactos by viewModel.listaContactos.collectAsState()

    // Contenedor principal de datos
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Muestra mensaje si vacío
        if (listaContactos.isEmpty()) {
            // Tarjeta de información vacía
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Aún no hay envíos registrados. Completa el formulario en la pestaña de Contacto para ver los datos aquí.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            // Muestra contador total registros
            Text(
                text = "Total de envíos: ${listaContactos.size}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Contenedor con scroll horizontal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                // Encabezado de la tabla
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(8.dp)
                ) {
                    Text("Nombre", modifier = Modifier.width(140.dp), fontWeight = FontWeight.Bold)
                    Text("Correo", modifier = Modifier.width(180.dp), fontWeight = FontWeight.Bold)
                    Text("Teléfono", modifier = Modifier.width(120.dp), fontWeight = FontWeight.Bold)
                    Text("Comentarios", modifier = Modifier.width(220.dp), fontWeight = FontWeight.Bold)
                    Text("Fecha", modifier = Modifier.width(140.dp), fontWeight = FontWeight.Bold)
                }

                // Divisor horizontal de fila
                HorizontalDivider()

                // Lista desplazable de registros
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(listaContactos) { registro ->
                        // Fila individual de datos
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 8.dp)
                        ) {
                            Text(registro.nombre, modifier = Modifier.width(140.dp))
                            Text(registro.correo, modifier = Modifier.width(180.dp))
                            Text(registro.numeroContacto, modifier = Modifier.width(120.dp))
                            Text(registro.comentario, modifier = Modifier.width(220.dp))
                            Text(registro.fecha, modifier = Modifier.width(140.dp))
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}