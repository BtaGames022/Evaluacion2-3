package com.levelupgamer.app.model

/**
 * Representa el estado del formulario de Login (basado en Guía 11).
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",

    val emailError: String? = null,
    val passwordError: String? = null,

    val isLoading: Boolean = false,
    val loginError: String? = null // Error general (ej. "Credenciales incorrectas")
)