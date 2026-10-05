package com.example.kpmdk0103

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val login: String,
    val password: String,
    var balance: Double,
    val name: String
)