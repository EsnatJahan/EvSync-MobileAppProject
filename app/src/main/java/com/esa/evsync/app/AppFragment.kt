package com.esa.evsync.app

import android.os.Bundle
import android.provider.CalendarContract.Events
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.esa.evsync.R
import com.esa.evsync.app.pages.EventsFragment
import com.esa.evsync.app.pages.SettingsFragment
import com.esa.evsync.app.pages.TasksFragment
import com.esa.evsync.databinding.FragmentAppBinding


class AppFragment : Fragment() {
    lateinit var tasks: TasksFragment
    lateinit var events: EventsFragment
    lateinit var settingss: SettingsFragment

    lateinit private var binding: FragmentAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasks = TasksFragment()
        events = EventsFragment()
        settingss = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppBinding.inflate(inflater, container, false)

        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fg_app, tasks)
            commit()
        }

        binding.btnNavTasks.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fg_app, tasks)
                commit()
            }
        }

        binding.btnNavEvents.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fg_app, events)
                commit()
            }
        }

        binding.btnNavSettings.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fg_app, settingss)
                commit()
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AppFragment().apply {}
    }
}