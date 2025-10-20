package com.levelupgamer.app.model

/**
 * Representa el estado del formulario de registro (basado en Guía 11).
 * Contiene el valor de cada campo y su mensaje de error.
 */
data class RegistrationUiState(
    // Valores de los campos
    val nombre: String = "",
    val email: String = "",
    val edad: String = "", // Lo manejamos como String para el TextField
    val password: String = "",
    val confirmPassword: String = "",

    // Mensajes de error
    val nombreError: String? = null,
    val emailError: String? = null,
    val edadError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    // Estado de la UI
    val isLoading: Boolean = false, // Para mostrar el CircularProgressIndicator
    val generalError: String? = null // Para errores de registro (ej. "email ya existe")
)