package com.example.corpaliv.ui.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

// Gestor de permisos para almacenamiento
@Composable
fun rememberGestorPermisosAlmacenamiento(
    onPermisosConcedidos: () -> Unit,
    onPermisosDenegados: () -> Unit = {}
): () -> Unit {
    val context = LocalContext.current

    // Contrato para pedir permisos
    val launcherPermisos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultados ->
        // Evalúa si fueron concedidos
        val concedido = if (resultados.isEmpty()) false else resultados.values.any { it }

        if (concedido) {
            Toast.makeText(context, "Permisos de almacenamiento concedidos", Toast.LENGTH_SHORT).show()
            onPermisosConcedidos()
        } else {
            Toast.makeText(context, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show()
            onPermisosDenegados()
        }
    }

    // Lógica ejecutable de permisos
    return {
        when {
            // Android 13 o superior
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                val permisosAComprobar = mutableListOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
                )

                // Android 14 opcional
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    permisosAComprobar.add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
                }

                val tieneAlgunPermiso = permisosAComprobar.any { permiso ->
                    ContextCompat.checkSelfPermission(context, permiso) == PackageManager.PERMISSION_GRANTED
                }

                if (tieneAlgunPermiso) {
                    onPermisosConcedidos()
                } else {
                    launcherPermisos.launch(permisosAComprobar.toTypedArray())
                }
            }

            // Android 10 a 12
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                val permisoLectura = Manifest.permission.READ_EXTERNAL_STORAGE
                val yaTienePermiso = ContextCompat.checkSelfPermission(
                    context,
                    permisoLectura
                ) == PackageManager.PERMISSION_GRANTED

                if (yaTienePermiso) {
                    onPermisosConcedidos()
                } else {
                    launcherPermisos.launch(arrayOf(permisoLectura))
                }
            }

            // Android 9 o inferior
            else -> {
                val permisoLectura = Manifest.permission.READ_EXTERNAL_STORAGE
                val permisoEscritura = Manifest.permission.WRITE_EXTERNAL_STORAGE

                val tieneLectura = ContextCompat.checkSelfPermission(
                    context,
                    permisoLectura
                ) == PackageManager.PERMISSION_GRANTED

                val tieneEscritura = ContextCompat.checkSelfPermission(
                    context,
                    permisoEscritura
                ) == PackageManager.PERMISSION_GRANTED

                if (tieneLectura && tieneEscritura) {
                    onPermisosConcedidos()
                } else {
                    launcherPermisos.launch(arrayOf(permisoLectura, permisoEscritura))
                }
            }
        }
    }
}