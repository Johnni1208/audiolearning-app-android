package com.example.audiolearning.ui.fragments.recorder

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
import com.example.audiolearning.audio.audio_store.AudioStore
import com.example.audiolearning.databinding.FragmentRecorderBinding
import com.example.audiolearning.models.Audio
import com.example.audiolearning.models.Subject
import com.example.audiolearning.ui.dialogs.new_recording.NewRecordingDialog
import com.example.audiolearning.ui.dialogs.new_recording.NewRecordingDialogButtonsListener
import java.io.File

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

        val viewModelFactory = RecorderViewModelFactory(audioStore = AudioStore(requireContext()))
        recorderViewModel = ViewModelProviders.of(this, viewModelFactory)
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
        recorderViewModel.recordedFile.observe(this, Observer { newFile ->
            if (newFile != null) {
                NewRecordingDialog.display(
                    getNewRecordingDialogButtonsListener(newFile),
                    requireFragmentManager()
                )
            }
        })
    }

    private fun getNewRecordingDialogButtonsListener(newFile: File) =
        object : NewRecordingDialogButtonsListener {
            override fun onSaveButtonClicked(name: String, subject: Subject) {
                recorderViewModel.onSaveAudio(Audio(newFile, name, subject))
            }

            override fun onDiscardButtonClicked() {
                newFile.delete()
            }
        }

    private fun switchButtonAppearancesOnAudioRecorderChange() {
        recorderViewModel.audioRecorderState.observe(this, Observer { newState ->
            when (newState!!) {
                AudioRecorderState.IDLING -> {
                    binding.apply {
                        btnPauseAndResume.isEnabled = false
                        btnPauseAndResume.isClickable = false
                        btnPauseAndResume.text = getString(R.string.pause_text)

                        btnRecordAndStop.text = getString(R.string.record_text)
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

        })
    }

    override fun onDetach() {
        super.onDetach()
        recorderViewModel.onDestroy()
    }
}