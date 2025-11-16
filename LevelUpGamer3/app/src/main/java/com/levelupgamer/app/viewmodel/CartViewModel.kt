package com.levelupgamer.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.levelupgamer.app.LevelUpGamerApplication
import com.levelupgamer.app.data.CartRepository
import com.levelupgamer.app.model.CartProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado para la pantalla del Carrito.
 */
data class CartUiState(
    val cartProducts: List<CartProduct> = emptyList(),
    val totalAmount: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // --- ¡¡PETICIÓN 2 IMPLEMENTADA (LÓGICA)!! ---
    val showCheckoutSuccessDialog: Boolean = false // <-- NUEVO ESTADO PARA EL DIÁLOGO
)

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val cartRepository: CartRepository =
        (application as LevelUpGamerApplication).cartRepository

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        loadCart()
    }

    private fun loadCart() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val cartFlow = cartRepository.getCartContents()
                if (cartFlow == null) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Usuario no encontrado") }
                    return@launch
                }

                cartFlow
                    .catch { e ->
                        _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                    }
                    .collect { products ->
                        val total = products.sumOf { it.product.precio * it.quantity }
                        _uiState.update {
                            it.copy(
                                cartProducts = products,
                                totalAmount = total,
                                isLoading = false
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    // ... (increase, decrease, removeItem sin cambios) ...
    fun increaseQuantity(productId: String) {
        viewModelScope.launch {
            cartRepository.increaseItemQuantity(productId)
        }
    }
    fun decreaseQuantity(productId: String) {
        viewModelScope.launch {
            cartRepository.decreaseItemQuantity(productId)
        }
    }
    fun removeItem(productId: String) {
        viewModelScope.launch {
            cartRepository.removeItemFromCart(productId)
        }
    }
    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearUserCart()
        }
    }

    // --- ¡¡PETICIÓN 2 IMPLEMENTADA (LÓGICA)!! ---

    /**
     * Se llama al presionar "Pagar".
     * Vacía el carrito y muestra el diálogo de éxito.
     */
    fun onCheckoutClicked() {
        viewModelScope.launch {
            try {
                if (_uiState.value.cartProducts.isEmpty()) return@launch // No pagar si el carrito está vacío

                cartRepository.clearUserCart()
                // El 'collect' en loadCart() refrescará la lista a vacío.
                // Ahora mostramos el diálogo:
                _uiState.update { it.copy(showCheckoutSuccessDialog = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al procesar el pago: ${e.message}") }
            }
        }
    }

    /**
     * Oculta el diálogo de éxito de la compra.
     */
    fun dismissCheckoutDialog() {
        _uiState.update { it.copy(showCheckoutSuccessDialog = false) }
    }
}