package com.esa.evsync.app.pages.EventDetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.esa.evsync.app.dataModels.EventModel
import com.esa.evsync.app.dataModels.TaskModel
import com.esa.evsync.app.pages.EventList.EventDetailsMembersRCAdapter
import com.esa.evsync.app.pages.EventList.EventListFragment
import com.esa.evsync.app.pages.EventList.EventDetailsTasksRCAdapter
import com.esa.evsync.databinding.FragmentEventDetailsTasksBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EventDetailsTasksFragment(
    private val viewModel: EventDetailsViewModel) : Fragment() {
    private var columnCount = 1
    private lateinit var binding: FragmentEventDetailsTasksBinding
    private var event: EventModel? = null


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
        binding = FragmentEventDetailsTasksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddTask.setOnClickListener {
            val navController = findNavController()
            val action = EventDetailsFragmentDirections.actionNavEventDetailsToNavTaskAdd(
                eventId = event?.id ?: "",
                eventName = event?.name ?: "",
                eventDescription = event?.description ?: ""
            )
            navController.navigate(action)
        }

        binding.rcEventTasks.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }

        if (binding.rcEventTasks.adapter == null)
            binding.rcEventTasks.adapter = EventDetailsTasksRCAdapter(ArrayList(), binding.root)
        if (viewModel.tasks.value == null) showLoading(true)
        else (binding.rcEventTasks.adapter as EventDetailsTasksRCAdapter).setData(viewModel.tasks.value!!)

        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            (binding.rcEventTasks.adapter as EventDetailsTasksRCAdapter).setData(tasks)
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (view == null) return
        if (isLoading) {
            binding.pbTlDetails.visibility = View.VISIBLE
            binding.rcEventTasks.visibility = View.GONE
        } else {
            binding.pbTlDetails.visibility = View.GONE
            binding.rcEventTasks.visibility = View.VISIBLE
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