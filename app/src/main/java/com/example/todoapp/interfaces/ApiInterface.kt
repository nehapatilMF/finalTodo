package com.example.todoapp.interfaces

import com.example.todoapp.responses.ForgotPasswordCheckEmailResponse
import com.example.todoapp.responses.ForgotPasswordVerifyOtp
import com.example.todoapp.responses.LoginResponse
import com.example.todoapp.responses.ResendForgotPasswordResponse
import com.example.todoapp.responses.ResendUserOtpResponse
import com.example.todoapp.responses.ResetPasswordResponse
import com.example.todoapp.responses.SignupResponse
import com.example.todoapp.responses.SignupVerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {
    @FormUrlEncoded
    @POST("sign-up")
    suspend fun signup(
        @Field("name") name: String,
        @Field("mobile") mobile: Long,
        @Field("email") email: String,
        @Field("password") password: String): Response<SignupResponse>

    @FormUrlEncoded
    @POST("signup-verify-otp")
    suspend fun signupVerifyOtp(
        @Field("email") email : String,
        @Field("otp") otp :Long
    ):Response<SignupVerifyOtpResponse>

    @FormUrlEncoded
    @POST("resend-user-otp")
    suspend fun resendUserOtp(
        @Field("email") email : String
    ) : Response<ResendUserOtpResponse>

    @FormUrlEncoded
    @POST("sign-in")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("forgot-password-check-email")
    suspend fun forgotPasswordRequestOtp(
        @Field("email") email : String
    ) : Response<ForgotPasswordCheckEmailResponse>

    @FormUrlEncoded
    @POST("resend-forgot-password-otp")
    suspend fun forgotPasswordResendOtp(
        @Field("email") email : String
    ) : Response<ResendForgotPasswordResponse>

    @FormUrlEncoded
    @POST("forgot-password-verify-otp")
    suspend fun forgotPasswordVerifyOtp(
        @Field("email") email :String,
        @Field("otp") otp :Long
    ) : Response<ForgotPasswordVerifyOtp>

    @FormUrlEncoded
    @POST("reset-password")
    suspend fun resetPassword(
    @Field("opt") otp : Long,
    @Field("email") email : String,
    @Field("password") password: String
    ):Response<ResetPasswordResponse>



}


