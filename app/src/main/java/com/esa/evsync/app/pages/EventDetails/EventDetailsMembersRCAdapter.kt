package com.esa.evsync.app.pages.EventList

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.esa.evsync.R
import com.esa.evsync.app.dataModels.UserModel
import com.esa.evsync.app.pages.EventDetails.EventDetailsViewModel

import com.esa.evsync.databinding.CardMemberListitemBinding

/**
 * [RecyclerView.Adapter] that can display a [UserModel]..
 */
class EventDetailsMembersRCAdapter(
    private var members: ArrayList<UserModel>,
    private val viewMdoel: EventDetailsViewModel
) : RecyclerView.Adapter<EventDetailsMembersRCAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardMemberListitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member = members[position]
        holder.idName.text = member.username?:"Unknown group"
        holder.idDesc.text = member.email?:"Unknown description"
        val context = holder.idName.context
        if (member.profile_picture != null) {
            Glide.with(context)
                .load(member.profile_picture)
                .placeholder(R.drawable.baseline_cached_black_24dp)
                .error(R.drawable.baseline_sync_problem_black_24dp)
                .into(holder.idImage)
        } else {
            holder.idImage.setImageResource(R.drawable.baseline_account_circle_black_24dp)
        }

        holder.btn_remove.setOnClickListener {
            val id = members[position].id
            if (id != null) {
                viewMdoel.removeMember(id) {
                    members.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }

    override fun getItemCount(): Int = members.size

    fun setData(newMembers: ArrayList<UserModel>) {
        members = newMembers
        notifyDataSetChanged()  // Notify that the dataset has changed
    }
    inner class ViewHolder(binding: CardMemberListitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idName: TextView = binding.tvName
        val idDesc: TextView = binding.tvDescription
        val idImage: ImageView = binding.imImage
        val btn_remove: ImageButton = binding.btnRemove

        override fun toString(): String {
            return super.toString() + " '(${idName.text}: ${idDesc.text})'"
        }
    }


}