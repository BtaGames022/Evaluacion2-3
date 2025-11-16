package com.levelupgamer.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.levelupgamer.app.LevelUpGamerApplication
import com.levelupgamer.app.data.CartRepository
import com.levelupgamer.app.data.ProductRepository
import com.levelupgamer.app.model.Product
import com.levelupgamer.app.model.ProductStock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado para la pantalla de Detalle de Producto.
 */
data class ProductDetailUiState(
    val product: Product? = null,
    val stockByRegion: List<ProductStock> = emptyList(),
    val isLoading: Boolean = true
)

class ProductDetailViewModel(
    application: Application,
    productId: String // Recibe el ID del producto
) : AndroidViewModel(application) {

    private val productRepository: ProductRepository =
        (application as LevelUpGamerApplication).productRepository
    private val cartRepository: CartRepository =
        (application as LevelUpGamerApplication).cartRepository

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        loadProductDetails(productId)
    }

    private fun loadProductDetails(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Combine (junta) dos Flows: el del producto y el de su stock
            val productFlow = productRepository.getProductById(productId)
            val stockFlow = productRepository.getStockForProduct(productId)

            productFlow.combine(stockFlow) { product, stockList ->
                ProductDetailUiState(
                    product = product,
                    stockByRegion = stockList,
                    isLoading = false
                )
            }.collect { combinedState ->
                _uiState.value = combinedState
            }
        }
    }

    /**
     * Añade el producto actual al carrito.
     */
    fun addToCart(productId: String) {
        viewModelScope.launch {
            cartRepository.addProductToCart(productId)
            // TODO: Mostrar un feedback (Snackbar)
        }
    }
}