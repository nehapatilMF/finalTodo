package com.example.todoapp.responses

data class ForgotPasswordVerifyOtpResponse(
    val data: DataForgotPasswordVerifyOtp,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class DataForgotPasswordVerifyOtp(
    val user: User1
)
data class User1(
    val created_at: String,
    val email: String,
    val id: Int,
    val otp: Int,
    val updated_at: String
)