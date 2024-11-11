package com.example.myapplication

data class AdminData(
    val admin_id: Int, // Adjusted based on your JSON response
    val first_name: String,
    val last_name: String,
    val middle_name: String,
    val email: String,
    val user_id: Int // Adjusted from String to Int based on your JSON response
)