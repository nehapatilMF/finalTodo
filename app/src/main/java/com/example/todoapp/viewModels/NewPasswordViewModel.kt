package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import kotlinx.coroutines.launch

class NewPasswordViewModel : ViewModel() {

    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)
    private val _resetPasswordResult = MutableLiveData<String>()
    val resetPasswordResult : LiveData<String> get() =  _resetPasswordResult

    fun resetPassword(
        otp: String,
        email: String,
        password: String){
        viewModelScope.launch {
           try{
                val apiResponse = apiInterface?.resetPassword(otp,email,password)
                val response = apiResponse?.body()

                if( apiResponse?.isSuccessful == true){
                    val status = response?.status.toString()
                    _resetPasswordResult.postValue(status)
                }else{
                   _resetPasswordResult.postValue(response?.message)
               }
            } catch (e: Exception) {
                _resetPasswordResult.postValue (e.message)
            }
        }
    }
}
