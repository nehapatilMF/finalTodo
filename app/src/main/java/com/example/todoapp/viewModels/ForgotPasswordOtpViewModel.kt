package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import kotlinx.coroutines.launch

class ForgotPasswordOtpViewModel : ViewModel() {

    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)
    private val _otpResult = MutableLiveData<String>()
    val otpResult : LiveData<String> get() =  _otpResult

    private val _otp = MutableLiveData<String>()
    val otp : LiveData<String> get() =  _otp
    private val _email = MutableLiveData<String>()
    val email : LiveData<String> get() =  _email
    private val _resendOtpResult = MutableLiveData<String>()
    val resendOtpResult : LiveData<String> get() = _resendOtpResult
    private val _newOtpResult = MutableLiveData<String>()
    val newOtpResult : LiveData<String> get() = _newOtpResult



    fun forgotPasswordVerifyOtp(email: String, otp: Long){
        viewModelScope.launch {
            try {
                val signupVerifyOtpResponse = apiInterface?.forgotPasswordVerifyOtp( email, otp)
                val response = signupVerifyOtpResponse?.body()
                if(signupVerifyOtpResponse?.isSuccessful == true) {
                    val status = response?.status.toString()
                    _otpResult.postValue(status)
                    _otp.postValue(response?.data?.user?.otp.toString())
                    _email.postValue(response?.data?.user?.email.toString())

                }else{
                    _otpResult.postValue(response?.message)
                }
            } catch (e: Exception) {
                _otpResult.postValue (e.message)
            }
        }
    }

    fun forgotPasswordResendOtp(email : String){
        viewModelScope.launch {
            try{
                val resendOtp = apiInterface?.forgotPasswordResendOtp(email)
                val response = resendOtp?.body()
                if(resendOtp?.isSuccessful == true){
                    val status = response?.status.toString()
                    _resendOtpResult.postValue(status)
                    _newOtpResult.postValue(response?.data?.otp.toString())

                }else{
                    _resendOtpResult.postValue(response?.message)
                }
            } catch (e: Exception) {
                _resendOtpResult.postValue (e.message)
            }
        }
    }
}

