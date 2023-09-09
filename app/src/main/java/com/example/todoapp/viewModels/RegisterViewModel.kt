package com.example.todoapp.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    lateinit var email: String

    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)
    private val _signupResult =MutableLiveData<String>()
    val signupResult : LiveData<String> get() =  _signupResult

    private val _otpResult = MutableLiveData<String>()
    val otpResult : LiveData<String> get() =  _otpResult

      @SuppressLint("SuspiciousIndentation")
      fun signup(
        name: String,
        mobile: Long,
        email: String,
        password: String
    ) {
         viewModelScope.launch {
            try {
                val apiResponse = apiInterface?.signup(name, mobile, email, password)
                val response = apiResponse?.body()
                    if(response?.success == true) {
                        val status = response.status.toString()
                        _signupResult.postValue(status)
                         _otpResult.value = response.data.otp.toString()

                    }else{
                        _signupResult.postValue(response?.message)
                    }
            } catch (e: Exception) {
                _signupResult.postValue (e.message)
            }
        }
    }
}

