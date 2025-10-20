package com.levelupgamer.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.levelupgamer.app.model.Product
import com.levelupgamer.app.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Define la base de datos principal de la app usando Room.
 * [cite: 91, 1954]
 */
@Database(entities = [User::class, Product::class], version = 1, exportSchema = false)
abstract class LevelUpDatabase : RoomDatabase() {

    // Expone los DAOs para que el resto de la app los use
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao

    companion object {
        // 'Volatile' asegura que la instancia sea siempre la más actualizada
        @Volatile
        private var INSTANCE: LevelUpDatabase? = null

        fun getDatabase(context: Context): LevelUpDatabase {
            // Retorna la instancia si ya existe, si no, la crea de forma segura
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LevelUpDatabase::class.java,
                    "levelup_database"
                )
                    .addCallback(DatabaseCallback()) // Añade la precarga de datos
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Callback para precargar la base de datos con los productos
     * la primera vez que se crea.
     * [cite: 675]
     */
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                // Usamos corrutinas para hacer la inserción en un hilo secundario
                CoroutineScope(Dispatchers.IO).launch {
                    val productDao = database.productDao()
                    productDao.insertAll(getInitialProducts())
                }
            }
        }

        /**
         * Datos iniciales de productos tomados del caso.
         * [cite: 675, 681, 683, 685, 686, 687, 689, 690, 691, 692, 693]
         */
        fun getInitialProducts(): List<Product> {
            return listOf(
                Product("JM001", "Juegos de Mesa", "Catan", 29990.0),
                Product("JM002", "Juegos de Mesa", "Carcassonne", 24990.0),
                Product("AC001", "Accesorios", "Controlador Inalámbrico Xbox Series X", 59990.0),
                Product("AC002", "Accesorios", "Auriculares Gamer HyperX Cloud II", 79990.0),
                Product("CO001", "Consolas", "PlayStation 5", 549990.0),
                Product("CG001", "Computadores Gamers", "PC Gamer ASUS ROG Strix", 1299990.0),
                Product("SG001", "Sillas Gamers", "Silla Gamer Secretlab Titan", 349990.0),
                Product("MS001", "Mouse", "Mouse Gamer Logitech G502 HERO", 49990.0),
                Product("MP001", "Mousepad", "Mousepad Razer Goliathus Extended Chroma", 29990.0),
                Product("PP001", "Poleras Personalizadas", "Polera Gamer Personalizada 'Level-Up'", 14990.0)
            )
        }
    }
}