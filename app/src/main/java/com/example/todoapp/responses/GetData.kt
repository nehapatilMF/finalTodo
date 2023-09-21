package com.example.todoapp.responses

data class GetData(
    val list: List<TodoItem> // Changed data type to List<TodoItem>
)