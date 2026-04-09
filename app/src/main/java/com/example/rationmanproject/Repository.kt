package com.example.rationmanproject


class Repository(
    private val apiservice: apiserviceinterface
) {

    // ✅ SAME NAME (getallshop)
    suspend fun getallshop(): List<apiresponse> {
        return try {
            val response = apiservice.getAllShops()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ✅ SAME NAME (addReport)
    suspend fun addReport(shopId: String, report: Report): Report? {
        return try {
            val response = apiservice.addReport(shopId, report)
            if (response.isSuccessful) {
                response.body()
            } else null
        } catch (e: Exception) {
            null
        }
    }

    // ✅ SAME NAME (getreports)
    suspend fun getreports(shopId: String): List<Report> {
        return try {
            val response = apiservice.getReports(shopId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ✅ SAME NAME (getsubsidydetails)
//    suspend fun getsubsidydetails(): List<subsidyapiresponse> {
//        return try {
//            val response = apiservice.getsubsidydata()
//            if (response.isSuccessful) {
//                response.body() ?: emptyList()
//            } else emptyList()
//        } catch (e: Exception) {
//            emptyList()
//        }
//    }

    // ✅ SAME NAME (getSubsidyByAadhaar) — FIXED LOGIC
    suspend fun getSubsidyByAadhaar(aadharNumber: String): subsidyapiresponse? {
        return try {
            val response = apiservice.getSubsidy(aadharNumber)
            if (response.isSuccessful) {
                response.body()
            } else null
        } catch (e: Exception) {
            null
        }
    }
}




//class Repository(val apiservice : apiserviceinterface, val reportFirebaseService: Reportfirebaseservice){
//
//    suspend fun getallshop() : List<apiresponse> {
//        try {
//            val shops=apiservice.getallshops()
//
//            if(shops.isSuccessful){
//                return shops.body()?: emptyList()
//
//            }else return emptyList()
//        }
//        catch (e: Exception){
//            return emptyList()
//
//        }
//    }
//    suspend fun addReport(shopId: String, report: Report) {
//        reportFirebaseService.addReport(shopId, report)
//    }
//
//    // ✅ Get reports as Flow from Firebase
//    suspend fun getreports(shopId: String): Flow<List<Report>> {
//        return reportFirebaseService.getreports(shopId)
//    }
//
//    suspend fun getsubsidydetails() : List<subsidyapiresponse>{
//
//        try {
//            val subsidy = apiservice.getsubsidydata()
//            if(subsidy.isSuccessful)
//            {
//                return subsidy.body()?: emptyList()
//            }
//            else return emptyList()
//        }
//        catch (e: Exception)
//        {return emptyList()}
//
//    }
//
//    suspend fun getSubsidyByAadhaar(aadharNumber: String): subsidyapiresponse? {
//        val subsidyList = getsubsidydetails()
//        return subsidyList.find { it.aadhaar == aadharNumber }
//    }
//
//
//
//
//
//
//
//}