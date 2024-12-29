package com.esa.evsync.app.pages.EventList

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.esa.evsync.R
import com.esa.evsync.databinding.FragmentEventListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * A fragment representing a list of Items.
 */
class EventListFragment : Fragment() {

    private var columnCount = 1
    private lateinit var binding: FragmentEventListBinding
    private lateinit var currentUser: FirebaseUser
    private val viewModel: EventListViewModel by viewModels()

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
        binding = FragmentEventListBinding.inflate(layoutInflater)

        currentUser = FirebaseAuth.getInstance().currentUser!!

        val recycleView = binding.rcEvents
        with(recycleView) {
            recycleView.layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            if (adapter == null)
                adapter = EventListRecyclerViewAdapter(viewModel.event.value ?: ArrayList())
        }

        binding.btnAddEvent.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_eventsFragment_to_eventAddFragment)
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        viewModel.event.observe(viewLifecycleOwner) { events ->
            (recycleView.adapter as EventListRecyclerViewAdapter).setData(events)
            showLoading(false)
        }

        parentFragmentManager.setFragmentResultListener("request_add_event", viewLifecycleOwner) { requestKey, bundle ->
            if (requestKey == "request_add_event") {
                @Suppress("DEPRECATION") val newEvent = bundle.getParcelable<NewEventInfo>("newEvent")
                viewModel.addEvent(newEvent)
            }
        }

        if (viewModel.event.value == null) {
            showLoading(true)
            viewModel.fetchEvents()
        }
        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        if (view == null) return
        if (isLoading) {
            binding.pbEvents.visibility = View.VISIBLE
            binding.rcEvents.visibility = View.GONE
        } else {
            binding.pbEvents.visibility = View.GONE
            binding.rcEvents.visibility = View.VISIBLE
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