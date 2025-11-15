package com.example.rationmanproject

import android.content.Context

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent ::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideauthinterceptor(@ApplicationContext context: Context): Interceptor {
        return authInterceptor(context)

    }

    @Provides
    @Singleton
    fun okhttpclient(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()

    }

    @Provides
    @Singleton
    fun instance(client: OkHttpClient): apiserviceinterface {

        return Retrofit.Builder()
            .baseUrl(apiconstants.base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(apiserviceinterface::class.java)

    }

    @Provides
    @Singleton
    fun provideReportFirebaseService(firestore: FirebaseFirestore): Reportfirebaseservice {
        return Reportfirebaseservice(firestore)
    }

    @Provides
    @Singleton
    fun repository(api: apiserviceinterface , reportService: Reportfirebaseservice): Repository {
        return Repository(api , reportService)

    }

    @Provides
    @Singleton
    fun getfirebaseinstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()


//    @Provides
//    @Singleton
//    fun subsidyapiinstance(client: OkHttpClient): apiserviceinterface {
//
//        return Retrofit.Builder()
//            .baseUrl(apiconstants.baseurlsubsidy)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//            .create()
//
//    }
}