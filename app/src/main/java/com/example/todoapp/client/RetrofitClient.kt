package com.example.todoapp.client

import com.example.todoapp.MyApplication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val baseUrl = "http://13.127.90.201/api/v1/"

    private val sessionManager: SessionManager by lazy {
        SessionManager(MyApplication.getAppContext())
    }

    private val authInterceptor: AuthInterceptor by lazy {
        AuthInterceptor(sessionManager)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            //.authenticator(AccessTokenAuthenticator(sessionManager))
            .addInterceptor(authInterceptor)
            .build()
    }

    private var retrofit: Retrofit? = null

    fun getInstance(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}
