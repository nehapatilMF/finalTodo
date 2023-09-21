package com.example.todoapp.repository

import com.example.todoapp.interfaces.UserAPI

class UserRepository(private val userAPI: UserAPI) {

    suspend fun signup(name: String, mobile: String, email: String, password: String) = userAPI.signup(name, mobile, email, password)

    suspend fun signupVerifyOtp(email: String, otp: String) = userAPI.signupVerifyOtp(email, otp)

    suspend fun resendUserOtp(email: String) = userAPI.resendUserOtp(email)

    suspend fun login(email: String, password: String) = userAPI.login(email, password)

    suspend fun forgotPasswordRequestOtp(email: String) = userAPI.forgotPasswordRequestOtp(email)

    suspend fun forgotPasswordResendOtp(email: String) = userAPI.forgotPasswordResendOtp(email)

    suspend fun forgotPasswordVerifyOtp(email: String, otp: String) = userAPI.forgotPasswordVerifyOtp(email, otp)

    suspend fun resetPassword(otp: String, email: String, password: String) = userAPI.resetPassword(otp, email, password)



}