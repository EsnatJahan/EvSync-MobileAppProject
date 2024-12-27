package com.esa.evsync.app.pages.EventList

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.esa.evsync.R
import com.esa.evsync.app.dataModels.EventModel
import com.esa.evsync.app.dataModels.UserModel

import com.esa.evsync.databinding.FragmentMemberCardBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * [RecyclerView.Adapter] that can display a [UserModel]..
 */
class EventDetailsMembersRCAdapter(
    private val members: ArrayList<UserModel>,
    private val event: EventModel,
    private val itemView: View // fragments context
) : RecyclerView.Adapter<EventDetailsMembersRCAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentMemberCardBinding.inflate(
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
            Log.d("member_fetch", """
                members[position] : ${members[position]}
            """.trimIndent())
            if (id != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db = Firebase.firestore
                        val memberRef = db.collection("users").document(id)
                        if (event.owner == memberRef) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    itemView.context,
                                    "Can't remove the owner",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Log.d("member remove", "Can't remove owner")
                            return@launch
                        }
                        Log.d("member remove", "removing member")
                        db.collection("events").document(event.id!!).update("members", FieldValue.arrayRemove(memberRef)).await()
                        // remove from list
                        withContext(Dispatchers.Main) {
                            members.removeAt(position)
                            notifyItemRemoved(position)
                        }
                    } catch (e: Error) {
                        Toast.makeText(
                            itemView.context,
                            "Failed to remove user",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = members.size

    inner class ViewHolder(binding: FragmentMemberCardBinding) :
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