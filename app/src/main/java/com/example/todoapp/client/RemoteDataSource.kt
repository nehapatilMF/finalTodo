package com.example.todoapp.client

import com.android.volley.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    companion object {
        private const val base_url = "http://13.127.90.201/api/v1/"
    }

    fun <Api> buildApi(api: Class<Api>, authToken: String): Api {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $authToken")
                    .build()
                chain.proceed(request)
            }

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            client.addInterceptor(logging)
        }

        return Retrofit.Builder()
            .baseUrl(base_url)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}
