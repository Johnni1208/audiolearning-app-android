package com.example.audiolearning.ui.fragments.recorder

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.audiolearning.R
import com.example.audiolearning.audio.audio_recorder.AudioRecorder
import com.example.audiolearning.audio.audio_recorder.AudioRecorderState
import com.example.audiolearning.databinding.FragmentRecorderBinding
import com.example.audiolearning.ui.dialogs.new_recording.NewRecordingDialog
import com.example.audiolearning.util.timer.Timer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        val viewModelFactory = RecorderViewModelFactory(
            AudioRecorder(),
            Timer()
        )
        recorderViewModel = ViewModelProvider(this, viewModelFactory)
            .get(RecorderViewModel::class.java)

        binding.viewModel = recorderViewModel

        /* Hide btnPauseAndResume since pausing and resuming MediaRecorders
        * is only available on API > 24 */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            binding.btnPauseAndResume.visibility = View.GONE
        }

        observeIfNewAudioRecording()
        switchButtonAppearancesOnAudioRecorderChange()

        return binding.root
    }

    private fun observeIfNewAudioRecording() {
        recorderViewModel.recordingAndTimerHandler.recordedFile.observe(
            viewLifecycleOwner,
            Observer { newFile ->
                newFile?.let {
                    NewRecordingDialog.display(
                        newFile.path,
                        requireFragmentManager()
                    )
                }
            })
    }

    private fun switchButtonAppearancesOnAudioRecorderChange() {
        var stateBefore: AudioRecorderState = AudioRecorderState.IDLING
        recorderViewModel.recordingAndTimerHandler.audioRecorderState.observe(
            viewLifecycleOwner,
            Observer { newState ->
                when (newState!!) {
                    AudioRecorderState.IDLING -> {
                        binding.apply {
                            btnPauseAndResume.isEnabled = false
                            btnPauseAndResume.isClickable = false
                            btnPauseAndResume.text = getString(R.string.pause_text)

                            btnRecordAndStop.text = getString(R.string.record_text)
                        }
                        // Disable button so it is not clickable when the NewRecordingDialog opens
                        if (stateBefore == AudioRecorderState.RECORDING || stateBefore == AudioRecorderState.PAUSING) {
                            GlobalScope.launch(Dispatchers.Main) {
                                binding.apply {
                                    btnRecordAndStop.isEnabled = false
                                    btnRecordAndStop.isClickable = false
                                    delay(1000)
                                    btnRecordAndStop.isEnabled = true
                                    btnRecordAndStop.isClickable = true
                                }
                            }
                        }
                    }

                    AudioRecorderState.RECORDING -> {
                        binding.apply {
                            btnPauseAndResume.text = getString(R.string.pause_text)
                            btnPauseAndResume.isEnabled = true
                            btnPauseAndResume.isClickable = true
                            btnRecordAndStop.text = getString(R.string.stop_text)
                        }
                    }

                    AudioRecorderState.PAUSING -> {
                        binding.btnPauseAndResume.text = getString(R.string.resume_text)
                    }
                }
                stateBefore = newState
            })
    }

    override fun onDetach() {
        super.onDetach()
        recorderViewModel.onDestroy()
    }
}