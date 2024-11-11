package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.WindowInsets  // Add this import
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var hideeye: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)  // Set content view before enabling edge-to-edge

        enableEdgeToEdge()  // For edge-to-edge display

        // Initialize UI components
        usernameEditText = findViewById(R.id.editTextText3)
        passwordEditText = findViewById(R.id.textboxpassword)
        submitButton = findViewById(R.id.submit)
        hideeye = findViewById(R.id.hidenow)


        hideeye.setOnClickListener{


            if(passwordEditText.inputType == InputType.TYPE_CLASS_TEXT ){
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                hideeye.setImageResource(R.drawable.hide)
            }else{
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT

                hideeye.setImageResource(R.drawable.view)

            }



        }

        // Handle submit button click
        submitButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Call the API to log in
            ApiClient.login(username, password) { response ->
                runOnUiThread {
                    // Handle the response from the API
                    if (response != null) {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        when (response.role){
                            "Admin" ->{
                                ApiClient.getAdmin(response.user_id) { adminResponse ->
                                    runOnUiThread {
                                        if (adminResponse != null && adminResponse.status == "success") { // checkn natin kung success
                                            // Handle the admin response here
                                            val adminData = adminResponse.data.firstOrNull() // Get the first admin data
                                            if (adminData != null) {
                                                Toast.makeText(this, "Welcome, ${adminData.first_name} ${adminData.last_name}", Toast.LENGTH_SHORT).show()

                                                // Navigate to the adminView activity and pass admin details
                                                val intent = Intent(this, adminView::class.java)
                                                intent.putExtra("user_id", adminData.user_id) // Use adminData instead of adminResponse
                                                intent.putExtra("first_name", adminData.first_name) // Use adminData instead of adminResponse
                                                intent.putExtra("last_name", adminData.last_name)
                                                intent.putExtra("middle_name", adminData.middle_name) // Use adminData instead of adminResponse
                                                intent.putExtra("email", adminData.email) // Use adminData instead of adminResponse
                                                startActivity(intent)
                                                finish()
                                            } else {
                                                Toast.makeText(this, "No admin data found", Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            Toast.makeText(this, "Failed to fetch admin details", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }


                            }"Prof" ->{
                                val intent = Intent(this, profView::class.java)
                                intent.putExtra("user_id", response.user_id)
                                intent.putExtra("username", response.username)
                                startActivity(intent)
                            }"Student" -> {
                            Log.d("API", "userIdss: ${response.user_id}")
                            ApiClient.fetchStudents(userId = response.user_id) { fetchedStudents ->
                                runOnUiThread {
                                    // Check if the fetched students list is not null and contains at least one student
                                    if (fetchedStudents != null && fetchedStudents.isNotEmpty()) {
                                        val fetchedStudent = fetchedStudents[0] // Get the first student in the list
                                        val intent = Intent(this, studentView::class.java)

                                        // Pass the details of the fetched student and the user_id
                                        intent.putExtra("student_id", fetchedStudent.student_id)
                                        intent.putExtra("first_name", fetchedStudent.first_name)
                                        intent.putExtra("last_name", fetchedStudent.last_name)
                                        intent.putExtra("middle_name", fetchedStudent.middle_name)
                                        intent.putExtra("contact", fetchedStudent.contact_number)
                                        intent.putExtra("email", fetchedStudent.email)
                                        intent.putExtra("address", fetchedStudent.address)
                                        intent.putExtra("status", fetchedStudent.student_status)
                                        intent.putExtra("studentType", fetchedStudent.student_type.toString()) // If student_type is an Int
                                        intent.putExtra("user_id", response.user_id) // Pass the user_id

                                        // Start the new activity
                                        startActivity(intent)
                                    } else {
                                        // Handle the case where no students were fetched
                                        runOnUiThread {
                                            Toast.makeText(
                                                this,
                                                "No student found with that ID",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }}

                            else -> {
                                Toast.makeText(this, "Unknown role", Toast.LENGTH_SHORT).show()
                            }

                        }


                    } else {
                        Toast.makeText(this, "Login faileds", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Set up window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Function to enable edge-to-edge display
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
