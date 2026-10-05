package com.example.kpmdk0103

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity3 : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var sessionManager: SessionManager
    private var currentUserId: Int = -1

    private lateinit var balanceView: TextView
    private lateinit var nameView: TextView
    private lateinit var btnLoginExit: Button
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        db = AppDatabase.getDatabase(this)
        sessionManager = SessionManager(this)
        balanceView = findViewById(R.id.textView11)
        nameView = findViewById(R.id.textView3)
        btnLoginExit = findViewById(R.id.button2)
        val btnTopUp = findViewById<Button>(R.id.button5)
        val rvOrders = findViewById<RecyclerView>(R.id.recyclerViewOrders)
        rvOrders.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        orderAdapter = OrderAdapter(emptyList())
        rvOrders.adapter = orderAdapter
        btnLoginExit.setOnClickListener {
            handleLoginExit()
        }

        btnTopUp.setOnClickListener {
            startActivity(Intent(this, MainActivity6::class.java))
        }

        findViewById<ImageButton>(R.id.imageButton1).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.imageButton2).setOnClickListener {
            startActivity(Intent(this, MainActivity8::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        currentUserId = sessionManager.getUserId()
        loadProfileData()
        updateAuthUI()
    }

    private fun updateAuthUI() {
        lifecycleScope.launch {
            val user = db.userDao().getUserById(currentUserId)
            val isGuest = user?.login == "default" || currentUserId == -1
            btnLoginExit.text = if (isGuest) "Войти" else "Выйти"
        }
    }

    private fun loadProfileData() {
        lifecycleScope.launch {
            val user = db.userDao().getUserById(currentUserId)
            user?.let {
                nameView.text = it.name
                balanceView.text = String.format("%.2f ₽", it.balance)
            }

            val orders = db.orderDao().getUserOrders(currentUserId)

            orderAdapter.updateData(orders)
        }
    }

    private fun handleLoginExit() {
        lifecycleScope.launch {
            val user = db.userDao().getUserById(currentUserId)
            val isGuest = user?.login == "default" || currentUserId == -1

            if (isGuest) {

                startActivity(Intent(this@MainActivity3, MainActivity4::class.java))
            } else {

                sessionManager.logout()
                val guest = db.userDao().getUserByLogin("default")
                guest?.let { sessionManager.saveUserId(it.id) }
                recreate()
            }
        }
    }
}