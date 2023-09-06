package com.example.todoapp.client

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val baseUrl = "http://13.127.90.201/api/v1/"
    private var retrofit: Retrofit? = null
    fun getInstance(): Retrofit? {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit
    }
}
