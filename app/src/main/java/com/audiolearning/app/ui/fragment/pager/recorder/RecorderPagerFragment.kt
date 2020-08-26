package com.audiolearning.app.ui.fragment.pager.recorder

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.audiolearning.app.R
import com.audiolearning.app.audio.recorder.AudioRecorderState
import com.audiolearning.app.databinding.PagerFragmentRecorderBinding
import com.audiolearning.app.extension.hide
import com.audiolearning.app.ui.activity.audioplayer.AudioPlayerControlsViewModel
import com.audiolearning.app.ui.dialog.newrecording.NewRecordingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class RecorderPagerFragment : Fragment() {
    private val viewModel: RecorderPagerFragmentViewModel by viewModels()
    private val audioPlayerControlsViewModel: AudioPlayerControlsViewModel by viewModels()
    private lateinit var binding: PagerFragmentRecorderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.pager_fragment_recorder,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        /* Hide btnPauseAndResume since pausing and resuming MediaRecorders
        * is only available on API > 24 */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            binding.btnPauseAndResume.hide()
        }

        observeIfNewAudioRecording()
        switchButtonAppearancesOnAudioRecorderChange()

        return binding.root
    }

    private fun observeIfNewAudioRecording() {
        viewModel.recordingAndTimerHandler.recordedFile.observe(
            viewLifecycleOwner,
            { newFile: File? ->
                newFile?.let {
                    NewRecordingDialog.display(
                        newFile.path,
                        parentFragmentManager
                    )
                }
            })
    }

    private fun switchButtonAppearancesOnAudioRecorderChange() {
        var stateBefore: AudioRecorderState = AudioRecorderState.IDLING
        viewModel.recordingAndTimerHandler.audioRecorderState.observe(
            viewLifecycleOwner,
            { newState: AudioRecorderState ->
                when (newState) {
                    AudioRecorderState.IDLING -> {
                        binding.apply {
                            btnPauseAndResume.isEnabled = false
                            btnPauseAndResume.isClickable = false
                            btnPauseAndResume.text = getString(R.string.pause_text)

                            btnRecordAndStop.text = getString(R.string.record_text)
                        }

                        // Disable recording button so it is not clickable when the NewRecordingDialog opens
                        if (stateBefore == AudioRecorderState.RECORDING || stateBefore == AudioRecorderState.PAUSING) {
                            MainScope().launch {
                                binding.apply {
                                    val recordButtonUnavailableTime = 2000L
                                    btnRecordAndStop.isEnabled = false
                                    btnRecordAndStop.isClickable = false
                                    delay(recordButtonUnavailableTime)
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
                            audioPlayerControlsViewModel.stop()
                        }
                    }

                    AudioRecorderState.PAUSING -> {
                        binding.btnPauseAndResume.text = getString(R.string.resume_text)
                    }
                }
                stateBefore = newState
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.onDestroy()
    }
}
