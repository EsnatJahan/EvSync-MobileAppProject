package com.esa.evsync.app.pages.EventDetails

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.esa.evsync.app.dataModels.EventModel

class EventDetailsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments: List<Fragment> = listOf(
        EventDetailsTasksFragment(),
        EventDetailsMembersFragment()
    )

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]

    fun updateEvent(event: EventModel) {
        Log.d("datafetch", "PagerAdapter: update called with event $event")
        fragments.forEach {
            if (it is EventDetailsUpdatable) { // Interface for fragments that can be updated
                it.updateEvent(event)
            }
        }
    }
}