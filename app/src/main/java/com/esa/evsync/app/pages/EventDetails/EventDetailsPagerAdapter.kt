package com.esa.evsync.app.pages.EventDetails

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.esa.evsync.app.dataModels.EventModel

class EventDetailsPagerAdapter(
    fragment: Fragment,
    viewModel: EventDetailsViewModel) : FragmentStateAdapter(fragment) {

    private val fragments: List<Fragment> = listOf(
        EventDetailsTasksFragment(viewModel),
        EventDetailsMembersFragment(viewModel)
    )

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]

}