package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel(){

    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)

    private val  _forgotPasswordResult = MutableLiveData<String>()
    val  forgotPasswordResult : LiveData<String> get() =   _forgotPasswordResult

    private val  _msg = MutableLiveData<String>()
    val  msg : LiveData<String> get() =   _msg
    private val _otpResult = MutableLiveData<String>()
    val otpResult : LiveData<String> get() =  _otpResult

    fun forgotPasswordRequestOtp(email : String){
        viewModelScope.launch{
            try {
                val signupResponse = apiInterface?.forgotPasswordRequestOtp(email)
                val response = signupResponse?.body()
                if(response?.success == true) {
                    val status = response.status.toString()
                    _forgotPasswordResult.postValue(status)
                    _otpResult.postValue(response.data.otp.toString())
                    _msg.postValue(response.message)
                }else{
                    _forgotPasswordResult.postValue(response?.message)
                }
            } catch (e: Exception) {
                _forgotPasswordResult.postValue (e.message)
            }
        }
    }
}