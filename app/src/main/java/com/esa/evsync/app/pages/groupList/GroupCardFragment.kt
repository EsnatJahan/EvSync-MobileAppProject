package com.esa.evsync.app.pages.groupList

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.esa.evsync.R
import com.esa.evsync.app.dataModels.GroupModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * A fragment representing a list of Items.
 */
class GroupCardFragment : Fragment() {

    private var columnCount = 1
    private val db = Firebase.firestore


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
        val view = inflater.inflate(R.layout.fragment_group_list, container, false)


        view.findViewById<ImageView>(R.id.btnAddEvent).setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_eventsFragment_to_eventAddFragment)
        }

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                lifecycleScope.launch {
                    try {
                        val events = db.collection("Events")
                            .whereArrayContains(
                                "users",
                                FirebaseAuth.getInstance().currentUser!!.uid
                            )
                            .get()
                            .await()
                        val eventList = ArrayList<GroupModel>()
                        for (event in events.documents) {
                            eventList.add(event.toObject(GroupModel::class.java)!!)
                        }

                        for (i in 1..10) {
                            eventList.add(
                                GroupModel(
                                    name = "Group",
                                    members = ArrayList<String>(),
                                    image = Uri.parse("https://plus.unsplash.com/premium_photo-1664474619075-644dd191935f?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8aW1hZ2V8ZW58MHx8MHx8fDA%3D"),
                                    description = "Some random test group")
                            )
                        }


                        adapter = GroupCardRecyclerViewAdapter(eventList)
                    }catch (e: Error) {
                        Log.e("Firebase", "failed to load event list", e)
                    }
                }

            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "1"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            GroupCardFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}