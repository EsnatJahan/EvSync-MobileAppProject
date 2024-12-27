package com.esa.evsync.app.pages.EventDetails

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class EventDetailsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2 // Number of tabs/pages

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EventDetailsTasksFragment()
            else -> EventDetailsMembersFragment()
        }
    }
}