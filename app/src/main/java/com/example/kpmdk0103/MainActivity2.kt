package com.example.kpmdk0103

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val btnAddToCart = findViewById<Button>(R.id.button)
        val database = AppDatabase.getDatabase(this)

        val productId = intent.getIntExtra("PRODUCT_ID", 1)
        val userId = intent.getIntExtra("USER_ID", -1)

        btnAddToCart.setOnClickListener {
            if (userId == -1) {
                Toast.makeText(this, "Пожалуйста, авторизуйтесь!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val currentCart = database.cartDao().getUserCart(userId)
                val isProductInCart = currentCart.any { it.productId == productId }

                if (isProductInCart) {
                    Toast.makeText(this@MainActivity2, "Товар уже в корзине", Toast.LENGTH_SHORT).show()
                } else {
                    database.cartDao().insert(CartItem(id = 0, userId = userId, productId = productId, quantity = 1))
                    Toast.makeText(this@MainActivity2, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
