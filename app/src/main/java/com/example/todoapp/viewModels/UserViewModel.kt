package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.AuthTokens
import com.example.todoapp.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status
    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> get() = _msg
    private val _otpResult = MutableLiveData<String>()
    val otpResult : LiveData<String> get() =  _otpResult
    private val _name = MutableLiveData<String>()
    val name :  LiveData<String> get() = _name
    private val _email = MutableLiveData<String>()
    val email : LiveData<String> get() =  _email
    private val _authTokens = MutableLiveData<AuthTokens>()

    fun getAuthTokens(): LiveData<AuthTokens> {
        return _authTokens
    }

    private fun saveTokens(accessToken: String, refreshToken: String) {
        val authTokens = AuthTokens(accessToken, refreshToken)
        _authTokens.postValue(authTokens)
    }

    fun signup(
        name: String,
        mobile: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                val apiResponse = userRepository.signup(name, mobile, email, password)
                val response = apiResponse.body()
                if(response?.success == true) {
                    val status = response.status
                    _status.postValue(status)
                    _otpResult.postValue(response.data.otp)
                    _msg.postValue(response.message)
                }else{
                    _status.postValue(response?.message.toString())
                }
            } catch (e: Exception) {
                _status.postValue (e.message)
            }
        }
    }

    fun signupVerifyOtp(email: String, otp: String){
        viewModelScope.launch {
            try {
                val apiResponse = userRepository.signupVerifyOtp( email, otp)
                val response = apiResponse.body()
                if(response?.success == true) {
                    val status = response.status
                    _otpResult.postValue(status)
                    _name.postValue(response.data.user.name)
                    val accessToken = response.data.token.access_token
                    val refreshToken = response.data.token.refresh_token
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
                val apiResponse = userRepository.resendUserOtp(email)
                val response = apiResponse.body()
                if(response?.success == true){
                    val status = response.status
                    _status.postValue(status)
                    _otpResult.value = response.data.otp
                }else{
                    _status.postValue(response?.message)
                }
            } catch (e: Exception) {
                _status.postValue (e.message)
            }

        }
    }

    fun login(email : String, password : String){
        viewModelScope.launch {

            try {
                val apiResponse = userRepository.login(email,password)
                val response = apiResponse.body()
                if(response?.success == true){
                    val status = response.status
                    _status.postValue(status)
                    _name.postValue(response.data.user.name)
                    val accessToken = response.data.token.access_token
                    val refreshToken = response.data.token.refresh_token

                    saveTokens(accessToken, refreshToken)

                } else{
                    _status.postValue(response?.message.toString())
                }
            } catch (e : Exception){
                _status.value = e.message
            }
        }
    }

    fun forgotPasswordRequestOtp(email : String){
        viewModelScope.launch{
            try {
                val apiResponse = userRepository.forgotPasswordRequestOtp(email)
                val response = apiResponse.body()
                if(response?.success == true) {
                    val status = response.status
                    _status.postValue(status)
                    _otpResult.postValue(response.data.otp)
                    _msg.postValue(response.message)
                }else{
                    _status.postValue(response?.message)
                }
            } catch (e: Exception) {
                _status.postValue (e.message)
            }
        }
    }

    fun forgotPasswordVerifyOtp(email: String, otp: String){
        viewModelScope.launch {
            try {
                val apiResponse = userRepository.forgotPasswordVerifyOtp( email, otp)
                val response = apiResponse.body()
                if(response?.success == true) {
                    val status = response.status
                    _status.postValue(status)
                    _otpResult.postValue(response.data.user.otp.toString())
                    _email.postValue(response.data.user.email)
                    _msg.postValue(response.message)

                }else{
                    _status.postValue(response?.message)
                }
            } catch (e: Exception) {
                _status.postValue (e.message)
            }
        }
    }

    fun forgotPasswordResendOtp(email : String){
        viewModelScope.launch {
            try{
                val resendOtp = userRepository.forgotPasswordResendOtp(email)
                val response = resendOtp.body()
                if(response?.success == true){
                    val status = response.status
                    _status.postValue(status)
                    _otpResult.postValue(response.data.otp)
                    _msg.postValue(response.message)

                }else{
                    _status.postValue(response?.message)
                }
            } catch (e: Exception) {
                _status.postValue (e.message)
            }
        }
    }

    fun resetPassword(otp: String, email: String, password: String) {
        viewModelScope.launch {
            try{
                val apiResponse = userRepository.resetPassword(otp,email,password)
                val response = apiResponse.body()

                if( response?.success == true){
                    val status = response.status
                    _status.postValue(status)
                }else{
                    _status.postValue(response?.message)
                }
            } catch (e: Exception) {
                _status.postValue (e.message)
            }
        }
    }


}


