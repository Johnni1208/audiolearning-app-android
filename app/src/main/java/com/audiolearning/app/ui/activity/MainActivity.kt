package com.audiolearning.app.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.audiolearning.app.R
import com.audiolearning.app.databinding.ActivityMainBinding
import com.audiolearning.app.extension.dp
import com.audiolearning.app.extension.show
import com.audiolearning.app.ui.activity.audioplayer.AudioPlayerActivity
import com.audiolearning.app.ui.activity.audioplayer.AudioPlayerControlsViewModel
import com.audiolearning.app.ui.activity.audioplayer.AudioPlayerDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val dataViewModel: AudioPlayerDataViewModel by viewModels()
    private val controlsViewModel: AudioPlayerControlsViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val bottomNavigationBarHeight = (-56f).dp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        setupBottomAudioBar()
    }

    private fun setupBottomAudioBar() {
        // Texts and max. progress
        dataViewModel.mediaMetaData.observe(this, { metadata ->
            binding.bottomAudioBar.root.show()
            binding.bottomAudioBar.metadata = metadata
            binding.bottomAudioBar.plDuration.max = metadata.duration.toInt()
        })

        // Play-Pause-Button
        dataViewModel.mediaButtonRes.observe(this, { res ->
            binding.bottomAudioBar.btnPlayPauseAudio.setImageResource(res)
        })
        binding.bottomAudioBar.btnPlayPauseAudio.setOnClickListener {
            dataViewModel.mediaMetaData.value?.let {
                MainScope().launch { controlsViewModel.playAudioId(it.id) }
            }
        }

        // Open AudioPlayer
        binding.bottomAudioBar.apply {
            val audioPlayerActivity = Intent(applicationContext, AudioPlayerActivity::class.java)
            btnOpenAudioPlayer.setOnClickListener { startActivity(audioPlayerActivity) }
            containerAudioInfo.setOnClickListener { startActivity(audioPlayerActivity) }
        }

        // Update progress
        dataViewModel.mediaPosition.observe(this, { pos ->
            binding.bottomAudioBar.plDuration.progress = pos.toInt()
        })
    }

    override fun onStart() {
        super.onStart()

        // Make bottom-audio-bar higher if there is the bottom-nav-bar
        binding.navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.homeFragment -> binding.bottomAudioBar.root.animate()
                        .translationY(bottomNavigationBarHeight)
                    else -> binding.bottomAudioBar.root.animate()
                        .translationY(0f)
                }
            }
    }
}
