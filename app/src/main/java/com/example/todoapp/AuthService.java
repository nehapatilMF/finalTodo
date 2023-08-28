package com.example.todoapp;


interface AuthService {}
/**    @POST()
      fun getAccessToken(@Body requestBody:RequestBody): Call<AccessTokenResponse>

 @POST("oauth/refresh_token")
 fun refreshAccessToken(@Body requestBody: RequestBody): Call<AccessTokenResponse>


//Store Tokens Securely:
val sharedPreferences = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
val editor = sharedPreferences.edit()
editor.putString("access_token", accessToken)
editor.putString("refresh_token", refreshToken)
editor.apply()


//Use Access Token for API Requests

val accessToken = sharedPreferences.getString("access_token", "")
// Add the access token to the request headers
val request = Request.Builder()
.addHeader("Authorization", "Bearer $accessToken")
.url("https://api.example.com/resource")
.build()


**/
