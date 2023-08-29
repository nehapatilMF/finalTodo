package com.example.todoapp.client

import android.annotation.SuppressLint

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val baseUrl = ""
   private var retrofit: Retrofit? = null
    @SuppressLint("SuspiciousIndentation")
    fun getInstance():Retrofit? {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
               return retrofit

    }


}
