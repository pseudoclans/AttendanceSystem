package com.example.myapplication


// this is the student data for inserting the student
data class StudentData(
    val username: String?, // New field for username
    val password: String?,
    val role : String?,
val student_id: String,
val last_name: String,
val first_name: String,
val middle_name: String?, // Optional
val email: String,
val contact_number: String?,
val address: String?,
val student_type: Int?, // e.g., "Undergraduate", "Graduate", etc.
val student_status: Char? // e.g., "Active", "Inactive"

)
