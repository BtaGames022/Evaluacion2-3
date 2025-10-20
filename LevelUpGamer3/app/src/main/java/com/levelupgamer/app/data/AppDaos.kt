package com.levelupgamer.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.levelupgamer.app.model.Product
import com.levelupgamer.app.model.User
import kotlinx.coroutines.flow.Flow // Usamos Flow para que la UI se actualice sola

@Dao
interface ProductDao {
    /**
     * Inserta una lista de productos. Si ya existen, los reemplaza.
     * [cite: 675]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    /**
     * Obtiene todos los productos de la tabla, ordenados por categoría.
     * Devuelve un Flow, que permite a la UI observar cambios en tiempo real.
     */
    @Query("SELECT * FROM products ORDER BY categoria ASC")
    fun getAll(): Flow<List<Product>>

    /**
     * Obtiene productos filtrados por una categoría específica.
     */
    @Query("SELECT * FROM products WHERE categoria = :categoryName")
    fun getByCategory(categoryName: String): Flow<List<Product>>
}

@Dao
interface UserDao {
    /**
     * Inserta un nuevo usuario. Si el email ya existe (conflicto), aborta la operación.
     *
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User)

    /**
     * Busca un usuario por su email.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): User?
}