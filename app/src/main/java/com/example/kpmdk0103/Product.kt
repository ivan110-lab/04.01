package com.example.kpmdk0103

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUri: String, // путь или URL к изображению
    val price: Double,
    val description: String
)