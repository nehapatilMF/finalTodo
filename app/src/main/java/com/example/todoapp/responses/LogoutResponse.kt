package com.example.todoapp.responses

data class LogoutResponse(
    val message: String,
    val status: Int,
    val success: Boolean
)