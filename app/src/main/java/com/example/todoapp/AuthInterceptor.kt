package com.example.todoapp

import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(private val accessToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken") // Add the access token to the request headers
            .build()

        return chain.proceed(modifiedRequest)
    }
}