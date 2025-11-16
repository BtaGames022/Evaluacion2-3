package com.levelupgamer.app.data

import com.levelupgamer.app.model.CartItem
import com.levelupgamer.app.model.CartProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Repositorio para manejar todas las operaciones del Carrito.
 */
class CartRepository(
    private val cartDao: CartDao,
    private val sessionDataStore: SessionDataStore,
    private val userDao: UserDao
) {

    /**
     * Helper privado para obtener el ID del usuario actual de forma segura.
     */
    private suspend fun getCurrentUserId(): Int? {
        val email = sessionDataStore.userEmailFlow.first() ?: return null
        return userDao.getByEmail(email)?.id
    }

    /**
     * Obtiene el contenido del carrito (Productos + Cantidad) para el usuario logueado.
     */
    suspend fun getCartContents(): Flow<List<CartProduct>>? {
        val userId = getCurrentUserId() ?: return null
        return cartDao.getCartContents(userId)
    }

    /**
     * Añade un producto al carrito. Si ya existe, suma 1 a la cantidad.
     */
    suspend fun addProductToCart(productId: String) {
        val userId = getCurrentUserId() ?: return // Salir si no hay usuario

        val existingItem = cartDao.getCartItem(userId, productId)
        if (existingItem == null) {
            // No existe, lo inserta con cantidad 1
            cartDao.insertItem(CartItem(userId = userId, productId = productId, quantity = 1))
        } else {
            // Ya existe, actualiza la cantidad
            val newQuantity = existingItem.quantity + 1
            cartDao.updateQuantity(userId, productId, newQuantity)
        }
    }

    /**
     * Incrementa la cantidad de un item.
     */
    suspend fun increaseItemQuantity(productId: String) {
        val userId = getCurrentUserId() ?: return
        val existingItem = cartDao.getCartItem(userId, productId) ?: return
        val newQuantity = existingItem.quantity + 1
        cartDao.updateQuantity(userId, productId, newQuantity)
    }

    /**
     * Disminuye la cantidad de un item. Si llega a 0, lo borra.
     */
    suspend fun decreaseItemQuantity(productId: String) {
        val userId = getCurrentUserId() ?: return
        val existingItem = cartDao.getCartItem(userId, productId) ?: return

        if (existingItem.quantity > 1) {
            val newQuantity = existingItem.quantity - 1
            cartDao.updateQuantity(userId, productId, newQuantity)
        } else {
            // Si la cantidad es 1 o menos, borrar el item
            cartDao.deleteItem(userId, productId)
        }
    }

    /**
     * Elimina un producto del carrito, sin importar la cantidad.
     */
    suspend fun removeItemFromCart(productId: String) {
        val userId = getCurrentUserId() ?: return
        cartDao.deleteItem(userId, productId)
    }

    /**
     * Vacía el carrito del usuario.
     */
    suspend fun clearUserCart() {
        val userId = getCurrentUserId() ?: return
        cartDao.clearCart(userId)
    }
}