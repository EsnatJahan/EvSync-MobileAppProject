package com.esa.evsync.app.pages.EventDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.esa.evsync.R
import com.esa.evsync.app.pages.EventList.EventCardRecyclerViewAdapter
import com.esa.evsync.databinding.FragmentEventCardBinding
import com.esa.evsync.databinding.FragmentEventDetailsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class EventDetailsFragment : Fragment() {
    private val args: EventDetailsFragmentArgs by navArgs()
    private lateinit  var binding: FragmentEventDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEventDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.eventId
        val tabLayout: TabLayout = binding.tlEventDetails
        val viewPager2: ViewPager2 = binding.vpEventDetails

        // Set the adapter for ViewPager2
        viewPager2.adapter = EventDetailsPagerAdapter(this)

        // Connect TabLayout and ViewPager2
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "Tasks"
                else -> "Members"
            }
        }.attach()
    }
}