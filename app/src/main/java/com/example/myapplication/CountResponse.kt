package com.example.myapplication

data class CountResponse(
    val student_status_counts: List<StudentStatusCount>,
    val total_student_count: Int,
    val total_professor_count: Int
)