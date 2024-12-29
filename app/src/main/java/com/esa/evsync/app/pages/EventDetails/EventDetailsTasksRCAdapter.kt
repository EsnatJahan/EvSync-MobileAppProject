package com.esa.evsync.app.pages.EventList

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.esa.evsync.app.dataModels.TaskModel

import com.esa.evsync.databinding.CardTaskListitemBinding

/**
 * [RecyclerView.Adapter] that can display a [TaskModel]..
 */
class EventDetailsTasksRCAdapter(
    var tasks: List<TaskModel>,
    private val itemView: View // fragments context
) : RecyclerView.Adapter<EventDetailsTasksRCAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardTaskListitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.idName.text = task.name?:"Unknown group"
        holder.idDesc.text = task.description?:"Unknown description"
    }

    override fun getItemCount(): Int = tasks.size

    fun setData(newTasks: ArrayList<TaskModel>) {
        tasks = newTasks
        notifyDataSetChanged()  // Notify that the dataset has changed
    }

    inner class ViewHolder(binding: CardTaskListitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idName: TextView = binding.tvName
        val idDesc: TextView = binding.tvDescription
//        val idImage: ImageView = binding.imImage

        init {
//            binding.root.setOnClickListener {
//                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
//                    val navController = itemView.findNavController()
//                    val action = EventCardFragmentDirections.actionNavEventsToNavEventDetails(values[bindingAdapterPosition]?.id ?: "")
//                    navController.navigate(action)
//                }
//            }
        }

        override fun toString(): String {
            return super.toString() + " '(${idName.text}: ${idDesc.text})'"
        }
    }


}