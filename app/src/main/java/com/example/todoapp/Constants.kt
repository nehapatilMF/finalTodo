package com.example.todoapp

object Constants {
    var accessToken: String? = null
    var refreshToken: String? = null
    fun clearAccessToken() {
        accessToken = null
    }
}