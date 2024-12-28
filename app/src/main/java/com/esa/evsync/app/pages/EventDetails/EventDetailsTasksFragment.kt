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

class EventDetailsTasksFragment() : Fragment(),  EventDetailsUpdatable {
    private var columnCount = 1
    private lateinit var binding: FragmentEventDetailsTasksBinding
//    private var pendingUpdate = false
    private var event: EventModel? = null
    private var tasks = ArrayList<TaskModel>()
    private var mycount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mycount = ++count
        Log.d("datafetch", "Created TaskFragment: $mycount")
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

        displayTasks()
    }

//    override fun onResume() {
//        super.onResume()
//        if (pendingUpdate) updateEvent(event!!)
//    }

    override fun updateEvent(event: EventModel) {
        Log.d("datafetch", "TaskFragment: update called with event $event")
        Log.d("datafetch", "TaskFragment: count: $count, mycount: $mycount")
        this.event = event
        fetchTasks()
    }



    private fun fetchTasks() {
        CoroutineScope(Dispatchers.Main).launch {
            showLoading(true)
            try {
                Log.d("datafetch", "Fetching task data")
                Log.d("datafetch", "TaskFragment: count: $count, mycount: $mycount")
                tasks = arrayListOf(*(event?.tasks!!.map {taskRef ->
                    async(Dispatchers.IO) {taskRef.get().await()}
                }
                    .awaitAll()
                    .map {
                    val task = it.toObject(TaskModel::class.java)!!
                    task.id = it.id
                    task
                }).toTypedArray())
                displayTasks()
                Log.d("datafetch", "TaskFragment: count: $count, mycount: $mycount")
            } catch (e: Error) {
                Log.e("Firebase", "Failed to fetch task info", e)
                Toast.makeText(requireContext(), "Failed to load tasks", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun displayTasks() {
        if (view == null) return
        if (event == null) {
            showLoading(true)
            return
        }
        if (binding.rcEventTasks.adapter == null) {
            binding.rcEventTasks.adapter = EventDetailsTasksRCAdapter(tasks, binding.root)
        } else {
            (binding.rcEventTasks.adapter as EventDetailsTasksRCAdapter).setData(tasks)
        }
        showLoading(false)
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
        var count = 0

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