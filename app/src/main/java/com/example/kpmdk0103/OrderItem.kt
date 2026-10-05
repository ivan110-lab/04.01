package com.example.kpmdk0103

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "orders")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val productDescription: String,
    val price: Double,
    val date: Long = System.currentTimeMillis()
)
