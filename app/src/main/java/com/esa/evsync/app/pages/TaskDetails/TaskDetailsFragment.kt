package com.esa.evsync.app.pages.TaskDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.esa.evsync.app.pages.EventDetails.EventDetailsMembersFragment
import com.esa.evsync.app.pages.EventList.EventDetailsMembersRCAdapter
import com.esa.evsync.app.pages.EventList.TaskDetailsMembersRCAdapter
import com.esa.evsync.databinding.FragmentTaskDetailsBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class TaskDetailsFragment : Fragment() {
    private val args: TaskDetailsFragmentArgs by navArgs()
    private lateinit  var binding: FragmentTaskDetailsBinding
    private val db = Firebase.firestore
    private lateinit var viewModel:  TaskDetailsViewModel
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = TaskDetailsViewModel.Factory(args.taskId)
        viewModel = ViewModelProvider(this, factory)[TaskDetailsViewModel::class.java]
    }

    override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskDetailsBinding.inflate(inflater)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = args.taskname
        val id = args.taskId
        val fragment = this

        binding.taskname.text = args.taskname
        binding.taskDescriptiopn.text = args.taskdescription
        viewModel.toastMessage.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }


        viewModel.task.observe(viewLifecycleOwner) { task ->
            binding.taskname.text = task.name
            binding.taskDescriptiopn.text = task.description
            binding.taskDeadline.text = task.deadline.toString()
            binding.taskPriority.text = task.priority.toString()
            binding.members.adapter = TaskDetailsMembersRCAdapter(ArrayList(), viewModel)
            viewModel.members.observe(viewLifecycleOwner) { members ->
                (binding.members.adapter as TaskDetailsMembersRCAdapter).setData(members)
               // showLoading(false)
            }

        }

        viewModel.fetchEventInfo()

        // parentFragmentManager.setFragmentResultListener("request_search_member", viewLifecycleOwner) { requestKey, bundle ->

    }
}