package com.levelupgamer.app.model

import android.net.Uri

/**
 * Representa el estado de la pantalla de Perfil.
 *
 * Contiene los datos del usuario y la URI de su imagen.
 */
data class ProfileUiState(
    val isLoading: Boolean = true,
    val nombre: String = "",
    val email: String = "",
    val esUsuarioDuoc: Boolean = false,

    // --- CAMBIO REALIZADO (Guía 13) ---
    val profileImageUri: Uri? = null // Almacena la URI de la foto seleccionada
)