package com.example.rationmanproject

import retrofit2.Response

class AuthRepository(val apiservice : apiserviceinterface) {

    suspend fun login(email: String, password: String): Response<String> {
        return apiservice.login(AuthRequest(email, password))
    }

    suspend fun register(email: String, password: String): Response<String> {
        return apiservice.register(AuthRequest(email, password))
    }
}