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
import com.audiolearning.app.extension.animateAlphaTo
import com.audiolearning.app.extension.dp
import com.audiolearning.app.extension.fadeIn
import com.audiolearning.app.extension.fadeOut
import com.audiolearning.app.extension.runOnUiThread
import com.audiolearning.app.ui.activity.audioplayer.AudioPlayerControlsViewModel
import com.audiolearning.app.ui.dialog.newrecording.NewRecordingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.Timer
import java.util.TimerTask

private const val RECORD_BUTTON_UNAVAILABLE_TIME = 2000L

@AndroidEntryPoint
class RecorderPagerFragment : Fragment() {
    private val viewModel: RecorderPagerFragmentViewModel by viewModels()
    private val audioPlayerControlsViewModel: AudioPlayerControlsViewModel by viewModels()
    private lateinit var binding: PagerFragmentRecorderBinding
    private var audioRecordViewTimer: Timer? = null

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
                        audioRecordViewTimer?.cancel()

                        binding.apply {
                            btnPauseAndResume.setImageResource(R.drawable.ic_pause)
                            btnRecordAndStop.setImageResource(R.drawable.ic_record)
                        }

                        if (stateBefore == AudioRecorderState.RECORDING || stateBefore == AudioRecorderState.PAUSING) {
                            binding.apply {
                                tvRecordTime.animate().translationY((-64).dp())
                                tvRecordTime.animateAlphaTo(1f)

                                btnPauseAndResume.animate().translationX((-60).dp())

                                arv.fadeOut(View.INVISIBLE)
                                arv.recreate()

                                // Disable recording button so it is not clickable when the NewRecordingDialog opens
                                MainScope().launch {
                                    btnRecordAndStop.isEnabled = false
                                    btnRecordAndStop.isClickable = false
                                    delay(RECORD_BUTTON_UNAVAILABLE_TIME)
                                    btnRecordAndStop.isEnabled = true
                                    btnRecordAndStop.isClickable = true
                                }
                            }
                        }
                    }

                    AudioRecorderState.RECORDING -> {
                        updateAudioRecordView()
                        audioPlayerControlsViewModel.stop()

                        binding.apply {
                            btnPauseAndResume.setImageResource(R.drawable.ic_pause)
                            btnRecordAndStop.setImageResource(R.drawable.ic_stop)

                            /* Only show pause-and-resume-button if API >= 24 since pausing and resuming MediaRecorders
                             * is only available on API >= 24 */
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                btnPauseAndResume.animate().translationX(0.dp())
                            }

                            tvRecordTime.animate().translationY(0.dp())
                            tvRecordTime.animateAlphaTo(1f)

                            arv.fadeIn()
                            arv.animateAlphaTo(1f)
                        }
                    }

                    AudioRecorderState.PAUSING -> {
                        audioRecordViewTimer?.cancel()

                        binding.apply {
                            btnPauseAndResume.setImageResource(R.drawable.ic_play)
                            tvRecordTime.animateAlphaTo(0.5f)
                            arv.animateAlphaTo(0.5f)
                        }
                    }
                }

                stateBefore = newState
            }
        )
    }

    private fun updateAudioRecordView() {
        audioRecordViewTimer = Timer()

        audioRecordViewTimer?.schedule(object : TimerTask() {
            override fun run() {
                val currentMaxAmplitude =
                    viewModel.recordingAndTimerHandler.audioRecorderMaxAmplitude

                runOnUiThread {
                    binding.arv.update(currentMaxAmplitude)
                }
            }
        }, 0, 100)
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.onDestroy()
    }
}
