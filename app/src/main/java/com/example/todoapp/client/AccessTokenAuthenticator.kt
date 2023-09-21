package com.example.todoapp.client

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AccessTokenAuthenticator(private val sessionManager: SessionManager) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val accessToken = sessionManager.getAccessToken()
        if (!isRequestWithAccessToken(response) || accessToken == null) {
            return null
        }
        synchronized(this) {
            val newAccessToken = sessionManager.getAccessToken()!!
            if (accessToken != newAccessToken) {
                return newRequestWithAccessToken(response.request, newAccessToken)
            }

            val updatedAccessToken = sessionManager.getRefreshToken()!!
            return newRequestWithAccessToken(response.request, updatedAccessToken)
        }
    }

    private fun isRequestWithAccessToken(response: Response): Boolean {
        val header = response.request.header("Authorization")
        return header != null && header.startsWith("Bearer")
    }

    private fun newRequestWithAccessToken(request: Request, accessToken: String): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }
}