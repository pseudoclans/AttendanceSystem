package com.example.myapplication

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class StudentAttendanceDetailsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adminviewstudattendance_details)

        // Get any data passed through Intent
        val logId = intent.getStringExtra("log_id")
        val section = intent.getStringExtra("section")

        // Populate your layout elements as needed, e.g.:

        findViewById<EditText>(R.id.editTextText).setText(section) // Update EditText with section
    }
}