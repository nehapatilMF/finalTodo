package com.example.todoapp.responses

data class SignupResponse(
    val data: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class Data(
    val otp: Long
)