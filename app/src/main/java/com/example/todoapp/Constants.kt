package com.example.todoapp

object Constants {
    var accessToken: String? = null
    var refreshToken: String? = null
    fun clearAccessToken() {
        accessToken = null
    }


    var fpEmail : String? = null
    fun getEmail(){
        this.fpEmail
    }
    fun clearEmail(){
        fpEmail = null
    }

    var fpOtp : String? = null
    fun setfpOtp (){
        this.fpOtp
    }
    fun clearfpOtp(){
        fpOtp = null
    }
    }
