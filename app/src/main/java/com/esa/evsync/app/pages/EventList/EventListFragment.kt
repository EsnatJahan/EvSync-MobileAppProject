package com.esa.evsync.app.pages.EventList

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
import com.esa.evsync.app.dataModels.EventModel
import com.esa.evsync.databinding.FragmentEventListBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * A fragment representing a list of Items.
 */
class EventListFragment : Fragment() {

    private var columnCount = 1
    private val db = Firebase.firestore
    private lateinit var binding: FragmentEventListBinding

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
        binding = FragmentEventListBinding.inflate(layoutInflater)


        binding.btnAddEvent.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_eventsFragment_to_eventAddFragment)
        }

        showLoading(true)
        var recycleView = binding.rcEvents
        // Set the adapter
        if (recycleView is RecyclerView) {
            with(recycleView) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val events = db.collection("events")
                            .whereArrayContains(
                                "members",
                                FirebaseAuth.getInstance().currentUser!!.uid
                            )
                            .get()
                            .await()
                        val eventList = ArrayList<EventModel>()
                        for (event in events.documents) {
                            var eventData = event.toObject(EventModel::class.java)!!
                            eventData.id = event.id
                            eventList.add(eventData)
                        }

                        withContext(Dispatchers.Main) {
                            adapter = EventCardRecyclerViewAdapter(eventList, binding.root)
                            showLoading(false)
                        }
                    }catch (e: Error) {
                        Log.e("Firebase", "failed to load event list", e)
                    }
                }
            }
        }
        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
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