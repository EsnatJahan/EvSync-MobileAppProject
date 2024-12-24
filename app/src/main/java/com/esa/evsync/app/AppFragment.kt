package com.esa.evsync.app

import android.os.Bundle
import android.provider.CalendarContract.Events
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import com.esa.evsync.R
import com.esa.evsync.app.pages.EventsFragment
import com.esa.evsync.app.pages.SettingsFragment
import com.esa.evsync.app.pages.TasksFragment
import com.esa.evsync.databinding.FragmentAppBinding


class AppFragment : Fragment() {
    val tasks = TasksFragment()
    val events = EventsFragment()
    val settingss = SettingsFragment()
    lateinit private var binding: FragmentAppBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding = FragmentAppBinding.inflate(inflater, container, false)
        val view = inflater.inflate(R.layout.fragment_app, container, false)
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fg_app, tasks)
            commit()
        }

        view.findViewById<Button>(R.id.btnNavTasks).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fg_app, tasks)
                commit()
            }
        }

        view.findViewById<Button>(R.id.btnNavEvents).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fg_app, events)
                commit()
            }
        }

        view.findViewById<Button>(R.id.btnNavSettings).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fg_app, settingss)
                commit()
            }
        }

        return view
    }
}