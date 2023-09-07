package com.example.todoapp.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.AuthTokens
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import com.example.todoapp.responses.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Response



class LoginViewModel : ViewModel() {

    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)

    private val _loginResult = MutableLiveData<String>()
    val loginResult :  LiveData<String> get() = _loginResult

    private val _authTokens = MutableLiveData<AuthTokens>()

    fun getAuthTokens(): LiveData<AuthTokens> {
        return _authTokens
    }
    private fun saveTokens(accessToken: String, refreshToken: String) {
        val authTokens = AuthTokens(accessToken, refreshToken)
        _authTokens.postValue(authTokens)
    }

    @SuppressLint("SuspiciousIndentation")

    fun login(email : String, password : String){
        viewModelScope.launch {

                try {
                    val loginResponse: Response<LoginResponse>? = apiInterface?.login(email,password)
                    val response = loginResponse?.body()
                    if(loginResponse?.isSuccessful == true ){
                        val status = response?.status.toString()
                        _loginResult.postValue(status)
                        val accessToken = response?.data?.token?.access_token.toString()

                        val refreshToken = response?.data?.token?.refresh_token.toString()
                        saveTokens(accessToken, refreshToken)

                    } else{
                        _loginResult.value = response?.message
                    }
                } catch (e : Exception){
                    _loginResult.value = e.message
                }
            }
        }
    }


