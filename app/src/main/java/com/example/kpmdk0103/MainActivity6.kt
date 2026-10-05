package com.example.kpmdk0103

import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)

        val db = AppDatabase.getDatabase(this)
        val sessionManager = SessionManager(this)

        val sumInput = findViewById<EditText>(R.id.editTextSumPopolnenia)
        val cardCodeInput = findViewById<EditText>(R.id.editTextCod)
        val cvcInput = findViewById<EditText>(R.id.editTextCVC)
        val buttonPay = findViewById<Button>(R.id.buttonRegister)
        cardCodeInput.filters = arrayOf(InputFilter.LengthFilter(16))
        cvcInput.filters = arrayOf(InputFilter.LengthFilter(3))

        val userId = sessionManager.getUserId()

        buttonPay.setOnClickListener {
            val cardNumber = cardCodeInput.text.toString().trim()
            val cvc = cvcInput.text.toString().trim()
            if (cardNumber.length < 16 || !TextUtils.isDigitsOnly(cardNumber)) {
                Toast.makeText(this, "Введите правильный номер карты (не менее 16 цифр)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cvc.length < 3 || !TextUtils.isDigitsOnly(cvc)) {
                Toast.makeText(this, "Введите правильный CVC (не менее 3 цифр)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = sumInput.text.toString().toDoubleOrNull() ?: 0.0

            if (userId != -1) {
                lifecycleScope.launch {
                    val user = db.userDao().getUserById(userId)
                    if (user != null) {
                        user.balance += amount
                        db.userDao().updateUser(user)

                        Toast.makeText(this@MainActivity6, "Баланс пополнен на $amount", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity6, "Ошибка: пользователь не найден", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
            }
        }
    }
}