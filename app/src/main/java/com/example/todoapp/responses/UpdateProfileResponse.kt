package com.example.todoapp.responses

data class UpdateProfileResponse(
    val data: UPData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class UPData(
    val user: UPUser
)
data class UPUser(
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