package com.example.myapplication


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

class StudentAdapter(private val students: List<StudentResponse>, private val activity: adminView ) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    // Add Activity parameter

    private var filteredStudents: List<StudentResponse> = students
    private var isClickable: Boolean = true // Track clickability

    fun filters(filteredList: List<StudentResponse>) {
        filteredStudents = filteredList
        notifyDataSetChanged()
    }

    fun setClickable(clickable: Boolean) {
        isClickable = clickable
        notifyDataSetChanged() // Refresh the view to apply changes
    }

    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val studentNumber: TextView = view.findViewById(R.id.studentNumber)
        val fullName: TextView = view.findViewById(R.id.studentName)
        val email: TextView = view.findViewById(R.id.studentEmail)
        val address: TextView = view.findViewById(R.id.studentStatus)

        // Add more TextViews as needed for other fields
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)
        return StudentViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = filteredStudents[position]
        holder.studentNumber.text = student.student_id
        holder.fullName.text = "${student.first_name} ${student.last_name} ${student.middle_name}"
        holder.email.text = student.email
        holder.address.text = student.student_status

        // Set the click listener for the item
        holder.itemView.setOnClickListener {
            if (isClickable) { // Check if items are clickable
                Toast.makeText(holder.itemView.context, "Student Id: ${student.student_id}", Toast.LENGTH_SHORT).show()

                // Fetch student details by student number
                ApiClient.fetchStudents(student.student_id) { fetchedStudents ->
                    if (fetchedStudents != null && fetchedStudents.isNotEmpty()) {
                        val fetchedStudent = fetchedStudents[0]
                        val context = holder.itemView.context
                        val intent = Intent(context, AdminViewStudentOne::class.java)

                        // Pass the details of the fetched student
                        intent.putExtra("student_id", fetchedStudent.student_id)
                        intent.putExtra("first_name", fetchedStudent.first_name)
                        intent.putExtra("last_name", fetchedStudent.last_name)
                        intent.putExtra("middle_name", fetchedStudent.middle_name)
                        intent.putExtra("contact", fetchedStudent.contact_number)
                        intent.putExtra("email", fetchedStudent.email)
                        intent.putExtra("address", fetchedStudent.address)
                        intent.putExtra("status", fetchedStudent.student_status)
                        intent.putExtra("studentType", fetchedStudent.student_type.toString()) // If student_type is an Int
                        // Finish the current activity before starting the new one

                        context.startActivity(intent)

                    } else {
                        Toast.makeText(holder.itemView.context, "Failed to fetch student details", Toast.LENGTH_SHORT).show()
                    }
                }
            } // If not clickable, do nothing
        }
    }

    override fun getItemCount(): Int = filteredStudents.size
}

