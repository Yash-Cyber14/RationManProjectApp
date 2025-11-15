package com.example.rationmanproject

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class authInterceptor(val context: Context) : Interceptor {



    override fun intercept(chain: Interceptor.Chain): Response {

        val token = tokenmanager.gettoken(context)

        val newrequest = chain.request().newBuilder()

        if (!token.isNullOrBlank()) {
            newrequest.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(newrequest.build())





    }
}