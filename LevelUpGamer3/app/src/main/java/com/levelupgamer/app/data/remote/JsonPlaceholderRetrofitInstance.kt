package com.levelupgamer.app.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton que provee una instancia de Retrofit configurada
 */
object JsonPlaceholderRetrofitInstance {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    /**
     * Instancia 'lazy' de la ApiService.
     * Se crea solo una vez cuando se accede por primera vez.
     */
    val api: JsonPlaceholderApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JsonPlaceholderApiService::class.java)
    }
}