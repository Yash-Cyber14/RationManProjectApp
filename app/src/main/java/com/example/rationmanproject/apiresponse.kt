package com.example.rationmanproject

data class apiresponse(
    val address: String,
    val city: String,
    val id: String,
    val lastUpdated: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val phone: String,
    val stock: Stock,
    val timings: String
)

data class Stock(
    val dal: Boolean,
    val kerosene: Boolean,
    val oil: Boolean,
    val rice: Boolean,
    val salt: Boolean,
    val sugar: Boolean,
    val wheat: Boolean
)

data class shopswithdistance (
    val apiresponse: apiresponse,
    val distance : Float
)

data class Report(
    val username : String = "",
    val message : String = "",
    val timestamp : com.google.firebase.Timestamp? = null

)