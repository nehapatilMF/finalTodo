package com.example.todoapp.responses

data class ChangePasswordResponse(
    val message: String,
    val status: Int,
    val success: Boolean
)