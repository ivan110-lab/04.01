package com.example.kpmdk0103

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity8 : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var cartAdapter: CartAdapter
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main8)

        db = AppDatabase.getDatabase(this)
        currentUserId = SessionManager(this).getUserId()

        val rv = findViewById<RecyclerView>(R.id.recyclerViewCart)
        rv.layoutManager = LinearLayoutManager(this)

        cartAdapter = CartAdapter(emptyList()) { item, q -> handleQuantity(item, q) }
        rv.adapter = cartAdapter

        findViewById<Button>(R.id.buttonPay).setOnClickListener { processPayment() }

        findViewById<ImageButton>(R.id.imageButton1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<ImageButton>(R.id.imageButton3).setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java))
        }

        loadCart()
    }

    private fun loadCart() {
        lifecycleScope.launch {
            val items = db.cartDao().getUserCart(currentUserId)
            val list = items.mapNotNull { item ->
                db.productDao().getProductById(item.productId)?.let { item to it }
            }
            cartAdapter.updateData(list)
            val total = list.sumOf { it.first.quantity * it.second.price }
            findViewById<Button>(R.id.buttonPay).text = "Оплатить: $total ₽"
        }
    }

    private fun handleQuantity(item: CartItem, q: Int) {
        lifecycleScope.launch {
            if (q <= 0) db.cartDao().deleteItemById(item.id)
            else {
                item.quantity = q
                db.cartDao().insert(item)
            }
            loadCart()
        }
    }

    private fun processPayment() {
        val addressInput = findViewById<EditText>(R.id.editTextAddress)
        val address = addressInput.text.toString().trim()
        if (address.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, введите адрес доставки!", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val user = db.userDao().getUserById(currentUserId)
            val items = db.cartDao().getUserCart(currentUserId)

            val data = items.mapNotNull { i ->
                db.productDao().getProductById(i.productId)?.let { i to it }
            }

            val total = data.sumOf { it.first.quantity * it.second.price }

            if (user != null && user.balance >= total && data.isNotEmpty()) {
                user.balance -= total
                db.userDao().updateUser(user)

                data.forEach {
                    val orderDesc = "${it.second.description} (Доставка: $address)"
                    db.orderDao().insertOrder(OrderItem(
                        userId = currentUserId,
                        productDescription = orderDesc,
                        price = it.second.price * it.first.quantity
                    ))
                }

                db.cartDao().clearCart(currentUserId)
                Toast.makeText(this@MainActivity8, "Заказ успешно оформлен!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity8, MainActivity3::class.java))
                finish()
            } else {
                Toast.makeText(this@MainActivity8, "Недостаточно средств или корзина пуста", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
