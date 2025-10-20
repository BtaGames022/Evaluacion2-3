package com.levelupgamer.app

import android.app.Application
import com.levelupgamer.app.data.LevelUpDatabase
import com.levelupgamer.app.data.ProductRepository
import com.levelupgamer.app.data.UserRepository
import com.levelupgamer.app.data.SessionDataStore
import com.levelupgamer.app.data.FileProviderUtil

class LevelUpGamerApplication : Application() {

    val database: LevelUpDatabase by lazy {
        LevelUpDatabase.getDatabase(this)
    }
    val userRepository: UserRepository by lazy {
        UserRepository(database.userDao())
    }
    val sessionDataStore: SessionDataStore by lazy {
        SessionDataStore(this)
    }
    val productRepository: ProductRepository by lazy {
        ProductRepository(database.productDao())
    }
    val fileProviderUtil: FileProviderUtil by lazy {
        FileProviderUtil(this)
    }
}