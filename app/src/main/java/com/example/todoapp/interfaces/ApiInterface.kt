package com.example.todoapp.interfaces

import com.example.todoapp.responses.ForgotPasswordCheckEmailResponse
import com.example.todoapp.responses.ForgotPasswordVerifyOtpResponse
import com.example.todoapp.responses.LoginResponse
import com.example.todoapp.responses.LogoutResponse
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
    ////Sign Up
    @FormUrlEncoded
    @POST("sign-up")
    suspend fun signup(
        @Field("name") name: String,
        @Field("mobile") mobile: Long,
        @Field("email") email: String,
        @Field("password") password: String): Response<SignupResponse>

    ////Sign Up Verify Otp
    @FormUrlEncoded
    @POST("signup-verify-otp")
    suspend fun signupVerifyOtp(
        @Field("email") email : String,
        @Field("otp") otp :Long
    ):Response<SignupVerifyOtpResponse>

    ////Resend User Otp
    @FormUrlEncoded
    @POST("resend-user-otp")
    suspend fun resendUserOtp(
        @Field("email") email : String
    ) : Response<ResendUserOtpResponse>

    ////Sign In
    @FormUrlEncoded
    @POST("sign-in")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    ////Forgot Password Request Otp
    @FormUrlEncoded
    @POST("forgot-password-check-email")
    suspend fun forgotPasswordRequestOtp(
        @Field("email") email : String
    ) : Response<ForgotPasswordCheckEmailResponse>

    ////Forgot Password Resend Otp
    @FormUrlEncoded
    @POST("resend-forgot-password-otp")
    suspend fun forgotPasswordResendOtp(
        @Field("email") email : String
    ) : Response<ResendForgotPasswordResponse>

    ////Forgot Password Verify Otp
    @FormUrlEncoded
    @POST("forgot-password-verify-otp")
    suspend fun forgotPasswordVerifyOtp(
        @Field("email") email :String,
        @Field("otp") otp :Long
    ) : Response<ForgotPasswordVerifyOtpResponse>

    ////Reset Password
    @FormUrlEncoded
    @POST("reset-password")
    suspend fun resetPassword(
        @Field("otp") otp : Long,
        @Field("email") email : String,
        @Field("password") password: String
    ):Response<ResetPasswordResponse>

    ////Logout

    @POST("logout")
    suspend fun logout():Response<LogoutResponse>
}


