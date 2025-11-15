package com.example.rationmanproject


import retrofit2.Response
import retrofit2.http.GET

interface apiserviceinterface {

    @GET(apiconstants.endpoint)
    suspend fun getallshops() : Response<List<apiresponse>>

    @GET(apiconstants.endpointsubsidy)
    suspend fun getsubsidydata() : Response<List<subsidyapiresponse>>

}