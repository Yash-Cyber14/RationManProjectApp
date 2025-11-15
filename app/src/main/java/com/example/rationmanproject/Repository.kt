package com.example.rationmanproject


import kotlinx.coroutines.flow.Flow

class Repository(val apiservice : apiserviceinterface, val reportFirebaseService: Reportfirebaseservice){

    suspend fun getallshop() : List<apiresponse> {
        try {
            val shops=apiservice.getallshops()

            if(shops.isSuccessful){
                return shops.body()?: emptyList()

            }else return emptyList()
        }
        catch (e: Exception){
            return emptyList()

        }
    }
    suspend fun addReport(shopId: String, report: Report) {
        reportFirebaseService.addReport(shopId, report)
    }

    // ✅ Get reports as Flow from Firebase
    suspend fun getreports(shopId: String): Flow<List<Report>> {
        return reportFirebaseService.getreports(shopId)
    }

    suspend fun getsubsidydetails() : List<subsidyapiresponse>{

        try {
            val subsidy = apiservice.getsubsidydata()
            if(subsidy.isSuccessful)
            {
                return subsidy.body()?: emptyList()
            }
            else return emptyList()
        }
        catch (e: Exception)
        {return emptyList()}

    }

    suspend fun getSubsidyByAadhaar(aadharNumber: String): subsidyapiresponse? {
        val subsidyList = getsubsidydetails()
        return subsidyList.find { it.aadhaar == aadharNumber }
    }







}