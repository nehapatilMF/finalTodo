package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.AuthTokens
import com.example.todoapp.repository.RefreshRepository
import kotlinx.coroutines.launch

class RefreshTokenViewModel(private val refreshRepository: RefreshRepository) : ViewModel() {
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status
    private val _authTokens = MutableLiveData<AuthTokens>()

    fun getAuthTokens(): LiveData<AuthTokens> {
        return _authTokens
    }

    private fun saveTokens(accessToken: String, refreshToken: String) {
        val authTokens = AuthTokens(accessToken, refreshToken)
        _authTokens.postValue(authTokens)
    }

    fun refreshToken(refreshToken: String){

        viewModelScope.launch{

            try {
                val apiResponse = refreshRepository.refreshToken(refreshToken)
                val response = apiResponse.body()
                if(response?.success == true){
                    val status = response.status
                    _status.postValue(status)
                    val accessToken = response.data.token.access_token
                    val refreshToken1 = response.data.token.refresh_token
                    saveTokens(accessToken, refreshToken1)

                } else{
                    _status.postValue(response?.message.toString())
                }
            } catch (e : Exception){
                _status.value = e.message
            }
        }
    }
}