package com.levelupgamer.app.data.remote

import com.levelupgamer.app.model.SpringProduct
import retrofit2.http.GET

interface SpringApiService {
    @GET("products")
    suspend fun getSpringProducts(): List<SpringProduct>
}