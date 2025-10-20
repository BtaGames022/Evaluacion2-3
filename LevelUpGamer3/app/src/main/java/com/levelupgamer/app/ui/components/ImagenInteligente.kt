package com.levelupgamer.app.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

/**
 * Componente reutilizable para mostrar la foto de perfil (Guía 13).
 * Muestra la imagen desde una URI, o un ícono de perfil por defecto.
 */
@Composable
fun ImagenInteligente(
    modifier: Modifier = Modifier,
    imageUri: Uri?
) {
    if (imageUri == null) {
        // --- Estado por defecto (sin imagen) ---
        Box(
            modifier = modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Ícono de Perfil",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    } else {
        // --- Estado con imagen ---
        Image(
            painter = rememberAsyncImagePainter(model = imageUri),
            contentDescription = "Foto de Perfil",
            modifier = modifier
                .size(150.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop // Asegura que la imagen llene el círculo
        )
    }
}