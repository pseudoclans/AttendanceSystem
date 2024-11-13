package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class adminViewProfessorOne : AppCompatActivity() {

    private lateinit var attendanceRecyclerView: RecyclerView
    private lateinit var attendanceAdapter: AdminViewProfessorStudentAttendanceAdapter
    private var cachedProfessor: List<AdminViewProfessorStudentAttendanceData>? = null  // Cached professor attendance data

    private lateinit var profStudentRecycleView : RecyclerView
    private lateinit var profstudentAdapter : AdminViewProfessorStudentAdapter
    private var cachedStudentList : List<AdminViewProfessorStudentData>?  = null

    private lateinit var searchStudent : EditText
    private lateinit var searchAttendance: EditText

        private lateinit var courseName : TextView


    private lateinit var attendancedetailInfo: View

    private lateinit var buttonCloseA : Button


    private lateinit var statusAutoCompleteTextView: AutoCompleteTextView
    private lateinit var typeAutoCompleteTextView: AutoCompleteTextView



    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var middleNameEditText: EditText
    private lateinit var contactEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var studentNumberText: TextView

    private lateinit var student_user_layout : View
    private lateinit var close_btn_info : Button

    private lateinit var viewattendanceBtn : Button

    private lateinit var studentLayout : View
    private lateinit var attendanceLayout : View
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adminview_professorone)

        firstNameEditText = findViewById(R.id.firstName)
        lastNameEditText = findViewById(R.id.lastName)
        middleNameEditText = findViewById(R.id.middleName)
        contactEditText = findViewById(R.id.contact)
        emailEditText = findViewById(R.id.email)
        addressEditText = findViewById(R.id.address)
        studentNumberText = findViewById(R.id.student_number)

        student_user_layout = findViewById(R.id.student_user_layout)
        close_btn_info = findViewById(R.id.close_btn_info)

        studentLayout = findViewById(R.id.studentLayout)
        attendanceLayout = findViewById(R.id.attendanceLayout)

        viewattendanceBtn = findViewById(R.id.viewattendanceBtn)


        statusAutoCompleteTextView = findViewById(R.id.status)
        typeAutoCompleteTextView = findViewById(R.id.type)
        enableEdgeToEdge()

        buttonCloseA = findViewById(R.id.buttonCloseA)
        attendancedetailInfo = findViewById(R.id.detailsA)

        // Initialize views
        attendanceRecyclerView = findViewById(R.id.attendanceRecyclerViews)
        attendanceRecyclerView.layoutManager = LinearLayoutManager(this)

        profStudentRecycleView = findViewById(R.id.profStudentRecycleView)
        profStudentRecycleView.layoutManager = LinearLayoutManager(this)

        searchAttendance = findViewById(R.id.PsearchAttendance)
        courseName = findViewById(R.id.status_header)
        courseName.setText("Course")
        searchStudent = findViewById(R.id.ad_psearchStudent)
        findViewById<TextView>(R.id.sectionHeader).text = "Program"

        initSearchAttendance()

        fetchapiprofstudent()
        fetchProfessorAttendance("1")

        fetchProfessorStudentData("1")
        initSearchStudent()

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



    private fun setStatus(status: String?) {

        val displayValue = when (status) {
            "G" -> "Graduated"
            "E" -> "Enrolled"
            "D" -> "Drop Out"
            "T" -> "Transferred"
            else -> ""
        }
        // Set the value to the AutoCompleteTextView
        statusAutoCompleteTextView.setText(displayValue, false)
    }

    private fun setType(type: String?) {
        val displayValue = when (type){
            "1" -> "Regular"
            "2" -> "Irregular"
            else -> "21"

        }

        // set type to the autocompletete
        typeAutoCompleteTextView.setText(displayValue, false)
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

    private fun initSearchStudent() {
        searchStudent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                val filteredList = cachedStudentList?.filter {
                    it.student_id.contains(query, ignoreCase = true) ||
                            it.student_email.contains(query, ignoreCase = true)  ||
                            it.course_name.contains(query, ignoreCase = true)  ||

                            it.section.contains(query, ignoreCase = true)
                } ?: emptyList()

                profstudentAdapter.updateListed(filteredList)
            }
        })
    }

    private fun fetchProfessorStudentData(professorId: String) {
        if (professorId.isNotEmpty()) {
            ApiClient.getProfessorStudentData(professorId) { attendanceData ->
                runOnUiThread {
                    if (attendanceData != null && attendanceData.isNotEmpty()) {
                        cachedStudentList = attendanceData // Assign fetched data to cachedProfessor
                        updateStudentRecyclerView(attendanceData) // Update the RecyclerView with the fetched data
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

    private fun updateStudentRecyclerView(data: List<AdminViewProfessorStudentData>) {
        // Make sure to pass the correct data type here
        profstudentAdapter = AdminViewProfessorStudentAdapter(data) { attendanceData ->
            displayStudentDetails(attendanceData)
        }

        profStudentRecycleView.adapter = profstudentAdapter
    }


    fun displayStudentDetails(attendanceData: AdminViewProfessorStudentData) {

        toggleVisibility(student_user_layout)


        viewattendanceBtn.setOnClickListener{
            toggleVisibility(student_user_layout)
            toggleVisibility(attendanceLayout)
            toggleVisibility(studentLayout)
            filterStudents(attendanceData.student_id)

        }

        close_btn_info.setOnClickListener{
            toggleVisibility(student_user_layout)
        }
        // Assume the API returns a student response with all the required data
        ApiClient.fetchStudents(studentNumber = attendanceData.student_id) { students ->
            runOnUiThread{
            if (students != null && students.isNotEmpty()) {
                val student = students[0] // Assume only one student is returned based on the student ID

                // Set the data to the EditText fields
                firstNameEditText.setText(student.first_name)
                lastNameEditText.setText(student.last_name)
                middleNameEditText.setText(student.middle_name)
                contactEditText.setText(student.contact_number)
                emailEditText.setText(student.email)
                addressEditText.setText(student.address)
                studentNumberText.setText(student.student_id)
                setStatus(student.student_status)
                val studentType = student.student_type.toString()
                setType(studentType)


            } else {
                Toast.makeText(this, "Student not found.", Toast.LENGTH_SHORT).show()
            }
        }}
    }


    private fun filterStudents(studentId: String) {
        // Filter the list by student ID
        val filteredList = cachedProfessor?.filter { it.student_id == studentId } ?: emptyList()

        // Update the RecyclerView with the filtered list
        attendanceAdapter.updateListed(filteredList)
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
        toggleVisibility(attendancedetailInfo)

        buttonCloseA.setOnClickListener(){
            toggleVisibility(attendancedetailInfo)
        }

        findViewById<TextView>(R.id.textView8).text = attendanceData.log_date // Assuming this shows the date
        findViewById<EditText>(R.id.editTextText).setText(attendanceData.section)
        findViewById<EditText>(R.id.course).setText(attendanceData.course_name)
        findViewById<TextView>(R.id.proffessorText).setText("Student")
        findViewById<EditText>(R.id.professor).setText(attendanceData.student_last_name + ", " + attendanceData.student_first_name + " " + attendanceData.student_middle_name?.firstOrNull()?.toString() ?: "")
        findViewById<EditText>(R.id.program).setText(attendanceData.program_name)
        findViewById<EditText>(R.id.c_time_in).setText(attendanceData.time_start)
        findViewById<EditText>(R.id.c_time_out).setText(attendanceData.time_end)


    }


    private fun toggleVisibility(targetView: View) {
        if (targetView.visibility == View.VISIBLE) {
            targetView.visibility = View.GONE

        } else {
            targetView.visibility = View.VISIBLE

        }
    }


}
