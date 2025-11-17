package com.levelupgamer.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levelupgamer.app.data.CartRepository
import com.levelupgamer.app.data.ProductRepository
import com.levelupgamer.app.data.SpringRepository
import com.levelupgamer.app.model.Product
import com.levelupgamer.app.model.SpringProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SpringUiState(
    val products: List<SpringProduct> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null // Para mostrar "Agregado al carrito"
)

class SpringViewModel(
    private val repository: SpringRepository,
    private val productRepository: ProductRepository, // <-- Nuevo
    private val cartRepository: CartRepository        // <-- Nuevo
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpringUiState())
    val uiState: StateFlow<SpringUiState> = _uiState.asStateFlow()

    init {
        fetchBackendProducts()
    }

    fun fetchBackendProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val items = repository.getProductsFromBackend()
                _uiState.update { it.copy(isLoading = false, products = items) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Error: ${e.message}")
                }
            }
        }
    }

    // --- FUNCIÓN PARA COMPRAR ---
    fun addToCart(springProduct: SpringProduct) {
        viewModelScope.launch {
            try {
                // 1. Convertimos el producto de Spring (Internet) a producto Local (Room)
                // Usamos "SP-" + id para crear un código único
                val localProduct = Product(
                    codigo = "SP-${springProduct.id}",
                    categoria = "Ofertas Online",
                    nombre = springProduct.name,
                    precio = springProduct.price
                )

                // 2. Lo guardamos en la base de datos local (necesario para el carrito)
                productRepository.saveProduct(localProduct)

                // 3. Lo agregamos al carrito
                cartRepository.addProductToCart(localProduct.codigo)

                // 4. Avisamos a la UI
                _uiState.update { it.copy(message = "¡${springProduct.name} agregado al carrito!") }

                // Limpiar mensaje después de un momento (opcional)
                // delay(2000)
                // _uiState.update { it.copy(message = null) }

            } catch (e: Exception) {
                _uiState.update { it.copy(error = "No se pudo agregar: ${e.message}") }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}