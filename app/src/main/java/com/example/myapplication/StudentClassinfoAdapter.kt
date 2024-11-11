package com.example.myapplication
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast


data class StudentClassinfoAdapter(private val data: List<ClassInfoData>) :
        RecyclerView.Adapter<StudentClassinfoAdapter.StudentClassinfoHolder>(){


            class StudentClassinfoHolder(view: View) : RecyclerView.ViewHolder(view){
                val firstletter: TextView = view.findViewById(R.id.ci_firstletter)
                val classname: TextView = view.findViewById(R.id.ci_name)
                val sectionn: TextView = view.findViewById(R.id.ci_section)


            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentClassinfoHolder {
          val view = LayoutInflater.from(parent.context).inflate(R.layout.class_item, parent,  false)
          return StudentClassinfoHolder(view)
    }

    override fun onBindViewHolder(holder: StudentClassinfoHolder, position: Int) {
        val classdata = data[position]

        holder.firstletter.text = classdata.course_name.first().toString()
        holder.classname.text = classdata.course_name
        holder.sectionn.text = classdata.section
        holder.itemView.setOnClickListener{

            val context = holder.itemView.context

            if(context is AdminViewStudentOne){
                 context.callClassInfo(classdata)

            }else{
                Toast.makeText(context, "Error: Context is not the right type", Toast.LENGTH_SHORT)
                    .show()

            }
        }
    }

    override fun getItemCount(): Int = data.size
        }

