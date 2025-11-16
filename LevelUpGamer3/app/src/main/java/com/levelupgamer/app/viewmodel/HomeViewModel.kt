package com.levelupgamer.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.levelupgamer.app.LevelUpGamerApplication
import com.levelupgamer.app.data.CartRepository
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

    private val productRepository: ProductRepository =
        (application as LevelUpGamerApplication).productRepository

    // Inyectar el CartRepository
    private val cartRepository: CartRepository =
        (application as LevelUpGamerApplication).cartRepository

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    // --- ¡¡ESTA ES LA FUNCIÓN QUE FALTABA!! ---
    /**
     * Añade un producto al carrito del usuario actual.
     */
    fun addToCart(productId: String) {
        viewModelScope.launch {
            try {
                cartRepository.addProductToCart(productId)
                // TODO: Mostrar un feedback (ej. un Snackbar) de "Producto añadido"
            } catch (e: Exception) {
                // TODO: Manejar error
            }
        }
    }
    // --- FIN DE LA FUNCIÓN ---

    private fun loadProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts()
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch { exception ->
                    _uiState.update { it.copy(isLoading = false) }
                }
                .collect { productList ->
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