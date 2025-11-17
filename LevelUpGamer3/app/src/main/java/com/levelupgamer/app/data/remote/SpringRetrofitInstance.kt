package com.levelupgamer.app.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpringRetrofitInstance {
    // 10.0.2.2 es localhost para el Emulador.
    // Si usas celular físico, pon la IP de tu PC (ej. http://192.168.1.50:8080/)
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val api: SpringApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpringApiService::class.java)
    }
}