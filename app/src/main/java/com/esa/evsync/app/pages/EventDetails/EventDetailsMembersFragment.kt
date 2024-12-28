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
import com.esa.evsync.app.pages.MemberSearch.MemberResultModel
import com.esa.evsync.databinding.FragmentEventDetailsMembersBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EventDetailsMembersFragment(
    private var event: EventModel
) : Fragment() {
    private var columnCount = 1
    private val db = Firebase.firestore
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

        binding.btnAddMember.setOnClickListener {
            val navController = findNavController()
            val action = EventDetailsFragmentDirections.actionNavEventDetailsToNavMemberAdd(
                eventId = event.id ?: ""
            )
            navController.navigate(action)
        }



        val recycleView = binding.rcEventMembers
        // Set the adapter
        with(recycleView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val members = ArrayList<UserModel>()
                    for (memberRef in event.members!!) {
                        val memberData = memberRef.get().await()
                        val member = memberData.toObject(UserModel::class.java)
                        if (member != null) {
                            member.id = memberData.id
                            members.add(member)
                        }

                    }
                    withContext(Dispatchers.Main) {
                        adapter = EventDetailsMembersRCAdapter(members, event, binding.root)
                    }
                } catch (e: Error) {
                    Log.e("Firebase", "Failed to fetch member info", e)
                    Toast.makeText(requireContext(), "Failed to load members", Toast.LENGTH_SHORT).show()
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