package com.example.kpmdk0103

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity7 : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var currentUserId: Int = -1
    private var productId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main7)

        db = AppDatabase.getDatabase(this)
        productId = intent.getIntExtra("PRODUCT_ID", -1)
        currentUserId = intent.getIntExtra("USER_ID", -1)
        val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0)
        val productDesc = intent.getStringExtra("PRODUCT_DESC") ?: ""
        val productImageName = intent.getStringExtra("PRODUCT_IMAGE")
        val tvPrice = findViewById<TextView>(R.id.textView5)
        val tvDesc = findViewById<TextView>(R.id.textView7)
        val ivMain = findViewById<ImageView>(R.id.imageView5)
        val btnAddToCart = findViewById<Button>(R.id.button3)
        val btnBack = findViewById<ImageButton>(R.id.imageButton) // Кнопка Назад
        tvPrice.text = String.format("%.2f ₽", productPrice)
        tvDesc.text = productDesc
        val imageResId = resources.getIdentifier(productImageName, "drawable", packageName)
        if (imageResId != 0) ivMain.setImageResource(imageResId)
        btnBack.setOnClickListener {
            finish()
        }
        btnAddToCart.setOnClickListener {
            addItemToCart()
        }
        setupNavigation()
    }

    private fun addItemToCart() {
        if (currentUserId == -1 || productId == -1) return
        lifecycleScope.launch {
            val currentCart = db.cartDao().getUserCart(currentUserId)
            if (currentCart.any { it.productId == productId }) {
                Toast.makeText(this@MainActivity7, "Товар уже в корзине", Toast.LENGTH_SHORT).show()
            } else {
                db.cartDao().insert(CartItem(id = 0, userId = currentUserId, productId = productId, quantity = 1))
                Toast.makeText(this@MainActivity7, "Товар добавлен!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupNavigation() {
        findViewById<ImageButton>(R.id.imageButton1).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            finish()
        }
        findViewById<ImageButton>(R.id.imageButton2).setOnClickListener {
            startActivity(Intent(this, MainActivity8::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.imageButton3).setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java))
            finish()
        }
    }
}