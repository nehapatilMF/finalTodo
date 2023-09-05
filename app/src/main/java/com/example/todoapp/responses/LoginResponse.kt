package com.example.todoapp.responses

data class LoginResponse(
    val status: Long,
    val success: Boolean,
    val message: String,
    val data : Data2
)
data class Data2(
    val token: Tokens,
    val user: UserData
)

data class Tokens(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String
)
data class UserData(

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


