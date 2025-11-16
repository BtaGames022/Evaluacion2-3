package com.levelupgamer.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.levelupgamer.app.model.CartItem
import com.levelupgamer.app.model.CartProduct
import com.levelupgamer.app.model.Product
import com.levelupgamer.app.model.ProductStock
import com.levelupgamer.app.model.User
import kotlinx.coroutines.flow.Flow

// ... (ProductDao y UserDao se mantienen igual que antes) ...
@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Query("SELECT * FROM products ORDER BY categoria ASC")
    fun getAll(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE codigo = :productId LIMIT 1")
    fun getProductById(productId: String): Flow<Product?>
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User?
}

// --- DAO DEL CARRITO (NUEVO) ---
@Dao
interface CartDao {

    @Query("""
        SELECT products.*, cart_items.quantity 
        FROM cart_items 
        INNER JOIN products ON cart_items.productId = products.codigo
        WHERE cart_items.userId = :userId
    """)
    fun getCartContents(userId: Int): Flow<List<CartProduct>>

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId LIMIT 1")
    suspend fun getCartItem(userId: Int, productId: String): CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CartItem)

    @Query("UPDATE cart_items SET quantity = :newQuantity WHERE userId = :userId AND productId = :productId")
    suspend fun updateQuantity(userId: Int, productId: String, newQuantity: Int)

    @Query("DELETE FROM cart_items WHERE userId = :userId AND productId = :productId")
    suspend fun deleteItem(userId: Int, productId: String)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: Int)
}

// --- DAO DE STOCK (NUEVO) ---
@Dao
interface ProductStockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stock: List<ProductStock>)

    @Query("SELECT * FROM product_stock WHERE productId = :productId")
    fun getStockForProduct(productId: String): Flow<List<ProductStock>>
}