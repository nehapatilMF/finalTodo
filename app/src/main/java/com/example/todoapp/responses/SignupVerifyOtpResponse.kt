package com.example.todoapp.responses

data class SignupVerifyOtpResponse(
    val data: Data1,
    val message: String,
    val status: Int,
    val success: Boolean
)
data class Data1(
    val token: Token,
    val user: User
)
data class Token(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String
)

data class User(
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
