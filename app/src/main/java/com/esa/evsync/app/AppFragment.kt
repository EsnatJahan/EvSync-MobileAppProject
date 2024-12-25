package com.esa.evsync.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.esa.evsync.R
import com.esa.evsync.app.pages.EventsFragment
import com.esa.evsync.app.pages.SettingsFragment
import com.esa.evsync.app.pages.TasksFragment
import com.esa.evsync.databinding.FragmentAppBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class AppFragment : Fragment() {
    val tasks = TasksFragment()
    val events = EventsFragment()
    val settings = SettingsFragment()

    private val model: AppViewModel by activityViewModels()
    lateinit private var binding: FragmentAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppBinding.inflate(inflater, container, false)
//
//        model.page.observe(viewLifecycleOwner) { page ->
//            when (page) {
//                PageEnum.PAGE_TASKS -> {
//                    activity?.supportFragmentManager?.beginTransaction()?.apply {
//                        replace(R.id.fg_app, tasks)
//                        commit()
//                    }
//                }
//
//                PageEnum.PAGE_EVENTS -> {
//                    activity?.supportFragmentManager?.beginTransaction()?.apply {
//                        replace(R.id.fg_app, events)
//                        commit()
//                    }
//                }
//
//                PageEnum.PAGE_SETTINGS -> {
//                    activity?.supportFragmentManager?.beginTransaction()?.apply {
//                        replace(R.id.fg_app, settings)
//                        commit()
//                    }
//                }
//
//                else -> {
//                    throw IllegalStateException("Unknown page ${page}")
//                }
//            }
//        }
//
//        binding.btnNavTasks.setOnClickListener {model.switch_page(PageEnum.PAGE_TASKS)}
//        binding.btnNavEvents.setOnClickListener {model.switch_page(PageEnum.PAGE_EVENTS)}
//        binding.btnNavSettings.setOnClickListener {model.switch_page(PageEnum.PAGE_SETTINGS)}
//

//        val navController = binding.appBody.findNavController()
//        binding.bottomNavigation.setupWithNavController(navController)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.appBody) as NavHostFragment
        val navController = navHostFragment.navController

        if (savedInstanceState != null) {
            val navState = savedInstanceState.getBundle("nav_state")
            navState?.let { navController.restoreState(it) }
        }

        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val navController = childFragmentManager.findFragmentById(R.id.appBody)
            ?.findNavController()
        val navState = navController?.saveState()
        outState.putBundle("nav_state", navState)
    }

}