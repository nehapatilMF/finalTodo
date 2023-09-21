package com.example.todoapp.repository

import com.example.todoapp.interfaces.TodoAPI

class TodoRepository(private val todoAPI : TodoAPI) {

    suspend fun getTodoList() = todoAPI.getTodoList()

    suspend fun addTodo(
        title: String,
        description: String,
        todoDate: String,
        todoTime: String,
        status: Int
    ) = todoAPI.addTodo(title, description, todoDate, todoTime, status)

    suspend fun updateTodo(
        id: String,
        title: String,
        description: String,
        status: Int,
        todoDate: String,
        todoTime: String
    ) = todoAPI.updateTodo(id, title, description, status, todoDate, todoTime)

    suspend fun deleteTodo(id: String) = todoAPI.deleteTodo(id)

    suspend fun logout() = todoAPI.logout()

    suspend fun deleteUser() = todoAPI.deleteUser()

    suspend fun profileInfo() = todoAPI.profileInfo()

    suspend fun updateProfile(name : String, mobile : String) = todoAPI.updateProfile(name, mobile)

    suspend fun changePassword(oldPassword : String,
                               password : String) = todoAPI.changePassword(oldPassword, password)

}
