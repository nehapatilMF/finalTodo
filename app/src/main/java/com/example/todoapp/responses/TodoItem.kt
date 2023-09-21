package com.example.todoapp.responses

data class TodoItem(
    val created_at: String, // Changed data type to String
    val description: String, // Changed data type to String
    val id: Int, // Changed data type to Int
    val status: Int, // Changed data type to Int
    val title: String,
    val todo_date: String,
    val todo_time: String, // Changed data type to String
    val updated_at: String,
    val user_id: Int // Changed data type to Int
)