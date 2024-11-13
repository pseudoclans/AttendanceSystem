package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminViewProfessorStudentAdapter(
    private val originalData: List<AdminViewProfessorStudentData>, // Original unfiltered list
    private val onItemClick: (AdminViewProfessorStudentData) -> Unit // Click listener for each item
) : RecyclerView.Adapter<AdminViewProfessorStudentAdapter.ProfessorStudentViewHolder>() {

    private var filteredData: List<AdminViewProfessorStudentData> = originalData // Filtered list

    // Method to update the filtered data list
    fun updateListed(filteredList: List<AdminViewProfessorStudentData>) {
        filteredData = filteredList
        notifyDataSetChanged()
    }

    // ViewHolder class
    class ProfessorStudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val studentNumber: TextView = view.findViewById(R.id.studentNumber)
        val studentName: TextView = view.findViewById(R.id.studentName)
        val studentEmail: TextView = view.findViewById(R.id.studentEmail)
        val studentCourse: TextView = view.findViewById(R.id.studentStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessorStudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_item, parent, false) // Replace with your item layout
        return ProfessorStudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfessorStudentViewHolder, position: Int) {
        val studentData = filteredData[position]
        holder.studentNumber.text = studentData.student_id
        holder.studentName.text =
            "${studentData.student_first_name} ${studentData.student_middle_name} ${studentData.student_last_name}"
        holder.studentEmail.text = studentData.student_email
        holder.studentCourse.text = studentData.course_code

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick(studentData) // Call the provided onItemClick lambda
        }
    }

    override fun getItemCount(): Int = filteredData.size
}
