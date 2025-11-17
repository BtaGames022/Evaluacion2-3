package com.levelupgamer.app.model

/**
 * Representa un Post de la API JSONPlaceholder
 */
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)