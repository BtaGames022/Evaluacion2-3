package com.levelupgamer.app.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.levelupgamer.app.R // (Puede marcarse en rojo, se soluciona al construir)
import java.io.File

/**
 * Helper para crear URIs temporales usando el FileProvider
 * (Necesario para la cámara - Guía 13)
 */
class FileProviderUtil(private val context: Context) {

    /**
     * Crea una URI temporal para que la cámara guarde la foto.
     */
    fun getTempImageUri(): Uri {
        // 1. Define la autoridad del FileProvider (debe coincidir con AndroidManifest.xml)
        val authority = "${context.packageName}.provider"

        // 2. Crea el archivo temporal
        val tempFile = File.createTempFile(
            "JPEG_${System.currentTimeMillis()}_",
            ".jpg",
            context.cacheDir // Guarda en el caché de la app
        ).apply {
            createNewFile()
            deleteOnExit() // Borra el archivo cuando la app se cierre
        }

        // 3. Obtiene la URI para ese archivo
        return FileProvider.getUriForFile(context, authority, tempFile)
    }
}