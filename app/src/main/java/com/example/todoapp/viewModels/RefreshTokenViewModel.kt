package com.example.todoapp.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.AuthTokens
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import com.example.todoapp.responses.RefreshTokenResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class RefreshTokenViewModel : ViewModel() {

    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)

    private val _result = MutableLiveData<String>()
    val result : LiveData<String> get() = _result

    private val _authTokens = MutableLiveData<AuthTokens>()

    fun getAuthTokens(): LiveData<AuthTokens> {
        return _authTokens
    }
    private fun saveTokens(accessToken: String, refreshToken: String) {
        val authTokens = AuthTokens(accessToken, refreshToken)
        _authTokens.postValue(authTokens)
    }

    @SuppressLint("SuspiciousIndentation")

    fun refreshToken(refresh_token: String){
        viewModelScope.launch{

            try {
                val loginResponse: Response<RefreshTokenResponse>? = apiInterface?.refreshToken(refresh_token)
                val response = loginResponse?.body()
                if(response?.success == true){
                    val status = response.status
                    _result.postValue(status)
                    val accessToken = response.data.token.access_token
                    val refreshToken = response.data.token.refresh_token
                    saveTokens(accessToken, refreshToken)

                } else{
                    _result.postValue(response?.message.toString())
                }
            } catch (e : Exception){
                _result.value = e.message
            }
        }
    }
}
