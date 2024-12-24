package com.esa.evsync

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.esa.evsync.app.AppFragment
import com.esa.evsync.app.SplashFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var splashDone = false
    val splashDelay: Long = 1000  // jump to app screen after 1000ms

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContentView(R.layout.activity_main)
        val splashFragment = SplashFragment()

        if (!splashDone) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_root, splashFragment)
                commit()
            }
            lifecycleScope.launch(Dispatchers.Main) {
                delay(splashDelay)
                splashDone = true
                extracted()
            }
        } else {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_root, AppFragment())
                commit()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun extracted() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_root, AppFragment())
            commit()
        }
    }
}