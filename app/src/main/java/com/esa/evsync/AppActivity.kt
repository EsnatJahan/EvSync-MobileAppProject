package com.esa.evsync

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.esa.evsync.app.models.AppViewModel
import com.esa.evsync.app.nav.NavMap
import com.esa.evsync.databinding.ActivityAppBinding


class AppActivity : AppCompatActivity() {
    private val model: AppViewModel by viewModels()
    lateinit private var binding: ActivityAppBinding


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize NavHostFragment and NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.appBody) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up Bottom Navigation
        binding.bottomNav.setupWithNavController(navController)
//        navController.addOnDestinationChangedListener {navController, dest, bundle ->
//            botNavDestListener(navController, dest, bundle)
//        }

//         Handle the Bottom Navigation item clicks
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_events -> {
                    navController.navigate(R.id.nav_events)
                    true
                }
                R.id.nav_tasks -> {
                    navController.navigate(R.id.nav_tasks)
                    true
                }
                R.id.nav_settings -> {
                    navController.navigate(R.id.nav_settings)
                    true
                }
                else -> false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

//    private var suppressNavListener = false
//    private fun botNavDestListener (navController: NavController, destination: NavDestination, bundle: Bundle?) {
//
//        if (!suppressNavListener
//            && destination.id in NavMap.destinationToNavItemMap
//            && binding.bottomNav.selectedItemId != destination.id
//            && binding.bottomNav.selectedItemId != NavMap.destinationToNavItemMap[destination.id]) {
//            Log.d("nav transition", """"
//                    | destination: ${destination.id}
//                    | mapped: ${NavMap.destinationToNavItemMap[destination.id]}
//                    | selected: ${binding.bottomNav.selectedItemId}
//                """.trimIndent())
//            suppressNavListener = true
//            binding.bottomNav.selectedItemId = NavMap.destinationToNavItemMap[destination.id]!!
//            suppressNavListener = false
//            Log.d("nav transition", "set now: ${binding.bottomNav.selectedItemId}")
//        }
//    }

}