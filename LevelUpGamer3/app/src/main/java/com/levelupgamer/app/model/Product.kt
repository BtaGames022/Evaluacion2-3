package com.levelupgamer.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Define la tabla 'products' en la base de datos Room (SQLite).
 * [cite: 661, 675]
 */
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = false) // Usamos el código del caso [cite: 675]
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val precio: Double // Usamos Double para el precio
)