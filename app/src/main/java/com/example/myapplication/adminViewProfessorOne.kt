package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class adminViewProfessorOne : AppCompatActivity() {

    private lateinit var attendanceRecyclerView: RecyclerView
    private lateinit var attendanceAdapter: AdminViewProfessorStudentAttendanceAdapter
    private var cachedProfessor: List<AdminViewProfessorStudentAttendanceData>? = null  // Cached professor attendance data

    private lateinit var searchAttendance: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adminview_professorone)

        enableEdgeToEdge()

        // Initialize views
        attendanceRecyclerView = findViewById(R.id.attendanceRecyclerViews)
        attendanceRecyclerView.layoutManager = LinearLayoutManager(this)

        searchAttendance = findViewById(R.id.PsearchAttendance)
        initSearchAttendance()

        fetchapiprofstudent()
        fetchProfessorAttendance("1")
    }


    private fun fetchapiprofstudent(){
        ApiClient.getProfessorStudentData("1") { studentData ->
            if (studentData != null) {
                // Log the student data just before setting the RecyclerView
                Log.d("Api Client", "Received student data: $studentData")

                // Set up RecyclerView

            } else {
                // Handle error case
                Log.e("Api client", "No data or error fetching data")
            }
        }

    }

    private fun enableEdgeToEdge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = window.insetsController
            windowInsetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
                it.hide(WindowInsets.Type.navigationBars())
            }
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }



    private fun initSearchAttendance() {
        searchAttendance.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                val filteredList = cachedProfessor?.filter {
                    it.log_date.contains(query, ignoreCase = true) ||
                            it.time_in?.contains(query, ignoreCase = true) == true ||
                            it.time_out?.contains(query, ignoreCase = true) == true ||
                            it.status?.contains(query, ignoreCase = true) == true ||
                            it.section?.contains(query, ignoreCase = true) == true
                } ?: emptyList()

                attendanceAdapter.updateListed(filteredList)
            }
        })
    }


    private fun fetchProfessorAttendance(professorId: String) {
        if (professorId.isNotEmpty()) {
            ApiClient.getProfessorAttendance(professorId) { attendanceData ->
                runOnUiThread {
                    if (attendanceData != null && attendanceData.isNotEmpty()) {
                        cachedProfessor = attendanceData // Assign fetched data to cachedProfessor
                        updateRecyclerView(attendanceData) // Update the RecyclerView with the fetched data
                    } else {
                        Toast.makeText(this, "No attendance records found.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Log.e("FETCH", "Professor ID is empty")
            Toast.makeText(this, "Error: Professor ID is empty.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateRecyclerView(data: List<AdminViewProfessorStudentAttendanceData>) {
        // Make sure to pass the correct data type here
        attendanceAdapter = AdminViewProfessorStudentAttendanceAdapter(data) { attendanceData ->
            displayAttendanceDetails(attendanceData)
        }

        attendanceRecyclerView.adapter = attendanceAdapter
    }

    fun displayAttendanceDetails(attendanceData: AdminViewProfessorStudentAttendanceData) {
        // Implement the logic to display detailed attendance information
        // Example: Find views by ID and set text based on the clicked attendance item
        // findViewById<TextView>(R.id.textView8).text = attendanceData.log_date
    }
}
