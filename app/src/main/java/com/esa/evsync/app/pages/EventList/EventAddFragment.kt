package com.esa.evsync.app.pages.EventList


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.esa.evsync.databinding.FragmentEventAddBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore


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
        val newEvent = NewEventInfo(
            name = binding.etName.text.toString(),
            description = binding.etDesc.text.toString(),
            image = null,
        )

        if (newEvent.name == "" || newEvent.description == "") {
            Toast.makeText(requireContext(), "Insufficient information given", Toast.LENGTH_SHORT).show()
            return
        }

        val bundle = Bundle()
        bundle.putParcelable("newEvent", newEvent)
        parentFragmentManager.setFragmentResult("request_add_event", bundle)
        findNavController().popBackStack()

    }
}