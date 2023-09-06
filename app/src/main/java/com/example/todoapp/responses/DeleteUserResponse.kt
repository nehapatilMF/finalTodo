package com.example.todoapp.responses

data class DeleteUserResponse(
    val message: String,
    val status: Int,
    val success: Boolean
)