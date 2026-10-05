package com.example.kpmdk0103

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE login = :login AND password = :password LIMIT 1")
    suspend fun getUser(login: String, password: String): User?

    @Query("SELECT * FROM users WHERE login = :login LIMIT 1")
    suspend fun getUserByLogin(login: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): User?

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)
}