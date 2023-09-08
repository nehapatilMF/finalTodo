package com.example.todoapp.interfaces

import com.example.todoapp.responses.AddTodoResponse
import com.example.todoapp.responses.ChangePasswordResponse
import com.example.todoapp.responses.DeleteTodoResponse
import com.example.todoapp.responses.DeleteUserResponse
import com.example.todoapp.responses.ForgotPasswordCheckEmailResponse
import com.example.todoapp.responses.ForgotPasswordVerifyOtpResponse
import com.example.todoapp.responses.GetTodoListResponse
import com.example.todoapp.responses.LoginResponse
import com.example.todoapp.responses.LogoutResponse
import com.example.todoapp.responses.ProfileInfoResponse
import com.example.todoapp.responses.ResendForgotPasswordResponse
import com.example.todoapp.responses.ResendUserOtpResponse
import com.example.todoapp.responses.ResetPasswordResponse
import com.example.todoapp.responses.SignupResponse
import com.example.todoapp.responses.SignupVerifyOtpResponse
import com.example.todoapp.responses.UpdateProfileResponse
import com.example.todoapp.responses.UpdateTodoResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    ////Delete User
    @POST("profile/delete")
    suspend fun deleteUser():Response<DeleteUserResponse>

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
    ) : Response<AddTodoResponse>

    ////Update
    @FormUrlEncoded
    @POST("todo/update/{id}")
    suspend fun updateTodo(
        @Path("id") id : String,
        @Field("title") title:String,
        @Field("description") description : String,
        @Field("status") status : Int,
        @Field("todo_date") todo_date : String,
        @Field("todo_time") todo_time : String
    ) : Response<UpdateTodoResponse>

    ////Delete
    @DELETE("todo/delete/{id}")
    suspend fun deleteTodo(
        @Path("id") id :String
    ) : Response<DeleteTodoResponse>

    ////Profile information
    @GET("profile-info")
    suspend fun profileInfo() : Response<ProfileInfoResponse>

    ////Update Profile Information
    @FormUrlEncoded
    @POST("profile-update")
    suspend fun updateProfile(
        @Field("name") name : String,
        @Field("mobile") mobile : Long
    ):Response<UpdateProfileResponse>

    ////Profile changes Password
    @FormUrlEncoded
    @POST("profile/change-password")
    suspend fun changePassword(
        @Field("old_password")  old_password : String,
        @Field("password") password : String
    ) : Response<ChangePasswordResponse>

}


