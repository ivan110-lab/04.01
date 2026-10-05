package com.example.kpmdk0103

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity5 : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        val database = AppDatabase.getDatabase(this)
        userDao = database.userDao()

        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val login = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (name.isEmpty() || login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(name, login, password)
        }
    }

    private fun registerUser(name: String, login: String, password: String) {
        lifecycleScope.launch {
            val existingUser = userDao.getUserByLogin(login)
            if (existingUser != null) {
                Toast.makeText(this@MainActivity5, "Пользователь с таким email уже существует", Toast.LENGTH_SHORT).show()
                return@launch
            }
            val newUser = User(
                login = login,
                password = password,
                name = name,
                balance = 0.0
            )

            userDao.insertUser(newUser)
            Toast.makeText(this@MainActivity5, "Регистрация успешна!", Toast.LENGTH_SHORT).show()

            val registeredUser = userDao.getUser(login, password)
            registeredUser?.let {
                val intent = Intent(this@MainActivity5, MainActivity3::class.java)
                intent.putExtra("USER_ID", it.id)
                startActivity(intent)
                finish()
            } ?: run {
                Toast.makeText(this@MainActivity5, "Ошибка при переходе", Toast.LENGTH_SHORT).show()
            }
        }
    }
}