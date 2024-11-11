package com.example.myapplication

data class InsertStudentResponse(
    val status: String,
    val message: String,
    val data: StudentData? // You can include additional data if needed
)