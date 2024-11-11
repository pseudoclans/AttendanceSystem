package com.example.myapplication

data class ProfessorResponse (
    val professor_id: Int,
    val first_name: String?,
    val last_name: String?,
     val middle_name: String?,
    val email: String?,
    val user_id: Int?
)