package com.example.kpmdk0103

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Product::class, CartItem::class, OrderItem::class],
    version = 11,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getDatabase(context)
                                if (database.productDao().getAllProducts().isEmpty()) {
                                    database.productDao().insertProduct(Product(imageUri = "phone", price = 1500.0, description = "Смартфон Android"))
                                    database.productDao().insertProduct(Product(imageUri = "music", price = 3200.0, description = "Беспроводные наушники"))
                                    database.productDao().insertProduct(Product(imageUri = "power", price = 900.0, description = "Зарядное устройство"))
                                }
                            }
                        }
                    }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}