package com.example.kpmdk0103

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity4 : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        sessionManager = SessionManager(this)
        val database = AppDatabase.getDatabase(this)
        userDao = database.userDao()

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegister = findViewById(R.id.buttonRegister)

        buttonLogin.setOnClickListener {
            val login = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginUser(login, password)
        }

        buttonRegister.setOnClickListener {
            val intent = Intent(this, MainActivity5::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(login: String, password: String) {
        lifecycleScope.launch {
            val user = userDao.getUser(login, password)
            if (user != null) {
                // СОХРАНЯЕМ ID в сессию, чтобы он не терялся
                sessionManager.saveUserId(user.id)

                Toast.makeText(this@MainActivity4, "Вход успешен!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity4, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@MainActivity4, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }
}