package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import com.example.todoapp.responses.AddTodoResponse
import com.example.todoapp.responses.DeleteTodoResponse
import com.example.todoapp.responses.UpdateTodoResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class TodoViewModel : ViewModel() {
    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)

    private val _addTodoStatus = MutableLiveData<String>()
    val addTodoStatus: LiveData<String> get() = _addTodoStatus

    private val _deleteTodoStatus = MutableLiveData<String>()
    val deleteTodoStatus: LiveData<String> get() = _deleteTodoStatus

    private val _updateTodoStatus = MutableLiveData<String>()
    val updateTodoStatus: LiveData<String> get() = _updateTodoStatus
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
                    val addTodoStatus = response?.status.toString()
                    _addTodoStatus.postValue(addTodoStatus)
                }else{
                    _addTodoStatus.value = response?.message
                }
            }catch (e : Exception){
                _addTodoStatus.value = e.message
            }
        }
    }

    fun deleteTodo(id : String){
        viewModelScope.launch {
            try{
                val apiResponse: Response<DeleteTodoResponse>? = apiInterface?.deleteTodo(id)
                val response = apiResponse?.body()
                if(apiResponse?.isSuccessful == true){
                    val deleteTodoStatus = response?.status.toString()
                    _deleteTodoStatus.postValue(deleteTodoStatus)
                }else{
                    _deleteTodoStatus.value = response?.message
                }
            }catch (e : Exception){
                _deleteTodoStatus.value = e.message
            }
        }
    }

    fun updateTodo(id : String,
                   title : String,
                   description : String,
                   status : Int,
                   date : String,
                   time : String){
        viewModelScope.launch {
            try{
                val apiResponse: Response<UpdateTodoResponse>? = apiInterface?.updateTodo(id,
                    title,
                    description,
                    status,
                    date,
                    time)
                val response = apiResponse?.body()
                if(apiResponse?.isSuccessful == true){
                    val updateTodoStatus = response?.status.toString()
                    _updateTodoStatus.postValue(updateTodoStatus)
                }else{
                    _updateTodoStatus.value = response?.message
                }
            }catch (e : Exception){
                _updateTodoStatus.value = e.message
            }
        }
    }
}