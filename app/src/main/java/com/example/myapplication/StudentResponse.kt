package com.example.myapplication

data class StudentsResponse(
    val status: String,
    val students: List<StudentResponse>
)
