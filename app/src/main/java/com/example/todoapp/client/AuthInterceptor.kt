package com.example.todoapp.client

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = sessionManager.getAccessToken()

        return if (accessToken != null) {
            val modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
            chain.proceed(modifiedRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}
