package com.levelupgamer.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.levelupgamer.app.model.CartItem
import com.levelupgamer.app.model.Product
import com.levelupgamer.app.model.ProductStock
import com.levelupgamer.app.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Product::class, CartItem::class, ProductStock::class],
    // ¡IMPORTANTE! Sube la versión de tu base de datos (ej. si era 2, ahora es 3)
    version = 3,
    exportSchema = false
)
abstract class LevelUpDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun stockDao(): ProductStockDao

    companion object {
        @Volatile
        private var INSTANCE: LevelUpDatabase? = null

        fun getDatabase(context: Context): LevelUpDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LevelUpDatabase::class.java,
                    "levelup_database"
                )
                    // ¡IMPORTANTE! Esto borrará los datos de tus usuarios al actualizar la app.
                    // Es necesario porque cambiamos la estructura de la base de datos.
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    // Precargar productos
                    val productDao = database.productDao()
                    productDao.insertAll(getInitialProducts())

                    // Precargar stock
                    val stockDao = database.stockDao()
                    // --- ¡¡PETICIÓN 1 IMPLEMENTADA!! ---
                    // Se inserta la lista de stock completa con todas las regiones.
                    stockDao.insertAll(getInitialStock())
                }
            }
        }

        fun getInitialProducts(): List<Product> {
            // (Esta función es la misma que tenías antes)
            return listOf(
                Product("JM001", "Juegos de Mesa", "Catan", 29990.0),
                Product("JM002", "Juegos de Mesa", "Carcassonne", 24990.0),
                Product("AC001", "Accesorios", "Controlador Xbox Series X", 59990.0),
                Product("AC002", "Accesorios", "Auriculares HyperX Cloud II", 79990.0),
                Product("CO001", "Consolas", "PlayStation 5", 549990.0),
                Product("CG001", "Computadores Gamers", "PC Gamer ASUS ROG Strix", 1299990.0),
                Product("SG001", "Sillas Gamers", "Silla Gamer Secretlab Titan", 349990.0),
                Product("MS001", "Mouse", "Mouse Gamer Logitech G502", 49990.0),
                Product("MP001", "Mousepad", "Mousepad Razer Goliathus", 29990.0),
                Product("PP001", "Poleras", "Polera Gamer Personalizada", 14990.0)
            )
        }

        // --- ¡¡FUNCIÓN DE STOCK ACTUALIZADA Y COMPLETA!! ---
        // Implementa tu petición de stock regional variado.
        fun getInitialStock(): List<ProductStock> {
            val regiones = listOf(
                "Arica y Parinacota", "Tarapacá", "Antofagasta", "Atacama", "Coquimbo",
                "Valparaíso", "Metropolitana", "O'Higgins", "Maule", "Ñuble",
                "Biobío", "Araucanía", "Los Ríos", "Los Lagos", "Aysén", "Magallanes"
            )
            val productIds = getInitialProducts().map { it.codigo }
            val stockList = mutableListOf<ProductStock>()

            productIds.forEach { productId ->
                when (productId) {
                    // PlayStation 5 (Alta demanda, poco stock)
                    "CO001" -> {
                        stockList.add(ProductStock(productId, "Metropolitana", 5))
                        stockList.add(ProductStock(productId, "Valparaíso", 2))
                        stockList.add(ProductStock(productId, "Biobío", 1))
                        // Resto de regiones "Fuera de Stock"
                        regiones.filter { it !in listOf("Metropolitana", "Valparaíso", "Biobío") }
                            .forEach { stockList.add(ProductStock(productId, it, 0)) }
                    }
                    // Controlador Xbox (Stock alto)
                    "AC001" -> {
                        regiones.forEach { region ->
                            val stock = if (region == "Metropolitana") 50 else (10..25).random()
                            stockList.add(ProductStock(productId, region, stock))
                        }
                    }
                    // Catan (Stock medio, aleatorio)
                    "JM001" -> {
                        regiones.forEach { region ->
                            val stock = (0..15).random() // Algunos 0 (Fuera de Stock)
                            stockList.add(ProductStock(productId, region, stock))
                        }
                    }
                    // Silla Gamer (Stock solo en RM, Valpo y Biobío)
                    "SG001" -> {
                        stockList.add(ProductStock(productId, "Metropolitana", 20))
                        stockList.add(ProductStock(productId, "Valparaíso", 5))
                        stockList.add(ProductStock(productId, "Biobío", 3))
                        regiones.filter { it !in listOf("Metropolitana", "Valparaíso", "Biobío") }
                            .forEach { stockList.add(ProductStock(productId, it, 0)) }
                    }
                    // Resto de productos
                    else -> {
                        regiones.forEach { region ->
                            // Stock aleatorio, 1 de cada 5 veces estará "Fuera de Stock"
                            val stock = if ((1..5).random() == 1) 0 else (5..30).random()
                            stockList.add(ProductStock(productId, region, stock))
                        }
                    }
                }
            }
            return stockList
        }
    }
}