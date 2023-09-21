package com.example.todoapp.interfaces

import com.example.todoapp.responses.GetTodoListResponse
import com.example.todoapp.responses.TodoResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TodoAPI {

    ////Logout
    @POST("logout")
    suspend fun logout(): Response<TodoResponse>

    ////Delete User
    @POST("profile/delete")
    suspend fun deleteUser(): Response<TodoResponse>

    ///Get List
    @GET("todo/list")
    suspend fun getTodoList(): Response<GetTodoListResponse>

    ////Add
    @FormUrlEncoded
    @POST("todo/add")
    suspend fun addTodo(
        @Field("title") title:String,
        @Field("description") description : String,
        @Field("todo_date") todo_date : String,
        @Field("todo_time") todo_time : String,
        @Field("status") status : Int
    ) : Response<TodoResponse>

    ////Update
    @FormUrlEncoded
    @POST("todo/update/{id}")
    suspend fun updateTodo(
        @Path("id") id: String,
        @Field("title") title:String,
        @Field("description") description: String,
        @Field("status") status: Int,
        @Field("todo_date") todo_date: String,
        @Field("todo_time") todo_time: String
    ) : Response<TodoResponse>

    ////Delete
    @DELETE("todo/delete/{id}")
    suspend fun deleteTodo(
        @Path("id") id :String
    ) : Response<TodoResponse>

    ////Profile information
    @GET("profile-info")
    suspend fun profileInfo() : Response<TodoResponse>

    ////Update Profile Information
    @FormUrlEncoded
    @POST("profile-update")
    suspend fun updateProfile(
        @Field("name") name: String,
        @Field("mobile") mobile: String
    ): Response<TodoResponse>

    ////Profile changes Password
    @FormUrlEncoded
    @POST("profile/change-password")
    suspend fun changePassword(
        @Field("old_password")  old_password : String,
        @Field("password") password : String
    ) : Response<TodoResponse>


}