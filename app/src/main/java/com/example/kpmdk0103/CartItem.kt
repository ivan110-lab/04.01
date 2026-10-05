package com.example.kpmdk0103

import androidx.room.*

@Entity(tableName = "cart")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val productId: Int,
    var quantity: Int = 1
)

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItem)

    @Query("SELECT * FROM cart WHERE userId = :uId")
    suspend fun getUserCart(uId: Int): List<CartItem>

    @Query("DELETE FROM cart WHERE userId = :uId")
    suspend fun clearCart(uId: Int)

    @Query("DELETE FROM cart WHERE id = :cartItemId")
    suspend fun deleteItemById(cartItemId: Int)
}