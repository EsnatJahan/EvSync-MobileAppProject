package com.esa.evsync

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.esa.evsync.app.models.AppViewModel
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
        binding.bottomNavigation.setupWithNavController(navController)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.app)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}