package com.esa.evsync.app.pages.UserPicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esa.evsync.R
import com.esa.evsync.databinding.CardMemberSearchItemBinding

class MemberSearchAdapter(private val onItemSelected: (UserPickerDataModel, Boolean) -> Unit) :
    ListAdapter<UserPickerDataModel, MemberSearchAdapter.SearchViewHolder>(ResultDiffCallback()) {

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

        fun bind(item: UserPickerDataModel) {
            binding.name.text = item.name
            binding.email.text = item.email

            if (item.profileImageUrl != null) {
                Glide.with(binding.profileImage.context)
                    .load(item.profileImageUrl)
                    .placeholder(R.drawable.baseline_cached_black_24dp)
                    .error(R.drawable.baseline_account_circle_black_24dp)
                    .into(binding.profileImage)
            } else {
                binding.profileImage.setImageResource(R.drawable.baseline_account_circle_black_24dp)
            }
            binding.checkbox.isChecked = item.isSelected
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                onItemSelected(item, isChecked)
            }
        }
    }
}


class ResultDiffCallback : DiffUtil.ItemCallback<UserPickerDataModel>() {
    override fun areItemsTheSame(oldItem: UserPickerDataModel, newItem: UserPickerDataModel): Boolean {
        return oldItem.name == newItem.name // Assuming name is unique, adjust if needed
    }

    override fun areContentsTheSame(oldItem: UserPickerDataModel, newItem: UserPickerDataModel): Boolean {
        return oldItem == newItem
    }
}
