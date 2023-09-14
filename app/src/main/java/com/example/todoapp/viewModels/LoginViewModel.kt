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

    private val _name = MutableLiveData<String>()
    val name :  LiveData<String> get() = _name

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
                if(response?.success == true){
                    val status = response.status.toString()
                    _loginResult.postValue(status)
                    _name.postValue(response.data.user.name)
                    val accessToken = response.data.token.access_token
                    val refreshToken = response.data.token.refresh_token

                    saveTokens(accessToken, refreshToken)

                } else{
                    _loginResult.postValue(response?.message.toString())
                }
            } catch (e : Exception){
                _loginResult.value = e.message
            }
        }
    }
}


