package com.example.todoapp.responses

data class GetTodoListResponse(
    val data: GetData,
    val message: String,
    val status: Int,
    val success: Boolean
)

