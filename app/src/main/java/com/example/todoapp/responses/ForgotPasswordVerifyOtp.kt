package com.example.todoapp.responses

data class ForgotPasswordVerifyOtp (
    val email : String,
    val otp : Long
)
