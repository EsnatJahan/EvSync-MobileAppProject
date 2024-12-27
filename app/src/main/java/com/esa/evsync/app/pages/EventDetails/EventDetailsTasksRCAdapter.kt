package com.esa.evsync.app.pages.EventList

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.esa.evsync.R
import com.esa.evsync.app.dataModels.TaskModel

import com.esa.evsync.databinding.FragmentEventCardBinding
import com.esa.evsync.databinding.FragmentTaskCardBinding

/**
 * [RecyclerView.Adapter] that can display a [TaskModel]..
 */
class EventDetailsTasksRCAdapter(
    var values: List<TaskModel>,
    private val itemView: View // fragments context
) : RecyclerView.Adapter<EventDetailsTasksRCAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentTaskCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idName.text = item.name?:"Unknown group"
        holder.idDesc.text = item.description?:"Unknown description"
        val context = holder.idName.context
//        if (item.image != null) {
//            Glide.with(context)
//                .load(item.image)
//                .placeholder(R.drawable.baseline_cached_black_24dp)
//                .error(R.drawable.baseline_sync_problem_black_24dp)
//                .into(holder.idImage)
//        } else {
//            holder.idImage.setImageResource(R.drawable.baseline_event_black_24dp)
//        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentTaskCardBinding) :
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