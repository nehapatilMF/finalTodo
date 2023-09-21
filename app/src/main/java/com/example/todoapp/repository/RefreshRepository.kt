package com.example.todoapp.repository

import com.example.todoapp.interfaces.RefreshTokenAPI

class RefreshRepository(private val refreshTokenAPI: RefreshTokenAPI) {
    suspend fun refreshToken(refreshToken: String) = refreshTokenAPI.refreshToken(refreshToken)
}