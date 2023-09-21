package com.example.todoapp.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.repository.TodoRepository
import com.example.todoapp.viewModels.TodoViewModel

class TodoViewModelFactory(private val todoRepository: TodoRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(todoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

