package com.example.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Constants
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.interfaces.ApiInterface
import com.example.todoapp.responses.ProfileInfoResponse
import com.example.todoapp.responses.UpdateProfileResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class PersonalInformationViewModel : ViewModel(){
    private val apiInterface = RetrofitClient.getInstance()?.create(ApiInterface::class.java)

    private val _getResult = MutableLiveData<String>()
    val getResult : LiveData<String> get() = _getResult

    private val _updateUserResult = MutableLiveData<String>()
    val updateUserResult : LiveData<String> get() =_updateUserResult

    private val _msg = MutableLiveData<String>()
    val msg : LiveData<String> get() = _msg
    private val _name = MutableLiveData<String>()
    val name : LiveData<String> get() = _name
    private val _email = MutableLiveData<String>()
    val email : LiveData<String> get() = _email
    private val _mobile = MutableLiveData<String>()
    val mobile : LiveData<String> get() = _mobile

     fun get(){
        viewModelScope.launch {
            try {
                val apiResponse: Response<ProfileInfoResponse>? = apiInterface?.profileInfo()
                val response = apiResponse?.body()
                if(response?.success == true ){
                    val status = response.status
                    _getResult.postValue(status)
                    _msg.value = response.message
                     _name.value = response.data.user.name
                     _mobile.value = response.data.user.mobile_no.toString()
                     _email.value = response.data.user.email


                } else{
                    _getResult.value = response?.message
                }
            } catch (e : Exception){
                _getResult.value = e.message
            }
        }

    }

    fun updateUser(name : String, mobile : String){
        viewModelScope.launch {
            try {
                val apiResponse: Response<UpdateProfileResponse>? = apiInterface?.updateProfile(name,mobile)
                val response = apiResponse?.body()
                if(response?.success == true ){
                    val status = response.status.toString()
                    _updateUserResult.postValue(status)
                    Constants.clearAccessToken()
                    _msg.postValue(response.message)

                } else{
                    _updateUserResult.postValue(response?.message)
                }
            } catch (e : Exception){
                _updateUserResult.value = e.message
            }
        }
    }
}

