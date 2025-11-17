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
    // --- CAMPOS DE TOTAL ACTUALIZADOS ---
    val subtotal: Double = 0.0,         // Total antes de descuentos
    val discount: Double = 0.0,         // El monto descontado
    val totalAmount: Double = 0.0,      // El total final a pagar
    val isDuocUser: Boolean = false,    // Para que la UI sepa si mostrar el descuento
    // --- FIN CAMPOS ACTUALIZADOS ---
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showCheckoutSuccessDialog: Boolean = false
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
                // --- LÓGICA DE DESCUENTO (INICIO) ---
                // 1. Obtener el usuario actual y su estado de Duoc
                val user = cartRepository.getCurrentUser()
                val isDuoc = user?.isDuocUser == true
                // Actualizamos el estado de la UI con esta información
                _uiState.update { it.copy(isDuocUser = isDuoc) }
                // --- LÓGICA DE DESCUENTO (FIN) ---


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
                        // --- LÓGICA DE DESCUENTO (APLICACIÓN) ---

                        // 1. Calcular el subtotal (total sin descuento)
                        val subtotal = products.sumOf { it.product.precio * it.quantity }

                        // 2. Obtener el estado de Duoc (que ya cargamos)
                        val isDuocUser = _uiState.value.isDuocUser

                        // 3. Calcular descuento y total
                        val discount = if (isDuocUser) subtotal * 0.20 else 0.0
                        val total = subtotal - discount
                        // --- FIN LÓGICA DE DESCUENTO ---

                        _uiState.update {
                            it.copy(
                                cartProducts = products,
                                // Guardamos los 3 valores
                                subtotal = subtotal,
                                discount = discount,
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

    // ... (increase, decrease, removeItem, clearCart sin cambios) ...
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

    // ... (onCheckoutClicked, dismissCheckoutDialog sin cambios) ...
    fun onCheckoutClicked() {
        viewModelScope.launch {
            try {
                if (_uiState.value.cartProducts.isEmpty()) return@launch

                cartRepository.clearUserCart()
                _uiState.update { it.copy(showCheckoutSuccessDialog = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al procesar el pago: ${e.message}") }
            }
        }
    }

    fun dismissCheckoutDialog() {
        _uiState.update { it.copy(showCheckoutSuccessDialog = false) }
    }
}