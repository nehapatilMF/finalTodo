package com.example.todoapp

object Constants {
    var accessToken: String? = null
    var refreshToken: String? = null
    fun clearAccessToken() {
        accessToken = null
    }
    fun clearRefreshToken() {
       refreshToken = null
    }
    var name : String? = null
    var mobile : String? = null
    var emailP :String? = null


    var userEmail : String? = null
    var userOtp : String? = null
   var displayName :String? = null
    var newOtp : String? = null

}
