package com.example.todoapp.responses

data class RefreshTokenResponse (
    val data : RefData,
    val message: String,
    val status: String,
    val success : Boolean
    )
 data class RefData(
     val token: newToken
 )

data class newToken(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String
)
