package com.esa.evsync.app.pages.EventDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.esa.evsync.app.pages.EventList.EventDetailsMembersRCAdapter
import com.esa.evsync.app.pages.EventList.EventListFragment
import com.esa.evsync.databinding.FragmentEventDetailsMembersBinding

class EventDetailsMembersFragment(
    private val viewModel: EventDetailsViewModel) : Fragment() {
    private var columnCount = 1
    private lateinit var binding: FragmentEventDetailsMembersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventDetailsMembersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddMember.setOnClickListener {
            val navController = findNavController()
            val action = EventDetailsFragmentDirections.actionNavEventDetailsToNavMemberAdd(
                eventId = viewModel.eventId
            )
            navController.navigate(action)
        }

        binding.rcEventMembers.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }

        if (binding.rcEventMembers.adapter == null)
            binding.rcEventMembers.adapter = EventDetailsMembersRCAdapter(ArrayList(), binding.root, viewModel)
        if (viewModel.members.value == null) showLoading(true)
        else (binding.rcEventMembers.adapter as EventDetailsMembersRCAdapter).setData(viewModel.members.value!!)

        viewModel.members.observe(viewLifecycleOwner) { members ->
            (binding.rcEventMembers.adapter as EventDetailsMembersRCAdapter).setData(members)
            showLoading(false)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (view == null) return
        if (isLoading) {
            binding.pbTlDetails.visibility = View.VISIBLE
            binding.rcEventMembers.visibility = View.GONE
        } else {
            binding.pbTlDetails.visibility = View.GONE
            binding.rcEventMembers.visibility = View.VISIBLE
        }
    }

    companion object {
        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "1"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            EventListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}