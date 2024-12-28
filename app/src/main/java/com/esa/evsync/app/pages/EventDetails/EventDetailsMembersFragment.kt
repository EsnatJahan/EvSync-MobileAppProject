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
import com.esa.evsync.app.dataModels.EventModel
import com.esa.evsync.app.dataModels.UserModel
import com.esa.evsync.app.pages.EventList.EventDetailsMembersRCAdapter
import com.esa.evsync.app.pages.EventList.EventListFragment
import com.esa.evsync.databinding.FragmentEventDetailsMembersBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EventDetailsMembersFragment(
) : Fragment(),  EventDetailsUpdatable {
    private var columnCount = 1
    private val db = Firebase.firestore
    private lateinit var binding: FragmentEventDetailsMembersBinding
//    private var pendingUpdate = false
    private var event: EventModel? = null
    private var members = ArrayList<UserModel>()
    private var mycount = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mycount = ++count
        Log.d("datafetch", "Created MembersFragment: $mycount")
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
                eventId = event?.id ?: ""
            )
            navController.navigate(action)
        }

        binding.rcEventMembers.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }

        displayMembers()
    }

//    override fun onResume() {
//        super.onResume()
//        if (pendingUpdate) updateEvent(event!!)
//    }



    override fun updateEvent(event: EventModel) {
        Log.d("datafetch", "MemberFragment: update called with event $event")
        Log.d("datafetch", "MemberFragment: count: $count, mycount: $mycount")
        this.event = event
        fetchMembers()
    }

    private fun fetchMembers() {
        try {
            CoroutineScope(Dispatchers.Main).launch {
                showLoading(true)
                Log.d("datafetch", "Fetching member data with event:\n $event")
                Log.d("datafetch", "MemberFragment: count: $count, mycount: $mycount")
                val membersUpdated = ArrayList<UserModel>()
                val memberSnapshots = event?.members!!.map {memberRef ->
                    async(Dispatchers.IO) {memberRef.get().await()}
                }.awaitAll()
                for (memberData in memberSnapshots) {
                    val member = memberData.toObject(UserModel::class.java)
                    if (member != null) {
                        member.id = memberData.id
                        membersUpdated.add(member)
                    }
                }
                Log.d("datafetch", "Fetching member data")
                Log.d("datafetch", "MemberFragment: count: $count, mycount: $mycount")
                members = membersUpdated

                displayMembers()
            }
        } catch (e: Error) {
            Log.e("Firebase", "Failed to fetch member info", e)
            Toast.makeText(requireContext(), "Failed to load members", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun displayMembers() {
        if (view == null) return
        if (event == null) {
            showLoading(true)
            return
        }
        if (binding.rcEventMembers.adapter == null) {
            binding.rcEventMembers.adapter = EventDetailsMembersRCAdapter(members, event!!, binding.root)
        } else {
            (binding.rcEventMembers.adapter as EventDetailsMembersRCAdapter).setData(members)
        }
        showLoading(false)
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