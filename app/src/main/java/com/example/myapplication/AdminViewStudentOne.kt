package com.example.myapplication


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.myapplication.AdminViewStudentAttendanceAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminViewStudentOne : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var classRecycleView: RecyclerView
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var middleNameEditText: EditText
    private lateinit var contactEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var studentNumberText: TextView
    private lateinit var statusAutoCompleteTextView: AutoCompleteTextView
    private lateinit var typeAutoCompleteTextView: AutoCompleteTextView
    private lateinit var updateButton: Button
    private lateinit var  questionStatusImageView: ImageView
    private lateinit var questionStatusBody: View
    private lateinit var closeButtonStatus: Button
    private lateinit var updateQuestion: View
    private lateinit var closeButton : Button
    private lateinit var yesButton : Button
    private lateinit var deleteButton : Button
    private lateinit var questionBody : View
    private lateinit var typeCloseButton : Button
    private lateinit var typeImageQuestion : ImageView
    private lateinit var infoText2: TextView
    private lateinit var attendance: TextView
    private lateinit var classText: TextView
    private lateinit var accountText: TextView

    private lateinit var frameLayout3: View
    private lateinit var attendanceLayout: View
    private lateinit var classLayout: View
    private lateinit var accountLayout: View
    private lateinit var buttonCloseA: Button
    private lateinit var avs_class_info_layout : View
    private lateinit var ci_info_close_icon : ImageView
    private lateinit var searchAttendance : EditText
    private lateinit var backadminview : ImageView

   private lateinit var adminViewStudentAdapter: AdminViewStudentAttendanceAdapter


    // declare classinfo
    private lateinit var classInfoView : View
    private var cachedclassinfo: List<ClassInfoData>? = null


    // declare attendanceinfo
    private lateinit var attendancedetailInfo: View
    private  var cachedstudentAttendance: List<AdminViewStudentAttendanceData>? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adminviewstudentone)
        backadminview = findViewById(R.id.backadminview)
        yesButton = findViewById(R.id.yesButton)
        firstNameEditText = findViewById(R.id.firstName)
        lastNameEditText = findViewById(R.id.lastName)
        middleNameEditText = findViewById(R.id.middleName)
        contactEditText = findViewById(R.id.contact)
        emailEditText = findViewById(R.id.email)
        addressEditText = findViewById(R.id.address)
        studentNumberText = findViewById(R.id.student_number)
        statusAutoCompleteTextView = findViewById(R.id.status)
        typeAutoCompleteTextView = findViewById(R.id.type)
        typeImageQuestion = findViewById(R.id.typeImageQuestion)
        ci_info_close_icon = findViewById(R.id.ci_info_close_icon)



        //set  up the recucle view
        classRecycleView = findViewById(R.id.classRecycleView)
        recyclerView = findViewById(R.id.recyclerView)

        //need this
        classRecycleView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = LinearLayoutManager(this)




        //body to show visible or not
        questionBody = findViewById(R.id.typebodyQquestion)
        attendancedetailInfo = findViewById(R.id.detailsA)
        typeCloseButton = findViewById(R.id.typecloseButton)
        avs_class_info_layout = findViewById((R.id.avs_class_info_layout))



        //button
        deleteButton = findViewById(R.id.deleteButton)
        updateButton = findViewById(R.id.UpdateButton)
        updateQuestion = findViewById(R.id.updateQuestion)
        closeButton = findViewById(R.id.closeButtonQuestion)
        closeButtonStatus = findViewById(R.id.closeButton)
        buttonCloseA = findViewById(R.id.buttonCloseA)
        // New references for question status ImageView and body
        questionStatusImageView = findViewById(R.id.question_status)
        questionStatusBody = findViewById(R.id.relativeLayout) // Get the included layout




        //this is for my navbar i use this to navigate in student info...
        // Initialize TextViews
        infoText2 = findViewById(R.id.infoText2)
        attendance = findViewById(R.id.attendance)
        classText = findViewById(R.id.classtext)
        accountText = findViewById(R.id.accountext)
        searchAttendance = findViewById(R.id.searchAttendance)

        // Initialize FrameLayouts
        frameLayout3 = findViewById(R.id.frameLayout3)
        attendanceLayout = findViewById(R.id.attendancelayout)
        classLayout = findViewById(R.id.classlayout)
        accountLayout = findViewById(R.id.accountlayout)

        // Retrieve data from the Intent
        val studentNumber = intent.getStringExtra("student_id")
        val firstName = intent.getStringExtra("first_name")
        val middleName = intent.getStringExtra("middle_name")
        val lastName = intent.getStringExtra("last_name")
        val contact = intent.getStringExtra("contact")
        val email = intent.getStringExtra("email")
        val address = intent.getStringExtra("address")
        val status = intent.getStringExtra("status")
        val type = intent.getStringExtra("studentType")

        // Set the data to the EditText fields
        firstNameEditText.setText(firstName)
        lastNameEditText.setText(lastName)
        contactEditText.setText(contact)
        emailEditText.setText(email)
        addressEditText.setText(address)
        middleNameEditText.setText(middleName)
        studentNumberText.setText(studentNumber)

        updateClick()
        imageViewClick()

        setupStatusDropdown()
        setStatus(status)
        setType(type)
        setUpTypeDropdown()
        enableEdgeToEdge()
       typeImageClick()
        callUpdateStudent()



        if (studentNumber != null) {
            // Now it's safe to call fetchStudentAttendance since studentNumber is non-null
            clickTextNavigate(studentNumber)
        } else {
            // Handle the null case, e.g., log an error or show a message
            Log.e("AdminViewStudentOne", "student_id is null")
            Toast.makeText(this, "Error: Student ID is null.", Toast.LENGTH_SHORT).show()
        }
    }

    // method to click the text
    fun clickTextNavigate(studentNumber: String){

        // Set click listeners for each TextView
        infoText2.setOnClickListener {
            showLayout(frameLayout3)
            changeTextColor(infoText2)
        }

        attendance.setOnClickListener {
            showLayout(attendanceLayout)
            changeTextColor(attendance)
            searchAttendance()
            fetchStudentAttendance(studentNumber)



        }


        backadminview.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()

            intent.putExtra("focusLayout", "studentLayout") // Indicate the layout to focus oon

        }

        classText.setOnClickListener {
            showLayout(classLayout)
            changeTextColor(classText)
            searchAttendance.clearFocus()
            getclass(studentNumber)
        }

        accountText.setOnClickListener {
            showLayout(accountLayout)
            changeTextColor(accountText)
        }
    }


    private fun searchAttendance() {
        searchAttendance.addTextChangedListener(object : TextWatcher {
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

    // Method to show the clicked layout and hide others
    private fun showLayout(layoutToShow: View) {
        // List of all layouts
        val allLayouts = listOf(frameLayout3, attendanceLayout, classLayout, accountLayout)

        // Slide out and fade out all layouts except the selected one
        allLayouts.forEach { layout ->
            if (layout != layoutToShow) {
                layout.animate()
                    .translationY(layout.height.toFloat()) // Slide out to the bottom
                    .alpha(0f) // Fade out
                    .setDuration(300) // Duration of animation in milliseconds
                    .withEndAction {
                        layout.visibility = View.GONE // Make layout invisible after animation
                        layout.translationY = 0f // Reset translation for future animations
                    }
            }
        }

        // Set up the selected layout for slide-up and fade-in
        layoutToShow.visibility = View.VISIBLE
        layoutToShow.alpha = 0f // Start from fully transparent
        layoutToShow.translationY = layoutToShow.height.toFloat() // Start below the visible area

        // Slide in and fade in the selected layout
        layoutToShow.animate()
            .translationY(0f) // Move to original position
            .alpha(1f) // Fade in to full visibility
            .setDuration(300) // Animation duration
            .start()
    }


    // Method to change the text color to pure white
    private fun changeTextColor(clickedTextView: TextView) {
        // Reset all TextViews to default color (optional)
        resetTextColors()

        // Change the clicked TextView color to white
        clickedTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
    }
    private fun resetTextColors() {
        val defaultColor = (ContextCompat.getColor(this, R.color.white_text))
        infoText2.setTextColor(defaultColor)
        attendance.setTextColor(defaultColor)
        classText.setTextColor(defaultColor)
        accountText.setTextColor(defaultColor)
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

    private fun updateRecyclerView(data: List<AdminViewStudentAttendanceData>) {
        adminViewStudentAdapter = AdminViewStudentAttendanceAdapter(data) { attendanceData ->
            displayAttendanceDetails(attendanceData) // Define what happens on item click
        }
        recyclerView.adapter = adminViewStudentAdapter
    }



       private fun getclass(StudentNumber: String) {
           if (StudentNumber != null) {
               ApiClient.getClassInfo(StudentNumber) { classInfoList ->
                   runOnUiThread {
                       if (classInfoList != null && classInfoList.isNotEmpty()) {
                           cachedclassinfo = classInfoList
                           updateClassRecycleView(classInfoList)


                       } else {
                           Toast.makeText(this, "No class rECORD", Toast.LENGTH_SHORT).show()

                       }

                   }
               }
           } else {
               Log.e("FETCH", "student_id is null") // Log an error if student ID is null
               Toast.makeText(this, "Error: Student ID is null.", Toast.LENGTH_SHORT)
                   .show() // Notify user


           }
       }
    private fun updateClassRecycleView(data: List<ClassInfoData>){
        val adapter =  StudentClassinfoAdapter(data)
        classRecycleView.adapter = adapter

    }
    private fun updateClick() {
        updateButton.setOnClickListener {
            // Get input values and trim them
            val firstNameInput = firstNameEditText.text.toString().trim()
            val lastNameInput = lastNameEditText.text.toString().trim()
            val middleNameInput = middleNameEditText.text.toString().trim()
            val contactInput = contactEditText.text.toString().trim()
            val emailInput = emailEditText.text.toString().trim()
            val addressInput = addressEditText.text.toString().trim() // Trimmed address

            val statusInput = statusAutoCompleteTextView.text.toString().trim()
            val typeInput = typeAutoCompleteTextView.text.toString().trim()

            // Validate inputs
            if (firstNameInput.isBlank() || lastNameInput.isBlank() || middleNameInput.isBlank()) {
                Toast.makeText(
                    this@AdminViewStudentOne,
                    "Please fill in all required fields",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Validate email format
            if (!isValidEmail(emailInput)) {
                Toast.makeText(
                    this@AdminViewStudentOne,
                    "Please enter a valid email address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val validStatuses = listOf("Enrolled", "Drop out", "Drop Out","Graduated", "Transferred", "E", "D", "G", "T")
            if (statusInput !in validStatuses) {
                Toast.makeText(
                    this@AdminViewStudentOne,
                    "Please enter a valid status (Enrolled, Drop out,  Graduated, Transferred)",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Validate type input
            val validTypes = listOf("Regular", "Irregular", "1", "2")
            if (typeInput !in validTypes) {
                Toast.makeText(
                    this@AdminViewStudentOne,
                    "Please enter a valid type (Regular, Irregular)",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!contactInput.isValidContactNumber()) {
                Toast.makeText(
                    this@AdminViewStudentOne,
                    "Please enter a valid contact number (11 digits)",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            // Address validation (optional, you can add more checks if needed)
            if (addressInput.isBlank()) {
                Toast.makeText(
                    this@AdminViewStudentOne,
                    "Please enter a valid address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // If all validations pass
            toggleVisibility(updateQuestion, updateButton, deleteButton)
        }

        closeButton.setOnClickListener {
            toggleVisibility(updateQuestion, updateButton, deleteButton)
        }
    }

    // Define an extension function on String
    fun String.isValidContactNumber(): Boolean {
        // Regex to match a valid phone number format for PH (11 digits)
        return this.matches(Regex("^\\d{11}$")) // Matches exactly 11 digits
    }


    // Function to validate email format
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun typeImageClick(){
        typeImageQuestion.setOnClickListener{
            toggleVisibility(questionBody, updateButton, deleteButton)
        }
        typeCloseButton.setOnClickListener{
            toggleVisibility(questionBody, updateButton, deleteButton)
        }
    }
    private fun imageViewClick() {
        questionStatusImageView.setOnClickListener {
            toggleVisibility(questionStatusBody, updateButton, deleteButton)
        }
        // Set up click listener for the button
        closeButtonStatus.setOnClickListener {
            toggleVisibility(questionStatusBody, updateButton, deleteButton)
        }
    }

    private fun toggleVisibility(targetView: View, relatedButton: Button?, deleteButton: Button?) {
        if (targetView.visibility == View.VISIBLE) {
            targetView.visibility = View.GONE
            relatedButton?.isEnabled = true // Safe call on relatedButton
            deleteButton?.isEnabled = true
            infoText2.isClickable = true
            attendance.isClickable = true
            classText.isClickable = true
            accountText.isClickable = true

        } else {
            targetView.visibility = View.VISIBLE
            deleteButton?.isEnabled = false
            relatedButton?.isEnabled = false // Safe call on relatedButton
            infoText2.isClickable = false
            attendance.isClickable = false
            classText.isClickable = false
            accountText.isClickable = false
        }
    }





    private fun callUpdateStudent() {
        yesButton.setOnClickListener {
            // Gather input data and ensure they are not blank
            val firstNameInput = firstNameEditText.text.toString()
            val lastNameInput = lastNameEditText.text.toString()
            val middleNameInput = middleNameEditText.text.toString()
            val contactInput = contactEditText.text.toString()
            val emailInput = emailEditText.text.toString()
            val addressInput = addressEditText.text.toString()

            val statusInput = statusAutoCompleteTextView.text.toString()
            val typeInput = typeAutoCompleteTextView.text.toString()

            val statusId = when (statusInput) {
                "Enrolled" -> "E"
                "Drop out" -> "D"
                "Graduated" -> "G"
                "Transferred" -> "T"
                "E" -> "E"
                "D" -> "D"
                "G" -> "G"
                "T" -> "T"
                else -> ""
            }

            val typeId = when (typeInput) {
                "Regular" -> 1
                "Irregular" -> 2
                "1" -> 1
                "2" -> 2
                else -> ""
            }




            // Prepare data for the API
            val updateData = mapOf(
                "first_name" to firstNameInput.capitalizeWords(),
                "last_name" to lastNameInput.capitalizeWords(),
                "middle_name" to middleNameInput.capitalizeWords(),
                "contact_number" to contactInput,
                "email" to emailInput,
                "address" to addressInput.capitalizeWords(),
                "student_type" to typeId,
                "student_status" to statusId
            )



            // Call the updateStudent function from ApiClient
            ApiClient.updateStudent(studentNumberText.text.toString(), updateData) { updateSuccess, updatedData ->
                runOnUiThread {
                    if (updateSuccess && updatedData != null) {
                        // Update EditTexts with the new data received from the server
                        // ... [setting EditTexts with updated data] ...

                        Toast.makeText(this@AdminViewStudentOne, "Update successful", Toast.LENGTH_SHORT).show()
                        toggleVisibility(updateQuestion, updateButton, deleteButton) // check this if the buttons are not clickable
                        // Send a broadcast to notify adminView to refresh the student list
                        val intent = Intent("com.example.myapplication.UPDATE_STUDENT")
                        sendBroadcast(intent)

                    } else {
                        Toast.makeText(this@AdminViewStudentOne, "Update failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



    private fun setupStatusDropdown() {
        val statusOptions = arrayOf("Enrolled", "Drop out", "Graduated", "Transferred")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, statusOptions)
        statusAutoCompleteTextView.setAdapter(adapter)
    }

    private fun setUpTypeDropdown() {
        val typeOptions = arrayOf("Regular", "Irregular")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, typeOptions)
        typeAutoCompleteTextView.setAdapter(adapter)
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
    fun String.capitalizeWords(): String {
        return this.split(" ").joinToString(" ") { it.capitalize() }
    }


    fun displayAttendanceDetails(attendanceData: AdminViewStudentAttendanceData) {
        // Update the views with the attendance data
        toggleVisibility(attendancedetailInfo, null, null)

        buttonCloseA.setOnClickListener(){
            toggleVisibility(attendancedetailInfo, null, null)
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

    //  if the course and class info click show the all details about it
    fun callClassInfo(classInfoData: ClassInfoData){


        toggleVisibility(avs_class_info_layout, null, null)

        ci_info_close_icon.setOnClickListener(){
            toggleVisibility(avs_class_info_layout, null, null)
        }

        findViewById<TextView>(R.id.ci_info_first_letter).text = classInfoData.course_name.first().toString()
        findViewById<TextView>(R.id.ci_info_course_name).text = classInfoData.course_name
        findViewById<TextView>(R.id.ci_info_section_text).text = classInfoData.section
        findViewById<TextView>(R.id.ci_info_program_text).text = classInfoData.program_code
        findViewById<TextView>(R.id.ci_info_program).text = classInfoData.program_name
        findViewById<TextView>(R.id.ci_info_prof).text = classInfoData.professor_last_name + ", " + classInfoData.professor_first_name + " " + classInfoData.professor_middle_name
        findViewById<TextView>(R.id.ci_info_course).text = classInfoData.course_description



    }
    private fun enableEdgeToEdge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = window.insetsController
            windowInsetsController?.hide(WindowInsets.Type.statusBars())
            windowInsetsController?.hide(WindowInsets.Type.navigationBars())
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }
}
