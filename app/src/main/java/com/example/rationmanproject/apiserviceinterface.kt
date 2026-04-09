package com.example.rationmanproject


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface apiserviceinterface {

    @GET("/shops")
    suspend fun getAllShops(): Response<List<apiresponse>>

    @POST("/shops/bulk")
    suspend fun addAllShops(@Body shops: List<apiresponse>): Response<List<apiresponse>>

    @POST("/subsidy/bulk")
    suspend fun addAllSubsidy(@Body subsidies: List<subsidyapiresponse>): Response<List<subsidyapiresponse>>

    @GET("/subsidy/{aadhaar}")
    suspend fun getSubsidy(@Path("aadhaar") aadhaar: String): Response<subsidyapiresponse>

    @POST("/shops/{shopId}/reports")
    suspend fun addReport(
        @Path("shopId") shopId: String,
        @Body report: Report
    ): Response<Report>

    @GET("/shops/{shopId}/reports")
    suspend fun getReports(
        @Path("shopId") shopId: String
    ): Response<List<Report>>

    @POST("/login")
    suspend fun login(@Body request: AuthRequest): Response<String>

    @POST("/register")
    suspend fun register(@Body request: AuthRequest): Response<String>

}