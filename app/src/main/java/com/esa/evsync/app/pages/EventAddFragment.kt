package com.esa.evsync.app.pages


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
import com.esa.evsync.app.utils.documentReference
import com.esa.evsync.databinding.FragmentEventAddBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class EventAddFragment : Fragment() {
    private lateinit var binding: FragmentEventAddBinding
    private val db = Firebase.firestore
    private lateinit var currentUser: FirebaseUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventAddBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCreate.setOnClickListener(::addEvent)
        currentUser = FirebaseAuth.getInstance().currentUser!!
    }

    private fun addEvent(view: View?) {
        val event = EventModel(
            name = binding.etName.text.toString(),
            description = binding.etDesc.text.toString(),
            image = null,
            owner = currentUser.documentReference,
            members = ArrayList(listOf(currentUser.documentReference)),
            tasks = ArrayList()
        )
        
        if (event.name == null || event.name == "" || event.description == null || event.description == ""
            || event.owner == null) {
            Toast.makeText(requireContext(), "Insufficient information given", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    db.collection("events").add(event).await()
                }
                Toast.makeText(requireContext(), "Event created", Toast.LENGTH_SHORT).show()
                val navController = findNavController()
                navController.navigate(R.id.action_nav_eventAddFragment_to_nav_events)
            } catch (e: Error) {
                Log.e("Firebase", "Failed to create event", e)
                Toast.makeText(requireContext(), "Failed to create event", Toast.LENGTH_SHORT).show()
            }
        }

    }
}