package com.example.todoapp

object Constants {
    var accessToken: String? = null
    var refreshToken: String? = null
    fun clearAccessToken() {
        accessToken = null
    }
    var name : String? = null
    var mobile : String? = null
    var emailP :String? = null

    fun clear(){
        name = null
        mobile = null
        emailP = null
    }
    var userEmail : String? = null
    var userOtp : String? = null

    var newOtp : String? = null

}
