package com.esa.evsync.app.pages.UserPicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esa.evsync.databinding.FragmentUserPickerBinding

class UserPickerFragment : Fragment() {
    private val args: UserPickerFragmentArgs by navArgs()

    private lateinit var viewModel: UserPickerViewModel
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var doneButton: Button
    private lateinit var searchAdapter: MemberSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = UserPickerViewModel.Factory(ArrayList(args.userIDs.toList()))
        viewModel = ViewModelProvider(this, factory)[UserPickerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserPickerBinding.inflate(inflater, container, false)

        // Initialize views
        searchView = binding.svMembersSerachtext
        recyclerView = binding.rvMemberSearchList
        doneButton = binding.doneButton

        // Set up RecyclerView with adapter
        searchAdapter = MemberSearchAdapter { result, isSelected ->
            handleSelection(result, isSelected)
        }
        searchAdapter.submitList(getSearchResults())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = searchAdapter


        // Done button click
        doneButton.setOnClickListener {
            val result = viewModel.markedResults // Array of selected results
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


        viewModel.fetchUserInfo()
        viewModel.users.observe(viewLifecycleOwner) {filterResults(searchView.query.toString())}
        viewModel.toastMessage.observe(viewLifecycleOwner) { msg->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()}

        return binding.root
    }

    private fun filterResults(query: String) {
        // Filter the search results based on the query
        val filteredList = getSearchResults().filter {
            it.name.contains(query, true)|| it.email.contains(query, true)
        }
        searchAdapter.submitList(filteredList)
    }

    private fun handleSelection(result: UserPickerDataModel, isSelected: Boolean) {
        if (isSelected) {
            viewModel.markedResults.add(result)
        } else {
            viewModel.markedResults.remove(result)
        }
    }

    private fun sendResultsBack(results: List<UserPickerDataModel>) {
        // Send the marked results back to the previous fragment
        val bundle = Bundle()
        bundle.putStringArrayList("selected_result", ArrayList(results.map { it.id }))
        parentFragmentManager.setFragmentResult("request_user_picker", bundle)
        findNavController().popBackStack()
    }

    private fun getSearchResults(): List<UserPickerDataModel> {
        return viewModel.users.value ?: listOf()
    }

}
