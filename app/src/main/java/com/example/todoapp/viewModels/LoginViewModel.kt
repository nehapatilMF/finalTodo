package com.example.todoapp.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.responses.LoginResponse
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response



class LoginViewModel : ViewModel() {

    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)

    private val _loginResult = MutableLiveData<String>()
    val loginResult :  LiveData<String> get() = _loginResult

    @SuppressLint("SuspiciousIndentation")

    fun login(email : String, password : String){
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    val loginResponse: Response<LoginResponse>? = apiInterface?.login(email,password)
                    val response = loginResponse?.body()
                    if(loginResponse?.isSuccessful == true ){
                        val status = response?.status.toString()
                        _loginResult.postValue(status)

                    } else{
                        _loginResult.value = response?.message
                    }
                } catch (e : Exception){
                    _loginResult.value = e.message
                }
            }
        }
    }
}
