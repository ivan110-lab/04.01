package com.example.kpmdk0103

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUserId(userId: Int) {
        prefs.edit().putInt("current_user_id", userId).apply()
    }

    fun getUserId(): Int {
        return prefs.getInt("current_user_id", -1)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}