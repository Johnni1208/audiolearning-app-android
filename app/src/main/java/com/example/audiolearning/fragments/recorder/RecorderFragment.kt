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
import com.example.audiolearning.databinding.FragmentRecorderBinding
import java.util.jar.Attributes

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
            false
        )

        binding.lifecycleOwner = this

        recorderViewModel =
            ViewModelProviders.of(this).get(RecorderViewModel::class.java)

        binding.viewModel = recorderViewModel

        /* Hide PauseAndResumeButton since pausing and resuming MediaRecorders
        * is only available on API > 24 */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            binding.btnPauseAndResume.visibility = View.GONE
        }

        recorderViewModel.recordedFile.observe(this, Observer { newFile ->

            if(newFile != null){
                var mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes
                            .Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    setDataSource(newFile!!.absolutePath)
                    prepare()
                    start()
                }
            }
        })

        return binding.root
    }
}