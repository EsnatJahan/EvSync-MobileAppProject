package com.esa.evsync.app.pages.EventDetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esa.evsync.app.dataModels.EventModel
import com.esa.evsync.app.dataModels.TaskModel
import com.esa.evsync.app.pages.EventList.EventDetailsMembersRCAdapter
import com.esa.evsync.app.pages.EventList.EventListFragment
import com.esa.evsync.app.pages.EventList.EventDetailsTasksRCAdapter
import com.esa.evsync.databinding.FragmentEventDetailsTasksBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EventDetailsTasksFragment(
    private var event: EventModel
) : Fragment() {
    private var columnCount = 1
    private lateinit var binding: FragmentEventDetailsTasksBinding


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

        binding.btnAddTask.setOnClickListener {
            val navController = findNavController()
            val action = EventDetailsFragmentDirections.actionNavEventDetailsToNavTaskAdd(
                eventId = event.id ?: "",
                eventName = event.name ?: "",
                eventDescription = event.description ?: ""
            )
            navController.navigate(action)
        }

        var recycleView = binding.rcEventTasks
        // Set the adapter

        with(recycleView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val tasks = ArrayList<TaskModel>()
                    for (taskRef in event.tasks!!) {
                        val taskData = taskRef.get().await()
                        val task = taskData.toObject(TaskModel::class.java)
                        if (task != null) {
                            task.id = taskData.id
                            tasks.add(task)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        adapter = EventDetailsTasksRCAdapter(tasks, binding.root)
                    }
                } catch (e: Error) {
                    Log.e("Firebase", "Failed to fetch task info", e)
                    Toast.makeText(requireContext(), "Failed to load tasks", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
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