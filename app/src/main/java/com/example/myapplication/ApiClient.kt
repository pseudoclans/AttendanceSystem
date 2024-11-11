// ApiClient.kt
package com.example.myapplication

import android.os.Handler
import android.os.Looper
import okhttp3.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import android.util.Log
import com.google.gson.JsonObject

object ApiClient {

    private const val BASE_URL = "http://34.228.230.132/"// Base URL for your API
    private val client = OkHttpClient()
    private val gson = Gson()


    fun sendEmail(username: String, email: String, password: String, role: String, callback: (EmailResponse?) -> Unit) {
        // Build the URL with query parameters
        val url = "$BASE_URL/sendgmail.php?username=$username&email=$email&password=$password&role=$role"

        val request = Request.Builder()
            .url(url) // Use the constructed URL
            .get() // Change to GET request
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ApiClient", "Network error: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("ApiClient", "Response failed: ${response.message}")
                        callback(null)
                        return
                    }

                    val responseBody = response.body?.string()
                    Log.d("ApiClient", "Response body: $responseBody")
                    val emailResponse = try {
                        gson.fromJson(responseBody, EmailResponse::class.java)
                    } catch (e: JsonSyntaxException) {
                        Log.e("ApiClient", "Failed to parse response: ${e.message}")
                        null
                    }
                    callback(emailResponse)
                }
            }
        })
    }

    // Login function (your existing code)
    fun login(username: String, password: String, callback: (LoginResponse?) -> Unit) {
        val loginRequest = LoginRequest(username, password)
        val requestBody = gson.toJson(loginRequest)
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/login.php")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ApiClient", "Network error: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("ApiClient", "Response failed: ${response.message}")
                        callback(null)
                        return
                    }

                    val responseBody = response.body?.string()
                    Log.d("ApiClient", "Response body: $responseBody")
                    val loginResponse = try {
                        gson.fromJson(responseBody, LoginResponse::class.java)
                    } catch (e: JsonSyntaxException) {
                        Log.e("ApiClient", "Failed to parse response: ${e.message}")
                        null
                    }
                    callback(loginResponse)
                }
            }
        })
    }

    // New insertStudent function using a Map for dynamic fields
    fun insertStudent(
        studentDetails: Map<String, Any?>, // Using a Map for dynamic fields
        callback: (InsertStudentResponse?) -> Unit
    ) {
        val requestBody = gson.toJson(studentDetails)
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/insertstudent.php") // Ensure this is the correct URL
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ApiClient", "Network error: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("ApiClient", "Response failed: ${response.message}")
                        callback(null)
                        return
                    }

                    val responseBody = response.body?.string()
                    Log.d("ApiClient", "Response body: $responseBody")
                    val insertResponse = try {
                        gson.fromJson(responseBody, InsertStudentResponse::class.java)
                    } catch (e: JsonSyntaxException) {
                        Log.e("ApiClient", "Failed to parse response: ${e.message}")
                        null
                    }
                    callback(insertResponse)
                }
            }
        })
    }

    fun insertProfessor(
        professorDetails: Map<String, Any?>, // Using a Map for dynamic fields
        callback: (InsertProfessorResponse?) -> Unit
    ) {
        // Convert professor details map to JSON
        val requestBody = gson.toJson(professorDetails)
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/insertprof.php") // Ensure this is the correct URL
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ApiClient", "Network error: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("ApiClient", "Response failed: ${response.message}")
                        callback(null)
                        return
                    }

                    val responseBody = response.body?.string()
                    Log.d("ApiClient", "Response body: $responseBody")
                    val insertResponse = try {
                        gson.fromJson(responseBody, InsertProfessorResponse::class.java)
                    } catch (e: JsonSyntaxException) {
                        Log.e("ApiClient", "Failed to parse response: ${e.message}")
                        null
                    }
                    callback(insertResponse)
                }
            }
        })
    }


    fun getAttendance(studentId: String?, callback: (List<AdminViewStudentAttendanceData>?) -> Unit) {
        val url = "$BASE_URL/getAttendance.php?student_id=$studentId"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ApiClient", "Network error: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("ApiClient", "Response failed: ${response.message}")
                        callback(null)
                        return
                    }

                    val responseBody = response.body?.string()
                    Log.d("ApiClient", "Response body: $responseBody")

                    val attendanceResponse = try {
                        // Parse the JSON into AdminViewStudentAttendanceResponse
                        gson.fromJson(responseBody, AdminViewStudentAttendanceResponse::class.java)
                    } catch (e: JsonSyntaxException) {
                        Log.e("ApiClient", "Failed to parse response: ${e.message}")
                        null
                    }

                    // Use the data field from the parsed response object
                    callback(attendanceResponse?.data)
                }
            }
        })
    }

    fun getClassInfo(studentId: String, callback: (List<ClassInfoData>?) -> Unit) {
        val url = "$BASE_URL/getStudentClassInfo.php?student_id=$studentId"
       // Log.d("ApiClient", "Request URL: $url")

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ApiClient", "Network error: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("ApiClient", "Response failed: Code ${response.code}, Message: ${response.message}")
                        callback(null)
                        return
                    }

                    val responseBody = response.body?.string()
                    Log.d("ApiClient", "Response body: $responseBody")

                    val classInfoResponse = try {
                        gson.fromJson(responseBody, ClassInfoResponse::class.java)
                    } catch (e: JsonSyntaxException) {
                        Log.e("ApiClient", "Failed to parse response: ${e.message}")
                        null
                    }

                    callback(classInfoResponse?.data)
                }
            }
        })
    }

    // New function to fetch students
    fun fetchStudents(studentNumber: String? = null, userId: String? = null, callback: (List<StudentResponse>?) -> Unit) {
        // Build the URL dynamically based on whether a student number or user ID is provided
        val url = when {
            studentNumber != null -> "$BASE_URL/selectstudent.php?student_id=$studentNumber" // Fetch specific student by student_id
            userId != null -> "$BASE_URL/selectstudent.php?user_id=$userId" // Fetch students associated with a specific user_id
            else -> "$BASE_URL/selectstudent.php" // Fetch all students
        }
        Log.d("API", "Request URL: $url")
        Log.d("API", "userIdss: $userId")
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ApiClient", "Network error: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("ApiClient", "Response failed: ${response.message}")
                        callback(null)
                        return
                    }

                    val responseBody = response.body?.string()
                    Log.d("ApiClient", "Response body: $responseBody")
                    val studentsResponse = try {
                        gson.fromJson(responseBody, StudentsResponse::class.java)
                    } catch (e: JsonSyntaxException) {
                        Log.e("ApiClient", "Failed to parse response: ${e.message}")
                        null
                    }

                    // Pass the students list to the callback
                    callback(studentsResponse?.students)
                }
            }
        })
    }





    fun fetchProfessors(professorId: String? = null, callback: (List<ProfessorResponse>?) -> Unit) {
        // Build the URL dynamically based on whether a professor ID is provided
        val url = if (professorId != null) {
            "$BASE_URL/selectprof.php?professor_id=$professorId" // Fetch specific professor
        } else {
            "$BASE_URL/selectprof.php" // Fetch all professors
        }

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ApiClient", "Network error: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("ApiClient", "Response failed: ${response.message}")
                        callback(null)
                        return
                    }

                    val responseBody = response.body?.string()
                    Log.d("ApiClient", "Response body: $responseBody")

                    val professorsList = try {
                        // Directly parse as a List of ProfessorResponse
                        gson.fromJson(responseBody, Array<ProfessorResponse>::class.java).toList()
                    } catch (e: JsonSyntaxException) {
                        Log.e("ApiClient", "Failed to parse response: ${e.message}")
                        null
                    }

                    // Pass the professors list to the callback
                    callback(professorsList)
                }
            }
        })
    }

    fun updateStudent(
        studentId: String,
        studentDetails: Map<String, Any?>,
        callback: (Boolean, Map<String, Any?>?) -> Unit // Updated callback to return updated data
    ) {
        val updateRequest = mutableMapOf<String, Any?>(
            "student_id" to studentId // Ensure student_id is included in the request
        )

        // Add non-null fields to the request
        studentDetails.forEach { (key, value) ->
            if (value != null) {
                updateRequest[key] = value
            }
        }

        val requestBody = gson.toJson(updateRequest)
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/updatestudent.php") // Ensure this is the correct URL
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ApiClient", "Network error: ${e.message}")
                callback(false, null) // Return false on failure
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("ApiClient", "Response failed: ${response.message}")
                        callback(false, null)
                        return
                    }

                    val responseBody = response.body?.string()
                    Log.d("ApiClient", "Response body: $responseBody")

                    val updatedData = mutableMapOf<String, Any?>() // To hold updated data

                    val success = try {
                        val jsonResponse = gson.fromJson(responseBody, JsonObject::class.java)
                        if (jsonResponse.get("status").asString == "success") {
                            // Extract the updated data from the 'data' object
                            val dataObject = jsonResponse.getAsJsonObject("data")
                            updatedData["student_id"] = dataObject.get("student_id")?.asString
                            updatedData["first_name"] = dataObject.get("first_name")?.asString
                            updatedData["last_name"] = dataObject.get("last_name")?.asString
                            updatedData["middle_name"] = dataObject.get("middle_name")?.asString
                            updatedData["contact_number"] = dataObject.get("contact_number")?.asString
                            updatedData["email"] = dataObject.get("email")?.asString
                            updatedData["address"] = dataObject.get("address")?.asString
                            updatedData["student_type"] = dataObject.get("student_type")?.asString
                            updatedData["student_status"] = dataObject.get("student_status")?.asString
                            true // Update successful
                        } else {
                            false // Update failed
                        }
                    } catch (e: JsonSyntaxException) {
                        Log.e("ApiClient", "Failed to parse response: ${e.message}")
                        false
                    }

                    callback(success, if (success) updatedData else null) // Return updated data if successful
                }
            }
        })
    }




    fun getAdmin(user_id: String?, callback: (AdminResponse?) -> Unit) {
        val url = "$BASE_URL/getAdmin.php?user_id=$user_id"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ApiClient", "Network error: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("ApiClient", "Response failed: ${response.message}")
                        callback(null)
                        return
                    }

                    val responseBody = response.body?.string()
                    Log.d("ApiClient", "Response body: $responseBody")
                    val adminResponse = try {
                        gson.fromJson(responseBody, AdminResponse::class.java)
                    } catch (e: JsonSyntaxException) {
                        Log.e("ApiClient", "Failed to parse response: ${e.message}")
                        null
                    }
                    callback(adminResponse)
                }
            }
        })
    }


    fun fetchCounts(callback: (CountResponse?) -> Unit, uiUpdate: ((CountResponse) -> Unit)? = null) {
        val url = "$BASE_URL/getCount.php"  // Update the endpoint path if necessary

        // Create a GET request
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        // Asynchronous call
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ApiClient", "Network error: ${e.message}")
                callback(null)  // Notify callback with null on failure
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("ApiClient", "Response failed: ${response.message}")
                        callback(null)  // Notify callback with null if response is not successful
                        return
                    }

                    val responseBody = response.body?.string()
                    Log.d("ApiClient", "Response body: $responseBody")

                    val countResponse = try {
                        gson.fromJson(responseBody, CountResponse::class.java)
                    } catch (e: JsonSyntaxException) {
                        Log.e("ApiClient", "Failed to parse response: ${e.message}")
                        null  // Return null if parsing fails
                    }

                    // Pass the parsed response to the callback
                    callback(countResponse)


                }
            }
        })
    }


}
