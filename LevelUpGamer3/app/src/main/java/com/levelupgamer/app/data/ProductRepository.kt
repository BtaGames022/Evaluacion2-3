package com.levelupgamer.app.data

import com.levelupgamer.app.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que maneja las operaciones de datos para los Productos.
 */
class ProductRepository(private val productDao: ProductDao) {

    /**
     * Obtiene un Flow con todos los productos.
     * La UI observará este Flow para actualizarse automáticamente
     * si los datos cambian.
     */
    fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAll()
    }

    /**
     * Obtiene un Flow de productos filtrados por categoría.
     */
    fun getProductsByCategory(category: String): Flow<List<Product>> {
        return productDao.getByCategory(category)
    }
}