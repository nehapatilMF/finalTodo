package com.example.todoapp

object Constants {
    var accessToken: String? = null
    var refreshToken: String? = null
    fun clearAccessToken() {
        accessToken = null
    }

    var userEmail : String? = null
    var userOtp : String? = null
    var resenOtp : String? = null
}
