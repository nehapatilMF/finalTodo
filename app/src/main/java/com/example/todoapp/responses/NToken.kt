package com.example.todoapp.responses

data class NToken(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String
)