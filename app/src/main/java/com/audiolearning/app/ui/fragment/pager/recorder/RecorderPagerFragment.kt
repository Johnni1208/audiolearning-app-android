package com.audiolearning.app.ui.fragment.pager.recorder

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
private const val AUDIO_RECORD_VIEW_UPDATE_TIME = 100L

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
        updateUiAppearancesOnAudioRecorderChange()

        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                }
                else -> viewModel.recordingAndTimerHandler.onDestroy()
            }
        }

        return binding.root
    }

    private fun observeIfNewAudioRecording() {
        viewModel.recordingAndTimerHandler.recordedFile.observe(
            viewLifecycleOwner,
            { newFile: File? ->
                newFile?.let {
                    if (!newFile.exists()) return@observe

                    NewRecordingDialog.display(
                        newFile.path,
                        parentFragmentManager
                    )
                }
            })
    }

    private fun updateUiAppearancesOnAudioRecorderChange() {
        var stateBefore: AudioRecorderState = AudioRecorderState.IDLING
        viewModel.recordingAndTimerHandler.audioRecorderState.observe(
            viewLifecycleOwner,
            { newState: AudioRecorderState ->
                when (newState) {
                    AudioRecorderState.IDLING -> displayIdlingUiState(stateBefore)
                    AudioRecorderState.RECORDING -> displayRecordingUiState()
                    AudioRecorderState.PAUSING -> displayPausingUiState()
                }

                stateBefore = newState
            }
        )
    }

    private fun displayIdlingUiState(stateBefore: AudioRecorderState) {
        audioRecordViewTimer?.cancel()

        binding.apply {
            btnPauseAndResume.setImageResource(R.drawable.ic_pause)
            btnRecordAndStop.setImageResource(R.drawable.ic_record)
        }

        if (stateBefore == AudioRecorderState.RECORDING || stateBefore == AudioRecorderState.PAUSING) {
            binding.apply {
                val overAudioRecordView = -64
                tvRecordTime.animate().translationY(overAudioRecordView.dp())
                tvRecordTime.animateAlphaTo(1f)

                val underRecordAndPauseButton = -60
                btnPauseAndResume.animate().translationX((underRecordAndPauseButton).dp())

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

    private fun displayRecordingUiState() {
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
        }, 0, AUDIO_RECORD_VIEW_UPDATE_TIME)
    }

    private fun displayPausingUiState() {
        audioRecordViewTimer?.cancel()

        binding.apply {
            val halfAlpha = 0.5f

            btnPauseAndResume.setImageResource(R.drawable.ic_play)
            tvRecordTime.animateAlphaTo(halfAlpha)
            arv.animateAlphaTo(halfAlpha)
        }
    }

    override fun onDestroy() {
        viewModel.recordingAndTimerHandler.onDestroy()
        super.onDestroy()
    }
}
