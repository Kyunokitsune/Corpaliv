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

// Gestor de permisos para almacenamiento.
// Evalúa la API del sistema para solicitar únicamente los permisos vigentes según la versión de Android.
// Ejecuta `onPermisosConcedidos` si se aceptan o no se requieren, y `onPermisosDenegados` si son rechazados.
@Composable
fun rememberGestorPermisosAlmacenamiento(
    onPermisosConcedidos: () -> Unit,
    onPermisosDenegados: () -> Unit = {}
): () -> Unit {
    val context = LocalContext.current

    // 1. Contrato de Compose para solicitar uno o varios permisos al sistema operativo
    val launcherPermisos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultados ->
        // Evalúa si al menos uno o todos los permisos enviados fueron concedidos
        val concedido = if (resultados.isEmpty()) false else resultados.values.any { it }

        if (concedido) {
            Toast.makeText(context, "Permisos de almacenamiento concedidos", Toast.LENGTH_SHORT).show()
            onPermisosConcedidos()
        } else {
            Toast.makeText(context, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show()
            onPermisosDenegados()
        }
    }

    // 2. Retorna la función ejecutable cuando se presione el botón deseado
    return {
        when {
            // Caso A: Android 13 (API 33) o superior
            // Solicitamos permisos de lectura de medios granulares (READ_MEDIA_IMAGES, READ_MEDIA_VIDEO, READ_MEDIA_AUDIO).
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                val permisosAComprobar = mutableListOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
                )

                // En Android 14 (API 34) agregamos el permiso opcional de selección parcial
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

            // Caso B: Android 10 a Android 12 (API 29 a API 32)
            // WRITE_EXTERNAL_STORAGE ya no es necesario por Scoped Storage.
            // Evaluamos y solicitamos READ_EXTERNAL_STORAGE.
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

            // Caso C: Android 9 (API 28) o inferior
            // Requiere tanto READ_EXTERNAL_STORAGE como WRITE_EXTERNAL_STORAGE.
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