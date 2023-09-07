package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Constants
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import com.example.todoapp.responses.DeleteUserResponse
import com.example.todoapp.responses.LogoutResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)

    private val _logoutResult = MutableLiveData<String>()
    val logoutResult : LiveData<String> get() = _logoutResult

    private val _deleteUserResult = MutableLiveData<String>()
    val deleteUserResult : LiveData<String> get() =_deleteUserResult

    fun logout(){
    viewModelScope.launch {
            try {
                val apiResponse: Response<LogoutResponse>? = apiInterface?.logout()
                val response = apiResponse?.body()
                if(apiResponse?.isSuccessful == true ){
                    val status = response?.status.toString()
                    _logoutResult.postValue(status)
                    Constants.clearAccessToken()
                } else{
                    _logoutResult.value = response?.message
                }
            } catch (e : Exception){
                _logoutResult.value = e.message
            }
        }

    }

    fun deleteUser(){
        viewModelScope.launch {
            try {
                val apiResponse: Response<DeleteUserResponse>? = apiInterface?.deleteUser()
                val response = apiResponse?.body()
                if(apiResponse?.isSuccessful == true ){
                    val status = response?.status.toString()
                    _deleteUserResult.postValue(status)
                    Constants.clearAccessToken()
                } else{
                    _deleteUserResult.value = response?.message
                }
            } catch (e : Exception){
                _deleteUserResult.value = e.message
            }
        }
    }
}
