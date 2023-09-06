package com.example.todoapp.responses

data class UpdateTodoResponse(
    val message: String,
    val status: Int,
    val success: Boolean
)