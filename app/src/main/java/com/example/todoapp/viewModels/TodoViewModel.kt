package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.AuthTokens
import com.example.todoapp.repository.TodoRepository
import com.example.todoapp.responses.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status
    private val _todoMessage =MutableLiveData<String>()
    val todoMessage : LiveData<String> get() = _todoMessage
    private val _name = MutableLiveData<String>()
    val name : LiveData<String> get() = _name
    private val _email = MutableLiveData<String>()
    val email : LiveData<String> get() = _email
    private val _mobile = MutableLiveData<String>()
    val mobile : LiveData<String> get() = _mobile
    private val _todoList = MutableLiveData<List<TodoItem>>()
    val todoList: LiveData<List<TodoItem>> get() = _todoList
    private val _authTokens = MutableLiveData<AuthTokens>()

    fun getAuthTokens(): LiveData<AuthTokens> {
        return _authTokens
    }

    private fun saveTokens(accessToken: String, refreshToken: String) {
        val authTokens = AuthTokens(accessToken, refreshToken)
        _authTokens.postValue(authTokens)
    }

    fun fetchTodoList() {
        viewModelScope.launch{
            try {
                val apiResponse = todoRepository.getTodoList()
                val response = apiResponse.body()

                if (apiResponse.isSuccessful) {
                    val status = response?.status.toString()
                    withContext(Dispatchers.Main) {
                        _status.value = status
                        _todoList.postValue(response?.data?.list)
                    }
                } else {
                    _status.value = response?.message

                }
            } catch (e: Exception) {
                _status.value = e.message
            }
        }
    }

    fun addTodo(title : String,
                description : String,
                todoDate : String,
                todoTime : String,
                status : Int){
        viewModelScope.launch {
            try{
                val apiResponse = todoRepository.addTodo(title,description,todoDate,todoTime,status)
                val response = apiResponse.body()
                if(response?.success == true){
                    val addTodoStatus = response.status
                    _status.postValue(addTodoStatus)
                    _todoMessage.postValue(response.message)
                }else{
                    _status.value = response?.message
                }
            }catch (e : Exception){
                _status.value = e.message
            }
        }
    }

    fun deleteTodo(id : String){
        viewModelScope.launch {
            try{
                val apiResponse= todoRepository.deleteTodo(id)
                val response = apiResponse.body()
                if(response?.success == true){
                    val deleteTodoStatus = response.status
                    _status.postValue(deleteTodoStatus)
                    _todoMessage.postValue(response.message)

                }else{
                    _status.value = response?.message
                }
            }catch (e : Exception){
                _status.value = e.message
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
                val apiResponse= todoRepository.updateTodo(id,
                    title,
                    description,
                    status,
                    date,
                    time)
                val response = apiResponse.body()
                if(response?.success == true){
                    val updateTodoStatus = response.status
                    _status.postValue(updateTodoStatus)
                    _todoMessage.postValue(response.message)

                }else{
                    _status.value = response?.message
                }
            }catch (e : Exception){
                _status.value = e.message
            }
        }
    }
    fun logout(){
        viewModelScope.launch {
            try {
                val apiResponse= todoRepository.logout()
                val response = apiResponse.body()
                if(response?.success == true ){
                    val status = response.status
                    _status.postValue(status)

                    _todoMessage.postValue(response.message)
                } else{
                    _status.value = response?.message
                }
            } catch (e : Exception){
                _status.value = e.message
            }
        }
    }

    fun deleteUser(){
        viewModelScope.launch {
            try {
                val apiResponse = todoRepository.deleteUser()
                val response = apiResponse.body()
                if(response?.success == true ){
                    val status = response.status
                    _status.postValue(status)

                    _todoMessage.postValue(response.message)

                } else{
                    _status.postValue(response?.message)
                }
            } catch (e : Exception){
                _status.value = e.message
            }
        }
    }

    fun get(){
        viewModelScope.launch {
            try {
                val apiResponse = todoRepository.profileInfo()
                val response = apiResponse.body()
                if(response?.success == true ){
                    val status = response.status
                    _status.postValue(status)
                    _todoMessage.value = response.message
                    _name.value = response.data.user.name
                    _mobile.value = response.data.user.mobile_no
                    _email.value = response.data.user.email
                } else{
                    _status.value = response?.message
                }
            } catch (e : Exception){
                _status.value = e.message
            }
        }

    }

    fun updateUser(name : String, mobile : String){
        viewModelScope.launch {
            try {
                val apiResponse= todoRepository.updateProfile(name,mobile)
                val response = apiResponse.body()
                if(response?.success == true ){
                    val status = response.status
                    _status.postValue(status)
                    _todoMessage.value = response.message

                } else{
                    _status.postValue(response?.message)
                }
            } catch (e : Exception){
                _status.value = e.message
            }
        }
    }

    fun changePassword(oldPassword : String,
                       password : String){
        viewModelScope.launch {
            try{
                val apiResponse = todoRepository.changePassword(oldPassword,password)
                val response = apiResponse.body()
                if( response?.success == true){
                    val status = response.status
                    _status.postValue(status)
                    _todoMessage.postValue(response.message)

                }else{
                    _status.postValue(response?.message)
                }
            } catch (e: Exception) {
                _status.postValue (e.message)
            }
        }
    }
}