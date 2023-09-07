package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import com.example.todoapp.responses.GetTodoListResponse
import com.example.todoapp.responses.TodoItem
import kotlinx.coroutines.launch
import retrofit2.Response
class HomeViewModel : ViewModel() {
    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)

    private val _todoList = MutableLiveData<List<TodoItem>>()
    val todoList: LiveData<List<TodoItem>> get() = _todoList

    private val _fetchTodoListStatus = MutableLiveData<String>()
    val fetchTodoListStatus: LiveData<String> get() = _fetchTodoListStatus


    fun fetchTodoList() {
        viewModelScope.launch {
            try {
                val apiResponse: Response<GetTodoListResponse>? = apiInterface?.getTodoList()
                val response = apiResponse?.body()

                if (apiResponse?.isSuccessful == true) {
                    val status = response?.status.toString()
                    _fetchTodoListStatus.postValue(status)
                    _todoList.postValue(response?.data?.list)

                } else {
                    _fetchTodoListStatus.value = response?.message
                }
            } catch (e: Exception) {
                _fetchTodoListStatus.value = e.message
            }
        }
    }


  }
