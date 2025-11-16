package com.levelupgamer.app.model

import androidx.room.Embedded

/**
 * POJO (Plain Old Java Object) para combinar un Producto
 * con la cantidad del carrito en una consulta JOIN.
 */
data class CartProduct(
    @Embedded // Trae todas las columnas de Product (codigo, nombre, precio, etc.)
    val product: Product,
    val quantity: Int
)