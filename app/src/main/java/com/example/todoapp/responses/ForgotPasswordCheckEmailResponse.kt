package com.example.todoapp.responses

data class ForgotPasswordCheckEmailResponse (
    val data: DataForgotPassword,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class DataForgotPassword(
    val otp: Long
)

