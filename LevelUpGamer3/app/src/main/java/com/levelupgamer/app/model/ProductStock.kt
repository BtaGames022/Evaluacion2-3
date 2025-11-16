package com.levelupgamer.app.model

import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Entidad para la disponibilidad.
 * Vincula un Producto con una región y una cantidad de stock.
 */
@Entity(
    tableName = "product_stock",
    primaryKeys = ["productId", "region"], // Clave compuesta
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["codigo"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductStock(
    val productId: String,
    val region: String, // Ej: "Metropolitana", "Valparaíso"
    val stock: Int
)