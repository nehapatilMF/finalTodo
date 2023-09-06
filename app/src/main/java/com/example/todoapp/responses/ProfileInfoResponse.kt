package com.example.todoapp.responses

data class ProfileInfoResponse(
    val data: PIData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class PIData(
    val user: PIUser
)

data class PIUser(
    val id : Int,
    val name : String,
    val email : String,
    val mobile : Long
)