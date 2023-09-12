package com.example.todoapp.client

import android.content.Context
import com.auth0.jwt.JWT
import java.util.Date

class SessionManager(private val context: Context) {
    private val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String) {
        val editor = sharedPref.edit()
        editor.putString("access_token", accessToken)
        editor.putString("refresh_token", refreshToken)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return sharedPref.getString("access_token", null)
    }

    fun getRefreshToken(): String? {
        return sharedPref.getString("refresh_token", null)
    }

    fun clearTokens() {
        val editor = sharedPref.edit()
        editor.remove("access_token")
        editor.remove("refresh_token")
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }

    fun isAccessTokenExpired(): Boolean {
        val accessToken = getAccessToken()
        if (accessToken != null) {
            val jwt = JWT.decode(accessToken)
            val expirationTime = jwt.expiresAt
            return Date().after(expirationTime)
        }
        return true
    }

}