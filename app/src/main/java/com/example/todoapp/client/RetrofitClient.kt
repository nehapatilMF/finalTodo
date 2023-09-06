package com.example.todoapp.client

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val baseUrl = "http://13.127.90.201/api/v1/"
    private var retrofit: Retrofit? = null

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor()) // Add your AuthInterceptor
            // Add other interceptors, if needed
            .build()
    }

    fun getInstance(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient) // Set the OkHttpClient with interceptors
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}
