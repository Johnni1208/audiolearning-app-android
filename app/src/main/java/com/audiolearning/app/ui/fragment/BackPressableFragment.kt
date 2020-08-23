package com.audiolearning.app.ui.fragment

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

abstract class BackPressableFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    abstract fun onBackPressed()
}
