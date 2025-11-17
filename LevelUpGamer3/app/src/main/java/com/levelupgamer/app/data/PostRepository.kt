package com.levelupgamer.app.data

// Importamos el modelo desde la ruta correcta
import com.levelupgamer.app.model.Post
// Importamos la instancia de Retrofit desde su nueva ubicación
import com.levelupgamer.app.data.remote.JsonPlaceholderRetrofitInstance

/**
 * Repositorio para obtener los Posts desde la API remota
 */
class PostRepository {
    // Apuntamos al servicio de la instancia de Retrofit
    private val apiService = JsonPlaceholderRetrofitInstance.api

    /**
     * Obtiene la lista de posts desde la API
     */
    suspend fun getPosts(): List<Post> {
        return apiService.getPosts()
    }
}