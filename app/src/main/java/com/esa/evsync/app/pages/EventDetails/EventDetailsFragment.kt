package com.esa.evsync.app.pages.EventDetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.esa.evsync.R
import com.esa.evsync.app.dataModels.EventModel
import com.esa.evsync.app.pages.EventList.EventCardRecyclerViewAdapter
import com.esa.evsync.databinding.FragmentEventCardBinding
import com.esa.evsync.databinding.FragmentEventDetailsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class EventDetailsFragment : Fragment() {
    private val args: EventDetailsFragmentArgs by navArgs()
    private lateinit  var binding: FragmentEventDetailsBinding
    private var db = Firebase.firestore
    private lateinit var event: EventModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventDetailsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = args.eventName
        val id = args.eventId
        val tabLayout: TabLayout = binding.tlEventDetails
        val viewPager2: ViewPager2 = binding.vpEventDetails

        val fragment = this

        binding.tvName.text = args.eventName
        binding.tvDescriptiopn.text = args.eventDescription

        showLoading(true)
        lifecycleScope.launch {
            val ref = withContext(Dispatchers.IO) {
                db.collection("events").document(id).get().await()
            }
            event = ref.toObject(EventModel::class.java)!!
            event.id = ref.id

            // Set the adapter for ViewPager2
            viewPager2.adapter = EventDetailsPagerAdapter(fragment, event)
            // Connect TabLayout and ViewPager2
            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.text = when (position) {
                    0 -> "Tasks"
                    else -> "Members"
                }
            }.attach()
            showLoading(false)
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbTlDetails.visibility = View.VISIBLE
            binding.vpEventDetails.visibility = View.GONE
        } else {
            binding.pbTlDetails.visibility = View.GONE
            binding.vpEventDetails.visibility = View.VISIBLE
        }
    }
}