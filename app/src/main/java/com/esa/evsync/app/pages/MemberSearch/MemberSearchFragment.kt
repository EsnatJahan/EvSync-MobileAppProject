package com.esa.evsync.app.pages.MemberSearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esa.evsync.app.dataModels.EventModel
import com.esa.evsync.app.dataModels.UserModel
import com.esa.evsync.app.pages.EventDetails.EventDetailsFragmentArgs
import com.esa.evsync.databinding.FragmentMemberSearchBinding
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MemberSearchFragment : Fragment() {
    private val args: MemberSearchFragmentArgs by navArgs()

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var doneButton: Button
    private lateinit var searchAdapter: MemberSearchAdapter
    private var markedResults = mutableListOf<MemberResultModel>() // A list to hold selected items

    private lateinit var membersAll: List<MemberResultModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMemberSearchBinding.inflate(inflater, container, false)

        // Initialize views
        searchView = binding.svMembersSerachtext
        recyclerView = binding.rvMemberSearchList
        doneButton = binding.doneButton

        Log.d("Args", "creation Args: $args")
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val event = Firebase.firestore.collection("events")
                    .document(args.eventId)
                    .get()
                    .await()!!

                val eventinfo = event.toObject(EventModel::class.java)!!

                val users = Firebase.firestore.collection("users")
                    .whereNotIn(FieldPath.documentId(), eventinfo.members!!)
                    .get()
                    .await()

                membersAll = users.documents.map {
                    val user = it.toObject(UserModel::class.java)!!
                    MemberResultModel(
                        id = it.id,
                        name = user.username!!,
                        email = user.email!!,
                        profileImageUrl = user.profile_picture,
                        isSelected = false
                    )
                }
                withContext(Dispatchers.Main) {
                    // Set up RecyclerView with adapter
                    searchAdapter = MemberSearchAdapter { result, isSelected ->
                        handleSelection(result, isSelected)
                    }
                    searchAdapter.submitList(membersAll)
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = searchAdapter


                    // Done button click
                    doneButton.setOnClickListener {
                        val result = markedResults // Array of selected results
                        sendResultsBack(result)
                    }
                    // Set up search listener
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            // Handle search submit (if needed)
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            // Update the RecyclerView with filtered results
                            filterResults(newText ?: "")
                            return true
                        }
                    })

                }
            } catch (e: Error) {
                Log.e("Firebase", "Failed to fetch event info", e)
            }
        }

        return binding.root
    }

    private fun filterResults(query: String) {
        // Filter the search results based on the query
        val filteredList = getSearchResults().filter { it.name.contains(query, true) }
        searchAdapter.submitList(filteredList)
    }

    private fun handleSelection(result: MemberResultModel, isSelected: Boolean) {
        if (isSelected) {
            markedResults.add(result)
        } else {
            markedResults.remove(result)
        }
    }

    private fun sendResultsBack(results: List<MemberResultModel>) {
        // Send the marked results back to the previous fragment
        val bundle = Bundle()
        bundle.putParcelableArrayList("new_members",ArrayList(results))

        Log.d("new member", "packaged: ${ArrayList(results)}")
        parentFragmentManager.setFragmentResult("request_add_member", bundle)
        findNavController().popBackStack()
    }

    private fun getSearchResults(): List<MemberResultModel> {
        return membersAll
    }

}
