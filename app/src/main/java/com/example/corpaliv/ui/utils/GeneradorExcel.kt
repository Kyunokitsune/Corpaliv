package com.example.corpaliv.ui.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Genera archivos CSV para Excel
object GeneradorExcel {

    // Guarda el formulario en CSV
    fun guardarFormularioEnExcel(
        context: Context,
        nombre: String,
        correo: String,
        numeroContacto: String,
        comentario: String,
    ): ResultadoExcel {
        // Formato fecha para archivo
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fechaFormateada = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        val nombreArchivo = "Formulario_Contacto_$timeStamp.csv"

        // Genera contenido CSV con BOM
        val contenidoCsv = buildString {
            append("\uFEFF")
            append("Nombre completo;Correo electrónico;Número de contacto;Comentarios;Fecha de envío\n")
            append("\"${limpiarTexto(nombre)}\";")
            append("\"${limpiarTexto(correo)}\";")
            append("\"${limpiarTexto(numeroContacto)}\";")
            append("\"${limpiarTexto(comentario)}\";")
            append("\"$fechaFormateada\"\n")
        }

        return try {
            // Android 10 o superior
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
                    put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                    ?: return ResultadoExcel(exito = false, mensajeError = "No se pudo crear el archivo en MediaStore")

                resolver.openOutputStream(uri)?.use { stream ->
                    stream.write(contenidoCsv.toByteArray(Charsets.UTF_8))
                }

                val rutaPublica = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath}/$nombreArchivo"
                ResultadoExcel(
                    exito = true,
                    rutaArchivo = rutaPublica,
                    uri = uri,
                    nombreArchivo = nombreArchivo
                )
            } else {
                // Android 9 o inferior
                val directorioDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!directorioDescargas.exists()) {
                    directorioDescargas.mkdirs()
                }
                val archivo = File(directorioDescargas, nombreArchivo)
                FileOutputStream(archivo).use { stream ->
                    stream.write(contenidoCsv.toByteArray(Charsets.UTF_8))
                }

                val contentUri = try {
                    FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        archivo
                    )
                } catch (_: Exception) {
                    Uri.fromFile(archivo)
                }

                ResultadoExcel(
                    exito = true,
                    rutaArchivo = archivo.absolutePath,
                    uri = contentUri,
                    nombreArchivo = nombreArchivo
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResultadoExcel(
                exito = false,
                mensajeError = e.localizedMessage ?: "Error desconocido al crear el archivo Excel"
            )
        }
    }

    // Abre el archivo en sistema
    fun abrirArchivoExcel(context: Context, resultado: ResultadoExcel) {
        val uri = resultado.uri ?: return
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "text/csv")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(context, "No se encontró una aplicación para abrir el archivo CSV/Excel", Toast.LENGTH_SHORT).show()
        }
    }

    // Limpia comillas para CSV
    private fun limpiarTexto(texto: String): String {
        return texto.replace("\"", "\"\"")
    }
}

// Resultado del proceso Excel
data class ResultadoExcel(
    val exito: Boolean,
    val rutaArchivo: String = "",
    val uri: Uri? = null,
    val nombreArchivo: String = "",
    val mensajeError: String = ""
)