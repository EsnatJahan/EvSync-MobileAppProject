package com.esa.evsync.app.pages

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.esa.evsync.R
import com.esa.evsync.app.dataModels.TaskModel
import com.esa.evsync.app.dataModels.TaskPriority
import com.esa.evsync.app.pages.EventDetails.TaskAddDataModel
import com.esa.evsync.databinding.FragmentTaskAddBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class TaskAddFragment : Fragment() {
    private val args: TaskAddFragmentArgs by navArgs()
    private lateinit var binding: FragmentTaskAddBinding
    private val db = Firebase.firestore
    private lateinit var eventId: String
    private val taskCollection = db.collection("tasks")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskAddBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner: Spinner = binding.dropdownPriority

        eventId = args.eventId

        val arrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.task_priorities,
            android.R.layout.simple_spinner_item
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        binding.btnPickdate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    binding.tvDate.text = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                },
                year, month, day
            )
            datePickerDialog.show()
        }
        binding.btnCreate.setOnClickListener{ addTask() }
    }

    private fun addTask() {
        val task = TaskAddDataModel(
            name = binding.etName.text.toString(),
            description = binding.etDesc.text.toString(),
            priority = binding.dropdownPriority.selectedItem as String,
            deadline = binding.tvDate.text.toString(),
            assigned = ArrayList(),
        )

        if (task.name == "" || task.description == "" || task.deadline == ""
            || task.priority.uppercase() == TaskPriority.UNSET.toString().uppercase()) {
            Log.w("Firebase", "Not enough information provided")
            Toast.makeText(requireContext(), "Not enough information provided", Toast.LENGTH_SHORT).show()
            return
        }

        val bundle = Bundle()
        bundle.putParcelable("task_info", task)
        parentFragmentManager.setFragmentResult("request_task_add", bundle)
        findNavController().popBackStack()
    }
}