package com.esa.evsync

import android.os.Bundle
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



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun botNavDestListener (navController: NavController, destination: NavDestination, bundle: Bundle?) {
//        when (destination.id) {
//            in NavMap.destinationToNavItemMap -> binding.bottomNavigation.selectedItemId = NavMap.destinationToNavItemMap[destination.id]!!
//            else -> binding.bottomNavigation.selectedItemId = destination.id
//        }
    }

}