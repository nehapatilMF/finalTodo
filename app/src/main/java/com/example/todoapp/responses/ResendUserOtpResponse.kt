package com.example.todoapp.responses

data class ResendUserOtpResponse(
    val data: DataResendOtp,
    val message: String,
    val status: Long,
    val success: Boolean
)
data class DataResendOtp(
    val otp: Long
)