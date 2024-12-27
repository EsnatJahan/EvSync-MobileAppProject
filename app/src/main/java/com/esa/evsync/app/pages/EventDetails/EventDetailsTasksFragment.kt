package com.esa.evsync.app.pages.EventDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esa.evsync.R
import com.esa.evsync.app.dataModels.EventModel
import com.esa.evsync.app.dataModels.TaskModel
import com.esa.evsync.app.pages.EventList.EventListFragment
import com.esa.evsync.app.pages.EventList.EventDetailsTasksRCAdapter
import com.esa.evsync.databinding.FragmentEventDetailsTasksBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class EventDetailsTasksFragment(
    private var event: EventModel
) : Fragment() {
    private var columnCount = 1
    private val db = Firebase.firestore
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
    ): View? {
//        val view = inflater.inflate(R.layout.fragment_event_details_tasks, container, false)
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
        if (recycleView is RecyclerView) {
            with(recycleView) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = EventDetailsTasksRCAdapter(event.tasks ?: ArrayList<TaskModel>(), binding.root)

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