package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast


import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.saveable.Saver
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class studentView : AppCompatActivity() {
    private lateinit var statusSpinner: Spinner
    private lateinit var displayValueEditText: EditText
    private lateinit var recyclerView :  RecyclerView
    private lateinit var stud_searchAttendance : EditText

    private lateinit var adminViewStudentAdapter: AdminViewStudentAttendanceAdapter
    private  var cachedstudentAttendance: List<AdminViewStudentAttendanceData>? = null


    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var middleNameEditText: EditText
    private lateinit var contactEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var studentNumberText: TextView

    private lateinit var statusAutoCompleteTextView: AutoCompleteTextView
    private lateinit var typeAutoCompleteTextView: AutoCompleteTextView

    private lateinit var showstudentinfo : ImageView
    private lateinit var student_user_layout : View

    private lateinit var attendancedetailInfo: View


    private lateinit var buttonCloseA : Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.studentview) // Ensure this matches your XML file name

        attendancedetailInfo = findViewById(R.id.detailsA)

        adminViewStudentAdapter = AdminViewStudentAttendanceAdapter(
            data = listOf(), // Empty data initially, will be updated later
            onItemClick = { attendanceData ->
                displayAttendanceDetails(attendanceData) // Handle the item click
            }
        )


        student_user_layout = findViewById(R.id.student_user_layout)
        showstudentinfo = findViewById(R.id.showstudentinfo)


        statusAutoCompleteTextView = findViewById(R.id.status)
        typeAutoCompleteTextView = findViewById(R.id.type)

        val firstName = intent.getStringExtra("first_name")
        val middleName = intent.getStringExtra("middle_name")
        val lastName = intent.getStringExtra("last_name")
        val contact = intent.getStringExtra("contact")
        val email = intent.getStringExtra("email")
        val address = intent.getStringExtra("address")
        val status = intent.getStringExtra("status")
        val type = intent.getStringExtra("studentType")
        val studentNumber = intent.getStringExtra("student_id")

        firstNameEditText = findViewById(R.id.firstName)
        lastNameEditText = findViewById(R.id.lastName)
        middleNameEditText = findViewById(R.id.middleName)
        contactEditText = findViewById(R.id.contact)
        emailEditText = findViewById(R.id.email)
        addressEditText = findViewById(R.id.address)
        studentNumberText = findViewById(R.id.student_number)

        // Set the data to the EditText fields
        firstNameEditText.setText(firstName)
        lastNameEditText.setText(lastName)
        contactEditText.setText(contact)
        emailEditText.setText(email)
        addressEditText.setText(address)
        middleNameEditText.setText(middleName)
        studentNumberText.setText(studentNumber)

        buttonCloseA = findViewById(R.id.buttonCloseA)



           recyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        stud_searchAttendance = findViewById(R.id.stud_searchAttendance)


        if (studentNumber != null) {
            // Now it's safe to call fetchStudentAttendance since studentNumber is non-null
            fetchStudentAttendance(studentNumber)
        } else {
            // Handle the null case, e.g., log an error or show a message
            Log.e("AdminViewStudentOne", "student_id is null")
            Toast.makeText(this, "Error: Student ID is null.", Toast.LENGTH_SHORT).show()
        }
