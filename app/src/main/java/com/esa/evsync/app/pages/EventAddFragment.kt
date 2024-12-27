package com.esa.evsync.app.pages

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.esa.evsync.R
import com.esa.evsync.app.dataModels.EventModel
import com.esa.evsync.app.dataModels.TaskModel
import com.esa.evsync.databinding.FragmentEventAddBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.*
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class EventAddFragment : Fragment() {
    lateinit var binding: FragmentEventAddBinding
    private var original_title: String? = null
    private val db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventAddBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivPic.setOnClickListener(::addImage)
        binding.btnCreate.setOnClickListener(::addEvent)
    }

    private fun addImage(view: View?) {

    }
    private fun addEvent(view: View?) {
        var event = EventModel(
            name = binding.etName.text.toString(),
            description = binding.etDesc.text.toString(),
            image = null,
            owner = getInstance().currentUser?.uid,
            members = ArrayList(listOf(getInstance().currentUser?.uid ?: "")),
            tasks = ArrayList()
        )
        
        if (event.name == null || event.name == "" || event.description == null || event.description == ""
            || event.owner == null || event.owner == "") {
            Toast.makeText(requireContext(), "Insufficient information given", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val ref = withContext(Dispatchers.IO) {
                    db.collection("events").add(event).await()
                }
                Toast.makeText(requireContext(), "Event created", Toast.LENGTH_SHORT).show()
                val navController = findNavController()
                navController.navigate(R.id.action_nav_eventAddFragment_to_nav_events)

            } catch (e: Error) {
                Log.e("Firebase", "Failed to create event", e)
                Toast.makeText(requireContext(), "Error occured failed to register event", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        original_title = activity?.title as? String
        activity?.title = "Add Event"
    }
    override fun onStop() {
        super.onStop()
        activity?.title = original_title
    }
}