package com.esa.evsync.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.esa.evsync.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    val splashDelay: Long = 1000  // jump to app screen after 1000ms

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(splashDelay)
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fl_root, AppFragment())
                commit()
            }
        }
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SplashFragment().apply {}
    }
}