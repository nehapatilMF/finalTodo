package com.example.todoapp.responses

data class ProfileInfoResponse(
    val data : DataModel,
    val message: String,
    val status: String,
    val success: Boolean
)

data class DataModel(
    val user: UserModel
)

data class UserModel(
    val name: String,
    val email: String,
    val mobile_no : Long,
    val id : Int
)