package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ProfessorAdapter(private val professors: List<ProfessorResponse>) :
    RecyclerView.Adapter<ProfessorAdapter.ProfessorViewHolder>() {

    private var filteredProfessors: List<ProfessorResponse> = professors
    private var isClickable: Boolean = true // Track clickability

    fun filters(filteredList: List<ProfessorResponse>) {
        filteredProfessors = filteredList
        notifyDataSetChanged()
    }

    fun setClickable(clickable: Boolean) {
        isClickable = clickable
        notifyDataSetChanged() // Refresh the view to apply changes
    }

    class ProfessorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val professorId: TextView = view.findViewById(R.id.ProffesorId)
        val professorName: TextView = view.findViewById(R.id.proffessorName)
        val professorEmail: TextView = view.findViewById(R.id.professorEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessorViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.professor_item, parent, false)
        return ProfessorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfessorViewHolder, position: Int) {
        val professor = filteredProfessors[position]
        holder.professorId.text = professor.professor_id.toString()
        holder.professorName.text = "${professor.last_name}, ${professor.first_name} ${professor.middle_name ?: ""}".trim()
        holder.professorEmail.text = professor.email

        // Set the click listener for the item
        holder.itemView.setOnClickListener {
            if (isClickable) { // Check if items are clickable
                Toast.makeText(holder.itemView.context, "Professor ID: ${professor.professor_id}", Toast.LENGTH_SHORT).show()


            }
        }
    }


    override fun getItemCount(): Int = filteredProfessors.size
}
