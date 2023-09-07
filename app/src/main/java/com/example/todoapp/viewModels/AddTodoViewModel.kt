package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import com.example.todoapp.responses.AddTodoResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class AddTodoViewModel : ViewModel() {
    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)
    private val _addTodoStatus = MutableLiveData<String>()
    val addTodoStatus: LiveData<String> get() = _addTodoStatus


    fun addTodo(title : String,
                description : String,
                todo_date : String,
                todo_time : String,
                status : Int){
        viewModelScope.launch {
            try{
                val apiResponse: Response<AddTodoResponse>? = apiInterface?.addTodo(title,description,todo_date,todo_time,status)
                val response = apiResponse?.body()
                if(apiResponse?.isSuccessful == true){
                    val status = response?.status.toString()
                    _addTodoStatus.postValue(status)
                }else{
                    _addTodoStatus.value = response?.message
                }
            }catch (e : Exception){
                _addTodoStatus.value = e.message
            }
        }
    }
}