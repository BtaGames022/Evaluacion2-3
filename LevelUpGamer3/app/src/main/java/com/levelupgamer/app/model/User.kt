package com.levelupgamer.app.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Define la tabla 'users' en la base de datos.
 * El 'índice' asegura que no puedan existir dos usuarios con el mismo email.
 *
 */
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)] // El email debe ser único
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,
    val email: String,
    val passwordHash: String, // ¡Importante! Nunca guardes contraseñas en texto plano
    val isDuocUser: Boolean = false // Para el descuento del 20% [cite: 590]
)