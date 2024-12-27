package com.esa.evsync.app.pages.EventList

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.esa.evsync.R
import com.esa.evsync.app.dataModels.GroupModel

import com.esa.evsync.databinding.FragmentEventCardBinding

/**
 * [RecyclerView.Adapter] that can display a [GroupModel]..
 */
class EventCardRecyclerViewAdapter(
    var values: List<GroupModel>
) : RecyclerView.Adapter<EventCardRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentEventCardBinding.inflate(
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
        if (item.image != null) {
            Glide.with(context)
                .load(item.image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.baseline_settings_black_24dp)
                .into(holder.idImage)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentEventCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idName: TextView = binding.tvName
        val idDesc: TextView = binding.tvDescription
        val idImage: ImageView = binding.imImage

        override fun toString(): String {
            return super.toString() + " '(${idName.text}: ${idDesc.text})'"
        }
    }

}