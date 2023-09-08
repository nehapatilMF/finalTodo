package com.example.todoapp.responses

data class ForgotPasswordVerifyOtpResponse(
    val data: DataForgotPasswordVerifyOtp,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class DataForgotPasswordVerifyOtp(
    val user: User1,
    val token : Token1
)
data class User1(
    val created_at: String,
    val email: String,
    val email_verified_at: Any?,
    val id: Int,
    val mobile_no: String,
    val name: String,
    val otp: Any?,
    val otp_expiry: Any?,
    val otp_verified: Int,
    val updated_at: String
)
data class Token1(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String
)