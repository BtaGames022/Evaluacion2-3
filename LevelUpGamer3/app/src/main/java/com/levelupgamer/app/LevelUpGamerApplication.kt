package com.levelupgamer.app

import android.app.Application
import com.levelupgamer.app.data.CartRepository
import com.levelupgamer.app.data.FileProviderUtil
import com.levelupgamer.app.data.LevelUpDatabase
import com.levelupgamer.app.data.ProductRepository
import com.levelupgamer.app.data.SessionDataStore
import com.levelupgamer.app.data.UserRepository

class LevelUpGamerApplication : Application() {

    val database: LevelUpDatabase by lazy {
        LevelUpDatabase.getDatabase(this)
    }

    // --- REPOSITORIOS ACTUALIZADOS ---

    val userRepository: UserRepository by lazy {
        UserRepository(database.userDao())
    }

    val productRepository: ProductRepository by lazy {
        // Ahora pasamos ambos DAOs
        ProductRepository(database.productDao(), database.stockDao())
    }

    val sessionDataStore: SessionDataStore by lazy {
        SessionDataStore(this)
    }

    val fileProviderUtil: FileProviderUtil by lazy {
        FileProviderUtil(this)
    }

    // --- REPOSITORIO DE CARRITO (NUEVO) ---
    val cartRepository: CartRepository by lazy {
        CartRepository(database.cartDao(), sessionDataStore, database.userDao())
    }
}