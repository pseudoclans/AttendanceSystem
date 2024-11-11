package com.example.myapplication

data class AdminViewStudentAttendanceResponse (
    val status: String,
    val data: List<AdminViewStudentAttendanceData>?
    )