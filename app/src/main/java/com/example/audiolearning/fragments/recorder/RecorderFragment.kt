package com.example.audiolearning.fragments.recorder

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.audiolearning.R
import com.example.audiolearning.databinding.FragmentRecorderBinding

class RecorderFragment : Fragment() {

    private lateinit var recorderViewModel: RecorderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRecorderBinding>(
            inflater,
            R.layout.fragment_recorder,
            container,
            false)

        recorderViewModel =
            ViewModelProviders.of(this).get(RecorderViewModel::class.java)

        binding.viewModel = recorderViewModel

        /* Hide PauseAndResumeButton since pausing and resuming MediaRecorders
        * is only available on API > 24 */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            binding.pauseAndResumeButton.visibility = View.GONE
        }

        return binding.root
    }
}