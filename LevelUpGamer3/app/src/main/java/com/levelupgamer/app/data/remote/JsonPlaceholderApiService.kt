package com.levelupgamer.app.data.remote

// Importamos el modelo desde la ruta correcta
import com.levelupgamer.app.model.Post
import retrofit2.http.GET

/**
 * Interfaz que define los endpoints de la API de JSONPlaceholder
 */
interface JsonPlaceholderApiService {
    /**
     * Obtiene la lista de posts desde el endpoint "/posts"
     */
    @GET("posts")
    suspend fun getPosts(): List<Post>
}