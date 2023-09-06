package com.example.todoapp.responses

data class AddTodoResponse(
    val message: String,
    val status: Int,
    val success: Boolean
)