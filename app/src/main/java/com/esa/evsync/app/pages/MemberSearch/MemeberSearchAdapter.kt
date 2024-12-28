package com.esa.evsync.app.pages.MemberSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esa.evsync.databinding.CardMemberSearchItemBinding

class MemberSearchAdapter(private val onItemSelected: (MemberResultModel, Boolean) -> Unit) :
    ListAdapter<MemberResultModel, MemberSearchAdapter.SearchViewHolder>(ResultDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = CardMemberSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class SearchViewHolder(private val binding: CardMemberSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MemberResultModel) {
            binding.name.text = item.name
            binding.email.text = item.email
            // Load the profile image using Glide or Picasso
            Glide.with(binding.profileImage.context)
                .load(item.profileImageUrl) // URL or local resource
                .into(binding.profileImage)

            binding.checkbox.isChecked = item.isSelected
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                onItemSelected(item, isChecked)
            }
        }
    }
}


class ResultDiffCallback : DiffUtil.ItemCallback<MemberResultModel>() {
    override fun areItemsTheSame(oldItem: MemberResultModel, newItem: MemberResultModel): Boolean {
        return oldItem.name == newItem.name // Assuming name is unique, adjust if needed
    }

    override fun areContentsTheSame(oldItem: MemberResultModel, newItem: MemberResultModel): Boolean {
        return oldItem == newItem
    }
}
