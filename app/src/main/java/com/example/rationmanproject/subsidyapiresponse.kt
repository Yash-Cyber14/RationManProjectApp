package com.example.rationmanproject

data class subsidyapiresponse(
    val aadhaar: String,
    val eligible: Boolean,
    val entitlements: String,
    val monthlyLimit: String,
    val remaining: String,
    val scheme: String,
    val state: String,
    val used: String,
    val validUntil: String
)