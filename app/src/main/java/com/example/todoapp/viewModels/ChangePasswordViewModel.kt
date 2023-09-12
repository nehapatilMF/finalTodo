package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import kotlinx.coroutines.launch

class ChangePasswordViewModel : ViewModel() {
    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)
    private val _result = MutableLiveData<String>()
    val result : LiveData<String> get() =  _result
    private val _msg = MutableLiveData<String>()
    val msg : LiveData<String> get() =  _msg
    fun changePassword(old_password : String,
                      password : String){
        viewModelScope.launch {
            try{
                val apiResponse = apiInterface?.changePassword(old_password,password)
                val response = apiResponse?.body()
                if( apiResponse?.isSuccessful == true){
                    val status = response?.success.toString()
                    _result.postValue(status)
                    _msg.postValue(response?.message)

                }else{
                    _result.postValue(response?.message)
                    _msg.postValue(response?.message)
                }
            } catch (e: Exception) {
                _result.postValue (e.message)
            }
        }
    }
}
