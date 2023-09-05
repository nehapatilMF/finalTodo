package com.example.todoapp.responses

data class ResetPasswordResponse (
    val status : Long,
    val success : Boolean,
    val message : String,
    val data : DataResetPassword
)
data class DataResetPassword(
    val data : String
)
