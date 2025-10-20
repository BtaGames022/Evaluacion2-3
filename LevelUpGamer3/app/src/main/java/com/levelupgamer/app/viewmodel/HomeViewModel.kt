package com.levelupgamer.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.levelupgamer.app.LevelUpGamerApplication
import com.levelupgamer.app.data.ProductRepository
import com.levelupgamer.app.model.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Obtenemos el repositorio
    private val productRepository: ProductRepository =
        (application as LevelUpGamerApplication).productRepository

    // --- Estado Interno y Público ---
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // init se ejecuta cuando el ViewModel es creado
    init {
        loadProducts()
    }

    /**
     * Carga la lista de productos desde el repositorio (Room).
     */
    private fun loadProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts()
                .onStart {
                    // Muestra la carga al inicio
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch { exception ->
                    // Maneja un posible error
                    _uiState.update { it.copy(isLoading = false) }
                    // TODO: Mostrar error en la UI
                }
                .collect { productList ->
                    // Éxito: actualiza la lista y oculta la carga
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            productList = productList
                        )
                    }
                }
        }
    }
}