package com.example.todoapp.responses

data class ResendForgotPasswordResponse (
    val data: DataResendForgotPassword,
    val message: String,
    val status: Long,
    val success: Boolean
)
data class DataResendForgotPassword(
    val otp: Long
)

