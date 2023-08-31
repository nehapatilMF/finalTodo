package com.example.todoapp.util

import java.util.regex.Pattern

object ValidPatterns {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\",./<>?|\\\\]).{8,}$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
    fun isValidNumber(phoneNumber : String):Boolean{
        val pattern = Regex("\\d{10}")
        return pattern.matches(phoneNumber)
    }

}