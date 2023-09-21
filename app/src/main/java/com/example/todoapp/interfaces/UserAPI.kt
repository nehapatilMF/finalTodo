package com.example.todoapp.interfaces

import com.example.todoapp.responses.TodoResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserAPI {

    @FormUrlEncoded
    @POST("sign-up")
    suspend fun signup(
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("email") email: String,
        @Field("password") password: String): Response<TodoResponse>

    ////Sign Up Verify Otp
    @FormUrlEncoded
    @POST("signup-verify-otp")
    suspend fun signupVerifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: String
    ):Response<TodoResponse>

    ////Resend User Otp
    @FormUrlEncoded
    @POST("resend-user-otp")
    suspend fun resendUserOtp(
        @Field("email") email : String
    ) : Response<TodoResponse>

    ////Sign In
    @FormUrlEncoded
    @POST("sign-in")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<TodoResponse>

    ////Forgot Password Request Otp
    @FormUrlEncoded
    @POST("forgot-password-check-email")
    suspend fun forgotPasswordRequestOtp(
        @Field("email") email : String
    ) : Response<TodoResponse>

    ////Forgot Password Resend Otp
    @FormUrlEncoded
    @POST("resend-forgot-password-otp")
    suspend fun forgotPasswordResendOtp(
        @Field("email") email : String
    ) : Response<TodoResponse>

    ////Forgot Password Verify Otp
    @FormUrlEncoded
    @POST("forgot-password-verify-otp")
    suspend fun forgotPasswordVerifyOtp(
        @Field("email") email:String,
        @Field("otp") otp: String
    ) : Response<TodoResponse>

    ////Reset Password
    @FormUrlEncoded
    @POST("reset-password")
    suspend fun resetPassword(
        @Field("otp") otp: String,
        @Field("email") email: String,
        @Field("password") password: String
    ):Response<TodoResponse>




}