enableEdgeToEdge()


        setStatus(status)
        setType(type)
        searchAttendance()
            showinfo()
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



  private fun showinfo(){
      showstudentinfo.setOnClickListener {
          if (student_user_layout .visibility == View.GONE) {
              student_user_layout .visibility = View.VISIBLE
              showstudentinfo.setImageResource(R.drawable.close)
              adminViewStudentAdapter.setClickables(false)
          } else {
              student_user_layout.visibility = View.GONE
              showstudentinfo.setImageResource(R.drawable.user)
              adminViewStudentAdapter.setClickables(true)
          }
      }
  }



    fun toggleVisibility(view: View) {
        view.visibility = if (view.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchAttendance() {
        stud_searchAttendance.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                val filteredList = cachedstudentAttendance?.filter {
                    it.log_date.contains(query, ignoreCase = true) ||
                            it.time_in?.contains(query, ignoreCase = true) == true ||
                            it.time_out?.contains(query, ignoreCase = true) == true ||
                            it.status?.contains(query, ignoreCase = true) == true ||
                            it.section?.contains(query, ignoreCase = true) == true
                } ?: emptyList()

                adminViewStudentAdapter.updateList(filteredList)
            }
        })
    }


    private fun fetchStudentAttendance(StudentNumber : String ) {


        // Check if the student ID is not null
        if (StudentNumber != null) {
            // Call the API to fetch attendance data
            ApiClient.getAttendance(StudentNumber) { attendanceData ->
                runOnUiThread {
                    // Check if the returned data is not null and not empty
                    if (attendanceData != null && attendanceData.isNotEmpty()) {
                        cachedstudentAttendance = attendanceData
                        updateRecyclerView(attendanceData) // Update the RecyclerView with the fetched data
                    } else {
                        // Handle case where no attendance data is found
                        Toast.makeText(this, "No attendance records found for this student.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Log.e("FETCH", "student_id is null") // Log an error if student ID is null
            Toast.makeText(this, "Error: Student ID is null.", Toast.LENGTH_SHORT).show() // Notify user
        }
    }

    fun displayAttendanceDetails(attendanceData: AdminViewStudentAttendanceData) {
        // Update the views with the attendance data



        findViewById<TextView>(R.id.textView8).text = attendanceData.log_date // Assuming this shows the date
        findViewById<EditText>(R.id.editTextText).setText(attendanceData.section)
        findViewById<EditText>(R.id.course).setText(attendanceData.course_name)
        findViewById<EditText>(R.id.professor).setText(attendanceData.professor_last_name + ", " + attendanceData.professor_first_name + attendanceData.professor_middle_name?.firstOrNull()?.toString() ?: "")
        findViewById<EditText>(R.id.program).setText(attendanceData.program_name)
        findViewById<EditText>(R.id.c_time_in).setText(attendanceData.time_start)
        findViewById<EditText>(R.id.c_time_out).setText(attendanceData.time_end)


        // Optionally, you can also set other attendance details in the appropriate views
    }


    private fun updateRecyclerView(data: List<AdminViewStudentAttendanceData>) {
        adminViewStudentAdapter = AdminViewStudentAttendanceAdapter(data) { attendanceData ->
            displayAttendanceDetailes(attendanceData) // Define what happens on item click
        }
        recyclerView.adapter = adminViewStudentAdapter
    }

    fun displayAttendanceDetailes(attendanceData: AdminViewStudentAttendanceData) {
        // Update the views with the attendance data
        toggleVisibility(attendancedetailInfo)

        buttonCloseA.setOnClickListener(){
            toggleVisibility(attendancedetailInfo)
        }

        findViewById<TextView>(R.id.textView8).text = attendanceData.log_date // Assuming this shows the date
        findViewById<EditText>(R.id.editTextText).setText(attendanceData.section)
        findViewById<EditText>(R.id.course).setText(attendanceData.course_name)
        findViewById<EditText>(R.id.professor).setText(attendanceData.professor_last_name + ", " + attendanceData.professor_first_name + attendanceData.professor_middle_name?.firstOrNull()?.toString() ?: "")
        findViewById<EditText>(R.id.program).setText(attendanceData.program_name)
        findViewById<EditText>(R.id.c_time_in).setText(attendanceData.time_start)
        findViewById<EditText>(R.id.c_time_out).setText(attendanceData.time_end)


        // Optionally, you can also set other attendance details in the appropriate views
    }


    private fun enableEdgeToEdge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = window.insetsController
            windowInsetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
                it.hide(WindowInsets.Type.navigationBars())
            }
        } else {
            // For devices with lower Android versions
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }
}
