package com.example.todoapp.responses

data class TodoResponse(
    val status: String,
    val success: Boolean,
    val message: String,
    val data : AData
)

