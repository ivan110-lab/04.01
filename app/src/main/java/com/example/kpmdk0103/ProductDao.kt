package com.example.kpmdk0103

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(product: Product)

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>
    @Query("SELECT * FROM products WHERE id = :pId LIMIT 1")
    suspend fun getProductById(pId: Int): Product?
}