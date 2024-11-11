package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

// Adapter for displaying student attendance data in a RecyclerView
class AdminViewStudentAttendanceAdapter(
    private val data: List<AdminViewStudentAttendanceData>,
    private val onItemClick: (AdminViewStudentAttendanceData) -> Unit // Click listener
) : RecyclerView.Adapter<AdminViewStudentAttendanceAdapter.AdminViewStudentAttendanceHolder>() {


    private var filterAttendance: List<AdminViewStudentAttendanceData> = data


    private var isClickable: Boolean = true // Track clickability

    fun setClickables(clickable: Boolean) {
        isClickable = clickable
        notifyDataSetChanged() // Refresh the view to apply changes
    }

    // Function to update the list and refresh the RecyclerView
    fun updateList(filterList: List<AdminViewStudentAttendanceData>) {
        filterAttendance = filterList
        notifyDataSetChanged()
    }

    // ViewHolder class for holding each attendance item view
    class AdminViewStudentAttendanceHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateAttendance: TextView = view.findViewById(R.id.Date)
        val timeIn: TextView = view.findViewById(R.id.Time_in)
        val timeOut: TextView = view.findViewById(R.id.Time_out)
        val status: TextView = view.findViewById(R.id.Status)
        val section: TextView = view.findViewById(R.id.Section)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewStudentAttendanceHolder {
        // Inflate the attendance item layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_attendance_item, parent, false)
        return AdminViewStudentAttendanceHolder(view)
    }

    override fun onBindViewHolder(holder: AdminViewStudentAttendanceHolder, position: Int) {
        // Get the data for the current position
        val attendanceData = filterAttendance[position]

        // Populate the ViewHolder with data
        holder.dateAttendance.text = attendanceData.log_date
        holder.timeIn.text = attendanceData.time_in
        holder.timeOut.text = attendanceData.time_out
        holder.status.text = attendanceData.status
        holder.section.text = attendanceData.section

        // Set click listener for the item view using the passed lambda
        holder.itemView.setOnClickListener {
            if (isClickable) {
                onItemClick(attendanceData) // Trigger the click event if clickable
            }
        }
    }

    override fun getItemCount(): Int = filterAttendance.size
}
