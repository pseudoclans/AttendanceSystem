package com.example.myapplication

data class AdminViewStudentAttendanceData(
    val log_id: Int,
    val log_date: String,
    val time_in: String?,
    val time_out: String?,
    val status: String?,
    val student_id: String,
    val school_year: String,
    val year: Int,
    val section: String?,
    val time_start: String,
    val time_end: String,
    val professor_id: Int,
    val professor_last_name: String,
    val professor_first_name: String,
    val professor_middle_name: String?,
    val professor_email: String,
    val course_id: Int,
    val course_name: String,
    val course_code: String,
    val course_description: String,
    val program_id: Int,
    val program_name: String,
    val program_code: String

)