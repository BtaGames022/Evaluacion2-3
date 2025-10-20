package com.levelupgamer.app.model

/**
 * Representa el estado de la pantalla Home (Catálogo).
 */
data class HomeUiState(
    val productList: List<Product> = emptyList(),
    val isLoading: Boolean = true
    // (Podríamos añadir un 'filtroActual' más adelante)
)