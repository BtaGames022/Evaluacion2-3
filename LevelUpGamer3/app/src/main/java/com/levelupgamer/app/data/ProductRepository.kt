package com.levelupgamer.app.data

import com.levelupgamer.app.model.Product
import com.levelupgamer.app.model.ProductStock
import kotlinx.coroutines.flow.Flow

// 1. La Interfaz
interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    fun getProductById(productId: String): Flow<Product?>
    fun getStockForProduct(productId: String): Flow<List<ProductStock>>
    // --- NUEVA FUNCIÓN ---
    suspend fun saveProduct(product: Product)
}

// 2. La Implementación
class ProductRepositoryImpl(
    private val productDao: ProductDao,
    private val stockDao: ProductStockDao
) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAll()
    }

    override fun getProductById(productId: String): Flow<Product?> {
        return productDao.getProductById(productId)
    }

    override fun getStockForProduct(productId: String): Flow<List<ProductStock>> {
        return stockDao.getStockForProduct(productId)
    }

    // --- IMPLEMENTACIÓN NUEVA ---
    override suspend fun saveProduct(product: Product) {
        // Usamos insertAll porque tu DAO tiene ese método, y pasamos una lista de 1 elemento
        productDao.insertAll(listOf(product))
    }
}