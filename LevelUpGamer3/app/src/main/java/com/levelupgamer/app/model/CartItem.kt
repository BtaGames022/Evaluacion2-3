package com.levelupgamer.app.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Entidad que representa un artículo en el carrito de compras.
 * Vincula un Usuario (userId) con un Producto (productId).
 */
@Entity(
    tableName = "cart_items",
    primaryKeys = ["userId", "productId"], // Un usuario solo puede tener una fila por producto
    indices = [Index(value = ["userId"]), Index(value = ["productId"])],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // Si se borra el usuario, se borra su carrito
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["codigo"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE // Si se saca el producto, se saca del carrito
        )
    ]
)
data class CartItem(
    val userId: Int,
    val productId: String, // Coincide con Product.codigo
    var quantity: Int
)