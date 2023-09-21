package com.example.todoapp.responses

data class NUser(
    val otp: Int,
    val id: Int,
    val name: String,
    val email: String,
    val mobile_no: String,
    val email_verified_at: Any?,
    val otp_expiry: Any?,
    val otp_verified: Int,
    val created_at: String,
    val updated_at: String
)