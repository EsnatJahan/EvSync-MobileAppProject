package com.esa.evsync.app.pages

import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.esa.evsync.R
import com.esa.evsync.app.dataModels.EventModel
import com.esa.evsync.app.dataModels.TaskModel
import com.esa.evsync.databinding.FragmentEventCardBinding
import com.esa.evsync.databinding.FragmentTaskListBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * [RecyclerView.Adapter] that can display a [EventModel]..
 */
class Task_ListAdapter(
    var events: List<TaskModel>,
    private val itemView: View // fragments context

) : RecyclerView.Adapter<Task_ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentTaskListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = events[position]
        holder.idName.text = item.name?:"Unknown group"
        holder.idDesc.text = item.description?:"Unknown description"

        val idDate = item.deadline?.let {
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            dateFormat.format(it)
        } ?: "Unknown date"

        holder.dateTextView.text = idDate
        val context = holder.idName.context
    }

    override fun getItemCount(): Int = events.size

    inner class ViewHolder(binding: FragmentTaskListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idName: TextView = itemView.findViewById(R.id.rv_Event)
        val idDesc: TextView = itemView.findViewById(R.id.rv_Description)
        val dateTextView: TextView = binding.idDateTextView

        override fun toString(): String {
            return super.toString() + " '(${idName.text}: ${idDesc.text})'"
        }
    }
}