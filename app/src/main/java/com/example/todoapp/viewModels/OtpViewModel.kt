package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.AuthTokens
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import kotlinx.coroutines.launch

class OtpViewModel : ViewModel()  {

    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)

    private val _otpResult = MutableLiveData<String>()
    val otpResult : LiveData<String> get() =  _otpResult

    private val _newOtpResult = MutableLiveData<String>()
    val newOtpResult : LiveData<String> get() =  _newOtpResult
    private val _resendOtpResult = MutableLiveData<String>()
    val resendOtpResult : LiveData<String> get() = _resendOtpResult
    private val _authTokens = MutableLiveData<AuthTokens>()

    fun getAuthTokens(): LiveData<AuthTokens> {
        return _authTokens
    }
    private fun saveTokens(accessToken: String, refreshToken: String) {
        val authTokens = AuthTokens(accessToken, refreshToken)
        _authTokens.postValue(authTokens)
    }


    fun signupVerifyOtp(email: String, otp: Long){
        viewModelScope.launch {
            try {
                val signupVerifyOtpResponse = apiInterface?.signupVerifyOtp( email, otp)
                val response = signupVerifyOtpResponse?.body()
                if(signupVerifyOtpResponse?.isSuccessful == true) {
                    val status = response?.status.toString()
                    _otpResult.postValue(status)
                    val accessToken = response?.data?.token?.access_token.toString()

                    val refreshToken = response?.data?.token?.refresh_token.toString()
                    saveTokens(accessToken, refreshToken)


                }else{
                    _otpResult.postValue(response?.message)
                }
            } catch (e: Exception) {
                _otpResult.postValue (e.message)
            }
        }

    }

    fun resendUserOtp(email : String){
        viewModelScope.launch {
            try{
                val resendOtp = apiInterface?.resendUserOtp(email)
                val response = resendOtp?.body()
                if(resendOtp?.isSuccessful == true){
                    val status = response?.status.toString()
                    _resendOtpResult.postValue(status)
                    _newOtpResult.value = response?.data?.otp.toString()
                    }else{
                    _resendOtpResult.postValue(response?.message)
                }
            } catch (e: Exception) {
                _resendOtpResult.postValue (e.message)
            }

        }
    }
}

