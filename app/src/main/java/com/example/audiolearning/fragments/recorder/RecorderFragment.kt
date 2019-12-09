package com.example.audiolearning.fragments.recorder

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.audiolearning.R
import com.example.audiolearning.audio.audio_recorder.AudioRecorderState
import com.example.audiolearning.databinding.FragmentRecorderBinding

class RecorderFragment : Fragment() {

    private lateinit var recorderViewModel: RecorderViewModel
    private lateinit var binding: FragmentRecorderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_recorder,
            container,
            false
        )

        binding.lifecycleOwner = this

        recorderViewModel =
            ViewModelProviders.of(this).get(RecorderViewModel::class.java)

        binding.viewModel = recorderViewModel

        /* Hide PauseAndResumeButton since pausing and resuming MediaRecorders
        * is only available on API > 24 */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            binding.pauseAndResumeButton.visibility = View.GONE
        }

        recorderViewModel.recordedFile.observe(this, Observer { newFile ->

            if (newFile != null) {
                var mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes
                            .Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    setDataSource(newFile.absolutePath)
                    prepare()
                    start()
                }
            }
        })

        switchButtonAppearancesOnAudioRecorderChange()

        return binding.root
    }

    private fun switchButtonAppearancesOnAudioRecorderChange() {
        recorderViewModel.audioRecorderState.observe(this, Observer { newState ->
            when (newState!!) {
                AudioRecorderState.IDLING -> {
                    binding.apply {
                        pauseAndResumeButton.isEnabled = false
                        pauseAndResumeButton.text = getString(R.string.pause_text)

                        recordAndStopButton.text = getString(R.string.record_text)
                    }
                }

                AudioRecorderState.RECORDING -> {
                    binding.apply {
                        pauseAndResumeButton.isEnabled = true
                        recordAndStopButton.text = getString(R.string.stop_text)
                    }
                }

                AudioRecorderState.PAUSING -> {
                    binding.pauseAndResumeButton.text = getString(R.string.resume_text)
                }

                AudioRecorderState.RESUMING -> {
                    binding.pauseAndResumeButton.text = getString(R.string.pause_text)
                }
            }

        })
    }
}