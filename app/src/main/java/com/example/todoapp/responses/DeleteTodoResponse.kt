package com.example.todoapp.responses

data class DeleteTodoResponse(
    val message: String,
    val status: Int,
    val success: Boolean
)