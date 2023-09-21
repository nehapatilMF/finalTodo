package com.example.todoapp.interfaces

import com.example.todoapp.responses.TodoResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RefreshTokenAPI {
    ///RefreshToken
    @FormUrlEncoded
    @POST("refresh-token")
    suspend fun refreshToken(
        @Field("refresh_token") refreshToken : String
    ) : Response<TodoResponse>


}