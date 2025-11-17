package com.levelupgamer.app.data

import com.levelupgamer.app.model.SpringProduct

interface SpringRepository {
    suspend fun getProductsFromBackend(): List<SpringProduct>
}