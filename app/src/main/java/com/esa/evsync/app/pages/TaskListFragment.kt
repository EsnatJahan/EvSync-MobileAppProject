package com.esa.evsync.app.pages
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esa.evsync.R
import com.esa.evsync.app.dataModels.EventModel
import com.esa.evsync.app.dataModels.TaskModel

import com.esa.evsync.app.pages.EventList.EventListFragment.Companion.ARG_COLUMN_COUNT
import com.esa.evsync.app.utils.documentReference
import com.esa.evsync.databinding.FragmentEventListBinding
import com.esa.evsync.databinding.FragmentTaskListBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class TaskListFragment : Fragment() {


    private var columnCount = 1
    private val db = Firebase.firestore
    private lateinit var binding: FragmentTaskListBinding
    private lateinit var currentUser: FirebaseUser

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
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)
        binding = FragmentTaskListBinding.inflate(layoutInflater)

        currentUser = FirebaseAuth.getInstance().currentUser!!



        var recycleView = binding.rview
            //view.findViewById<RecyclerView>(R.id.rview)
        with(recycleView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    Log.d("Firebase", "data request sents")
                    val events = db.collection("tasks")
                        .whereArrayContains(
                            "members",
                            currentUser.documentReference
                        )
                        .get()
                        .await()

                    val taskList = ArrayList<TaskModel>()

                    for (task in events.documents) {
                        var taskData = task.toObject(TaskModel::class.java)!!
                        taskData.id = task.id
                        Toast.makeText(requireContext(), "Value: ${task.id}", Toast.LENGTH_SHORT).show()

                        taskList.add(taskData)
                    }

                    Log.d("Firebase", "event data fetched: ${taskList}")
                    withContext(Dispatchers.Main) {
                        adapter = Task_ListAdapter(taskList, binding.root)
                    }
                }catch (e: Error) {
                    Log.e("Firebase", "failed to load event list", e)
                }
            }
        }
    return binding.root;


    }
}