package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter for displaying professor-student attendance data
class AdminViewProfessorStudentAttendanceAdapter(
    private val data: List<AdminViewProfessorStudentAttendanceData>, // Ensure the correct data type
    private val onItemClick: (AdminViewProfessorStudentAttendanceData) -> Unit // Click listener
) : RecyclerView.Adapter<AdminViewProfessorStudentAttendanceAdapter.AdminViewProfessorStudentAttendanceHolder>() {

    private var filteredAttendance: List<AdminViewProfessorStudentAttendanceData> = data

    fun updateListed(filteredList: List<AdminViewProfessorStudentAttendanceData>) {
        filteredAttendance = filteredList
        notifyDataSetChanged()
    }

    class AdminViewProfessorStudentAttendanceHolder(view: View) : RecyclerView.ViewHolder(view) {
        val logDate: TextView = view.findViewById(R.id.Date)
        val timeIn: TextView = view.findViewById(R.id.Time_in)
        val timeOut: TextView = view.findViewById(R.id.Time_out)
        val section: TextView = view.findViewById(R.id.Section)
        val status: TextView = view.findViewById(R.id.Status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewProfessorStudentAttendanceHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_attendance_item, parent, false)
        return AdminViewProfessorStudentAttendanceHolder(view)
    }

    override fun onBindViewHolder(holder: AdminViewProfessorStudentAttendanceHolder, position: Int) {
        val attendanceData = filteredAttendance[position]

        holder.logDate.text = attendanceData.log_date
        holder.timeIn.text = attendanceData.time_in
        holder.timeOut.text = attendanceData.time_out ?: "N/A"
        holder.status.text = attendanceData.status
        holder.section.text = attendanceData.program_code

        holder.itemView.setOnClickListener {
            onItemClick(attendanceData)
        }
    }

    override fun getItemCount(): Int = filteredAttendance.size
}
