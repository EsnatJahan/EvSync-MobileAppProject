package com.esa.evsync.app.pages.EventDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.esa.evsync.databinding.FragmentEventDetailsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class EventDetailsFragment : Fragment() {
    private val args: EventDetailsFragmentArgs by navArgs()
    private lateinit  var binding: FragmentEventDetailsBinding
    private val db = Firebase.firestore
    private lateinit var viewModel: EventDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = EventDetailsViewModel.Factory(args.eventId)
        viewModel = ViewModelProvider(this, factory)[EventDetailsViewModel::class.java]
    }

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

        viewModel.toastMessage.observe(viewLifecycleOwner) {msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        viewModel.eventName.observe(viewLifecycleOwner) {name -> binding.tvName.text = name}
        viewModel.eventDescriptor.observe(viewLifecycleOwner) {description -> binding.tvDescriptiopn.text = description}

        viewPager2.adapter = EventDetailsPagerAdapter(fragment, viewModel)
        // Connect TabLayout and ViewPager2
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "Tasks"
                else -> "Members"
            }
        }.attach()

        viewModel.fetchEventInfo()

        parentFragmentManager.setFragmentResultListener("request_user_picker", viewLifecycleOwner) { requestKey, bundle ->
            if (requestKey == "request_user_picker") {
                val selectedResults = bundle.getStringArrayList("selected_result")
                viewModel.addMembers(selectedResults)
            }
        }
        parentFragmentManager.setFragmentResultListener("request_task_add", viewLifecycleOwner) { requestKey, bundle ->
            if (requestKey == "request_task_add") {
                val taskInfo = bundle.getParcelable<TaskAddDataModel>("task_info")
                viewModel.addTask(taskInfo)
            }
        }
    }

}