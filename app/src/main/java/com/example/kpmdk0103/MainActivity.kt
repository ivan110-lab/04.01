package com.example.kpmdk0103

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var adapter: ProductAdapter
    private lateinit var sessionManager: SessionManager
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = AppDatabase.getDatabase(this)
        sessionManager = SessionManager(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewProducts)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = ProductAdapter(
            products = emptyList(),
            onCartClick = { product ->
                addItemToCart(product)
            },
            onItemClick = { product ->
                val intent = Intent(this, MainActivity7::class.java)
                intent.putExtra("PRODUCT_ID", product.id)
                intent.putExtra("PRODUCT_PRICE", product.price)
                intent.putExtra("PRODUCT_DESC", product.description)
                intent.putExtra("PRODUCT_IMAGE", product.imageUri)
                intent.putExtra("USER_ID", currentUserId)
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter
        val editTextSearch = findViewById<EditText>(R.id.editTextSearch)
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        findViewById<ImageButton>(R.id.imageButton3).setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
        findViewById<ImageButton>(R.id.imageButton2).setOnClickListener {
            val intent = Intent(this, MainActivity8::class.java)
            startActivity(intent)
        }

        checkAuthAndLoadData()
    }

    private fun checkAuthAndLoadData() {
        lifecycleScope.launch {
            val savedId = sessionManager.getUserId()
            if (savedId != -1) {
                currentUserId = savedId
            } else {
                val guest = db.userDao().getUserByLogin("default")
                currentUserId = guest?.id ?: -1
            }
            val products = db.productDao().getAllProducts()
            adapter.updateData(products)
        }
    }
    private fun addItemToCart(product: Product) {
        if (currentUserId == -1) {
            Toast.makeText(this, "Ошибка: пользователь не найден!", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val currentCart = db.cartDao().getUserCart(currentUserId)
            val isProductInCart = currentCart.any { it.productId == product.id }

            if (isProductInCart) {
                Toast.makeText(this@MainActivity, "Товар уже в корзине", Toast.LENGTH_SHORT).show()
            } else {
                val cartItem = CartItem(id = 0, userId = currentUserId, productId = product.id, quantity = 1)
                db.cartDao().insert(cartItem)
                Toast.makeText(this@MainActivity, "${product.description} добавлен в корзину!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}