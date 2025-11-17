package com.levelupgamer.app.data

import com.levelupgamer.app.data.remote.SpringRetrofitInstance
import com.levelupgamer.app.model.SpringProduct

class SpringRepositoryImpl : SpringRepository {
    private val apiService = SpringRetrofitInstance.api

    override suspend fun getProductsFromBackend(): List<SpringProduct> {
        return apiService.getSpringProducts()
    }
}