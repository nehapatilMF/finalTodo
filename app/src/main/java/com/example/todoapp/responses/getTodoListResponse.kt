package com.example.todoapp.responses

data class getTodoListResponse(
    val data: GetData,
    val message: String,
    val status: Int,
    val success: Boolean
)
data class GetData(
    val list: List
)

data class List(
    val created_at: String,
    val description: String,
    val id: Int,
    val status: Int,
    val title: String,
    val todo_date: String,
    val todo_time: String,
    val updated_at: String,
    val user_id: Int
)


