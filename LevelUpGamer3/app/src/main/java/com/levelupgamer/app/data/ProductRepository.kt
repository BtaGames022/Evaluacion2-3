package com.levelupgamer.app.data

import com.levelupgamer.app.model.Product
import com.levelupgamer.app.model.ProductStock
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio actualizado para manejar Productos y su Stock.
 */
class ProductRepository(
    private val productDao: ProductDao,
    private val stockDao: ProductStockDao // <-- DAO DE STOCK AÑADIDO
) {

    fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAll()
    }

    // --- NUEVAS FUNCIONES ---

    /**
     * Obtiene un solo producto por su ID.
     */
    fun getProductById(productId: String): Flow<Product?> {
        return productDao.getProductById(productId)
    }

    /**
     * Obtiene la disponibilidad (stock) de un producto.
     */
    fun getStockForProduct(productId: String): Flow<List<ProductStock>> {
        return stockDao.getStockForProduct(productId)
    }
}