package com.example.kpmdk0103

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: OrderItem)

    @Query("SELECT * FROM orders WHERE userId = :uId ORDER BY date DESC")
    suspend fun getUserOrders(uId: Int): List<OrderItem>
}