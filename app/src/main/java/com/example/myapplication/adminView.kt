package com.example.myapplication

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.util.Calendar
import kotlin.random.Random

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class adminView : AppCompatActivity() {
    private lateinit var teacherRecycleView : RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var name: TextView
    private lateinit var updateReceiver: BroadcastReceiver
    private lateinit var av_search_student : EditText
    private lateinit var show_cs_layout : Button
    private lateinit var next_cs_button : Button
    private lateinit var cs_info_first : View
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var cs_close_layout : ImageView

    private lateinit var cs_info_second : View
    private lateinit var adminviewlayout : View
    private lateinit var cs_layout : View
    private lateinit var  spinner : Spinner
    private lateinit var  spinner2 : Spinner
    private lateinit var  adminIcon :ImageView
    private lateinit var  studentIcon : ImageView
    private lateinit var  homeIcon : ImageView
    private lateinit var  teacherIcon : ImageView


    private lateinit var hl_prof_tab : View
    private lateinit var adminLayout: View
    private lateinit var homeLayout: View
    private lateinit var teacherLayout: View
    private lateinit var studentLayout: View
    private lateinit var homeScrollView : ScrollView

    private lateinit var adminText : TextView
    private lateinit var homeText : TextView
    private lateinit var studentText : TextView
    private lateinit var teacherText : TextView
    private lateinit var button3 : Button

    private lateinit var navBar : View
    private lateinit var footer : ViewGroup

    private var currentSelectedLayout: View? = null


    private lateinit var stud_enrolledLayout : View
    private lateinit var av_seachProf : EditText
    // Introduce a cache for students
    private var cachedStudents: List<StudentResponse>? = null

    private var cachedProfessor: List<ProfessorResponse>? = null
    private lateinit var professorAdapter: ProfessorAdapter

    private lateinit var hl_graduatedLayout : View
    private lateinit var stud_dropoutLayout : View
    private lateinit var stud_transLayout : View
    private lateinit var stud_totalLayout : View

    private lateinit var stud_enrolledCounts : TextView
    private lateinit var stud_graduateCount : TextView
    private lateinit var stud_dropoutCount: TextView
    private lateinit var  stud_transCount: TextView
    private lateinit var stud_totalCount: TextView
    private lateinit var hl_profCount: TextView

    private lateinit var ct_layout : View
    private lateinit var ct_close_layout : ImageView

    // create proff button ini
    private lateinit var  ad_create_btn_prof : Button

    private lateinit var create_prof_btn : Button
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adminview)

        ct_close_layout = findViewById(R.id.ct_close_layout)
        ad_create_btn_prof = findViewById(R.id.ad_create_btn_prof)


        // view
        navBar = findViewById(R.id.navbar)
        footer = findViewById(R.id.footer)

        ct_layout = findViewById(R.id.ct_layout)

       //button
        create_prof_btn = findViewById(R.id.create_prof_btn)
        //icon
        adminIcon = findViewById(R.id.adminIcon)
        studentIcon = findViewById(R.id.studentIcon)
        homeIcon = findViewById(R.id.homeIcon)
        teacherIcon = findViewById(R.id.teacherIcon)

        adminLayout = findViewById(R.id.adminLayout)
        homeLayout = findViewById(R.id.homeLayout)
        teacherLayout = findViewById(R.id.teacherLayout)
        studentLayout = findViewById(R.id.studentLayout)
        homeScrollView = findViewById(R.id.homeScrollView )

        //textview
        adminText = findViewById(R.id.adminText)
        homeText = findViewById(R.id.homeText)
        studentText = findViewById(R.id.studentText)
        teacherText = findViewById(R.id.teacherText)

        // tab
        hl_prof_tab = findViewById(R.id.hl_prof_tab)
        stud_enrolledLayout = findViewById(R.id.stud_enrolledLayout)
        hl_graduatedLayout = findViewById(R.id.hl_graduatedLayout)
        stud_dropoutLayout = findViewById(R.id.stud_dropoutLayout)
        stud_transLayout = findViewById(R.id. stud_transLayout)
        stud_totalLayout = findViewById(R.id.stud_totalLayout)
        // Set click listeners for each icon
        adminIcon.setOnClickListener { showLayout(adminLayout) }
        studentIcon.setOnClickListener { showLayout(studentLayout)
            filterStudentsByStatus("all")}
        homeIcon.setOnClickListener { showLayout(homeScrollView) }
        teacherIcon.setOnClickListener { showLayout(teacherLayout)}


        // set click listener for each tab
        hl_prof_tab.setOnClickListener {showLayout(teacherLayout)}
        stud_enrolledLayout.setOnClickListener {showLayout(studentLayout)
            filterStudentsByStatus("enrolled")
        }
        hl_graduatedLayout.setOnClickListener {showLayout(studentLayout)
            filterStudentsByStatus("graduated")
        }
        stud_dropoutLayout.setOnClickListener {showLayout(studentLayout)
            filterStudentsByStatus("dropped")
        }
        stud_transLayout.setOnClickListener {showLayout(studentLayout)
            filterStudentsByStatus("transferred")
        }

        stud_totalLayout.setOnClickListener {showLayout(studentLayout)
            filterStudentsByStatus("all")
          }

        // Initialize TextViews
        stud_enrolledCounts = findViewById(R.id.stud_enrolledCounts)
        stud_graduateCount = findViewById(R.id.stud_graduateCount)
        stud_dropoutCount = findViewById(R.id.stud_dropoutCount)
        stud_transCount = findViewById(R.id.stud_transCount)
        stud_totalCount = findViewById(R.id.stud_totalCount)
        hl_profCount = findViewById(R.id.hl_profCount)

        // Initialize UI components
        name = findViewById(R.id.name)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        av_search_student = findViewById(R.id.av_search_student)


        //proffessor recycle view
        teacherRecycleView = findViewById(R.id.teacherRecycleView)
        teacherRecycleView.layoutManager = LinearLayoutManager(this)
        av_seachProf = findViewById(R.id.av_seachProf)

        // Check if we need to focus on a specific layout
        val focusLayout = intent.getStringExtra("focusLayout")
        if (focusLayout == "studentLayout") {
            showLayout(studentLayout)
        }


         spinner = findViewById(R.id.spinner)
        spinner2 = findViewById(R.id.spinner2)

        //set up button
        show_cs_layout = findViewById(R.id.show_cs_layout)
        next_cs_button = findViewById(R.id.next_cs_button)
        adminviewlayout  = findViewById(R.id.adminviewlayout)

        button3 = findViewById(R.id.button3)
        // image view
        cs_close_layout = findViewById(R.id.cs_close_layout)
        // set up view
        cs_layout = findViewById(R.id.cs_layout)
        cs_info_first = findViewById(R.id.cs_info_first)
        cs_info_second = findViewById(R.id.cs_info_second)
        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Register the BroadcastReceiver to listen for updates
        updateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                fetchStudents() // Refresh the student list when the broadcast is received
            }
        }
        val filter = IntentFilter("com.example.myapplication.UPDATE_STUDENT")
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver, filter)



        updateCountsUI()
        // Set default colors for icons and text

        // Enable edge-to-edge layout for better aesthetics
        enableEdgeToEdge()

        // Fetch and display students
        fetchStudents()


        searchStudent()

      //prof
        searchProf()
        fetchProf()




        clickedshow()
        spinner()
        spinner2()
        insertStudentData()
        insertProfessor()



    }


    private fun updateCountsUI() {
        // Show loading indicator
        progressBar.visibility = View.VISIBLE

        // Call the API to fetch counts
        ApiClient.fetchCounts({ countResponse ->
            // Hide loading indicator
            runOnUiThread {
                progressBar.visibility = View.GONE  // Hide loading indicator on the main thread

                if (countResponse != null) {
                    // Update the UI with the fetched data
                    val enrolledCount = countResponse.student_status_counts.find { it.student_status == "E" }?.count ?: 0
                    val graduatedCount = countResponse.student_status_counts.find { it.student_status == "G" }?.count ?: 0
                    val droppedCount = countResponse.student_status_counts.find { it.student_status == "D" }?.count ?: 0
                    val transferredCount = countResponse.student_status_counts.find { it.student_status == "T" }?.count ?: 0

                    stud_enrolledCounts.text = enrolledCount.toString()
                    stud_graduateCount.text = graduatedCount.toString()
                    stud_dropoutCount.text = droppedCount.toString()
                    stud_transCount.text = transferredCount.toString()
                    stud_totalCount.text = countResponse.total_student_count.toString()
                    hl_profCount.text = countResponse.total_professor_count.toString()
                } else {
                    // Handle error, e.g., show a message
                    Log.e("YourActivity", "Failed to fetch counts")
                    Toast.makeText(this, "Failed to fetch counts. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }, null) // You can pass null if you don't want to provide a uiUpdate callback
    }


    private fun filterStudentsByStatus(status: String) {
        val filteredList = when (status.lowercase()) {
            "enrolled" -> cachedStudents?.filter { it.student_status == "E" }
            "transferred" -> cachedStudents?.filter { it.student_status == "T" }
            "dropped" -> cachedStudents?.filter { it.student_status == "D" }
            "graduated" -> cachedStudents?.filter { it.student_status == "G" }
            "all" -> cachedStudents // Return all students
            else -> emptyList() // If status doesn't match any known keyword
        } ?: emptyList()

        // Use the studentAdapter instance to filter
        studentAdapter.filters(filteredList)
    }


    private fun showLayout(selectedLayout: View) {
        // List of layouts with their corresponding icons and text labels
        val layoutsAndElements = mapOf(
            adminLayout to Pair(adminIcon, adminText),
            homeScrollView to Pair(homeIcon, homeText),
            teacherLayout to Pair(teacherIcon, teacherText),
            studentLayout to Pair(studentIcon, studentText)
        )

        // Define your colors
        val selectedColor = ContextCompat.getColor(this, R.color.white) // Color for selected state
        val defaultColor = ContextCompat.getColor(this, R.color.white_text) // Color for unselected state

        // Slide out and fade out all layouts except the selected one
        layoutsAndElements.forEach { (layout, elements) ->
            val (icon, text) = elements
            if (layout != selectedLayout) {
                layout.animate()
                    .translationY(layout.height.toFloat()) // Slide out to the bottom
                    .alpha(0f) // Fade out
                    .setDuration(300) // Animation duration in milliseconds
                    .withEndAction {
                        layout.visibility = View.GONE // Make it invisible after animation
                        layout.translationY = 0f // Reset translation for future animations
                    }
                icon.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN) // Reset icon color to default
                text.setTextColor(defaultColor) // Reset text color to default
            }
        }

        // Handle the selected layout only if it's different from the current one
        if (currentSelectedLayout != selectedLayout) {
            selectedLayout.visibility = View.VISIBLE
            selectedLayout.alpha = 0f // Start from invisible
            selectedLayout.translationY = selectedLayout.height.toFloat() // Start below the visible area

            // Slide in the selected layout and fade it in
            selectedLayout.animate()
                .translationY(0f) // Move to original position
                .alpha(1f) // Fade in to full visibility
                .setDuration(300) // Animation duration in milliseconds
                .start()

            // Change the icon and text color of the selected layout
            layoutsAndElements[selectedLayout]?.let { (icon, text) ->
                icon.setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN) // Set selected icon color
                text.setTextColor(selectedColor) // Set selected text color
            }

            // Update the current selected layout
            currentSelectedLayout = selectedLayout
        }
    }

    private fun insertStudentData() {


        // Retrieve data from EditText fields


        // Check if any field is empty
        next_cs_button.setOnClickListener() {
            val firstName = findViewById<EditText>(R.id.cs_firstname).text.toString().trim()
            val lastName = findViewById<EditText>(R.id.cs_lastname).text.toString().trim()
            val middleName = findViewById<EditText>(R.id.cs_middlename).text.toString().trim()
            val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString().trim()
            val contactNumber = findViewById<EditText>(R.id.editText).text.toString().trim()
            val address = findViewById<EditText>(R.id.editTextTextPostalAddress2).text.toString().trim() // Assuming address EditText ID is cs_address

            if (firstName.isBlank() || lastName.isBlank() || middleName.isBlank()) {
                Toast.makeText(this, "All fields are requiredrrrr", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                Toast.makeText(
                    this@adminView,
                    "Please enter a valid email address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (!contactNumber.isValidContactNumber()) {
                Toast.makeText(
                    this@adminView,
                    "Please enter a valid contact number (11 digits)",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }else{

                    toggleVisibility(cs_info_first, next_cs_button, cs_info_second)



            }
        }


        button3.setOnClickListener(){

            val firstName = findViewById<EditText>(R.id.cs_firstname).text.toString().trim()
            val lastName = findViewById<EditText>(R.id.cs_lastname).text.toString().trim()
            val middleName = findViewById<EditText>(R.id.cs_middlename).text.toString().trim()
            val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString().trim()
            val contactNumber = findViewById<EditText>(R.id.editText).text.toString().trim()
            val address = findViewById<EditText>(R.id.editTextTextPostalAddress2).text.toString().trim()
            // Assuming address EditText ID is cs_address
            val statusInputRaw = findViewById<Spinner>(R.id.spinner2).selectedItem.toString()
            val typeInputRaw = findViewById<Spinner>(R.id.spinner).selectedItem.toString()
            val statusInput = when (statusInputRaw) {
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

            val typeInput = when (typeInputRaw) {
                "Regular" -> 1
                "Irregular" -> 2
                "1" -> 1
                "2" -> 2
                else -> ""
            }

            val gUsername = generateUsername(firstName, lastName)
            val gPassword = generateRandomPassword(12)

            Log.d("GeneratedCredentials", "Username: $gUsername, Password: $gPassword")


            // Continue with data preparation and API call
            val studentDetails = mapOf(
                "username" to gUsername,
                "password" to gPassword,
                "role" to "Student", // Replace with actual role if different
                "student_id" to generateStudentId(),
                "first_name" to firstName,
                "last_name" to lastName,
                "middle_name" to middleName,
                "email" to email,
                "contact_number" to contactNumber,
                "address" to address,
                "student_type" to typeInput, // Or another student type if needed
                "student_status" to statusInput // Or another status if needed
            )




            ApiClient.insertStudent(studentDetails) { response ->
                Handler(Looper.getMainLooper()).post {
                    if (response != null) {
                        Log.d("InsertStudent", "Insert successful: ${response.message}")
                        Toast.makeText(this, "Student inserted successfully", Toast.LENGTH_SHORT).show()
                        ApiClient.sendEmail(gUsername, email, gPassword, "Student") { response ->
                            Handler(Looper.getMainLooper()).post {
                                Log.d("EmailContent", "Sending to: $email, Username: $gUsername, Password: $gPassword")

                                // Check if the response is not null and contains the expected data
                                if (response != null && response.status == "success") {
                                    Log.d("EmailStatus", "Email sent successfully: ${response.message}")
                                } else {
                                    Log.d("EmailStatus", "Failed to send email.")
                                }
                            }
                        }
                        // Clear the text fields and reset the spinners
                        findViewById<EditText>(R.id.cs_firstname).text.clear()
                        findViewById<EditText>(R.id.cs_lastname).text.clear()
                        findViewById<EditText>(R.id.cs_middlename).text.clear()
                        findViewById<EditText>(R.id.editTextTextEmailAddress).text.clear()
                        findViewById<EditText>(R.id.editText).text.clear()
                        findViewById<EditText>(R.id.editTextTextPostalAddress2).text.clear()
                        findViewById<Spinner>(R.id.spinner2).setSelection(0)
                        findViewById<Spinner>(R.id.spinner).setSelection(0)
                        setNavBarClickability(true)
                        studentAdapter.setClickable(true)


                        // call send gmail to send the username, password on student


                        closebtnInfo()
                  // last udaate that work
                    fetchStudents()

                    } else {
                        Log.d("InsertStudent", "Insert failed")
                        Toast.makeText(this, "Failed to insert student", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Call your insertStudent method

    }

    private fun insertProfessor(){
        create_prof_btn.setOnClickListener{

            val firstName = findViewById<EditText>(R.id.ct_firstname).text.toString().trim()
            val lastName = findViewById<EditText>(R.id.ct_lastname).text.toString().trim()
            val middleName = findViewById<EditText>(R.id.ct_middlename).text.toString().trim()
            val email = findViewById<EditText>(R.id.ct_email).text.toString().trim()

            val gUsername = generateUsername(firstName, lastName)
            val gPassword = generateRandomPassword(3)
            Log.d("EmailContent", "Usernames: $gUsername, Passwords: $gPassword")

            // Continue with data preparation and API call
            val ProfessorDetails = mapOf(
                "username" to gUsername,
                "password" to gPassword,
                "role" to "Professor", // Replace with actual role if different
                "student_id" to generateStudentId(),
                "first_name" to firstName,
                "last_name" to lastName,
                "middle_name" to middleName,
                "email" to email,
            )
                ApiClient.insertProfessor(ProfessorDetails) { response ->
                    Handler(Looper.getMainLooper()).post {
                        if (response != null) {
                            Log.d("InsertProfessor", "Insert successful: ${response.message}")
                            Toast.makeText(this, "Proffessor inserted successfully", Toast.LENGTH_SHORT).show()
                            ApiClient.sendEmail(gUsername, email, gPassword, "Professor") { response ->
                                Handler(Looper.getMainLooper()).post {
                                    Log.d("EmailContent", "Sending to: $email, Username: $gUsername, Password: $gPassword")

                                    // Check if the response is not null and contains the expected data
                                    if (response != null && response.status == "success") {
                                        Log.d("EmailStatus", "Email sent successfully: ${response.message}")
                                    } else {
                                        Log.d("EmailStatus", "Failed to send email.")
                                    }
                                }
                            }
                            // Clear the text fields and reset the spinners
                            findViewById<EditText>(R.id.ct_firstname).text.clear()
                            findViewById<EditText>(R.id.ct_lastname).text.clear()
                            findViewById<EditText>(R.id.ct_middlename).text.clear()
                            findViewById<EditText>(R.id.ct_email).text.clear()

                            fetchProf()


                                toggleVisibility(ct_layout, null, null)
                                setNavBarClickability(true)

                                showLayout(teacherLayout)


                        } else {
                            Log.d("InsertStudent", "Insert failed")
                            Toast.makeText(this, "Failed to insert student", Toast.LENGTH_SHORT).show()
                        }
                    }
                }



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
        private fun generateStudentId(): String {
        // Get the current year
        val year = Calendar.getInstance().get(Calendar.YEAR) % 100

        // Generate a random 4-digit number
        val randomNumber = Random.nextInt(1000, 10000) // 1000 to 9999

        // Combine year and random number to form the student ID
        return "$year-$randomNumber"
    }

    private fun generateUsername(firstName: String, lastName: String): String {
        // Generate username: first letter of first name + last name in lowercase
        return "${firstName.first().lowercaseChar()}${lastName.lowercase(Locale.getDefault())}"
    }

    fun generateRandomPassword(length: Int): String {

        // call the generaterandompassword

      //  val passwordLength = 12 // Desired password length
      //  val randomPassword = generateRandomPassword(passwordLength)
        // Define character sets
        val uppercaseLetters = ('A'..'Z').toList()
        val lowercaseLetters = ('a'..'z').toList()
        val digits = ('0'..'9').toList()
        val specialCharacters = listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+')

        // Combine all characters
        val allCharacters = uppercaseLetters + lowercaseLetters + digits + specialCharacters

        // Ensure at least one character from each set
        val passwordBuilder = StringBuilder()
        passwordBuilder.append(uppercaseLetters.random())
        passwordBuilder.append(lowercaseLetters.random())
        passwordBuilder.append(digits.random())
        passwordBuilder.append(specialCharacters.random())

        // Fill the rest of the password length with random characters
        for (i in 4 until length) {
            passwordBuilder.append(allCharacters.random())
        }

        // Shuffle the result to avoid predictable patterns
        return passwordBuilder.toString().toList().shuffled().joinToString("")
    }

    private fun spinner(){
        //set up spinner
        val irregularityOptions = arrayOf("Regular", "Irregular")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, irregularityOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
                // Handle selection
                // For example, you can log or display the selected item
                println("Selected: $selectedItem")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case when nothing is selected
            }
        })
    }
    private fun spinner2(){
        //set up spinner
        val irregularityOptions = arrayOf("Enrolled", "Graduated", "Transferred", "Drop out")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, irregularityOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = adapter
        spinner2.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
                // Handle selection
                // For example, you can log or display the selected item
                println("Selected: $selectedItem")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case when nothing is selected
            }
        })
    }



    private fun closebtnInfo(){
        cs_info_second.visibility = View.GONE // Hide cs_info_second explicitly
        cs_info_first.visibility = View.VISIBLE // Show cs_info_first
        next_cs_button.visibility = View.VISIBLE
        cs_layout.visibility = View.GONE// Make next button visible if needed
        setNavBarClickability(true)
        setFooterIconsClickability(true, studentLayout)
        showLayout(studentLayout)
        studentAdapter.setClickable(true)
    }

    private fun clickedshow() {

        show_cs_layout.setOnClickListener {
            toggleVisibility(cs_layout, null, null)
            setNavBarClickability(false)
            studentAdapter.setClickable(false)

        }


        cs_close_layout.setOnClickListener {
            // Hide cs_info_second and make cs_info_first visible
            closebtnInfo()
        }


        ad_create_btn_prof.setOnClickListener{

            toggleVisibility(ct_layout, null, null)
            setNavBarClickability(false)

        }

        ct_close_layout.setOnClickListener{
            toggleVisibility(ct_layout, null, null)
            setNavBarClickability(true)
            showLayout(teacherLayout)
        }
    }
    private fun setNavBarClickability(isClickable: Boolean) {
        navBar.isClickable = isClickable
        navBar.isEnabled = isClickable
        navBar.isFocusable = !isClickable
        navBar.isFocusableInTouchMode = !isClickable

        // Search clickable state
        av_search_student.isClickable = isClickable
        av_search_student.isEnabled = isClickable

        // Footer icons clickability with the current selected layout
        setFooterIconsClickability(isClickable, currentSelectedLayout)
    }


    private fun setFooterIconsClickability(isClickable: Boolean, selectedLayout: View?) {
        // Loop through each icon and apply the appropriate clickability and color
        setIconClickability(adminIcon, isClickable, adminLayout, selectedLayout == adminLayout)
        setIconClickability(studentIcon, isClickable, studentLayout, selectedLayout == studentLayout)
        setIconClickability(homeIcon, isClickable, homeScrollView, selectedLayout == homeScrollView)
        setIconClickability(teacherIcon, isClickable, teacherLayout, selectedLayout == teacherLayout)
    }


    private fun setIconClickability(
        icon: ImageView,
        isClickable: Boolean,
        layoutToShow: View,
        isSelected: Boolean
    ) {
        icon.isClickable = isClickable
        icon.isEnabled = isClickable

        // Maintain selected icon color if this is the currently selected layout
        val color = when {
            isSelected -> ContextCompat.getColor(this, R.color.white) // Selected icon color
            isClickable -> ContextCompat.getColor(this, R.color.white) // Default clickable color
            else -> ContextCompat.getColor(this, R.color.white_text) // Non-clickable state
        }
        icon.setColorFilter(color, PorterDuff.Mode.SRC_IN)

        // Manage click listeners only if clickable
        if (isClickable) {
            icon.setOnClickListener { showLayout(layoutToShow) }
        } else {
            icon.setOnClickListener(null) // Disable listener
        }
    }


    private fun toggleVisibility(targetView: View, buttonS: Button?, secondLayout: View?) {
        if (targetView.visibility == View.VISIBLE) {
            targetView.visibility = View.GONE
            buttonS?.visibility = View.GONE
            secondLayout?.visibility = View.VISIBLE

        } else {
            targetView.visibility = View.VISIBLE
            buttonS?.visibility = View.VISIBLE
            secondLayout?.visibility = View.GONE
        }
    }

    private fun searchStudent() {
        av_search_student.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                val filteredList = cachedStudents?.filter {
                    it.first_name.contains(query, ignoreCase = true) ||
                            it.last_name.contains(query, ignoreCase = true) ||
                            it.middle_name.contains(query, ignoreCase = true) ||
                            it.student_id.contains(query, ignoreCase = true) ||
                            it.email.contains(query, ignoreCase = true) ||
                            it.student_status.contains(query, ignoreCase = true) ||
                            // Check if the query is 'enrollment' to match the corresponding status
                            (query.equals("enrollment", ignoreCase = true) && it.student_status == "E") ||
                            (query.equals("transferred", ignoreCase = true) && it.student_status == "T") ||
                            (query.equals("dropped", ignoreCase = true) && it.student_status == "D") ||
                            (query.equals("graduated", ignoreCase = true) && it.student_status == "G")
                } ?: emptyList()

                // Use the studentAdapter instance to filter
                studentAdapter.filters(filteredList)
            }
        })
    }



    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver to prevent memory leaks
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateReceiver)
    }


    // Fetch students again when the activity resumes (when you come back from AdminViewStudentOne)
    override fun onResume() {
        super.onResume()
        val firstname = intent.getStringExtra("first_name")
        val lastname = intent.getStringExtra("last_name")
        if (firstname != null && lastname != null) {
            name.text = "$lastname $firstname"
        } else {
            Log.d("adminView", "First name or last name is null")
        }
        // Refresh the students list whenever the activity resumes
        fetchStudents()
        updateCountsUI()
    }

    private fun startAdminViewStudentOne() {
        val intent = Intent(this, AdminViewStudentOne::class.java)
        startActivity(intent) // Simply start the activity
    }



    // Enable edge-to-edge layout for devices with Android R and above
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




    // Fetch the students data..
    private fun fetchStudents() {
        progressBar.visibility = View.VISIBLE // Show the fucking progress bar

        // // Call our Existing Api shit
        ApiClient.fetchStudents { students ->
            runOnUiThread {
                progressBar.visibility = View.GONE
                if (students != null && students.isNotEmpty()) {
                    cachedStudents = students
                    updateRecyclerView(students)
                } else {
                    Log.d("adminView", "No students found.")
                    Toast.makeText(this, "No students found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateRecyclerView(students: List<StudentResponse>) {

        studentAdapter = StudentAdapter(students, this ) // need this
        recyclerView.adapter = studentAdapter
    }
    private fun searchProf() {
        av_seachProf.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                Log.d("searchProf", "Search query: $query")

                val filteredList = if (query.isEmpty()) {
                    cachedProfessor ?: emptyList()
                } else {
                    cachedProfessor?.filter {
                        (it.first_name?.contains(query, ignoreCase = true) == true) ||
                                (it.last_name?.contains(query, ignoreCase = true) == true) ||
                                (it.middle_name?.contains(query, ignoreCase = true) == true) ||
                                (it.email?.contains(query, ignoreCase = true) == true)
                    } ?: emptyList()
                }

                Log.d("searchProf", "Filtered results count: ${filteredList.size}")
                professorAdapter.filters(filteredList) // Use the member variable
            }
        })
    }

    private fun fetchProf() {
        // Call the ApiClient to fetch professors instead of students
        ApiClient.fetchProfessors { professors ->
            runOnUiThread {
                if (professors != null && professors.isNotEmpty()) {
                    cachedProfessor = professors
                    updateTeacherView(professors)
                } else {
                    Log.d("adminView", "No professors found.")
                    Toast.makeText(this, "No professors found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateTeacherView(professors: List<ProfessorResponse>) {
        // Initialize ProfessorAdapter with the list of professors and set it to the RecyclerView
        professorAdapter = ProfessorAdapter(professors) // Use member variable
        teacherRecycleView.adapter = professorAdapter
    }
}



