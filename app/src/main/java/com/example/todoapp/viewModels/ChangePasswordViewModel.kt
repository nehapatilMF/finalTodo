package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import kotlinx.coroutines.launch

class ChangePasswordViewModel : ViewModel() {
    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)
    private val _changePasswordResult = MutableLiveData<String>()
    val changePasswordResult : LiveData<String> get() =  _changePasswordResult

    fun changePassword(oldPassword : String,
                      newPassword : String){
        viewModelScope.launch {
            try{
                val apiResponse = apiInterface?.changePassword(oldPassword,newPassword)
                val response = apiResponse?.body()
                if( response?.success == true){
                    val status = response.status.toString()
                    _changePasswordResult.postValue(status)

                }else{
                    _changePasswordResult.postValue(response?.message)
                }
            } catch (e: Exception) {
                _changePasswordResult.postValue (e.message)
            }
        }
    }
}
