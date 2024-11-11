package com.example.myapplication

data class StudentResponse(
    val student_id: String,
    val last_name: String,
    val first_name: String,
    val middle_name: String,
    val email: String,
    val contact_number: String,
    val address: String,
    val student_type: Int,
    val student_status: String,

)
